package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch._types.InlineScript;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.base.entity.VmCloudHost;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseVmCloudServerMapper;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.base.service.IBaseVmCloudHostService;
import com.fit2cloud.base.service.IBaseWorkspaceService;
import com.fit2cloud.common.es.constants.IndexConstants;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.provider.entity.F2CEntityType;
import com.fit2cloud.common.utils.*;
import com.fit2cloud.constants.ResourcePerfMetricEnum;
import com.fit2cloud.constants.SpecialAttributesConstants;
import com.fit2cloud.controller.request.server.PageServerRequest;
import com.fit2cloud.controller.request.server.ResourceAnalysisRequest;
import com.fit2cloud.controller.response.BarTreeChartData;
import com.fit2cloud.controller.response.ChartData;
import com.fit2cloud.dao.entity.OrgWorkspace;
import com.fit2cloud.dao.mapper.OrgWorkspaceMapper;
import com.fit2cloud.dto.AnalysisServerDTO;
import com.fit2cloud.dto.KeyValue;
import com.fit2cloud.es.entity.PerfMetricMonitorData;
import com.fit2cloud.service.IBaseResourceAnalysisService;
import com.fit2cloud.service.IPermissionService;
import com.fit2cloud.service.IServerAnalysisService;
import com.fit2cloud.utils.OperationUtils;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jianneng
 * {@code @date} 2022/12/24 11:29
 **/
@Service
public class ServerAnalysisServiceImpl implements IServerAnalysisService {

    @Resource
    private OrgWorkspaceMapper orgWorkspaceMapper;
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
    @Resource
    private IBaseResourceAnalysisService iBaseResourceAnalysisService;
    @Resource
    private IPermissionService permissionService;
    @Resource
    private BaseVmCloudServerMapper baseVmCloudServerMapper;

    /**
     * 分页查询云主机明细
     * @param request 分页查询云主机参数
     */
    @Override
    public IPage<AnalysisServerDTO> pageServer(PageServerRequest request) {
        Page<AnalysisServerDTO> page = PageUtil.of(request, AnalysisServerDTO.class, null, true);
        // 构建查询参数
        MPJLambdaWrapper<VmCloudServer> wrapper = addServerPageQuery(request);
        IPage<AnalysisServerDTO> result = baseVmCloudServerMapper.selectJoinPage(page,AnalysisServerDTO.class, wrapper);
        //设置监控数据
        getVmPerfMetric(result.getRecords());
        return result;
    }

    /**
     * 构建分页查询参数
     */
    private MPJLambdaWrapper<VmCloudServer> addServerPageQuery(PageServerRequest request) {
        List<String> sourceIds = permissionService.getSourceIds();
        if (!CurrentUserUtils.isAdmin() && CollectionUtils.isNotEmpty(sourceIds)) {
            request.setSourceIds(sourceIds);
        }
        MPJLambdaWrapper<VmCloudServer> wrapper = addServerQuery(new MPJLambdaWrapper<>(),request.getAccountIds());
        wrapper.selectAll(VmCloudServer.class);
        wrapper.orderByDesc(VmCloudServer::getCreateTime);
        wrapper.selectAs(CloudAccount::getName,AnalysisServerDTO::getAccountName);
        wrapper.selectAs(CloudAccount::getPlatform,AnalysisServerDTO::getPlatform);
        wrapper.like(StringUtils.isNotBlank(request.getName()), VmCloudServer::getInstanceName, request.getName());
        wrapper.in(CollectionUtils.isNotEmpty(request.getSourceIds()), VmCloudServer::getSourceId, request.getSourceIds());
        wrapper.notIn(true,VmCloudServer::getInstanceStatus, List.of(SpecialAttributesConstants.StatusField.VM_DELETE,SpecialAttributesConstants.StatusField.FAILED));
        return wrapper;
    }

    /**
     * 查询云主机统一参数
     */
    private MPJLambdaWrapper<VmCloudServer> addServerQuery( MPJLambdaWrapper<VmCloudServer> wrapper,List<String> accountIds) {
        wrapper.selectAll(VmCloudServer.class);
        wrapper.in(CollectionUtils.isNotEmpty(accountIds), VmCloudServer::getAccountId, accountIds);
        wrapper.leftJoin(CloudAccount.class, CloudAccount::getId, VmCloudServer::getAccountId);
        return wrapper;
    }

