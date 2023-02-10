package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch._types.InlineScript;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.RecycleBin;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseRecycleBinMapper;
import com.fit2cloud.base.mapper.BaseVmCloudServerMapper;
import com.fit2cloud.common.constants.RecycleBinStatusConstants;
import com.fit2cloud.common.constants.ResourceTypeConstants;
import com.fit2cloud.common.es.constants.IndexConstants;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.constants.OptimizationConstants;
import com.fit2cloud.controller.request.optimize.PageOptimizationRequest;
import com.fit2cloud.dto.AnalyticsServerDTO;
import com.fit2cloud.service.IOptimizeAnalysisService;
import com.fit2cloud.service.IPermissionService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jianneng
 * {@code @date} 2022/12/24 11:29
 **/
@Service
public class OptimizeAnalysisServiceImpl implements IOptimizeAnalysisService {

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    @Resource
    private IPermissionService permissionService;
    @Resource
    private BaseVmCloudServerMapper baseVmCloudServerMapper;

    @Resource
    private BaseRecycleBinMapper baseRecycleBinMapper;

    @Override
    public  IPage<AnalyticsServerDTO> pageServer(PageOptimizationRequest request) {
        if(StringUtils.equalsIgnoreCase(OptimizationConstants.DERATING.getCode(),request.getOptimizeSuggest())
                || StringUtils.equalsIgnoreCase(OptimizationConstants.UPGRADE.getCode(),request.getOptimizeSuggest())){
            return getDeratingUpgrade(request);
        }
        if(StringUtils.equalsIgnoreCase(OptimizationConstants.PAYMENT.getCode(),request.getOptimizeSuggest())){
            return getPayment(request);
        }
        if(StringUtils.equalsIgnoreCase(OptimizationConstants.RECOVERY.getCode(),request.getOptimizeSuggest())){
            return getRecover(request);
        }
        return new Page<>();
    }

    private IPage<AnalyticsServerDTO> getDeratingUpgrade(PageOptimizationRequest request){
        boolean isDerating = StringUtils.equalsIgnoreCase(OptimizationConstants.DERATING.getCode(),request.getOptimizeSuggest());
        List<AnalyticsServerDTO> metricList = getMetricData(request,  isDerating);
        if(CollectionUtils.isEmpty(metricList)){
            return new Page<>();
        }
        List<String> instanceUuids = metricList.stream().map(AnalyticsServerDTO::getInstanceUuid).toList();
        Page<AnalyticsServerDTO> page = PageUtil.of(request, AnalyticsServerDTO.class, null, true);
        MPJLambdaWrapper<VmCloudServer> queryServerWrapper = addServerAnalysisQuery(request);
        queryServerWrapper.in(true,VmCloudServer::getInstanceUuid,instanceUuids);
        IPage<AnalyticsServerDTO> pageData = baseVmCloudServerMapper.selectJoinPage(page,AnalyticsServerDTO.class,queryServerWrapper);
        if(pageData.getRecords().size()>0){
            OptimizationConstants optimizationConstants = OptimizationConstants.getByCode(request.getOptimizeSuggest());
            List<String> resourceIds = pageData.getRecords().stream().filter(vm->StringUtils.isNotEmpty(vm.getInstanceUuid())).map(AnalyticsServerDTO::getInstanceUuid).toList();
            request.setInstanceUuids(resourceIds);
            Map<String,AnalyticsServerDTO>  metricDataMap = metricList.stream().collect(Collectors.toMap(server -> server.getInstanceUuid()+server.getAccountId(),o->o,(k1,k2)->k1));
            pageData.getRecords().forEach(v->{
                String key = v.getInstanceUuid()+v.getAccountId();
                assert optimizationConstants != null;
                v.setOptimizeSuggest(optimizationConstants.getName());
                v.setOptimizeSuggestCode(optimizationConstants.getCode());
                if(metricDataMap.containsKey(key)){
                    AnalyticsServerDTO metricDto = metricDataMap.get(key);
                    v.setContent(metricDto.getContent());
                    v.setCpuMaximum(metricDto.getCpuMaximum());
                    v.setCpuAverage(metricDto.getCpuAverage());
                    v.setMemoryMaximum(metricDto.getMemoryMaximum());
                    v.setMemoryAverage(metricDto.getMemoryAverage());
                }
            });
            return pageData;
        }
        return new Page<>();
    }

