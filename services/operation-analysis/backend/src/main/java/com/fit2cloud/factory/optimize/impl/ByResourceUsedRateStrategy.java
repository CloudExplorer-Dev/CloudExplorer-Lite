package com.fit2cloud.factory.optimize.impl;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseVmCloudServerMapper;
import com.fit2cloud.common.es.constants.IndexConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.constants.*;
import com.fit2cloud.controller.request.optimize.PageOptimizeBaseRequest;
import com.fit2cloud.controller.request.optimize.strategy.ByResourceUsedRateRequest;
import com.fit2cloud.controller.request.optimize.strategy.OptimizeStrategyBaseRequest;
import com.fit2cloud.dto.AnalysisServerDTO;
import com.fit2cloud.factory.optimize.IOptimizeStrategy;
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
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 升降配策略
 *
 * @author jianneng
 * @date 2023/4/7 10:17
 **/
@Component
public class ByResourceUsedRateStrategy implements IOptimizeStrategy {

    @Resource
    private BaseVmCloudServerMapper baseVmCloudServerMapper;
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public IPage<AnalysisServerDTO> execute(PageOptimizeBaseRequest request) {
        List<AnalysisServerDTO> monitorData = getByResourceUsedRate(request);
        if (CollectionUtils.isEmpty(monitorData)) {
            return new Page<>();
        }
        //根据uuid以及条件分页查询
        Page<AnalysisServerDTO> page = PageUtil.of(request, AnalysisServerDTO.class, null, true);
        MPJLambdaWrapper<VmCloudServer> queryServerWrapper = addServerAnalysisQuery(request);
        IPage<AnalysisServerDTO> pageData = baseVmCloudServerMapper.selectJoinPage(page, AnalysisServerDTO.class, queryServerWrapper);
        if (CollectionUtils.isEmpty(pageData.getRecords())) {
            return new Page<>();
        }
        OptimizationConstants optimizationConstants = OptimizationConstants.getByCode(request.getOptimizeSuggestCode());
        //根据UUID分组
        Map<String, AnalysisServerDTO> serverUuidMap = monitorData.stream().collect(Collectors.toMap(VmCloudServer::getInstanceUuid, o -> o, (k1, k2) -> k1));
        //设置监控数据
        pageData.getRecords().forEach(v -> {
            String key = v.getInstanceUuid();
            assert optimizationConstants != null;
            v.setOptimizeSuggest(optimizationConstants.getName());
            v.setOptimizeSuggestCode(optimizationConstants.getCode());
            if (serverUuidMap.containsKey(key)) {
                AnalysisServerDTO metricDto = serverUuidMap.get(key);
                v.setContent(metricDto.getContent());
                v.setCpuMaximum(metricDto.getCpuMaximum());
                v.setCpuAverage(metricDto.getCpuAverage());
                v.setMemoryMaximum(metricDto.getMemoryMaximum());
                v.setMemoryAverage(metricDto.getMemoryAverage());
            }
        });
        return pageData;
    }

    @Override
    public Object defaultStrategy(String optimizeSuggestCode) {
        return new ByResourceUsedRateRequest(AnalysisConstants.OptimizeConditionEnum.OR_COMPARISON_CONDITION.getValue(),
                10L, 30L, AnalysisConstants.OptimizeConditionEnum.AVG.getValue(), 30L,
                AnalysisConstants.OptimizeConditionEnum.AVG.getValue(), 0L,
                AnalysisConstants.OptimizeSuggestEnum.UPGRADE.equals(AnalysisConstants.OptimizeSuggestEnum.byCode(optimizeSuggestCode)));
    }

