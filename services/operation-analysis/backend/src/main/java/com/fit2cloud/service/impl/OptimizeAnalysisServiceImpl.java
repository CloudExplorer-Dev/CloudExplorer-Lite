package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.InlineScript;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
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
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.constants.OptimizationConstants;
import com.fit2cloud.constants.ResourcePerfMetricEnum;
import com.fit2cloud.constants.SpecialAttributesConstants;
import com.fit2cloud.controller.request.optimize.PageOptimizationRequest;
import com.fit2cloud.dto.AnalysisServerDTO;
import com.fit2cloud.service.IOptimizeAnalysisService;
import com.fit2cloud.service.IPermissionService;
import com.fit2cloud.service.IServerAnalysisService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
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
    @Resource
    private CurrentUserResourceService currentUserResourceService;
    @Resource
    private IServerAnalysisService iServerAnalysisService;

    @Override
    public IPage<AnalysisServerDTO> pageServer(PageOptimizationRequest request) {
        if (CollectionUtils.isEmpty(request.getAccountIds())) {
            request.setAccountIds(currentUserResourceService.currentUserCloudAccountList().stream().map(CloudAccount::getId).toList());
        }
        if (!CurrentUserUtils.isAdmin() && CollectionUtils.isEmpty(request.getAccountIds())) {
            return new Page<>();
        }
        if (StringUtils.equalsIgnoreCase(OptimizationConstants.DERATING.getCode(), request.getOptimizeSuggest())
                || StringUtils.equalsIgnoreCase(OptimizationConstants.UPGRADE.getCode(), request.getOptimizeSuggest())) {
            return getDeratingUpgrade(request);
        }
        if (StringUtils.equalsIgnoreCase(OptimizationConstants.PAYMENT.getCode(), request.getOptimizeSuggest())) {
            return getPayment(request);
        }
        if (StringUtils.equalsIgnoreCase(OptimizationConstants.RECOVERY.getCode(), request.getOptimizeSuggest())) {
            return getRecover(request);
        }
        return new Page<>();
    }

    private IPage<AnalysisServerDTO> getDeratingUpgrade(PageOptimizationRequest request) {
        boolean isDerating = StringUtils.equalsIgnoreCase(OptimizationConstants.DERATING.getCode(), request.getOptimizeSuggest());
        List<AnalysisServerDTO> metricList = getMetricData(request, isDerating);
        if (CollectionUtils.isEmpty(metricList)) {
            return new Page<>();
        }
        //根据uuid以及条件分页查询
        Page<AnalysisServerDTO> page = PageUtil.of(request, AnalysisServerDTO.class, null, true);
        MPJLambdaWrapper<VmCloudServer> queryServerWrapper = addServerAnalysisQuery(request);
        queryServerWrapper.in(CollectionUtils.isNotEmpty(request.getInstanceUuids()), VmCloudServer::getInstanceUuid, request.getInstanceUuids());
        IPage<AnalysisServerDTO> pageData = baseVmCloudServerMapper.selectJoinPage(page, AnalysisServerDTO.class, queryServerWrapper);
        //分页数据添加监控数据
        if (CollectionUtils.isNotEmpty(pageData.getRecords())) {
            OptimizationConstants optimizationConstants = OptimizationConstants.getByCode(request.getOptimizeSuggest());
            //监控数据根据UUID分组
            Map<String, AnalysisServerDTO> metricDataMap = metricList.stream().collect(Collectors.toMap(VmCloudServer::getInstanceUuid, o -> o, (k1, k2) -> k1));
            pageData.getRecords().forEach(v -> {
                String key = v.getInstanceUuid();
                assert optimizationConstants != null;
                v.setOptimizeSuggest(optimizationConstants.getName());
                v.setOptimizeSuggestCode(optimizationConstants.getCode());
                if (metricDataMap.containsKey(key)) {
                    AnalysisServerDTO metricDto = metricDataMap.get(key);
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

    private IPage<AnalysisServerDTO> getPayment(PageOptimizationRequest request) {
        OptimizationConstants optimizationConstants = OptimizationConstants.getByCode(request.getOptimizeSuggest());
        Page<AnalysisServerDTO> page = PageUtil.of(request, AnalysisServerDTO.class, null, true);
        MPJLambdaWrapper<VmCloudServer> queryServerWrapper = addServerAnalysisQuery(request);
        // 按量付费资源持续开机时间超过day天建议变更付费方式为包年包月的云主机
        queryServerWrapper.and(a1 ->
                a1.and(a2 -> a2.eq(true, VmCloudServer::getInstanceStatus, "Running")
                                .eq(true, VmCloudServer::getInstanceChargeType, "PostPaid")
                                .le(AnalysisServerDTO::getLastOperateTime, LocalDateTime.now().minusDays(request.getVolumeContinuedDays())))
                        .or(a -> {
                            // 包年包月资源持续关机时间超过day天建议变更付费方式为按量付费的云主机
                            a.eq(true, VmCloudServer::getInstanceStatus, SpecialAttributesConstants.StatusField.STOPPED);
                            a.eq(true, VmCloudServer::getInstanceChargeType, "PrePaid");
                            a.le(VmCloudServer::getLastOperateTime, LocalDateTime.now().minusDays(request.getCycleContinuedDays()));
                        })
        );
        IPage<AnalysisServerDTO> pageData = baseVmCloudServerMapper.selectJoinPage(page, AnalysisServerDTO.class, queryServerWrapper);
        // 根据状态判断变更方式
        if (CollectionUtils.isNotEmpty(pageData.getRecords())) {
            pageData.getRecords().forEach(vm -> {
                assert optimizationConstants != null;
                vm.setOptimizeSuggest(optimizationConstants.getName());
                vm.setOptimizeSuggestCode(optimizationConstants.getCode());
                StringJoiner sj = new StringJoiner("");
                if (StringUtils.equalsIgnoreCase(vm.getInstanceStatus(), SpecialAttributesConstants.StatusField.VM_RUNNING)) {
                    sj.add("持续开机").add(String.valueOf(request.getVolumeContinuedDays())).add("天以上，建议转为包年包月");
                }
                if (StringUtils.equalsIgnoreCase(vm.getInstanceStatus(), SpecialAttributesConstants.StatusField.STOPPED)) {
                    sj.add("持续关机").add(String.valueOf(request.getCycleContinuedDays())).add("天以上，建议转为按需按量");
                }
                vm.setContent(sj.toString());
            });
            iServerAnalysisService.getVmPerfMetric(pageData.getRecords());
            return pageData;
        }
        return new Page<>();
    }


    private IPage<AnalysisServerDTO> getRecover(PageOptimizationRequest request) {
        QueryWrapper<RecycleBin> recycleQuery = new QueryWrapper<>();
        recycleQuery.eq(true, "recycle_bin.resource_type", ResourceTypeConstants.VM.name());
        recycleQuery.eq(true, "recycle_bin.status", RecycleBinStatusConstants.ToBeRecycled.name());
        // 回收站的
        List<RecycleBin> recycleBins = baseRecycleBinMapper.selectList(recycleQuery);
        // 回收站的资源ID
        List<String> temp = recycleBins.stream().map(RecycleBin::getResourceId).toList();
        List<String> recycleBinServerIds = new ArrayList<>();
        List<VmCloudServer> recycleBinServer = new ArrayList<>();
        // 回收站资源
        if (CollectionUtils.isNotEmpty(temp)) {
            recycleBinServer = baseVmCloudServerMapper.selectBatchIds(temp);
        }
        if (CollectionUtils.isNotEmpty(request.getAccountIds())) {
            recycleBinServer = recycleBinServer.stream().filter(v -> request.getAccountIds().contains(v.getAccountId())).toList();
        }
        if (StringUtils.isNotEmpty(request.getInstanceName())) {
            recycleBinServer = recycleBinServer.stream().filter(v -> v.getInstanceName().contains(request.getInstanceName())).toList();
        }
        if (CollectionUtils.isNotEmpty(recycleBinServer)) {
            recycleBinServerIds.addAll(recycleBinServer.stream().filter(this::filterRecycleBinServer).filter(v -> !StringUtils.equalsIgnoreCase("Deleted", v.getInstanceStatus())).map(VmCloudServer::getId).toList());
        }
        OptimizationConstants optimizationConstants = OptimizationConstants.getByCode(request.getOptimizeSuggest());
        Page<AnalysisServerDTO> page = PageUtil.of(request, AnalysisServerDTO.class, null, true);
        MPJLambdaWrapper<VmCloudServer> queryServerWrapper = addServerAnalysisQuery(request);
        queryServerWrapper.eq(true, VmCloudServer::getInstanceStatus, SpecialAttributesConstants.StatusField.STOPPED);
        queryServerWrapper.le(VmCloudServer::getLastOperateTime, LocalDateTime.now().minusDays(request.getContinuedDays()));
        if (CollectionUtils.isNotEmpty(recycleBinServerIds)) {
            queryServerWrapper.or(or -> or.in(true, VmCloudServer::getId, recycleBinServerIds));
        }
        IPage<AnalysisServerDTO> pageData = baseVmCloudServerMapper.selectJoinPage(page, AnalysisServerDTO.class, queryServerWrapper);
        if (CollectionUtils.isNotEmpty(pageData.getRecords())) {
            pageData.getRecords().forEach(vm -> {
                        assert optimizationConstants != null;
                        vm.setOptimizeSuggest(optimizationConstants.getName());
                        vm.setOptimizeSuggestCode(optimizationConstants.getCode());
                        if (recycleBinServerIds.contains(vm.getId())) {
                            vm.setContent("回收站");
                        } else {
                            vm.setContent("持续关机" + request.getContinuedDays() + "天以上，闲置机器建议回收删除");
                        }
                    }
            );
            iServerAnalysisService.getVmPerfMetric(pageData.getRecords());
            return pageData;
        }
        return new Page<>();
    }

    /**
     * 过滤回收站云主机
     * 是否拥有资源相同目录权限
     * 自己的资源
     * 管理员
     */
    private boolean filterRecycleBinServer(VmCloudServer server) {
        List<String> sourceIds = permissionService.getSourceIds();
        //拥有权限
        boolean source = sourceIds.contains(server.getSourceId());
        //自己的机器
        boolean self = StringUtils.equalsIgnoreCase(server.getApplyUser(), CurrentUserUtils.getUser().getId());
        //管理员
        boolean isAdmin = CurrentUserUtils.isAdmin();
        return source || self || isAdmin;
    }


    /**
     * 云主机分析参数
     */
    private MPJLambdaWrapper<VmCloudServer> addServerAnalysisQuery(PageOptimizationRequest request) {
        List<String> sourceIds = permissionService.getSourceIds();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(sourceIds)) {
            request.setSourceIds(sourceIds);
        }
        MPJLambdaWrapper<VmCloudServer> wrapper = addServerQuery(new MPJLambdaWrapper<>(), request.getAccountIds());
        wrapper.selectAll(VmCloudServer.class);
        wrapper.orderByDesc(VmCloudServer::getCreateTime);
        wrapper.selectAs(CloudAccount::getName, AnalysisServerDTO::getAccountName);
        wrapper.selectAs(CloudAccount::getPlatform, AnalysisServerDTO::getPlatform);
        wrapper.like(StringUtils.isNotBlank(request.getInstanceName()), VmCloudServer::getInstanceName, request.getInstanceName());
        wrapper.in(CollectionUtils.isNotEmpty(request.getSourceIds()), VmCloudServer::getSourceId, request.getSourceIds());
        return wrapper;
    }

    private List<AnalysisServerDTO> getMetricData(PageOptimizationRequest request, boolean isDerating) {
        String optimizationMsg = "持续{0}天以上，CPU{1}使用率{2}{3}%{4}内存{5}使用率{6}{7}%,建议{8}";
        List<AnalysisServerDTO> metricData = new ArrayList<>();
        //升配降配表示符
        Map<Boolean, String> upgradeCharacter = new HashMap<>(2);
        //升配
        upgradeCharacter.put(true, ">=");
        //降配
        upgradeCharacter.put(false, "<=");
        //升配降配
        Map<Boolean, String> upgradeLabel = new HashMap<>(2);
        //升配
        upgradeLabel.put(true, "升配");
        //降配
        upgradeLabel.put(false, "降配");
        //最大或者平均值
        Map<Boolean, String> maxLabel = new HashMap<>(2);
        maxLabel.put(true, "最大");
        maxLabel.put(false, "平均");
        //条件
        Map<Boolean, String> conditionLabel = new HashMap<>(2);
        conditionLabel.put(true, "并且");
        conditionLabel.put(false, "或");
        //条件
        boolean isAnd = request.getConditionOr().equalsIgnoreCase(SpecialAttributesConstants.SpecialField.AND);
        // 优化建议原因
        String[] contentArray = new String[]{
                String.valueOf(request.getDays()),
                maxLabel.get(request.isCpuMaxRate()),
                upgradeCharacter.get(!isDerating),
                String.valueOf(request.getCpuRate()),
                conditionLabel.get(isAnd),
                maxLabel.get(request.isMemoryMaxRate()),
                upgradeCharacter.get(!isDerating),
                String.valueOf(request.getMemoryRate()),
                upgradeLabel.get(!isDerating)};
        optimizationMsg = MessageFormat.format(optimizationMsg, contentArray);
        // 查询监控数据
        List<AnalysisServerDTO> list = getVmPerfMetric(request, isDerating);
        // 条件判断
        // -1表示小于，1表示大于
        Map<Boolean, Integer> containValue = new HashMap<>(2);
        containValue.put(true, -1);
        containValue.put(false, 1);
        // 判断没有监控数据依据是，平均值为空，理论上平均值为空，最大值也是为空的，所以某个指标的平均值为空，那么认为这个指标没有监控数据
        String finalOptimizationMsg = optimizationMsg;
        list.forEach(vo -> {
            vo.setContent(finalOptimizationMsg);
            // 空数据指标
            boolean cpuIsNull = Objects.isNull(vo.getCpuAverage());
            boolean memoryIsNull = Objects.isNull(vo.getMemoryAverage());
            // CPU比较结果
            Map<String, Boolean> cpuCompare = cpuCompare(cpuIsNull, vo, request, containValue, isDerating);
            // 内存比较结果
            Map<String, Boolean> memoryCompare = memoryCompare(memoryIsNull, vo, request, containValue, isDerating);
            boolean cpuCompareResult = cpuCompare.get(SpecialAttributesConstants.ResourceField.CPU);
            boolean memoryCompareResult = memoryCompare.get(SpecialAttributesConstants.ResourceField.MEMORY);
            // and的话，为空的不参加比较
            boolean andResult = (cpuCompareResult && !cpuIsNull) && (memoryCompareResult && !memoryIsNull);
            boolean orResult = cpuCompareResult || memoryCompareResult;
            if (isAnd && andResult) {
                metricData.add(vo);
            }
            if (!isAnd && orResult) {
                metricData.add(vo);
            }
        });
        request.setInstanceUuids(metricData.stream().map(AnalysisServerDTO::getInstanceUuid).toList());
        return metricData;
    }

    private Map<String, Boolean> cpuCompare(boolean cpuIsNull, AnalysisServerDTO vo, PageOptimizationRequest request, Map<Boolean, Integer> containValue, boolean isDerating) {
        Map<String, Boolean> cpuCompare = new HashMap<>(1);
        cpuCompare.put(SpecialAttributesConstants.ResourceField.CPU, false);
        if (!cpuIsNull) {
            if (request.isCpuMaxRate()) {
                cpuCompare.put(SpecialAttributesConstants.ResourceField.CPU, vo.getCpuMaximum().compareTo(BigDecimal.valueOf(request.getCpuRate())) == containValue.get(isDerating));
            } else {
                cpuCompare.put(SpecialAttributesConstants.ResourceField.CPU, vo.getCpuAverage().compareTo(BigDecimal.valueOf(request.getCpuRate())) == containValue.get(isDerating));
            }
        }
        return cpuCompare;
    }

    private Map<String, Boolean> memoryCompare(boolean memoryIsNull, AnalysisServerDTO vo, PageOptimizationRequest request, Map<Boolean, Integer> containValue, boolean isDerating) {
        Map<String, Boolean> compareResult = new HashMap<>(1);
        compareResult.put(SpecialAttributesConstants.ResourceField.MEMORY, false);
        if (!memoryIsNull) {
            if (request.isMemoryMaxRate()) {
                compareResult.put(SpecialAttributesConstants.ResourceField.MEMORY, vo.getMemoryMaximum().compareTo(BigDecimal.valueOf(request.getMemoryRate())) == containValue.get(isDerating));
            } else {
                compareResult.put(SpecialAttributesConstants.ResourceField.MEMORY, vo.getMemoryAverage().compareTo(BigDecimal.valueOf(request.getMemoryRate())) == containValue.get(isDerating));
            }
        }
        return compareResult;
    }

    /**
     * 查询云主机统一参数
     * 云账号、排除已删除
     */
    private MPJLambdaWrapper<VmCloudServer> addServerQuery(MPJLambdaWrapper<VmCloudServer> wrapper, List<String> accountIds) {
        wrapper.in(CollectionUtils.isNotEmpty(accountIds), VmCloudServer::getAccountId, accountIds);
        wrapper.leftJoin(CloudAccount.class, CloudAccount::getId, VmCloudServer::getAccountId);
        wrapper.notIn(true, VmCloudServer::getInstanceStatus, List.of("Deleted", "Failed"));
        return wrapper;
    }

    private List<AnalysisServerDTO> getVmPerfMetric(PageOptimizationRequest request, boolean isDerating) {
        List<AnalysisServerDTO> cloudServerMetricData = new ArrayList<>();
        try {
            request.setEsLastTime(getEsLastTime());
            Arrays.asList(ResourcePerfMetricEnum.CPU_USED_UTILIZATION.name(), ResourcePerfMetricEnum.MEMORY_USED_UTILIZATION.name()).forEach(metricName -> {
                boolean isCpu = StringUtils.equalsIgnoreCase(ResourcePerfMetricEnum.CPU_USED_UTILIZATION.name(), metricName);
                NativeQuery query = new NativeQueryBuilder()
                        .withPageable(PageRequest.of(0, 1))
                        .withTrackScores(true)
                        .withQuery(new Query.Builder().bool(addQueryBool(request, metricName).build()).build())
                        //按实例与云账号ID分组
                        .withAggregation("instanceId", addAggregation(isDerating, isCpu, request))
                        .build();
                SearchHits<Object> response = elasticsearchTemplate.search(query, Object.class, IndexCoordinates.of(IndexConstants.CE_PERF_METRIC_MONITOR_DATA.getCode()));
                ElasticsearchAggregations aggregations = (ElasticsearchAggregations) response.getAggregations();
                assert aggregations != null;
                List<StringTermsBucket> list = aggregations.aggregations().get(0).aggregation().getAggregate().sterms().buckets().array();
                for (StringTermsBucket v : list) {
                    AnalysisServerDTO dto = new AnalysisServerDTO();
                    dto.setInstanceUuid(v.key().stringValue());
                    Aggregate avg = v.aggregations().get(SpecialAttributesConstants.SpecialField.AVERAGE_VALUE);
                    Aggregate max = v.aggregations().get(SpecialAttributesConstants.SpecialField.MAX_VALUE);
                    Aggregate min = v.aggregations().get(SpecialAttributesConstants.SpecialField.MIN_VALUE);
                    BigDecimal avgBig = BigDecimal.valueOf(avg.avg().value()).setScale(3, RoundingMode.HALF_UP);
                    BigDecimal maxBig = BigDecimal.valueOf(max.max().value()).setScale(3, RoundingMode.HALF_UP);
                    BigDecimal minBig = BigDecimal.valueOf(min.min().value()).setScale(3, RoundingMode.HALF_UP);
                    if (isCpu) {
                        dto.setCpuAverage(avgBig);
                        dto.setCpuMaximum(maxBig);
                        dto.setCpuMinimum(minBig);
                    } else {
                        dto.setMemoryAverage(avgBig);
                        dto.setMemoryMaximum(maxBig);
                        dto.setMemoryMinimum(minBig);
                    }
                    cloudServerMetricData.add(dto);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<AnalysisServerDTO> result = new ArrayList<>();
        Map<String, List<AnalysisServerDTO>> instanceMap = cloudServerMetricData.stream().collect(Collectors.groupingBy(AnalysisServerDTO::getInstanceUuid));
        instanceMap.keySet().forEach(v -> {
            AnalysisServerDTO vo = new AnalysisServerDTO();
            instanceMap.get(v).forEach(filter -> BeanUtils.copyProperties(filter, vo, getNullPropertyNames(filter)));
            result.add(vo);
        });

        return result;
    }

    private BoolQuery.Builder addQueryBool(PageOptimizationRequest request, String metricName) {
        long currentTime = request.getEsLastTime();
        long sinceTime = currentTime - 1000L * 60 * 60 * 24 * request.getDays();
        //最外层bool
        BoolQuery.Builder bool = new BoolQuery.Builder();
        //filter bool
        BoolQuery.Builder filterBool = new BoolQuery.Builder();
        //资源类型
        TermQuery.Builder entityTypeShouldTerm = new TermQuery.Builder().field("entityType.keyword").value("VIRTUAL_MACHINE");
        filterBool.should(new co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder().term(entityTypeShouldTerm.build()).build());
        //指标类型
        TermQuery.Builder metricNameShouldTerm = new TermQuery.Builder().field("metricName.keyword").value(metricName);
        filterBool.should(new co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder().term(metricNameShouldTerm.build()).build());
        //时间区间类型
        RangeQuery.Builder timestampRangeShouldTerm = new RangeQuery.Builder().field("timestamp").gte(JsonData.of(sinceTime)).lte(JsonData.of(currentTime));
        filterBool.should(new co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder().range(timestampRangeShouldTerm.build()).build());
        bool.filter(filterBool.build().should());
        return bool;
    }

    /**
     * 聚合
     *
     * @param isDerating 是否降配
     * @param isCpu      是否是CPU指标
     * @param request    所有请求参数
     */
    private Aggregation addAggregation(boolean isDerating, boolean isCpu, PageOptimizationRequest request) {
        String mark = isDerating ? "<=" : ">=";
        boolean maxValue = request.isCpuMaxRate() || request.isMemoryMaxRate();
        String valueType = maxValue ? "maxValue" : "avgValue";
        Map<String, String> pathMap = new HashMap<>();
        pathMap.put(valueType, valueType);
        // TODO 这个地方不要比较了，因为比较后，就会忽略指标不达标的机器，导致指标无数据,直接时间段内的值就好了
        // BucketsPath bucketsPath = new BucketsPath.Builder().dict(pathMap).build();
        // Script script = new Script.Builder().inline(new InlineScript.Builder().source("params."+valueType+mark+(isCpu?request.getCpuRate():request.getMemoryRate())).build()).build();
        Script script = new Script.Builder().inline(new InlineScript.Builder().source("params." + valueType).build()).build();
        return new Aggregation.Builder().terms(new TermsAggregation.Builder().field("instanceId.keyword").size(Integer.MAX_VALUE).build())
                .aggregations("maxValue", new Aggregation.Builder().max(new MaxAggregation.Builder().field("maximum").build()).build())
                .aggregations("avgValue", new Aggregation.Builder().avg(new AverageAggregation.Builder().field("average").build()).build())
                .aggregations("minValue", new Aggregation.Builder().min(new MinAggregation.Builder().field("minimum").build()).build())
                //.aggregations("having",new Aggregation.Builder().bucketSelector(new BucketSelectorAggregation.Builder().bucketsPath(bucketsPath).script(script).build()).build())
                .build();
    }

    /**
     * 获取需要忽略的属性
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            // 此处判断可根据需求修改
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 最新一条数据的时间点
     */
    private long getEsLastTime() {
        BoolQuery.Builder bool = new BoolQuery.Builder();
        //filter bool
        BoolQuery.Builder filterBool = new BoolQuery.Builder();
        //资源类型
        TermQuery.Builder entityTypeShouldTerm = new TermQuery.Builder().field("entityType.keyword").value("VIRTUAL_MACHINE");
        filterBool.should(new co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder().term(entityTypeShouldTerm.build()).build());
        //指标类型
        List<FieldValue> fieldValueList = new ArrayList<>();
        fieldValueList.add(FieldValue.of("MEMORY_USED_UTILIZATION"));
        fieldValueList.add(FieldValue.of("CPU_USED_UTILIZATION"));
        TermsQuery.Builder metricNameShouldTerm = new TermsQuery.Builder().field("metricName.keyword").terms(new TermsQueryField.Builder().value(fieldValueList).build());
        bool.filter(filterBool.build().should());
        filterBool.should(new co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder().terms(metricNameShouldTerm.build()).build());
        NativeQuery query = new NativeQueryBuilder()
                .withPageable(PageRequest.of(0, 1))
                .withSort(Sort.by(Sort.Order.desc("timestamp")))
                .withTrackScores(true)
                .withQuery(new Query.Builder().bool(bool.build()).build())
                .build();
        SearchHits<Object> response = elasticsearchTemplate.search(query, Object.class, IndexCoordinates.of(IndexConstants.CE_PERF_METRIC_MONITOR_DATA.getCode()));
        if (CollectionUtils.isNotEmpty(response.getSearchHits())) {
            return Long.parseLong((String) response.getSearchHits().get(0).getSortValues().get(0));
        }
        return System.currentTimeMillis();
    }


}