    private IPage<AnalyticsServerDTO> getPayment(PageOptimizationRequest request){
        OptimizationConstants optimizationConstants = OptimizationConstants.getByCode(request.getOptimizeSuggest());
        Page<AnalyticsServerDTO> page = PageUtil.of(request, AnalyticsServerDTO.class, null, true);
        MPJLambdaWrapper<VmCloudServer> queryServerWrapper = addServerAnalysisQuery(request);
        // 按量付费资源持续开机时间超过day天建议变更付费方式为包年包月的云主机
        queryServerWrapper.and(a1->
                a1.and(a2->a2.eq(true,VmCloudServer::getInstanceStatus,"Running")
                                .eq(true,VmCloudServer::getInstanceChargeType,"PostPaid")
                                .le(AnalyticsServerDTO::getLastOperateTime,LocalDateTime.now().minusDays(request.getVolumeContinuedDays())))
                        .or(a->{
                            // 包年包月资源持续关机时间超过day天建议变更付费方式为按量付费的云主机
                            a.eq(true,VmCloudServer::getInstanceStatus,"Stopped");
                            a.eq(true,VmCloudServer::getInstanceChargeType,"PrePaid");
                            a.le(VmCloudServer::getLastOperateTime,LocalDateTime.now().minusDays(request.getCycleContinuedDays()));
                        })
        );
        IPage<AnalyticsServerDTO> pageData = baseVmCloudServerMapper.selectJoinPage(page,AnalyticsServerDTO.class,queryServerWrapper);
        // 根据状态判断变更方式
        if(pageData.getRecords().size()>0){
            pageData.getRecords().forEach(vm->{
                assert optimizationConstants != null;
                vm.setOptimizeSuggest(optimizationConstants.getName());
                vm.setOptimizeSuggestCode(optimizationConstants.getCode());
                StringJoiner sj = new StringJoiner("");
                if(StringUtils.equalsIgnoreCase(vm.getInstanceStatus(),"Running")){
                    sj.add("持续开机").add(String.valueOf(request.getVolumeContinuedDays())).add("天以上，建议转为包年包月");
                }
                if(StringUtils.equalsIgnoreCase(vm.getInstanceStatus(),"Stopped")){
                    sj.add("持续关机").add(String.valueOf(request.getCycleContinuedDays())).add("天以上，建议转为按需按量");
                }
                vm.setContent(sj.toString());
            });
            return pageData;
        }
        return new Page<>();
    }

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now().minusDays(10));
        System.out.println(LocalDateTime.now());
    }

    private IPage<AnalyticsServerDTO> getRecover(PageOptimizationRequest request){
        QueryWrapper recycleQuery = new QueryWrapper<>();
        recycleQuery.eq(true, "recycle_bin.resource_type", ResourceTypeConstants.VM.name());
        recycleQuery.eq(true, "recycle_bin.status", RecycleBinStatusConstants.ToBeRecycled.name());
        List<RecycleBin> recycleBins = baseRecycleBinMapper.selectList(recycleQuery);
        List<String> recycleBinServerIds = recycleBins.stream().map(RecycleBin::getResourceId).toList();
        OptimizationConstants optimizationConstants = OptimizationConstants.getByCode(request.getOptimizeSuggest());
        Page<AnalyticsServerDTO> page = PageUtil.of(request, AnalyticsServerDTO.class, null, true);
        MPJLambdaWrapper<VmCloudServer> queryServerWrapper = addServerAnalysisQuery(request);
        queryServerWrapper.eq(true,VmCloudServer::getInstanceStatus,"Stopped");
        queryServerWrapper.or(or->{
            or.in(CollectionUtils.isNotEmpty(recycleBinServerIds),VmCloudServer::getId,recycleBinServerIds);
        });
        queryServerWrapper.le(VmCloudServer::getLastOperateTime,LocalDateTime.now().minusDays(request.getContinuedDays()));
        IPage<AnalyticsServerDTO> pageData = baseVmCloudServerMapper.selectJoinPage(page,AnalyticsServerDTO.class,queryServerWrapper);
        if(pageData.getRecords().size()>0){
            pageData.getRecords().forEach(vm-> {
                        assert optimizationConstants != null;
                        vm.setOptimizeSuggest(optimizationConstants.getName());
                        vm.setOptimizeSuggestCode(optimizationConstants.getCode());
                        if(recycleBinServerIds.contains(vm.getId())){
                            vm.setContent("回收站");
                        }else{
                            vm.setContent("持续关机" + request.getContinuedDays() + "天以上，闲置机器建议回收删除");
                        }
                    }
                    );
            return pageData;
        }
        return new Page<>();
    }


    /**
     * 云主机分析参数
     */
    private MPJLambdaWrapper<VmCloudServer> addServerAnalysisQuery(PageOptimizationRequest request) {
        List<String> sourceIds = permissionService.getSourceIds();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(sourceIds)) {
            request.setSourceIds(sourceIds);
        }
        MPJLambdaWrapper<VmCloudServer> wrapper = addServerQuery(new MPJLambdaWrapper<>(),request.getAccountIds());
        wrapper.selectAll(VmCloudServer.class);
        wrapper.orderByDesc(VmCloudServer::getCreateTime);
        wrapper.selectAs(CloudAccount::getName,AnalyticsServerDTO::getAccountName);
        wrapper.selectAs(CloudAccount::getPlatform,AnalyticsServerDTO::getPlatform);
        wrapper.like(StringUtils.isNotBlank(request.getInstanceName()), VmCloudServer::getInstanceName, request.getInstanceName());
        wrapper.in(CollectionUtils.isNotEmpty(request.getSourceIds()), VmCloudServer::getSourceId, request.getSourceIds());
        return wrapper;
    }

    private List<AnalyticsServerDTO> getMetricData(PageOptimizationRequest request,boolean isDerating){
        List<AnalyticsServerDTO> metricData = new ArrayList<>();
        String mark = isDerating?"<":">";
        // -1表示小于，1表示大于
        int flag = isDerating?-1:1;
        // 优化建议原因
        StringJoiner sj = new StringJoiner("");
        sj.add("持续").add(String.valueOf(request.getDays())).add("天以上，CPU")
                .add(request.isCpuMaxRate()?"最大":"平均").add("使用率").add(mark).add(String.valueOf(request.getCpuRate()))
                .add("%，内存").add(request.isMemoryMaxRate()?"最大":"平均").add("使用率").add(mark).add(String.valueOf(request.getMemoryRate())).add("%")
                .add("，建议").add(isDerating?"降低":"升级").add("配置");
        //查询监控数据
        List<AnalyticsServerDTO> list = getVmPerfMetric(request);
        //条件判断
        list.forEach(vo->{
            vo.setContent(sj.toString());
            boolean cpuIsNull = Objects.isNull(vo.getCpuAverage());
            boolean memoryIsNull = Objects.isNull(vo.getMemoryAverage());
            //CPU内存都没有监控
            if(cpuIsNull && memoryIsNull){
                return;
            }
            Map<String,Boolean> cpuCompare = new HashMap<>();
            cpuCompare.put("cpu",false);
            if(!cpuIsNull){
                if(request.isCpuMaxRate()){
                    cpuCompare.put("cpu",vo.getCpuMaximum().compareTo(BigDecimal.valueOf(request.getCpuRate())) == flag);
                }else{
                    cpuCompare.put("cpu",vo.getCpuAverage().compareTo(BigDecimal.valueOf(request.getCpuRate())) == flag);
                }
            }
            Map<String,Boolean> memoryCompare = new HashMap<>();
            cpuCompare.put("memory",false);
            if(!memoryIsNull){
                if(request.isMemoryMaxRate()){
                    memoryCompare.put("memory", vo.getMemoryMaximum().compareTo(BigDecimal.valueOf(request.getMemoryRate())) == flag);
                }else{
                    memoryCompare.put("memory", vo.getMemoryAverage().compareTo(BigDecimal.valueOf(request.getMemoryRate())) == flag);
                }
            }
            if(StringUtils.equalsIgnoreCase("and",request.getConditionOr()) && ((cpuCompare.get("cpu") || cpuIsNull) && (cpuCompare.get("memory") || memoryIsNull))){
                metricData.add(vo);
            }
            if(StringUtils.equalsIgnoreCase("or",request.getConditionOr()) && (cpuCompare.get("cpu") || cpuCompare.get("memory"))){
                metricData.add(vo);
            }

        });
        request.setInstanceUuids(metricData.stream().map(AnalyticsServerDTO::getInstanceUuid).collect(Collectors.toList()));
        return metricData;
    }

    /**
     * 查询云主机统一参数
     * 云账号、排除已删除
     */
    private MPJLambdaWrapper<VmCloudServer> addServerQuery( MPJLambdaWrapper<VmCloudServer> wrapper,List<String> accountIds) {
        wrapper.in(CollectionUtils.isNotEmpty(accountIds), VmCloudServer::getAccountId, accountIds);
        wrapper.leftJoin(CloudAccount.class,CloudAccount::getId,VmCloudServer::getAccountId);
        wrapper.notIn(true,VmCloudServer::getInstanceStatus, List.of("Deleted"));
        return wrapper;
    }


    private List<AnalyticsServerDTO> getVmPerfMetric(PageOptimizationRequest optimizationParam) {
        List<AnalyticsServerDTO> cloudServerMetricData = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        long sinceTime = currentTime - 1000L*60*60*24*optimizationParam.getDays();
        List<QueryUtil.QueryCondition> queryConditions = new ArrayList<>();
        QueryUtil.QueryCondition start = new QueryUtil.QueryCondition(true, "timestamp", sinceTime, QueryUtil.CompareType.GTE);
        queryConditions.add(start);
        QueryUtil.QueryCondition end = new QueryUtil.QueryCondition(true, "timestamp", currentTime, QueryUtil.CompareType.LTE);
        queryConditions.add(end);
        QueryUtil.QueryCondition type = new QueryUtil.QueryCondition(true, "entityType.keyword", "VIRTUAL_MACHINE", QueryUtil.CompareType.EQ);
        queryConditions.add(type);
        QueryUtil.QueryCondition instanceId = new QueryUtil.QueryCondition(CollectionUtils.isNotEmpty(optimizationParam.getInstanceUuids()), "instanceId.keyword", optimizationParam.getInstanceUuids(), QueryUtil.CompareType.IN);
        queryConditions.add(instanceId);
        BoolQuery.Builder boolQuery = QueryUtil.getQuery(queryConditions);

        Query query = new NativeQueryBuilder()
                .withPageable(PageRequest.of(0, 1))
                .withQuery(new co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder().bool(boolQuery.build()).build())
                //按实例与云账号ID分组
                .withAggregation("groupInstanceId",addAggregation())
                .build();
        try {
            SearchHits<Object> response = elasticsearchTemplate.search(query, Object.class, IndexCoordinates.of(IndexConstants.CE_PERF_METRIC_MONITOR_DATA.getCode()));
            ElasticsearchAggregations aggregations = (ElasticsearchAggregations) response.getAggregations();
            assert aggregations != null;
            List<StringTermsBucket> list = aggregations.aggregations().get(0).aggregation().getAggregate().sterms().buckets().array();
            for (StringTermsBucket v : list) {
                AnalyticsServerDTO dto = new AnalyticsServerDTO();
                dto.setInstanceUuid(v.key().split("_")[0]);
                dto.setAccountId(v.key().split("_")[1]);
                //CPU
                if(v.aggregations().get("cpu").filter().aggregations().get("metricName").sterms().buckets().array().size()>0){
                    double cpuMax = v.aggregations().get("cpu").filter().aggregations().get("max").max().value();
                    dto.setCpuMaximum(BigDecimal.valueOf(cpuMax).setScale(3, RoundingMode.HALF_UP));
                    double cpuAvg = v.aggregations().get("cpu").filter().aggregations().get("avg").avg().value();
                    dto.setCpuAverage(BigDecimal.valueOf(cpuAvg).setScale(3, RoundingMode.HALF_UP));
                }
                //内存
                if(v.aggregations().get("memory").filter().aggregations().get("metricName").sterms().buckets().array().size()>0){
                    double memoryMax = v.aggregations().get("memory").filter().aggregations().get("max").max().value();
                    dto.setMemoryMaximum(BigDecimal.valueOf(memoryMax).setScale(3, RoundingMode.HALF_UP));
                    double memoryAvg = v.aggregations().get("memory").filter().aggregations().get("avg").avg().value();
                    dto.setMemoryAverage(BigDecimal.valueOf(memoryAvg).setScale(3, RoundingMode.HALF_UP));
                }
                cloudServerMetricData.add(dto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return cloudServerMetricData;
    }

    private Aggregation addAggregation(){
        Script script = new Script.Builder().inline(new InlineScript.Builder().source("doc['instanceId.keyword'].value+'_'+doc['cloudAccountId.keyword'].value").build()).build();
        Aggregation cpu = new Aggregation.Builder().filter(filterQuery->filterQuery.term(t->t.field("metricName.keyword").value("CPU_USED_UTILIZATION")))
                .aggregations("avg",new Aggregation.Builder().avg(new AverageAggregation.Builder().field("average").build()).build())
                .aggregations("max",new Aggregation.Builder().max(new MaxAggregation.Builder().field("maximum").build()).build())
                .aggregations("metricName",new Aggregation.Builder().terms(new TermsAggregation.Builder().field("metricName.keyword").build()).build())
                .build();
        Aggregation memory = new Aggregation.Builder().filter(filterQuery->filterQuery.term(t->t.field("metricName.keyword").value("MEMORY_USED_UTILIZATION")))
                .aggregations("avg",new Aggregation.Builder().avg(new AverageAggregation.Builder().field("average").build()).build())
                .aggregations("max",new Aggregation.Builder().max(new MaxAggregation.Builder().field("maximum").build()).build())
                .aggregations("metricName",new Aggregation.Builder().terms(new TermsAggregation.Builder().field("metricName.keyword").build()).build())
                .build();
        return new Aggregation.Builder().terms(new TermsAggregation.Builder().script(script).size(Integer.MAX_VALUE).build()).aggregations("cpu",cpu).aggregations("memory",memory).build();
    }


}