    /**
     * 云主机分析参数
     */
    private MPJLambdaWrapper<VmCloudServer> addServerAnalysisQuery(PageOptimizeBaseRequest request) {
        MPJLambdaWrapper<VmCloudServer> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(VmCloudServer.class);
        wrapper.selectAs(CloudAccount::getName, AnalysisServerDTO::getAccountName);
        wrapper.selectAs(CloudAccount::getPlatform, AnalysisServerDTO::getPlatform);
        wrapper.leftJoin(CloudAccount.class, CloudAccount::getId, VmCloudServer::getAccountId);
        wrapper.notIn(true, VmCloudServer::getInstanceStatus, List.of("Deleted", "Failed"));
        wrapper.like(StringUtils.isNotBlank(request.getInstanceName()), VmCloudServer::getInstanceName, request.getInstanceName());
        wrapper.in(CollectionUtils.isNotEmpty(request.getSourceIds()), VmCloudServer::getSourceId, request.getSourceIds());
        wrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()), VmCloudServer::getAccountId, request.getAccountIds());
        wrapper.in(CollectionUtils.isNotEmpty(request.getInstanceUuids()), VmCloudServer::getInstanceUuid, request.getInstanceUuids());
        wrapper.orderByDesc(VmCloudServer::getCreateTime);
        return wrapper;
    }

    /**
     * 根据资源使用率优化策略查询数据
     */
    public List<AnalysisServerDTO> getByResourceUsedRate(PageOptimizeBaseRequest request) {
        List<AnalysisServerDTO> result = new ArrayList<>();
        ByResourceUsedRateRequest req = JsonUtil.parseObject(request.getStrategyContent(), ByResourceUsedRateRequest.class);
        // 优化建议原因
        String optimizationMsg = "持续{0}天以上，CPU{1}使用率{2}{3}%{4}内存{5}使用率{6}{7}%,{8}";
        String[] contentArray = new String[9];
        contentArray[0] = String.valueOf(req.getDays());
        contentArray[1] = AnalysisConstants.OptimizeConditionEnum.getLabel(req.getCpuComparisonValue());
        contentArray[3] = String.valueOf(req.getCpuValue());
        contentArray[4] = AnalysisConstants.OptimizeConditionEnum.getLabel(req.getComparisonCondition());
        contentArray[5] = AnalysisConstants.OptimizeConditionEnum.getLabel(req.getMemoryComparisonValue());
        contentArray[7] = String.valueOf(req.getMemoryValue());
        // 降配查询
        if (Objects.requireNonNull(AnalysisConstants.OptimizeSuggestEnum.byCode(request.getOptimizeSuggestCode())) == AnalysisConstants.OptimizeSuggestEnum.DERATING) {
            result = getDerating(req, contentArray);
        }
        // 升配查询
        if (Objects.requireNonNull(AnalysisConstants.OptimizeSuggestEnum.byCode(request.getOptimizeSuggestCode())) == AnalysisConstants.OptimizeSuggestEnum.UPGRADE) {
            result = getUpgrade(req, contentArray);
        }
        if (CollectionUtils.isEmpty(result)) {
            return result;
        }
        optimizationMsg = MessageFormat.format(optimizationMsg, contentArray);
        String finalOptimizationMsg = optimizationMsg;
        result.forEach(vo -> vo.setContent(finalOptimizationMsg));
        request.setInstanceUuids(result.stream().map(AnalysisServerDTO::getInstanceUuid).toList());
        return result;
    }

    private List<AnalysisServerDTO> getDerating(ByResourceUsedRateRequest req, String[] msg) {
        req.setUpgrade(false);
        msg[2] = AnalysisConstants.CompareSymbolsEnum.L_E;
        msg[6] = AnalysisConstants.CompareSymbolsEnum.L_E;
        msg[8] = AnalysisConstants.OptimizeSuggestEnum.DERATING.getName();
        return getMonitorData(req);
    }

    private List<AnalysisServerDTO> getUpgrade(ByResourceUsedRateRequest req, String[] msg) {
        req.setUpgrade(true);
        msg[2] = AnalysisConstants.CompareSymbolsEnum.G_E;
        msg[6] = AnalysisConstants.CompareSymbolsEnum.G_E;
        msg[8] = AnalysisConstants.OptimizeSuggestEnum.UPGRADE.getName();
        return getMonitorData(req);
    }

    private List<AnalysisServerDTO> getMonitorData(ByResourceUsedRateRequest request) {
        boolean and = StringUtils.equalsIgnoreCase(request.getComparisonCondition(), "AND");
        List<AnalysisServerDTO> monitorData = new ArrayList<>();
        // 监控数据
        List<AnalysisServerDTO> list = getVmPerfMetric(request);
        // 判断没有监控数据依据是，平均值为空，理论上平均值为空，最大值也是为空的，所以某个指标的平均值为空，那么认为这个指标没有监控数据
        list.forEach(vo -> {
            // 空数据指标
            boolean cpuIsNull = Objects.isNull(vo.getCpuAverage());
            boolean memoryIsNull = Objects.isNull(vo.getMemoryAverage());
            // CPU比较结果
            boolean cpuCompare = !cpuIsNull && cpuCompare(vo, request);
            // 内存比较结果
            boolean memoryCompare = !memoryIsNull && memoryCompare(vo, request);
            // and的话，为空的不参加比较
            boolean andResult = cpuCompare && memoryCompare;
            boolean orResult = cpuCompare || memoryCompare;
            if (and && andResult) {
                monitorData.add(vo);
            }
            if (!and && orResult) {
                monitorData.add(vo);
            }
        });
        return monitorData;
    }

    /**
     * 比较CPU
     */
    private boolean cpuCompare(AnalysisServerDTO vo, ByResourceUsedRateRequest request) {
        boolean compare = false;
        switch (Objects.requireNonNull(AnalysisConstants.OptimizeConditionEnum.valueOfCode(request.getCpuComparisonValue()))) {
            case MAX ->
                    compare = vo.getCpuMaximum().compareTo(BigDecimal.valueOf(request.getCpuValue())) == (request.isUpgrade() ? 1 : -1);
            case AVG ->
                    compare = vo.getCpuAverage().compareTo(BigDecimal.valueOf(request.getCpuValue())) == (request.isUpgrade() ? 1 : -1);
            case MIN ->
                    compare = vo.getCpuMinimum().compareTo(BigDecimal.valueOf(request.getCpuValue())) == (request.isUpgrade() ? 1 : -1);
        }
        return compare;
    }

    /**
     * 比较内存
     */
    private boolean memoryCompare(AnalysisServerDTO vo, ByResourceUsedRateRequest request) {
        boolean compare = false;
        switch (Objects.requireNonNull(AnalysisConstants.OptimizeConditionEnum.valueOfCode(request.getMemoryComparisonValue()))) {
            case MAX ->
                    compare = vo.getMemoryMaximum().compareTo(BigDecimal.valueOf(request.getMemoryValue())) == (request.isUpgrade() ? 1 : -1);
            case AVG ->
                    compare = vo.getMemoryAverage().compareTo(BigDecimal.valueOf(request.getMemoryValue())) == (request.isUpgrade() ? 1 : -1);
            case MIN ->
                    compare = vo.getMemoryMinimum().compareTo(BigDecimal.valueOf(request.getMemoryValue())) == (request.isUpgrade() ? 1 : -1);
        }
        return compare;
    }

    /**
     * 查询ES获取云主机CPU、内存指标的监控数据
     */
    private List<AnalysisServerDTO> getVmPerfMetric(ByResourceUsedRateRequest request) {
        List<AnalysisServerDTO> cloudServerMetricData = new ArrayList<>();
        try {
            request.setEsLastTime(getEsLastTime());
            Arrays.asList(ResourcePerfMetricEnum.CPU_USED_UTILIZATION.name(), ResourcePerfMetricEnum.MEMORY_USED_UTILIZATION.name()).forEach(metricName -> {
                boolean cpuMetric = StringUtils.equalsIgnoreCase(ResourcePerfMetricEnum.CPU_USED_UTILIZATION.name(), metricName);
                NativeQuery query = new NativeQueryBuilder()
                        .withPageable(PageRequest.of(0, 1))
                        .withTrackScores(true)
                        .withQuery(new Query.Builder().bool(addQueryBool(request, metricName).build()).build())
                        //按实例与云账号ID分组
                        .withAggregation("instanceId", addAggregation())
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
                    if (cpuMetric) {
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

    /**
     * 查询ES的基础条件
     */
    private BoolQuery.Builder addQueryBool(ByResourceUsedRateRequest request, String metricName) {
        long currentTime = request.getEsLastTime();
        long sinceTime = currentTime - 1000L * 60 * 60 * 24 * request.getDays();
        //最外层bool
        BoolQuery.Builder bool = new BoolQuery.Builder();
        //filter bool
        BoolQuery.Builder filterBool = new BoolQuery.Builder();
        //资源类型
        TermQuery.Builder entityTypeShouldTerm = new TermQuery.Builder().field("entityType.keyword").value("VIRTUAL_MACHINE");
        filterBool.should(new Query.Builder().term(entityTypeShouldTerm.build()).build());
        //指标类型
        TermQuery.Builder metricNameShouldTerm = new TermQuery.Builder().field("metricName.keyword").value(metricName);
        filterBool.should(new Query.Builder().term(metricNameShouldTerm.build()).build());
        //时间区间类型
        RangeQuery.Builder timestampRangeShouldTerm = new RangeQuery.Builder().field("timestamp").gte(JsonData.of(sinceTime)).lte(JsonData.of(currentTime));
        filterBool.should(new Query.Builder().range(timestampRangeShouldTerm.build()).build());
        bool.filter(filterBool.build().should());
        return bool;
    }

    /**
     * 聚合
     */
    private Aggregation addAggregation() {
        return new Aggregation.Builder().terms(new TermsAggregation.Builder().field("instanceId.keyword").size(Integer.MAX_VALUE).build())
                .aggregations("maxValue", new Aggregation.Builder().max(new MaxAggregation.Builder().field("maximum").build()).build())
                .aggregations("avgValue", new Aggregation.Builder().avg(new AverageAggregation.Builder().field("average").build()).build())
                .aggregations("minValue", new Aggregation.Builder().min(new MinAggregation.Builder().field("minimum").build()).build())
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
        try {
            BoolQuery.Builder bool = new BoolQuery.Builder();
            //filter bool
            BoolQuery.Builder filterBool = new BoolQuery.Builder();
            //资源类型
            TermQuery.Builder entityTypeShouldTerm = new TermQuery.Builder().field("entityType.keyword").value("VIRTUAL_MACHINE");
            filterBool.should(new Query.Builder().term(entityTypeShouldTerm.build()).build());
            //指标类型
            List<FieldValue> fieldValueList = new ArrayList<>();
            fieldValueList.add(FieldValue.of("MEMORY_USED_UTILIZATION"));
            fieldValueList.add(FieldValue.of("CPU_USED_UTILIZATION"));
            TermsQuery.Builder metricNameShouldTerm = new TermsQuery.Builder().field("metricName.keyword").terms(new TermsQueryField.Builder().value(fieldValueList).build());
            bool.filter(filterBool.build().should());
            filterBool.should(new Query.Builder().terms(metricNameShouldTerm.build()).build());
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
        } catch (Exception e) {
            LogUtil.error("获取最新一条监控数据的时间点失败:" + e.getMessage());
        }
        return System.currentTimeMillis();
    }

    @Override
    public String getStrategyContent(OptimizeStrategyBaseRequest req) {
        if (Objects.isNull(req.getUsedRateRequest())) {
            throw new Fit2cloudException(ErrorCodeConstants.INVALID_PARAMETER.getCode(), ErrorCodeConstants.INVALID_PARAMETER.getMessage());
        }
        return JsonUtil.toJSONString(req.getUsedRateRequest());
    }

}
