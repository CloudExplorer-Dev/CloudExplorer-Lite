package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudHost;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.base.service.IBaseVmCloudHostService;
import com.fit2cloud.base.service.IBaseWorkspaceService;
import com.fit2cloud.common.es.constants.IndexConstants;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.provider.entity.F2CEntityType;
import com.fit2cloud.common.utils.*;
import com.fit2cloud.controller.request.server.PageServerRequest;
import com.fit2cloud.controller.request.server.ResourceAnalysisRequest;
import com.fit2cloud.controller.response.BarTreeChartData;
import com.fit2cloud.controller.response.ChartData;
import com.fit2cloud.dao.mapper.AnalyticsServerMapper;
import com.fit2cloud.dto.AnalyticsServerDTO;
import com.fit2cloud.dto.KeyValue;
import com.fit2cloud.es.entity.PerfMetricMonitorData;
import com.fit2cloud.service.IServerAnalysisService;
import com.fit2cloud.utils.OperationUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.list.TreeList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jianneng
 * @date 2022/12/24 11:29
 **/
@Service
public class ServerAnalysisService implements IServerAnalysisService {

    @Resource
    private AnalyticsServerMapper analyticsServerMapper;
    @Resource
    private IBaseCloudAccountService iBaseCloudAccountService;
    @Resource
    private IBaseVmCloudHostService iBaseVmCloudHostService;
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    private IBaseWorkspaceService iBaseWorkspaceService;
    @Resource
    private IBaseOrganizationService iBaseOrganizationService;
    @Override
    public IPage<AnalyticsServerDTO> pageServer(PageServerRequest request) {
        Page<AnalyticsServerDTO> page = PageUtil.of(request, AnalyticsServerDTO.class, new OrderItem(ColumnNameUtil.getColumnName(AnalyticsServerDTO::getCreateTime, true), false), true);
        // 构建查询参数
        QueryWrapper<AnalyticsServerDTO> wrapper = addServerQuery(request);
        IPage<AnalyticsServerDTO> result = analyticsServerMapper.pageList(page, wrapper);
        //设置监控数据
        getVmPerfMetric(result.getRecords());
        return result;
    }
    private QueryWrapper<AnalyticsServerDTO> addServerQuery(PageServerRequest request) {
        QueryWrapper<AnalyticsServerDTO> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(request.getName()), ColumnNameUtil.getColumnName(AnalyticsServerDTO::getInstanceName,true), request.getName());
        wrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()), ColumnNameUtil.getColumnName(AnalyticsServerDTO::getAccountId, true), request.getAccountIds());
        wrapper.notIn(true,ColumnNameUtil.getColumnName(AnalyticsServerDTO::getInstanceStatus, true), Arrays.asList("Deleted"));
        return wrapper;
    }

    @Override
    public List<CloudAccount> getAllCloudAccount() {
        QueryWrapper<CloudAccount> queryWrapper = new QueryWrapper<>();
        List<CloudAccount> accountList = iBaseCloudAccountService.list(queryWrapper);
        return accountList;
    }

    private void allCloudAccount(ResourceAnalysisRequest request) {
        if(CollectionUtils.isEmpty(request.getAccountIds())){
            request.setAccountIds(getAllCloudAccount().stream().map(CloudAccount::getId).collect(Collectors.toList()));
        }
    }

    @Override
    public List<VmCloudHost> getVmHost(ResourceAnalysisRequest request) {
        allCloudAccount(request);
        QueryWrapper<VmCloudHost> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()),ColumnNameUtil.getColumnName(VmCloudHost::getAccountId,true),request.getAccountIds());
        return iBaseVmCloudHostService.list(queryWrapper);
    }

    @Override
    public List<ChartData> vmIncreaseTrend(ResourceAnalysisRequest request){
        List<ChartData> tempChartDataList = new ArrayList<>();
        List<CloudAccount> accountList = getAllCloudAccount();
        Map<String,CloudAccount> accountMap = accountList.stream().collect(Collectors.toMap(CloudAccount::getId,v->v,(k1,k2)->k1));
        if(accountMap.size()==0){
            return tempChartDataList;
        }
        QueryWrapper<AnalyticsServerDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()),ColumnNameUtil.getColumnName(AnalyticsServerDTO::getAccountId,true),request.getAccountIds());
        queryWrapper.in(CollectionUtils.isNotEmpty(request.getHostIds()),ColumnNameUtil.getColumnName(AnalyticsServerDTO::getHostId,true),request.getHostIds());
        queryWrapper.ge(true,ColumnNameUtil.getColumnName(AnalyticsServerDTO::getCreateTime,true),LocalDateTime.now().minusDays(request.getDayNumber()));
        List<AnalyticsServerDTO> vmList = analyticsServerMapper.list(queryWrapper);
        if(CollectionUtils.isNotEmpty(vmList)){
            if(CollectionUtils.isNotEmpty(request.getHostIds())){
                Map<String,List<AnalyticsServerDTO>> hostGroup = vmList.stream().collect(Collectors.groupingBy(item->buildKey(item)));
                hostGroup.keySet().forEach(hostId->{
                    Map<String,Long> month = hostGroup.get(hostId).stream().collect(Collectors.groupingBy(AnalyticsServerDTO::getCreateMonth, Collectors.counting()));
                    month.keySet().forEach(k->{
                        ChartData chartData = new ChartData();
                        chartData.setXAxis(k);
                        chartData.setGroupName(hostGroup.get(hostId).get(0).getHost()+"("+hostGroup.get(hostId).get(0).getAccountName()+")");
                        chartData.setYAxis(new BigDecimal(month.get(k)));
                        tempChartDataList.add(chartData);
                    });
                });
            }else{
                Map<String,List<AnalyticsServerDTO>> accountGroup = vmList.stream().collect(Collectors.groupingBy(AnalyticsServerDTO::getAccountId));
                accountGroup.keySet().forEach(accountId->{
                    Map<String,Long> month = accountGroup.get(accountId).stream().collect(Collectors.groupingBy(AnalyticsServerDTO::getCreateMonth, Collectors.counting()));
                    month.keySet().forEach(k->{
                        ChartData chartData = new ChartData();
                        chartData.setXAxis(k);
                        chartData.setGroupName(accountGroup.get(accountId).get(0).getAccountName());
                        chartData.setYAxis(new BigDecimal(month.get(k)));
                        tempChartDataList.add(chartData);
                    });
                });
            }
        }
        return tempChartDataList;
    }

    public static String buildKey(AnalyticsServerDTO serverDTO) {
        return serverDTO.getAccountId() + "#" + serverDTO.getHostId();
    }

    @Override
    public Map<String,List<KeyValue>> spread(ResourceAnalysisRequest request){
        Map<String,List<KeyValue>> result = new HashMap<>();
        List<CloudAccount> accountList = getAllCloudAccount();
        Map<String,CloudAccount> accountMap = accountList.stream().collect(Collectors.toMap(CloudAccount::getId,v->v,(k1,k2)->k1));
        if(accountMap.size()==0){
            return result;
        }
        QueryWrapper<AnalyticsServerDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()),ColumnNameUtil.getColumnName(AnalyticsServerDTO::getAccountId,true),request.getAccountIds());
        queryWrapper.in(CollectionUtils.isNotEmpty(request.getHostIds()),ColumnNameUtil.getColumnName(AnalyticsServerDTO::getHostId,true),request.getHostIds());
        //只统计开关机的
        queryWrapper.in(true,ColumnNameUtil.getColumnName(AnalyticsServerDTO::getInstanceStatus,true), Arrays.asList("Running","Stopped"));
        List<AnalyticsServerDTO> vmList = analyticsServerMapper.list(queryWrapper);
        Map<String,Long> byAccountMap = vmList.stream().filter(v->StringUtils.isNotEmpty(v.getAccountId())).collect(Collectors.groupingBy(AnalyticsServerDTO::getAccountId, Collectors.counting()));
        result.put("byAccount",byAccountMap.entrySet().stream().map(c -> new KeyValue(StringUtils.isEmpty(accountMap.get(c.getKey()).getName())?c.getKey():accountMap.get(c.getKey()).getName(), c.getValue()) {}).collect(Collectors.toList()));
        Map<String,String> statusMap = new HashMap<>();
        statusMap.put("Running","运行中");
        statusMap.put("Stopped","已停止");
        Map<String,Long> byStatusMap = vmList.stream().collect(Collectors.groupingBy(AnalyticsServerDTO::getInstanceStatus, Collectors.counting()));
        result.put("byStatus",byStatusMap.entrySet().stream().map(c -> new KeyValue(statusMap.get(c.getKey()), c.getValue()) {}).collect(Collectors.toList()));
        Map<String,String> chargeTypeMap = new HashMap<>();
        chargeTypeMap.put("PostPaid","按需按量");
        chargeTypeMap.put("PrePaid","包年包月");
        chargeTypeMap.put("SpotPaid","竞价");
        Map<String,Long> byChargeTypeMap = vmList.stream().filter(v->StringUtils.isNotEmpty(v.getInstanceChargeType())).collect(Collectors.groupingBy(AnalyticsServerDTO::getInstanceChargeType, Collectors.counting()));
        result.put("byChargeType",byChargeTypeMap.entrySet().stream().map(c -> new KeyValue(chargeTypeMap.get(c.getKey()), c.getValue()) {}).collect(Collectors.toList()));
        return result;
    }

    /**
     * 获取虚拟机最近一次的监控数据 CPU、内存
     * @param list
     */
    private void getVmPerfMetric(List<AnalyticsServerDTO> list){
        List<String> resourceId = list.stream().map(AnalyticsServerDTO::getInstanceUuid).collect(Collectors.toList());
        list.forEach(vm->{
            String findKey = vm.getAccountId()+"@"+vm.getInstanceUuid();
            //查询虚拟机最新一次监控数据，仅有一条
            SearchHits<PerfMetricMonitorData> cpuHits = elasticsearchTemplate.search(getVmPerfMetricQuery(vm.getInstanceUuid(),"CPU_USED_UTILIZATION"), PerfMetricMonitorData.class, IndexCoordinates.of(IndexConstants.CE_PERF_METRIC_MONITOR_DATA.getCode()));
            if(CollectionUtils.isNotEmpty(cpuHits.getSearchHits())){
                List<PerfMetricMonitorData> response = cpuHits.stream().map(SearchHit::getContent).toList();
                List<PerfMetricMonitorData> result = response.stream().filter(v->StringUtils.equalsIgnoreCase(v.getCloudAccountId()+"@"+v.getInstanceId(),findKey)).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(result)){
                    vm.setCpuAverage(result.get(0).getAverage());
                    vm.setCpuMinimum(result.get(0).getMinimum());
                    vm.setCpuMaximum(result.get(0).getMaximum());
                }
            }
            SearchHits<PerfMetricMonitorData> memoryHits = elasticsearchTemplate.search(getVmPerfMetricQuery(vm.getInstanceUuid(),"MEMORY_USED_UTILIZATION"), PerfMetricMonitorData.class, IndexCoordinates.of(IndexConstants.CE_PERF_METRIC_MONITOR_DATA.getCode()));
            if(CollectionUtils.isNotEmpty(memoryHits.getSearchHits())){
                List<PerfMetricMonitorData> response = memoryHits.stream().map(SearchHit::getContent).toList();
                List<PerfMetricMonitorData> result = response.stream().filter(v->StringUtils.equalsIgnoreCase(v.getCloudAccountId()+"@"+v.getInstanceId(),findKey)).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(result)){
                    vm.setMemoryAverage(result.get(0).getAverage());
                    vm.setMemoryMinimum(result.get(0).getMinimum());
                    vm.setMemoryMaximum(result.get(0).getMaximum());
                }
            }
            SearchHits<PerfMetricMonitorData> diskHits = elasticsearchTemplate.search(getVmPerfMetricQuery(vm.getInstanceUuid(),"DISK_USED_UTILIZATION"), PerfMetricMonitorData.class, IndexCoordinates.of(IndexConstants.CE_PERF_METRIC_MONITOR_DATA.getCode()));
            if(CollectionUtils.isNotEmpty(diskHits.getSearchHits())){
                List<PerfMetricMonitorData> response = diskHits.stream().map(SearchHit::getContent).toList();
                List<PerfMetricMonitorData> result = response.stream().filter(v->StringUtils.equalsIgnoreCase(v.getCloudAccountId()+"@"+v.getInstanceId(),findKey)).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(result)){
                    vm.setDiskAverage(result.get(0).getAverage());
                }
            }
        });
    }

    private org.springframework.data.elasticsearch.core.query.Query getVmPerfMetricQuery(String instanceId,String metric) {
        List<QueryUtil.QueryCondition> queryConditions = new ArrayList<>();
        QueryUtil.QueryCondition entityType = new QueryUtil.QueryCondition(true, "entityType.keyword", F2CEntityType.VIRTUAL_MACHINE.toString(), QueryUtil.CompareType.EQ);
        queryConditions.add(entityType);
        QueryUtil.QueryCondition metricName = new QueryUtil.QueryCondition(true, "metricName.keyword", metric, QueryUtil.CompareType.EQ);
        queryConditions.add(metricName);
        if(StringUtils.isNotEmpty(instanceId)){
            QueryUtil.QueryCondition instanceIdQuery = new QueryUtil.QueryCondition(true, "instanceId.keyword", instanceId, QueryUtil.CompareType.EQ);
            queryConditions.add(instanceIdQuery);
        }
        BoolQuery.Builder boolQuery = QueryUtil.getQuery(queryConditions);
        NativeQueryBuilder query = new NativeQueryBuilder()
                .withPageable(PageRequest.of(0, 1))
                .withQuery(new co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder().bool(boolQuery.build()).build())
                .withSourceFilter(new FetchSourceFilter(new String[]{}, new String[]{"@version", "@timestamp", "host", "tags"}))
                .withSort(Sort.by(Order.desc("timestamp")));
        return query.build();
    }

    @Override
    public List<ChartData> getResourceTrendData(ResourceAnalysisRequest request){
        List<ChartData> result = new ArrayList<>();
        CalendarInterval intervalUnit = OperationUtils.getCalendarIntervalUnit(request.getStartTime(),request.getEndTime());
        try {
            request.setIntervalPosition(intervalUnit);
            SearchHits<PerfMetricMonitorData> response = elasticsearchTemplate.search(getSearchResourceTrendDataQuery(request), PerfMetricMonitorData.class, IndexCoordinates.of(IndexConstants.CE_PERF_METRIC_MONITOR_DATA.getCode()));
            ElasticsearchAggregations aggregations = (ElasticsearchAggregations) response.getAggregations();
            ElasticsearchAggregation aggregation = (ElasticsearchAggregation)aggregations.aggregations().get(0);
            List<DateHistogramBucket> dateHistogramBucketList = aggregation.aggregation().getAggregate().dateHistogram().buckets().array();
            Map<String,Map<String,Long>> groupDateAndRange = new HashMap<>();
            //时间
            dateHistogramBucketList.forEach(dateHistogramBucket->{
                Arrays.asList(0,20,40,60,80).stream().forEach(interval->{
                    String rangeKey = interval+"~"+(interval+20)+"%";
                    String key = dateHistogramBucket.key()+"-"+rangeKey;
                    Map<String,Long> map = new HashMap<>();
                    map.put(rangeKey,0L);
                    if(!groupDateAndRange.containsKey(key)){
                        List<StringTermsBucket> averageRanges = dateHistogramBucket.aggregations().get("instanceIds").sterms().buckets().array();
                        if(CollectionUtils.isNotEmpty(averageRanges)){
                            Long count = 0L;
                            //资源时间段内平均值
                            for(StringTermsBucket termsBucket:averageRanges){
                                double avgValue = termsBucket.aggregations().get("average").max().value();
                                //在区间里面
                                if(avgValue>interval && avgValue<(interval+20)){
                                    count++;
                                }
                            }
                            map.put(rangeKey,count);
                        }
                    }
                    groupDateAndRange.put(key,map);
                });
            });
            groupDateAndRange.keySet().forEach(k->{
                Map<String,Long> map = groupDateAndRange.get(k);
                map.keySet().forEach(r->{
                    ChartData chartData = new ChartData();
                    chartData.setXAxis(OperationUtils.getTimeFormat(DateUtil.dateToString(Long.valueOf(k.split("-")[0]),null),intervalUnit));
                    chartData.setGroupName(r);
                    chartData.setYAxis(new BigDecimal(map.get(r)));
                    result.add(chartData);
                });
            });
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.error("获取云主机资源使用趋势异常:"+e.getMessage());
        }
        return result;
    }

    private org.springframework.data.elasticsearch.core.query.Query getSearchResourceTrendDataQuery(ResourceAnalysisRequest request) {
        List<QueryUtil.QueryCondition> queryConditions = new ArrayList<>();
        //时间参数
        QueryUtil.QueryCondition startTimesTamp = new QueryUtil.QueryCondition(true, "timestamp", request.getStartTime(), QueryUtil.CompareType.GTE);
        queryConditions.add(startTimesTamp);
        QueryUtil.QueryCondition endTimesTamp = new QueryUtil.QueryCondition(true, "timestamp", request.getEndTime(), QueryUtil.CompareType.LTE);
        queryConditions.add(endTimesTamp);
        QueryUtil.QueryCondition entityType = new QueryUtil.QueryCondition(true, "entityType.keyword", F2CEntityType.VIRTUAL_MACHINE.toString(), QueryUtil.CompareType.EQ);
        queryConditions.add(entityType);
        QueryUtil.QueryCondition metricName = new QueryUtil.QueryCondition(true, "metricName.keyword", request.getMetricName(), QueryUtil.CompareType.EQ);
        queryConditions.add(metricName);
        if(CollectionUtils.isNotEmpty(request.getAccountIds())){
            QueryUtil.QueryCondition accountId = new QueryUtil.QueryCondition(true, "cloudAccountId.keyword", request.getAccountIds(), QueryUtil.CompareType.IN);
            queryConditions.add(accountId);
        }
        if(CollectionUtils.isNotEmpty(request.getHostIds())){
            QueryUtil.QueryCondition accountId = new QueryUtil.QueryCondition(true, "hostId.keyword", request.getHostIds(), QueryUtil.CompareType.IN);
            queryConditions.add(accountId);
        }
        BoolQuery.Builder boolQuery = QueryUtil.getQuery(queryConditions);
        NativeQueryBuilder query = new NativeQueryBuilder()
                .withPageable(PageRequest.of(0, 1))
                .withQuery(new co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder().bool(boolQuery.build()).build())
                .withSourceFilter(new FetchSourceFilter(new String[]{}, new String[]{"@version", "@timestamp", "host", "tags"}))
                .withAggregation("timestamp",new Aggregation.Builder().dateHistogram(new DateHistogramAggregation.Builder().field("timestamp").calendarInterval(request.getIntervalPosition()).build())
                        .aggregations("instanceIds",new Aggregation.Builder().terms(new TermsAggregation.Builder().field("instanceId.keyword").size(Integer.MAX_VALUE).build())
                        .aggregations("average",new Aggregation.Builder().max(new MaxAggregation.Builder().field("average").build()).build()).build()).build())
                ;
        return query.build();
    }

    public Map<String,List<BarTreeChartData>> analyticsVmCloudServerByOrgWorkspace(ResourceAnalysisRequest request){
        Map<String,List<BarTreeChartData>> result = new HashMap<>();
        QueryWrapper<AnalyticsServerDTO> wrapper = new QueryWrapper<>();
        wrapper.eq("org_workspace.type","workspace");
        wrapper.ne(ColumnNameUtil.getColumnName(AnalyticsServerDTO::getInstanceStatus,true),"Deleted");
        wrapper.in(CollectionUtils.isNotEmpty(request.getHostIds()), ColumnNameUtil.getColumnName(AnalyticsServerDTO::getHostId, true), request.getHostIds());
        wrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()), ColumnNameUtil.getColumnName(AnalyticsServerDTO::getAccountId, true), request.getAccountIds());
        wrapper.groupBy(true,Arrays.asList("org_workspace.id","org_workspace.name"));
        List<BarTreeChartData> chartDateWorkspaceList = analyticsServerMapper.analyticsVmCloudServerByOrgWorkspace(wrapper);
        List<BarTreeChartData> workspaceList = iBaseWorkspaceService.list().stream().map(v->{
            BarTreeChartData barTreeChartData = new BarTreeChartData();
            barTreeChartData.setId(v.getId());
            barTreeChartData.setName(v.getName());
            barTreeChartData.setValue(0L);
            barTreeChartData.setPId(v.getOrganizationId());
            return barTreeChartData;
        }).collect(Collectors.toList());
        setAnalyticsData(workspaceList,chartDateWorkspaceList);
        if(request.isAnalysisWorkspace()){
            workspaceList.sort((o1,o2)->o2.getValue().compareTo(o1.getValue()));
            result.put("all",workspaceList);
            result.put("tree",workspaceList);
            return result;
        }
        Map<String,List<BarTreeChartData>> workspaceMap = workspaceList.stream().collect(Collectors.groupingBy(BarTreeChartData::getPId));
        QueryWrapper<AnalyticsServerDTO> orgWrapper =new QueryWrapper<>();
        orgWrapper.eq("org_workspace.type","org");
        orgWrapper.ne(ColumnNameUtil.getColumnName(AnalyticsServerDTO::getInstanceStatus,true),"Deleted");
        orgWrapper.in(CollectionUtils.isNotEmpty(request.getHostIds()), ColumnNameUtil.getColumnName(AnalyticsServerDTO::getHostId, true), request.getHostIds());
        orgWrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()), ColumnNameUtil.getColumnName(AnalyticsServerDTO::getAccountId, true), request.getAccountIds());
        orgWrapper.groupBy(true,Arrays.asList("org_workspace.id","org_workspace.name"));
        List<BarTreeChartData> orgList = iBaseOrganizationService.list().stream().map(v->{BarTreeChartData barTreeChartData = new BarTreeChartData();
            barTreeChartData.setId(v.getId());
            barTreeChartData.setName(v.getName());
            barTreeChartData.setValue(0L);
            barTreeChartData.setPId(v.getPid());
            return barTreeChartData;
        }).collect(Collectors.toList());
        result.put("all",new ArrayList<>());
        result.put("tree",new ArrayList<>());
        if(CollectionUtils.isNotEmpty(orgList)){
            List<BarTreeChartData> list = analyticsServerMapper.analyticsVmCloudServerByOrgWorkspace(orgWrapper);
            setAnalyticsData(orgList,list);
            orgList.stream().forEach(v->{
                if(workspaceMap.get(v.getId())!=null){
                    // 设置工作空间
                    List<BarTreeChartData> wk = workspaceMap.get(v.getId()).stream().peek(workspace->workspace.setName(workspace.getName()+"(工作空间)")).collect(Collectors.toList());
                    Long wkVmCount = wk.stream().mapToLong(BarTreeChartData::getValue).sum();
                    v.setValue(v.getValue()+wkVmCount);
                    v.setChildren(wk);
                }else{
                    v.setChildren(new ArrayList<>());
                }
            });
            orgList.sort((o1,o2)->o2.getValue().compareTo(o1.getValue()));
            result.put("all",orgList);
            List<BarTreeChartData> chartDataList = new TreeList<>();
            orgList.stream().filter(o->StringUtils.isEmpty(o.getPId())).forEach(v->{
                v.setGroupName("org");
                v.setName(v.getName()+"(组织)");
                v.getChildren().addAll(getChildren(v,orgList,workspaceMap));
                List<BarTreeChartData> childrenList = v.getChildren();
                childrenList.sort((o1,o2)->o2.getValue().compareTo(o1.getValue()));
                v.setChildren(childrenList);
                chartDataList.add(v);
            });
            chartDataList.sort((o1,o2)->o2.getValue().compareTo(o1.getValue()));
            result.put("tree",chartDataList);
        }
        return result;
    }

    private static void setAnalyticsData(List<BarTreeChartData> orgList, List<BarTreeChartData> list) {
        orgList.forEach(v->{
            List<BarTreeChartData> tmp = list.stream().filter(c->StringUtils.equalsIgnoreCase(v.getId(),c.getId())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(tmp)){
                v.setValue(tmp.get(0).getValue());
            }
        });
    }

    /**
     * 递归获取子级
     * @param barTreeChartData
     * @param list
     * @return
     */
    public List<BarTreeChartData> getChildren(BarTreeChartData barTreeChartData,List<BarTreeChartData> list,Map<String,List<BarTreeChartData>> workspaceMap) {
        List<BarTreeChartData> childrenList = list.stream().filter(u -> Objects.equals(u.getPId(), barTreeChartData.getId())).map(
                u -> {
                    u.setName(u.getName()+"(子组织)");
                    u.setGroupName("org");
                    u.setChildren(getChildren(u, list,workspaceMap));
                    if(workspaceMap.get(u.getId())!=null){
                        // 设置工作空间
                        List<BarTreeChartData> wk = workspaceMap.get(u.getId());
                        Long wkVmCount = wk.stream().mapToLong(BarTreeChartData::getValue).sum();
                        u.setValue(u.getValue()+wkVmCount);
                        u.getChildren().addAll(wk);
                    }
                    barTreeChartData.setValue(barTreeChartData.getValue()+u.getValue());
                    return u;
                }
        ).collect(Collectors.toList());
        childrenList.sort((o1,o2)->o2.getValue().compareTo(o1.getValue()));
        return childrenList;
    }

}