    @Override
    public List<CloudAccount> getAllCloudAccount() {
        QueryWrapper<CloudAccount> queryWrapper = new QueryWrapper<>();
        return iBaseCloudAccountService.list(queryWrapper);
    }

    /**
     * 通过云账号查询宿主机
     * @param request 云主机分析参数
     */
    @Override
    public List<VmCloudHost> getVmHost(ResourceAnalysisRequest request) {
        if(CollectionUtils.isEmpty(request.getAccountIds())){
            request.setAccountIds(getAllCloudAccount().stream().map(CloudAccount::getId).toList());
        }
        QueryWrapper<VmCloudHost> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()),ColumnNameUtil.getColumnName(VmCloudHost::getAccountId,true),request.getAccountIds());
        return iBaseVmCloudHostService.list(queryWrapper);
    }

    /**
     * 云账号分组
     */
    @Override
    public Map<String,CloudAccount> getAllAccountIdMap(){
        List<CloudAccount> accountList = getAllCloudAccount();
        return accountList.stream().collect(Collectors.toMap(CloudAccount::getId,v->v,(k1,k2)->k1));
    }

    /**
     * 云主机分析参数
     */
    private MPJLambdaWrapper<VmCloudServer> addServerAnalysisQuery(ResourceAnalysisRequest request) {
        List<String> sourceIds = permissionService.getSourceIds();
        if (!CurrentUserUtils.isAdmin() && CollectionUtils.isNotEmpty(sourceIds)) {
            request.setSourceIds(sourceIds);
        }
        MPJLambdaWrapper<VmCloudServer> wrapper = addServerQuery(new MPJLambdaWrapper<>(),request.getAccountIds());
        wrapper.in(CollectionUtils.isNotEmpty(request.getSourceIds()), VmCloudServer::getSourceId, request.getSourceIds());
        wrapper.in(CollectionUtils.isNotEmpty(request.getHostIds()),VmCloudServer::getHostId,request.getHostIds());
        return wrapper;
    }


    /**
     * 云主机趋势，在一段时间内创建云主机数量产生趋势数据
     */
    @Override
    public List<ChartData> vmIncreaseTrend(ResourceAnalysisRequest request){
        List<ChartData> tempChartDataList = new ArrayList<>();
        Map<String,CloudAccount> accountMap = getAllAccountIdMap();
        if (accountMap.size() != 0) {
            MPJLambdaWrapper<VmCloudServer> queryWrapper = addServerAnalysisQuery(request);
            queryWrapper.selectAs(CloudAccount::getName,AnalysisServerDTO::getAccountName);
            queryWrapper.selectAs(CloudAccount::getPlatform,AnalysisServerDTO::getPlatform);
            List<AnalysisServerDTO> vmList = baseVmCloudServerMapper.selectJoinList(AnalysisServerDTO.class,queryWrapper);
            if (CollectionUtils.isNotEmpty(vmList)) {
                //格式化创建时间,删除时间
                vmList = vmList.stream().filter(v->accountMap.containsKey(v.getAccountId())).filter(v->Objects.nonNull(v.getCreateTime())).toList();
                vmList.forEach(v->{
                    v.setCreateMonth(v.getCreateTime().format(DateTimeFormatter.ofPattern(DateUtil.YYYY_MM_DD)));
                    if(Objects.nonNull(v.getLastOperateTime()) && StringUtils.equalsIgnoreCase(v.getInstanceStatus(), SpecialAttributesConstants.StatusField.VM_DELETE)){
                        v.setDeleteMonth(v.getLastOperateTime().format(DateTimeFormatter.ofPattern(DateUtil.YYYY_MM_DD)));
                    }
                });
                Map<String, List<AnalysisServerDTO>> resourceGroup;
                if (CollectionUtils.isNotEmpty(request.getHostIds())) {
                    resourceGroup = vmList.stream().collect(Collectors.groupingBy(ServerAnalysisServiceImpl::buildKey));
                } else {
                    resourceGroup = vmList.stream().collect(Collectors.groupingBy(AnalysisServerDTO::getAccountId));
                }
                setCloudServerIncreaseTrendData(resourceGroup,CollectionUtils.isNotEmpty(request.getHostIds()),request.getDayNumber(),tempChartDataList);
            }
        }
        return tempChartDataList;
    }

    private void setCloudServerIncreaseTrendData(Map<String,List<AnalysisServerDTO>> resourceGroup, boolean isHostGroup, Long days, List<ChartData> tempChartDataList){
        List<String> dateRangeList = getRangeDateStrList(days);
        resourceGroup.keySet().forEach(resourceId -> {
            //总数
            Map<String, Long> month = resourceGroup.get(resourceId).stream().collect(Collectors.groupingBy(AnalysisServerDTO::getCreateMonth, Collectors.counting()));
            //删除总数
            Map<String, Long> delMonth = resourceGroup.get(resourceId).stream()
                    .filter(v->StringUtils.equalsIgnoreCase(v.getInstanceStatus(),SpecialAttributesConstants.StatusField.VM_DELETE)
                            && StringUtils.isNotEmpty(v.getDeleteMonth()))
                    .collect(Collectors.groupingBy(AnalysisServerDTO::getDeleteMonth, Collectors.counting()));
            dateRangeList.forEach(dateStr -> {
                ChartData chartData = new ChartData();
                chartData.setXAxis(dateStr);
                if(isHostGroup){
                    chartData.setGroupName(resourceGroup.get(resourceId).get(0).getHost() + "(" + resourceGroup.get(resourceId).get(0).getAccountName() + ")");
                }else{
                    chartData.setGroupName(resourceGroup.get(resourceId).get(0).getAccountName());
                }
                BigDecimal v = new BigDecimal(getResourceTotalDateBefore(month,dateStr)-getResourceTotalDateBefore(delMonth,dateStr));
                chartData.setYAxis(v.compareTo(BigDecimal.ZERO)==-1?BigDecimal.ZERO:v);
                tempChartDataList.add(chartData);
            });
        });
    }

    @Override
    public Long getResourceTotalDateBefore(Map<String, Long> monthTreeMap, String dateStr){
        TreeMap<String,Long> totalMap = new TreeMap<>();
        monthTreeMap.keySet().forEach(k -> {
            if(k.compareTo(dateStr)<=0){
                totalMap.put(k,monthTreeMap.get(k));
            }
        });
        return totalMap.values().stream().mapToLong(x->x).sum();
    }

    @Override
    public List<String> getRangeDateStrList(Long day) {
        List<String> dateList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < day; i++) {
            Date date = DateUtils.addDays(new Date(), -i);
            String formatDate = sdf.format(date);
            dateList.add(formatDate);
        }
        return dateList;
    }

    /**
     * 趋势按宿主机，云主机按云账号与主机ID
     */
    public static String buildKey(AnalysisServerDTO serverDTO) {
        return serverDTO.getAccountId() + "#" + serverDTO.getHostId();
    }

    /**
     * 云主机分布
     * 按云账号、状态、付费方式
     */
    @Override
    public Map<String,List<KeyValue>> spread(ResourceAnalysisRequest request){
        Map<String,List<KeyValue>> result = new HashMap<>(1);
        Map<String,CloudAccount> accountMap = getAllAccountIdMap();
        if(accountMap.size()==0){
            return result;
        }
        MPJLambdaWrapper<VmCloudServer> queryWrapper = addServerAnalysisQuery(request);
        queryWrapper.notIn(true,VmCloudServer::getInstanceStatus, List.of(SpecialAttributesConstants.StatusField.VM_DELETE,SpecialAttributesConstants.StatusField.FAILED));
        List<AnalysisServerDTO> vmList = baseVmCloudServerMapper.selectJoinList(AnalysisServerDTO.class,queryWrapper);
        //将除了开机关机的其他状态设置为其他
        vmList.forEach(v->{
            if(!StringUtils.equalsIgnoreCase(v.getInstanceStatus(),SpecialAttributesConstants.StatusField.VM_RUNNING) && !StringUtils.equalsIgnoreCase(v.getInstanceStatus(),SpecialAttributesConstants.StatusField.STOPPED)){
                v.setInstanceStatus(SpecialAttributesConstants.StatusField.OTHER);
            }
        });
        vmList = vmList.stream().filter(v->StringUtils.isNotEmpty(v.getAccountId())).toList();
        Map<String,Long> byAccountMap = vmList.stream().filter(v->accountMap.containsKey(v.getAccountId())).collect(Collectors.groupingBy(AnalysisServerDTO::getAccountId, Collectors.counting()));
        result.put("byAccount",byAccountMap.entrySet().stream().map(c -> new KeyValue(StringUtils.isEmpty(accountMap.get(c.getKey()).getName())?c.getKey():accountMap.get(c.getKey()).getName(), c.getValue())).toList());
        Map<String,String> statusMap = new HashMap<>(3);
        statusMap.put(SpecialAttributesConstants.StatusField.VM_RUNNING,"运行中");
        statusMap.put(SpecialAttributesConstants.StatusField.STOPPED,"已停止");
        statusMap.put(SpecialAttributesConstants.StatusField.OTHER,"其他");
        Map<String,Long> byStatusMap = vmList.stream().collect(Collectors.groupingBy(AnalysisServerDTO::getInstanceStatus, Collectors.counting()));
        result.put("byStatus",byStatusMap.entrySet().stream().map(c -> new KeyValue(statusMap.get(c.getKey()), c.getValue())).toList());
        Map<String,String> chargeTypeMap = new HashMap<>(3);
        chargeTypeMap.put("PostPaid","按需按量");
        chargeTypeMap.put("PrePaid","包年包月");
        chargeTypeMap.put("SpotPaid","竞价");
        Map<String,Long> byChargeTypeMap = vmList.stream().filter(v->StringUtils.isNotEmpty(v.getInstanceChargeType())).collect(Collectors.groupingBy(AnalysisServerDTO::getInstanceChargeType, Collectors.counting()));
        result.put("byChargeType",byChargeTypeMap.entrySet().stream().map(c -> new KeyValue(chargeTypeMap.get(c.getKey()), c.getValue())).toList());
        return result;
    }

    /**
     * 获取虚拟机最近一次的监控数据 CPU、内存
     * @param list 每页查询的虚拟机数据
     */
    private void getVmPerfMetric(List<AnalysisServerDTO> list){
        List<String> instanceUuids = list.stream().map(AnalysisServerDTO::getInstanceUuid).filter(StringUtils::isNotEmpty).toList();
        try{
            Query query = getVmPerfMetricQuery(instanceUuids);
            SearchHits<Object> response = elasticsearchTemplate.search(query, Object.class, IndexCoordinates.of(IndexConstants.CE_PERF_METRIC_MONITOR_DATA.getCode()));
            ElasticsearchAggregations aggregations = (ElasticsearchAggregations) response.getAggregations();
            assert aggregations != null;
            List<StringTermsBucket> lastData = aggregations.aggregations().get(0).aggregation().getAggregate().sterms().buckets().array();
            List<PerfMetricMonitorData> metricMonitorDataList = new ArrayList<>();
            lastData.forEach(data-> data.aggregations().get("lastData").sterms().buckets().array().forEach(m->{
                PerfMetricMonitorData vo = JsonUtil.parseObject(Objects.requireNonNull(m.aggregations().get("top1").topHits().hits().hits().get(0).source()).toString(),PerfMetricMonitorData.class);
                metricMonitorDataList.add(vo);
            }));
            list.forEach(v-> metricMonitorDataList.stream().filter(d -> StringUtils.equalsIgnoreCase(v.getInstanceUuid(),d.getInstanceId())
                    && StringUtils.equalsIgnoreCase(v.getAccountId(),d.getCloudAccountId())).forEach(m->{
                if(StringUtils.equalsIgnoreCase(ResourcePerfMetricEnum.CPU_USED_UTILIZATION.name(),m.getMetricName())){
                    v.setCpuAverage(m.getAverage().setScale(3, RoundingMode.HALF_UP));
                    v.setCpuMaximum(m.getMaximum().setScale(3, RoundingMode.HALF_UP));
                    v.setCpuMinimum(m.getMinimum().setScale(3, RoundingMode.HALF_UP));
                }
                if(StringUtils.equalsIgnoreCase(ResourcePerfMetricEnum.MEMORY_USED_UTILIZATION.name(),m.getMetricName())){
                    v.setMemoryAverage(m.getAverage().setScale(3, RoundingMode.HALF_UP));
                    v.setMemoryMaximum(m.getMaximum().setScale(3, RoundingMode.HALF_UP));
                    v.setMemoryMinimum(m.getMinimum().setScale(3, RoundingMode.HALF_UP));
                }
                if(StringUtils.equalsIgnoreCase(ResourcePerfMetricEnum.DISK_USED_UTILIZATION.name(),m.getMetricName())){
                    v.setDiskAverage(m.getAverage().setScale(3, RoundingMode.HALF_UP));
                }
            }));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private org.springframework.data.elasticsearch.core.query.Query getVmPerfMetricQuery(List<String> instanceUuids) {
        List<QueryUtil.QueryCondition> queryConditions = new ArrayList<>();
        QueryUtil.QueryCondition entityType = new QueryUtil.QueryCondition(true, "entityType.keyword", F2CEntityType.VIRTUAL_MACHINE.toString(), QueryUtil.CompareType.EQ);
        queryConditions.add(entityType);
        if(CollectionUtils.isNotEmpty(instanceUuids)){
            QueryUtil.QueryCondition instanceIdQuery = new QueryUtil.QueryCondition(true, "instanceId.keyword", instanceUuids, QueryUtil.CompareType.IN);
            queryConditions.add(instanceIdQuery);
        }
        QueryUtil.QueryCondition accountId = new QueryUtil.QueryCondition(true, "cloudAccountId.keyword",null, QueryUtil.CompareType.NOT_EXIST);
        queryConditions.add(accountId);
        QueryUtil.QueryCondition metricName = new QueryUtil.QueryCondition(true, "metricName.keyword", Arrays.asList("CPU_USED_UTILIZATION","MEMORY_USED_UTILIZATION","DISK_USED_UTILIZATION"), QueryUtil.CompareType.IN);
        queryConditions.add(metricName);
        BoolQuery.Builder boolQuery = QueryUtil.getQuery(queryConditions);
        return new NativeQueryBuilder()
                .withPageable(PageRequest.of(0, 1))
                .withQuery(new co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder().bool(boolQuery.build()).build())
                //按实例与云账号ID分组
                .withAggregation("groupInstanceId",addAggregation())
                .build();
    }


    private Aggregation addAggregation(){
        Script script = new Script.Builder().inline(new InlineScript.Builder().source("doc['instanceId.keyword'].value+'_'+doc['cloudAccountId.keyword'].value").build()).build();
        Aggregation top1 = new Aggregation.Builder().topHits(new TopHitsAggregation.Builder().size(1).sort(SortOptions.of(s -> s.field(f -> f.field("timestamp").order(SortOrder.Desc)))).build()).build();
        return new Aggregation.Builder().terms(new TermsAggregation.Builder().script(script).size(Integer.MAX_VALUE).build()).aggregations("lastData",new Aggregation.Builder().terms(new TermsAggregation.Builder().field("metricName.keyword").size(Integer.MAX_VALUE).build()).aggregations("top1",top1).build()).build();
    }


    /**
     * 云主机资源使用率趋势
     */
    @Override
    public List<ChartData> getResourceTrendData(ResourceAnalysisRequest request){
        List<ChartData> result = new ArrayList<>();
        CalendarInterval intervalUnit = OperationUtils.getCalendarIntervalUnit(request.getStartTime(),request.getEndTime());
        try {
            request.setIntervalPosition(intervalUnit);
            SearchHits<PerfMetricMonitorData> response = elasticsearchTemplate.search(getSearchResourceTrendDataQuery(request), PerfMetricMonitorData.class, IndexCoordinates.of(IndexConstants.CE_PERF_METRIC_MONITOR_DATA.getCode()));
            result = iBaseResourceAnalysisService.convertToTrendData(response,intervalUnit);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.error("获取云主机资源使用趋势异常:"+e.getMessage());
        }
        return result;
    }

    /**
     * 查询云主机监控数据
     */
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
        MPJLambdaWrapper<VmCloudServer> queryServerWrapper = addServerAnalysisQuery(request);
        queryServerWrapper.notIn(true,VmCloudServer::getInstanceStatus, List.of(SpecialAttributesConstants.StatusField.VM_DELETE,SpecialAttributesConstants.StatusField.FAILED));
        List<AnalysisServerDTO> vmList = baseVmCloudServerMapper.selectJoinList(AnalysisServerDTO.class,queryServerWrapper);
        List<String> vmUuIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(vmList)) {
            vmUuIds = vmList.stream().map(VmCloudServer::getInstanceUuid).filter(StringUtils::isNotEmpty).toList();
        }
        if (CollectionUtils.isNotEmpty(vmUuIds)) {
            QueryUtil.QueryCondition resourceIds = new QueryUtil.QueryCondition(true, "instanceId.keyword", vmUuIds, QueryUtil.CompareType.IN);
            queryConditions.add(resourceIds);
        }
        return iBaseResourceAnalysisService.getRangeQuery(queryConditions, request.getIntervalPosition());
    }

    /**
     * 云主机在组织工作空间上分布
     */
    @Override
    public Map<String,List<BarTreeChartData>> analysisVmCloudServerByOrgWorkspace(ResourceAnalysisRequest request){
        Map<String,List<BarTreeChartData>> result = new HashMap<>(2);
        List<BarTreeChartData> workspaceList = workspaceSpread(request);
        if(request.isAnalysisWorkspace()){
            result.put("all",workspaceList);
            result.put("tree",workspaceList);
        }else{
            result = orgSpread(request,workspaceList);
        }
        return result;
    }

    /**
     * 组织分布
     */
    private Map<String,List<BarTreeChartData>> orgSpread(ResourceAnalysisRequest request, List<BarTreeChartData> workspaceList){
        Map<String,List<BarTreeChartData>> result = new HashMap<>(1);
        //组织下工作空间添加标识
        workspaceList.forEach(v-> v.setName(v.getName()+"(工作空间)"));
        //工作空间按照父级ID分组
        Map<String,List<BarTreeChartData>> workspaceMap = workspaceList.stream().collect(Collectors.groupingBy(BarTreeChartData::getPId));
        //查询所有组织，初始化为chart数据
        List<BarTreeChartData> orgList = initOrgChartData();
        //查询组织授权数据
        List<String> sourceIds = permissionService.getSourceIds();
        MPJLambdaWrapper<OrgWorkspace> orgWrapper = new MPJLambdaWrapper<>();
        orgWrapper.selectAll(OrgWorkspace.class);
        orgWrapper.selectCount(VmCloudServer::getId,BarTreeChartData::getValue);
        orgWrapper.eq(true, OrgWorkspace::getType,"org");
        orgWrapper.in(!CurrentUserUtils.isAdmin() && CollectionUtils.isNotEmpty(sourceIds),OrgWorkspace::getId,sourceIds);
        orgWrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()),VmCloudServer::getAccountId,request.getAccountIds());
        orgWrapper.notIn(true,VmCloudServer::getInstanceStatus, List.of(SpecialAttributesConstants.StatusField.VM_DELETE,SpecialAttributesConstants.StatusField.FAILED));
        orgWrapper.groupBy(OrgWorkspace::getId,OrgWorkspace::getName);
        orgWrapper.leftJoin(VmCloudServer.class,VmCloudServer::getSourceId,OrgWorkspace::getId);
        List<BarTreeChartData> list = orgWorkspaceMapper.selectJoinList(BarTreeChartData.class,orgWrapper);
        OperationUtils.initOrgWorkspaceAnalysisData(orgList,list);
        //初始化子级
        orgList.forEach(OperationUtils::setSelfToChildren);
        //扁平数据
        result.put("all",orgList);
        //树结构
        List<BarTreeChartData> chartDataList = new ArrayList<>();
        orgList.stream().filter(o->StringUtils.isEmpty(o.getPId())).forEach(v->{
            v.setGroupName("org");
            v.setName(v.getName()+"(组织)");
            v.getChildren().addAll(getChildren(v,orgList,workspaceMap));
            chartDataList.add(v);
        });
        //组织管理员的话，只有一个跟节点，然后只返回他的子集
        if (CurrentUserUtils.isOrgAdmin()) {
            result.put("tree",chartDataList.get(0).getChildren().stream().filter(this::childrenHasValue).toList());
        }else{
            result.put("tree",chartDataList.stream().filter(this::childrenHasValue).toList());
        }
        return result;
    }

    private boolean childrenHasValue(BarTreeChartData parent){
        return parent.getValue()>0;
    }

    /**
     * 工作空间分布
     * @param request 云主机分析查询条件
     * @return List<BarTreeChartData>
     */
    private List<BarTreeChartData> workspaceSpread(ResourceAnalysisRequest request){
        List<String> sourceIds = permissionService.getSourceIds();
        MPJLambdaWrapper<OrgWorkspace> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(OrgWorkspace.class);
        wrapper.selectCount(VmCloudServer::getId,BarTreeChartData::getValue);
        wrapper.eq(true, OrgWorkspace::getType,"workspace");
        wrapper.in(!CurrentUserUtils.isAdmin() && CollectionUtils.isNotEmpty(sourceIds),OrgWorkspace::getId,sourceIds);
        wrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()),VmCloudServer::getAccountId,request.getAccountIds());
        wrapper.notIn(true,VmCloudServer::getInstanceStatus, List.of(SpecialAttributesConstants.StatusField.VM_DELETE,SpecialAttributesConstants.StatusField.FAILED));
        wrapper.leftJoin(VmCloudServer.class,VmCloudServer::getSourceId,OrgWorkspace::getId);
        wrapper.groupBy(OrgWorkspace::getId,OrgWorkspace::getName);
        List<BarTreeChartData> chartDateWorkspaceList = orgWorkspaceMapper.selectJoinList(BarTreeChartData.class, wrapper);
        List<BarTreeChartData> workspaceList = initWorkspaceChartData();
        OperationUtils.initOrgWorkspaceAnalysisData(workspaceList,chartDateWorkspaceList);
        return workspaceList.stream().filter(v->v.getValue()!=0).toList();
    }

    /**
     * 初始化工作空间柱状图数据
     * @return List<BarTreeChartData>
     */
    @Override
    public List<BarTreeChartData> initWorkspaceChartData(){
        List<String> sourceIds = permissionService.getSourceIds();
        return iBaseWorkspaceService.list().stream().map(v->{
            BarTreeChartData barTreeChartData = new BarTreeChartData();
            barTreeChartData.setId(v.getId());
            barTreeChartData.setName(v.getName());
            barTreeChartData.setValue(0L);
            barTreeChartData.setPId(v.getOrganizationId());
            return barTreeChartData;
        }).toList().stream().filter(v->checkSourceId(sourceIds,v.getId())).toList();
    }

    /**
     * 初始化工作空间柱状图数据
     * @return List<BarTreeChartData>
     */
    @Override
    public List<BarTreeChartData> initOrgChartData(){
        List<String> sourceIds = permissionService.getSourceIds();
        return iBaseOrganizationService.list().stream().map(v->{
            BarTreeChartData barTreeChartData = new BarTreeChartData();
            barTreeChartData.setId(v.getId());
            barTreeChartData.setName(v.getName());
            barTreeChartData.setValue(0L);
            barTreeChartData.setPId(v.getPid());
            barTreeChartData.setChildren(new ArrayList<>());
            return barTreeChartData;
        }).toList().stream().filter(v->checkSourceId(sourceIds,v.getId())).toList();
    }

    private boolean checkSourceId(List<String> sourceIds,String id){
        if(CurrentUserUtils.isAdmin()){
            return true;
        }else{
            return sourceIds.contains(id);
        }
    }

    /**
     * 递归获取子级
     * @param barTreeChartData 父级
     * @param list 子级
     * @param workspaceMap 工作空间按组织ID分组
     * @return List<BarTreeChartData>
     */
    @Override
    public List<BarTreeChartData> getChildren(BarTreeChartData barTreeChartData,List<BarTreeChartData> list,Map<String,List<BarTreeChartData>> workspaceMap) {
        //父级数量加上子级数量作为父级总量
        OperationUtils.workspaceToOrgChildren(workspaceMap, barTreeChartData);
        List<BarTreeChartData> filterList = list.stream().filter(u -> StringUtils.equalsIgnoreCase(u.getPId(), barTreeChartData.getId())).toList();
        filterList.forEach(u->{
            u.setName(u.getName() + "(子组织)");
            // 用于区分组织与工作空间
            u.setGroupName("org");
            u.getChildren().addAll(getChildren(u, list, workspaceMap));
            barTreeChartData.setValue(barTreeChartData.getValue() + u.getValue());
        });
        return filterList.stream().filter(v->v.getValue()>0).toList();
    }

    @Override
    public long countCloudServerByCloudAccount(String cloudAccountId) {
        List<String> sourceIds = permissionService.getSourceIds();
        MPJLambdaWrapper<VmCloudServer> wrapper = new MPJLambdaWrapper<>();
        wrapper.isNotNull(true,VmCloudServer::getAccountId);
        wrapper.eq(StringUtils.isNotEmpty(cloudAccountId),VmCloudServer::getAccountId,cloudAccountId);
        wrapper.in(!CurrentUserUtils.isAdmin() && CollectionUtils.isNotEmpty(sourceIds),VmCloudServer::getSourceId,sourceIds);
        wrapper.notIn(true, VmCloudServer::getInstanceStatus, List.of(SpecialAttributesConstants.StatusField.VM_DELETE,SpecialAttributesConstants.StatusField.FAILED));
        return baseVmCloudServerMapper.selectCount(wrapper);
    }

}