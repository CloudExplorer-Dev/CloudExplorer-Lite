package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import com.alibaba.excel.EasyExcelFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.RecycleBin;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.entity.VmCloudServerStatusTiming;
import com.fit2cloud.base.mapper.BaseRecycleBinMapper;
import com.fit2cloud.base.mapper.BaseVmCloudServerMapper;
import com.fit2cloud.base.mapper.BaseVmCloudServerStatusTimingMapper;
import com.fit2cloud.common.constants.RecycleBinStatusConstants;
import com.fit2cloud.common.constants.ResourceTypeConstants;
import com.fit2cloud.common.es.constants.IndexConstants;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.constants.OptimizationRuleConditionTypeConstants;
import com.fit2cloud.constants.SpecialAttributesConstants;
import com.fit2cloud.controller.request.server.PageServerRequest;
import com.fit2cloud.dao.entity.OptimizationStrategy;
import com.fit2cloud.dto.AnalysisServerDTO;
import com.fit2cloud.dto.operation.VmCloudServerStatusTimingDTO;
import com.fit2cloud.dto.optimization.*;
import com.fit2cloud.service.ICloudServerOptimizationService;
import com.fit2cloud.service.IOptimizationStrategyIgnoreResourceService;
import com.fit2cloud.service.IOptimizationStrategyService;
import com.fit2cloud.service.IPermissionService;
import com.fit2cloud.utils.CustomCellWriteHeightConfig;
import com.fit2cloud.utils.CustomCellWriteWidthConfig;
import com.fit2cloud.utils.EasyExcelUtils;
import com.fit2cloud.utils.cache.ElasticSearchVmLatestMonitoringDataSyncCache;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author jianneng
 * @date 2023/6/2 10:49
 **/
@Service
public class CloudServerOptimizationServiceImpl implements ICloudServerOptimizationService {
    @Resource
    private IOptimizationStrategyService optimizationStrategyService;
    @Resource
    private IOptimizationStrategyIgnoreResourceService optimizationStrategyIgnoreResourceService;
    @Resource
    private BaseVmCloudServerMapper vmCloudServerMapper;
    @Resource
    private BaseVmCloudServerStatusTimingMapper vmCloudServerStatusTimingMapper;
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    @Resource
    private IPermissionService permissionService;

    @Resource
    private BaseRecycleBinMapper baseRecycleBinMapper;

    /**
     * 根据优化策略规则，分页获取符合条件的云主机列表
     * ignore 为true时，查询优化策略下所有已忽略的云主机
     * ignore 为false时，根据优化策略规则查询符合规则的云主机
     * 优先查询ES数据，符合ES数据后，根据ID聚合分页查询数据库数据
     *
     * @param request 请求
     * @return {@link IPage }<{@link VmCloudServerDTO }>
     */
    @Override
    public IPage<VmCloudServerDTO> pageList(PageServerRequest request) {
        Long start = System.currentTimeMillis();
        OptimizationStrategyDTO optimizationStrategy = optimizationStrategyService.getOneOptimizationStrategy(request.getOptimizationStrategyId());
        if (Objects.isNull(optimizationStrategy)) {
            return new Page<>();
        }
        if (request.isIgnore()) {
            return optimizationStrategyIgnoreResourceService.pageVmCloudServerList(request);
        }
        List<String> ignoreResourceIdList = optimizationStrategyIgnoreResourceService.getOptimizationStrategyIgnoreResourceIdList(request.getOptimizationStrategyId());
        Set<String> resourceUuIdList = new HashSet<>();
        Set<String> resourceIdList = new HashSet<>();
        List<VmCloudServerDTO> vmCloudServerMonitoringDataList = new ArrayList<>();
        Long esStart = System.currentTimeMillis();
        setConformToOptimizationStrategyRuleResource(optimizationStrategy, resourceUuIdList, resourceIdList, vmCloudServerMonitoringDataList);
        LogUtil.debug("ES耗时：" + (System.currentTimeMillis() - esStart));
        if (CollectionUtils.isEmpty(resourceUuIdList) && CollectionUtils.isEmpty(resourceIdList)) {
            return new Page<>();
        }
        MPJLambdaWrapper<VmCloudServer> wrapper = getVmCloudServerWrapper(request, ignoreResourceIdList, resourceUuIdList, resourceIdList);
        Page<VmCloudServerDTO> page = PageUtil.of(request, VmCloudServerDTO.class, null, true);
        IPage<VmCloudServerDTO> resultPageData = vmCloudServerMapper.selectJoinPage(page, VmCloudServerDTO.class, wrapper);
        resultPageData.getRecords().forEach(v -> {
            if (CollectionUtils.isNotEmpty(vmCloudServerMonitoringDataList)) {
                List<VmCloudServerDTO> monitoringDataList = vmCloudServerMonitoringDataList.stream().filter(a -> StringUtils.equalsIgnoreCase(v.getInstanceUuid(), a.getInstanceUuid())).toList();
                if (CollectionUtils.isNotEmpty(monitoringDataList)) {
                    v.setCpuMonitoringValue(monitoringDataList.get(0).getCpuMonitoringValue());
                    v.setMemoryMonitoringValue(monitoringDataList.get(0).getMemoryMonitoringValue());
                }
            }
            v.setContent(optimizationStrategy.getOptimizationContent());
        });
        LogUtil.debug("后台耗时：" + (System.currentTimeMillis() - start));
        return resultPageData;
    }

    /**
     * 设置符合资源优化策略规则的资源
     * 查询符合优化策略规则的云主机，id或者uuid(监控数据)
     * 如果优化策略是监控策略的话，给vmCloudServerMonitoringDataList赋值
     *
     * @param optimizationStrategy            优化策略
     * @param resourceUuIdList                符合优化策略规则的资源uu id列表
     * @param resourceIdList                  符合优化策略规则的资源id列表
     * @param vmCloudServerMonitoringDataList 云服务器监控数据列表
     */
    private void setConformToOptimizationStrategyRuleResource(OptimizationStrategyDTO optimizationStrategy, Set<String> resourceUuIdList, Set<String> resourceIdList, List<VmCloudServerDTO> vmCloudServerMonitoringDataList) {
        // 监控策略时，查询ES数据
        if ("MONITORING".equalsIgnoreCase(optimizationStrategy.getStrategyType())) {
            Long startTime = System.currentTimeMillis();
            List<VmCloudServerDTO> vmCloudServerList = getCloudServerMonitoringData(optimizationStrategy.getDays());
            LogUtil.debug("查询ES数据总耗时:" + (System.currentTimeMillis() - startTime));
            vmCloudServerMonitoringDataList.addAll(vmCloudServerList);
            esConformToOptimizationStrategyRuleResourceIdList(optimizationStrategy, vmCloudServerMonitoringDataList, resourceUuIdList);
        } else {
            mysqlConformToOptimizationStrategyRuleResourceIdList(optimizationStrategy, resourceIdList);
        }
    }

    /**
     * 获取查询云主机查询条件
     *
     * @param request              查询参数
     * @param ignoreResourceIdList 忽视资源id列表
     * @param resourceUuIdList     资源uu id列表
     * @param resourceIdList       资源ID列表
     * @return {@link MPJLambdaWrapper }<{@link VmCloudServer }>
     */
    @NotNull
    private MPJLambdaWrapper<VmCloudServer> getVmCloudServerWrapper(PageServerRequest request, List<String> ignoreResourceIdList, Set<String> resourceUuIdList, Set<String> resourceIdList) {
        MPJLambdaWrapper<VmCloudServer> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(VmCloudServer.class);
        wrapper.selectAs(CloudAccount::getName, AnalysisServerDTO::getAccountName);
        wrapper.selectAs(CloudAccount::getPlatform, AnalysisServerDTO::getPlatform);
        wrapper.leftJoin(CloudAccount.class, CloudAccount::getId, VmCloudServer::getAccountId);
        // 不是管理员就获取当前用户有权限的组织或工作空间下的优化策略
        List<String> sourceId = permissionService.getSourceIds();
        wrapper.in(!CurrentUserUtils.isAdmin(), VmCloudServer::getSourceId, sourceId);
        wrapper.notIn(true, VmCloudServer::getInstanceStatus, List.of(SpecialAttributesConstants.StatusField.VM_DELETE, SpecialAttributesConstants.StatusField.FAILED));
        wrapper.like(StringUtils.isNotBlank(request.getIpArray()), VmCloudServer::getIpArray, request.getIpArray());
        wrapper.like(StringUtils.isNotEmpty(request.getInstanceName()), VmCloudServerDTO::getInstanceName, request.getInstanceName());
        wrapper.in(CollectionUtils.isNotEmpty(resourceIdList), VmCloudServerDTO::getId, resourceIdList);
        wrapper.in(CollectionUtils.isNotEmpty(resourceUuIdList), VmCloudServerDTO::getInstanceUuid, resourceUuIdList);
        wrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()), VmCloudServerDTO::getAccountId, request.getAccountIds());
        wrapper.notIn(CollectionUtils.isNotEmpty(ignoreResourceIdList), VmCloudServer::getId, ignoreResourceIdList);
        return wrapper;
    }

    /**
     * 获取云主机优化导出数据
     *
     * @param optimizationStrategy 优化策略
     * @param request              请求
     * @return {@link List }<{@link VmCloudServerExcelDTO }>
     */
    private List<VmCloudServerExcelDTO> getVmCloudExcel(OptimizationStrategyDTO optimizationStrategy, PageServerRequest request) {
        List<VmCloudServerDTO> vmCloudServerMonitoringDataList = new ArrayList<>();
        List<VmCloudServerExcelDTO> resultList = new ArrayList<>();
        List<VmCloudServerDTO> list;
        // 已忽略资源列表
        if (request.isIgnore()) {
            list = optimizationStrategyIgnoreResourceService.vmCloudServerList(request);
        } else {
            // 已忽略资源ID,用于在查询优化资源时过滤条件
            List<String> ignoreResourceIdList = optimizationStrategyIgnoreResourceService.getOptimizationStrategyIgnoreResourceIdList(request.getOptimizationStrategyId());
            Set<String> resourceUuIdList = new HashSet<>();
            Set<String> resourceIdList = new HashSet<>();
            setConformToOptimizationStrategyRuleResource(optimizationStrategy, resourceUuIdList, resourceIdList, vmCloudServerMonitoringDataList);
            if (CollectionUtils.isEmpty(resourceUuIdList) && CollectionUtils.isEmpty(resourceIdList)) {
                return new ArrayList<>();
            }
            MPJLambdaWrapper<VmCloudServer> wrapper = getVmCloudServerWrapper(request, ignoreResourceIdList, resourceUuIdList, resourceIdList);
            list = vmCloudServerMapper.selectJoinList(VmCloudServerDTO.class, wrapper);
        }
        LogUtil.debug("命中:" + list.size());
        list.forEach(v -> {
            VmCloudServerExcelDTO vmCloudServerExcelDTO = new VmCloudServerExcelDTO();
            BeanUtils.copyProperties(v, vmCloudServerExcelDTO);
            if (CollectionUtils.isNotEmpty(vmCloudServerMonitoringDataList)) {
                List<VmCloudServerDTO> monitoringDataList = vmCloudServerMonitoringDataList
                        .stream()
                        .filter(a -> StringUtils.equalsIgnoreCase(v.getInstanceUuid(), a.getInstanceUuid()))
                        .toList();
                if (CollectionUtils.isNotEmpty(monitoringDataList)) {
                    MonitoringDataValueDTO cpuValue = monitoringDataList.get(0).getCpuMonitoringValue();
                    MonitoringDataValueDTO memoryValue = monitoringDataList.get(0).getMemoryMonitoringValue();
                    vmCloudServerExcelDTO.setCpuAvgValue(Objects.nonNull(cpuValue) ? cpuValue.getAvgValue().doubleValue() : null);
                    vmCloudServerExcelDTO.setCpuMaxValue(Objects.nonNull(cpuValue) ? cpuValue.getMaxValue().doubleValue() : null);
                    vmCloudServerExcelDTO.setMemoryMaxValue(Objects.nonNull(memoryValue) ? memoryValue.getMaxValue().doubleValue() : null);
                    vmCloudServerExcelDTO.setMemoryAvgValue(Objects.nonNull(memoryValue) ? memoryValue.getAvgValue().doubleValue() : null);
                }
            }
            if (!request.isIgnore()) {
                vmCloudServerExcelDTO.setContent(optimizationStrategy.getOptimizationContent());
            }
            resultList.add(vmCloudServerExcelDTO);
        });
        LogUtil.debug("导出云主机优化列表数据,查询到的数据量:" + resultList.size());
        return resultList;
    }

    @Override
    public void downloadExcel(PageServerRequest request, String version, HttpServletResponse response) {
        LogUtil.debug("开始导出优化策略资源");
        OptimizationStrategyDTO optimizationStrategy = optimizationStrategyService.getOneOptimizationStrategy(request.getOptimizationStrategyId());
        if (Objects.nonNull(optimizationStrategy)) {
            LogUtil.debug("优化策略名称:" + optimizationStrategy.getName());
            String fileName = optimizationStrategy.getName() + "_云主机_" + (request.isIgnore() ? "已忽略资源" : "优化资源");
            response.reset();
            setResponse(response, fileName, (StringUtils.equalsIgnoreCase("xlsx", version) ? version : "xls"));
            try {
                List<VmCloudServerExcelDTO> list = getVmCloudExcel(optimizationStrategy, request);
                EasyExcelFactory.write(response.getOutputStream(), VmCloudServerExcelDTO.class)
                        .sheet(fileName)
                        .registerWriteHandler(new CustomCellWriteWidthConfig())
                        .registerWriteHandler(new CustomCellWriteHeightConfig())
                        .registerWriteHandler(EasyExcelUtils.getStyleStrategy())
                        .doWrite(list);
            } catch (IOException ioe) {
                LogUtil.error("导出优化策略资源失败IO:" + ioe.getMessage());
                ioe.printStackTrace();
                throw new RuntimeException(ioe);
            } catch (Exception e) {
                LogUtil.error("导出优化策略资源失败:" + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        LogUtil.debug("结束导出优化策略资源");
    }

    /**
     * 设置响应内容
     *
     * @param response 响应
     * @param fileName 文件名称
     * @param version  excel版本
     */
    public static void setResponse(HttpServletResponse response, String fileName, String version) {
        // 文件名
        String sheetName = URLEncoder.encode(fileName, StandardCharsets.UTF_8) + "." + version;
        // contentType 响应内容的类型
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        // 设置字符
        response.setCharacterEncoding("utf-8");
        // 设置文件名
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + sheetName);
    }

    /**
     * es符合优化策略规则资源id列表
     * 支持两层关系
     *
     * @param optimizationStrategy            优化策略
     * @param vmCloudServerMonitoringDataList vm云服务器监控数据列表
     * @param resourceUuIdList                资源UUID列表
     */
    private void esConformToOptimizationStrategyRuleResourceIdList(OptimizationStrategy optimizationStrategy, List<VmCloudServerDTO> vmCloudServerMonitoringDataList, Set<String> resourceUuIdList) {
        List<VmCloudServerDTO> result = vmCloudServerMonitoringDataList.stream().filter(cloudServerMonitoringData -> {
            // 策略规则
            OptimizationRule optimizationRule = optimizationStrategy.getOptimizationRules();
            // 规则条件比较结果
            List<Boolean> ruleCompareResult = new ArrayList<>();
            // 第一层条件
            List<OptimizationRuleFieldCondition> conditionList = optimizationRule.getConditions();
            if (CollectionUtils.isNotEmpty(conditionList)) {
                ruleCompareResult.add(filterVmCloudServerMonitoringDataByFieldCondition(cloudServerMonitoringData, conditionList, isAnd(optimizationRule.getConditionType())));
            }
            // 第二层条件
            List<OptimizationRule> childrenList = optimizationRule.getChildren();
            for (OptimizationRule children : childrenList) {
                if (CollectionUtils.isNotEmpty(children.getConditions())) {
                    ruleCompareResult.add(filterVmCloudServerMonitoringDataByFieldCondition(cloudServerMonitoringData, children.getConditions(), isAnd(children.getConditionType())));
                }
            }
            if (isAnd(optimizationRule.getConditionType())) {
                return ruleCompareResult.stream().allMatch(Boolean::booleanValue);
            } else {
                return ruleCompareResult.stream().anyMatch(Boolean::booleanValue);
            }
        }).toList();
        if (CollectionUtils.isNotEmpty(result)) {
            resourceUuIdList.addAll(result.stream().map(VmCloudServerDTO::getInstanceUuid).toList());
        }
    }

    private boolean isAnd(OptimizationRuleConditionTypeConstants conditionType) {
        return StringUtils.equalsIgnoreCase(conditionType.name(), OptimizationRuleConditionTypeConstants.AND.name());
    }

    /**
     * 通过字段条件过滤云服务器监控数据
     * andCondition为true时，所有字段都必须满足
     * andCondition为false时，只要有一个满足就行
     *
     * @param vmCloudServerDTO vm云服务器dto
     * @param andCondition     和条件
     * @return boolean
     */
    private boolean filterVmCloudServerMonitoringDataByFieldCondition(VmCloudServerDTO vmCloudServerDTO, List<OptimizationRuleFieldCondition> conditions, Boolean andCondition) {
        List<Boolean> compareResult = new ArrayList<>();
        for (OptimizationRuleFieldCondition optimizationRuleFieldCondition : conditions) {
            if (optimizationRuleFieldCondition.isEsField()) {
                // 监控指标字段 field 形式为 MEMORY_USED_UTILIZATION@MAX
                // 通过field获取指标名称
                String metricName = optimizationRuleFieldCondition.getField().split("@")[0];
                // 通过field获取比较字段
                String metricField = optimizationRuleFieldCondition.getField().split("@")[1];
                MonitoringDataValueDTO monitoringDataValueDTO = getMonitoringDataValueByMetricName(metricName, vmCloudServerDTO);
                compareResult.add(compareField(metricField, optimizationRuleFieldCondition, monitoringDataValueDTO));
            }
        }
        if (andCondition) {
            return compareResult.stream().allMatch(Boolean::booleanValue);
        } else {
            return compareResult.stream().anyMatch(Boolean::booleanValue);
        }
    }

    /**
     * 通过指标名称获得监测数据
     *
     * @param metricName       指标名称
     * @param vmCloudServerDTO vm云服务器dto
     * @return {@link MonitoringDataValueDTO }
     */
    private MonitoringDataValueDTO getMonitoringDataValueByMetricName(String metricName, VmCloudServerDTO vmCloudServerDTO) {
        if (metricName.toLowerCase().startsWith(SpecialAttributesConstants.ResourceField.CPU.toLowerCase())) {
            return vmCloudServerDTO.getCpuMonitoringValue();
        }
        if (metricName.toLowerCase().startsWith(SpecialAttributesConstants.ResourceField.MEMORY.toLowerCase())) {
            return vmCloudServerDTO.getMemoryMonitoringValue();
        }
        return null;
    }

    /**
     * 比较字段
     *
     * @param metricField                    比较值，最大最小平均值
     * @param optimizationRuleFieldCondition 优化规则条件
     * @param monitoringDataValueDTO         监测数据值
     * @return boolean
     */
    private boolean compareField(String metricField, OptimizationRuleFieldCondition optimizationRuleFieldCondition, MonitoringDataValueDTO monitoringDataValueDTO) {
        if (monitoringDataValueDTO != null) {
            if (SpecialAttributesConstants.SpecialField.MAX.equalsIgnoreCase(metricField)) {
                return compareValue(optimizationRuleFieldCondition.getCompare(), optimizationRuleFieldCondition.getValue(), monitoringDataValueDTO.getMaxValue().doubleValue());
            }
            if (SpecialAttributesConstants.SpecialField.MIN.equalsIgnoreCase(metricField)) {
                return compareValue(optimizationRuleFieldCondition.getCompare(), optimizationRuleFieldCondition.getValue(), monitoringDataValueDTO.getMinValue().doubleValue());
            }
            if (SpecialAttributesConstants.SpecialField.AVERAGE.equalsIgnoreCase(metricField)) {
                return compareValue(optimizationRuleFieldCondition.getCompare(), optimizationRuleFieldCondition.getValue(), monitoringDataValueDTO.getAvgValue().doubleValue());
            }
        }
        return false;
    }

    /**
     * 比较值
     *
     * @param compare     比较
     * @param value       值,比较条件字段值
     * @param metricValue 指标数据值
     * @return boolean
     */
    private boolean compareValue(String compare, String value, Double metricValue) {
        if (Objects.isNull(metricValue)) {
            return false;
        }
        boolean compareResult = false;
        switch (OptimizationRuleFieldCompare.valueOf(compare)) {
            case EQ -> compareResult = metricValue == Double.parseDouble(value);
            case LE -> compareResult = metricValue <= Double.parseDouble(value);
            case LT -> compareResult = metricValue < Double.parseDouble(value);
            case GE -> compareResult = metricValue >= Double.parseDouble(value);
            case GT -> compareResult = metricValue > Double.parseDouble(value);
            default -> {
            }
        }
        return compareResult;
    }


    /**
     * 云服务器监控数据
     * 返回值中的VmCloudServerDTO，保存云主机UUID以及指定查询的指标数据
     *
     * @param days 过去多少天
     * @return {@link List }<{@link VmCloudServerDTO }>
     */
    public List<VmCloudServerDTO> getCloudServerMonitoringData(Long days) {
        Long latestTime = ElasticSearchVmLatestMonitoringDataSyncCache.getCacheOrUpdate();
        Long start = System.currentTimeMillis();
        NativeQuery query = new NativeQueryBuilder()
                .withTrackScores(false)
                .withTrackTotalHits(false)
                .withQuery(new Query.Builder().bool(addQueryBool(latestTime, days).build()).build())
                //按实例与云账号ID分组
                .withAggregation("instanceId", addAggregation())
                .build();
        LogUtil.debug("构建ES查询条件耗时:" + (System.currentTimeMillis() - start));
        System.out.println("最新时间:" + latestTime);
        Long startTime = System.currentTimeMillis();
        SearchHits<Object> response = elasticsearchTemplate.search(query, Object.class, IndexCoordinates.of(IndexConstants.CE_PERF_METRIC_MONITOR_DATA.getCode()));
        LogUtil.debug("查询ES数据耗时:" + (System.currentTimeMillis() - startTime));
        System.out.println("查询ES数据耗时:" + (System.currentTimeMillis() - startTime));
        ElasticsearchAggregations aggregations = (ElasticsearchAggregations) response.getAggregations();
        List<VmCloudServerDTO> vmCloudServerDTOList = new ArrayList<>();
        assert aggregations != null;
        if (CollectionUtils.isNotEmpty(aggregations.aggregations())) {
            List<StringTermsBucket> bucketList = aggregations.aggregations().get(0).aggregation().getAggregate().sterms().buckets().array();
            bucketList.forEach(instanceBucket -> {
                String instanceUuid = instanceBucket.key().stringValue();
                VmCloudServerDTO vmCloudServerDTO = new VmCloudServerDTO();
                vmCloudServerDTO.setInstanceUuid(instanceUuid);
                instanceBucket.aggregations().get("metricName").sterms().buckets().array().forEach(metricBucket -> setVmCloudServerMetricData(metricBucket, vmCloudServerDTO));
                vmCloudServerDTOList.add(vmCloudServerDTO);
            });
        }
        return vmCloudServerDTOList;
    }

    /**
     * 设置云主机监控指标数据
     * 目前只有CPU、内存
     *
     * @param metricBucket     度量桶
     * @param vmCloudServerDTO vm云服务器dto
     */
    private void setVmCloudServerMetricData(StringTermsBucket metricBucket, VmCloudServerDTO vmCloudServerDTO) {
        String metricName = metricBucket.key().stringValue();
        Aggregate avg = metricBucket.aggregations().get(SpecialAttributesConstants.SpecialField.AVERAGE_VALUE);
        Aggregate max = metricBucket.aggregations().get(SpecialAttributesConstants.SpecialField.MAX_VALUE);
        Aggregate min = metricBucket.aggregations().get(SpecialAttributesConstants.SpecialField.MIN_VALUE);
        MonitoringDataValueDTO monitoringDataValueDTO = new MonitoringDataValueDTO(getBigValue(max.max().value()), getBigValue(min.min().value()), getBigValue(avg.avg().value()));
        if (metricName.toLowerCase().startsWith(SpecialAttributesConstants.ResourceField.CPU.toLowerCase())) {
            vmCloudServerDTO.setCpuMonitoringValue(monitoringDataValueDTO);
        }
        if (metricName.toLowerCase().startsWith(SpecialAttributesConstants.ResourceField.MEMORY.toLowerCase())) {
            vmCloudServerDTO.setMemoryMonitoringValue(monitoringDataValueDTO);
        }
    }

    /**
     * 该方法将长小数点数据转为保留3位小数的big类型
     * ES查询的指标数据为长小数点数据
     *
     * @param value 值
     * @return {@link BigDecimal }
     */
    private BigDecimal getBigValue(Double value) {
        return BigDecimal.valueOf(value).setScale(3, RoundingMode.HALF_UP);
    }


    /**
     * 构建查询时间段内云主机CPU、内存监控指标数据的查询条件
     *
     * @param esLastTime 最新监控数据时间
     * @param days       过去多少天
     * @return {@link BoolQuery.Builder }
     */
    private BoolQuery.Builder addQueryBool(Long esLastTime, Long days) {
        // 最新监控数据时间过去10天的时间点
        long sinceTime = esLastTime - 1000L * 60 * 60 * 24 * days;
        //最外层bool
        BoolQuery.Builder bool = new BoolQuery.Builder();
        //must bool
        BoolQuery.Builder mustBool = new BoolQuery.Builder();
        //资源类型
        mustBool.should(new Query.Builder().term(new TermQuery.Builder().field("entityType.keyword").value("VIRTUAL_MACHINE").build()).build());
        //指标类型
        TermsQuery.Builder metricNameShouldTerm = new TermsQuery.Builder().field("metricName.keyword").terms(new TermsQueryField.Builder().value(Arrays.asList(FieldValue.of("MEMORY_USED_UTILIZATION"), FieldValue.of("CPU_USED_UTILIZATION"))).build());
        mustBool.should(new Query.Builder().terms(metricNameShouldTerm.build()).build());
        //时间区间类型
        RangeQuery.Builder timestampRangeShouldTerm = new RangeQuery.Builder().field("timestamp").gte(JsonData.of(sinceTime)).lte(JsonData.of(esLastTime));
        mustBool.should(new Query.Builder().range(timestampRangeShouldTerm.build()).build());
        bool.must(mustBool.build().should());
        return bool;
    }

    /**
     * 聚合云主机UUID以及监控指标，返回指标的最大、最小、平均值
     *
     * @return {@link Aggregation }
     */
    private Aggregation addAggregation() {
        return new Aggregation.Builder().terms(new TermsAggregation.Builder().field("instanceId.keyword").size(Integer.MAX_VALUE).build())
                .aggregations("metricName", new Aggregation.Builder().terms(new TermsAggregation.Builder().field("metricName.keyword").build())
                        .aggregations("maxValue", new Aggregation.Builder().max(new MaxAggregation.Builder().field("maximum").build()).build())
                        .aggregations("avgValue", new Aggregation.Builder().avg(new AverageAggregation.Builder().field("average").build()).build())
                        .aggregations("minValue", new Aggregation.Builder().min(new MinAggregation.Builder().field("minimum").build()).build())
                        .build())
                .build();
    }


    /**
     * 获取云主机cpu或内存监控数据最新数据的时间
     *
     * @return long
     */
    public long getVmCloudServerCpuOrMemoryMonitoringDataLatestTime() {
        try {
            //filter bool
            BoolQuery.Builder filterBool = new BoolQuery.Builder();
            //资源类型
            filterBool.must(new Query.Builder().term(new TermQuery.Builder().field("entityType.keyword").value("VIRTUAL_MACHINE").build()).build());
            //指标类型
            TermsQuery.Builder metricNameShouldTerm = new TermsQuery.Builder().field("metricName.keyword").terms(new TermsQueryField.Builder().value(Arrays.asList(FieldValue.of("MEMORY_USED_UTILIZATION"), FieldValue.of("CPU_USED_UTILIZATION"))).build());
            filterBool.should(new Query.Builder().terms(metricNameShouldTerm.build()).build());
            NativeQuery query = new NativeQueryBuilder()
                    .withPageable(PageRequest.of(0, 1))
                    .withSort(Sort.by(Sort.Order.desc("timestamp")))
                    .withQuery(new Query.Builder().bool(filterBool.build()).build())
                    .build();
            SearchHits<Object> response = elasticsearchTemplate.search(query, Object.class, IndexCoordinates.of(IndexConstants.CE_PERF_METRIC_MONITOR_DATA.getCode()));
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(response.getSearchHits())) {
                return Long.parseLong((String) response.getSearchHits().get(0).getSortValues().get(0));
            }
        } catch (Exception e) {
            LogUtil.error("获取云主机cpu或内存监控数据最新数据的时间失败:" + e.getMessage());
        }
        return System.currentTimeMillis();
    }

    /**
     * mysql符合优化策略规则资源id列表
     *
     * @param optimizationStrategy 优化策略
     * @param resourceIdList       资源id列表
     */
    private void mysqlConformToOptimizationStrategyRuleResourceIdList(OptimizationStrategy optimizationStrategy, Set<String> resourceIdList) {
        QueryWrapper<VmCloudServer> wrapper = new QueryWrapper<>();
        OptimizationRule optimizationRules = optimizationStrategy.getOptimizationRules();
        // 第一层
        boolean ruleAnd = isAnd(optimizationRules.getConditionType());
        if (CollectionUtils.isNotEmpty(optimizationRules.getConditions())) {
            if (ruleAnd) {
                wrapper.and(true, a1 -> toQueryWrapperByOptimizationRuleFieldCondition(optimizationRules.getConditions(), isAnd(optimizationRules.getConditionType()), a1));
            } else {
                wrapper.or(true, a1 -> toQueryWrapperByOptimizationRuleFieldCondition(optimizationRules.getConditions(), isAnd(optimizationRules.getConditionType()), a1));
            }
        }
        // 第二层
        List<OptimizationRule> childrenList = optimizationRules.getChildren();
        for (OptimizationRule children : childrenList) {
            if (CollectionUtils.isNotEmpty(children.getConditions())) {
                if (ruleAnd) {
                    wrapper.and(true, a1 -> toQueryWrapperByOptimizationRuleFieldCondition(children.getConditions(), isAnd(children.getConditionType()), a1));
                } else {
                    wrapper.or(true, a1 -> toQueryWrapperByOptimizationRuleFieldCondition(children.getConditions(), isAnd(children.getConditionType()), a1));
                }
            }
        }
        if (StringUtils.isNotEmpty(wrapper.getSqlSegment())) {
            resourceIdList.addAll(vmCloudServerMapper.selectList(wrapper).stream().map(VmCloudServer::getId).toList());
        }
    }

    /**
     * 通过优化规则条件字段包装查询云主机条件
     *
     * @param conditions   条件
     * @param andCondition and关系
     * @param wrapper      构建查询条件
     */
    private void toQueryWrapperByOptimizationRuleFieldCondition(List<OptimizationRuleFieldCondition> conditions, boolean andCondition, QueryWrapper<VmCloudServer> wrapper) {
        conditions.stream().filter(v -> !v.isEsField()).toList().forEach(optimizationRuleFieldCondition -> {
            // AND条件
            if (andCondition) {
                wrapper.and(checkFieldCondition(optimizationRuleFieldCondition.getField(), optimizationRuleFieldCondition.getValue(), VmCloudServer.class), a1 -> vmCloudServerConformToOptimizationStrategyRuleResourceIdList(optimizationRuleFieldCondition, a1));
                wrapper.and(checkFieldCondition(optimizationRuleFieldCondition.getField(), optimizationRuleFieldCondition.getValue(), VmCloudServerStatusTiming.class), a2 -> vmCloudServerStatusTimingConformToOptimizationStrategyRuleResourceIdList(optimizationRuleFieldCondition, a2));
                vmRecycleBinConformToOptimizationStrategyRuleResourceIdList(optimizationRuleFieldCondition, wrapper, true);
            } else {
                wrapper.or(checkFieldCondition(optimizationRuleFieldCondition.getField(), optimizationRuleFieldCondition.getValue(), VmCloudServer.class), a1 -> vmCloudServerConformToOptimizationStrategyRuleResourceIdList(optimizationRuleFieldCondition, a1));
                wrapper.or(checkFieldCondition(optimizationRuleFieldCondition.getField(), optimizationRuleFieldCondition.getValue(), VmCloudServerStatusTiming.class), a2 -> vmCloudServerStatusTimingConformToOptimizationStrategyRuleResourceIdList(optimizationRuleFieldCondition, a2));
                vmRecycleBinConformToOptimizationStrategyRuleResourceIdList(optimizationRuleFieldCondition, wrapper, false);
            }
        });
    }

    /**
     * 云主机表符合优化策略规则条件的云主机ID列表
     *
     * @param optimizationRuleFieldCondition 优化规则条件
     * @param wrapper                        包装器
     */
    private void vmCloudServerConformToOptimizationStrategyRuleResourceIdList(OptimizationRuleFieldCondition optimizationRuleFieldCondition, QueryWrapper<VmCloudServer> wrapper) {
        String tableColumnName = ColumnNameUtil.getColumnName(optimizationRuleFieldCondition.getField(), VmCloudServer.class);
        String tableColumnValue = optimizationRuleFieldCondition.getValue();
        boolean condition = StringUtils.isNotEmpty(tableColumnName) && StringUtils.isNotEmpty(optimizationRuleFieldCondition.getValue());
        if (condition) {
            switch (OptimizationRuleFieldCompare.valueOf(optimizationRuleFieldCondition.getCompare())) {
                case NOT_EQ -> wrapper.ne(true, tableColumnName, tableColumnValue);
                case EQ -> wrapper.eq(true, tableColumnName, tableColumnValue);
                case LE -> wrapper.le(true, tableColumnName, tableColumnValue);
                case LT -> wrapper.lt(true, tableColumnName, tableColumnValue);
                case GE -> wrapper.ge(true, tableColumnName, tableColumnValue);
                case GT -> wrapper.gt(true, tableColumnName, tableColumnValue);
                default -> {
                }
            }
        }
    }

    /**
     * 检查条件字段是否在对应的实体类中，并且字段值是否为空
     *
     * @param columnName  列名
     * @param columnValue 列值
     * @param c           字段对应的实体
     * @return boolean
     */
    private boolean checkFieldCondition(String columnName, String columnValue, Class<?> c) {
        // 实例回收状态单独处理
        if (StringUtils.equalsIgnoreCase(columnName, "instanceStatus") && StringUtils.equalsIgnoreCase(RecycleBinStatusConstants.ToBeRecycled.name(), columnValue)) {
            return false;
        }
        String tableColumnName = ColumnNameUtil.getColumnName(columnName, c);
        return StringUtils.isNotEmpty(tableColumnName) && StringUtils.isNotEmpty(columnValue);
    }

    /**
     * 回收站表符合优化策略规则条件的云主机ID列表
     *
     * @param optimizationRuleFieldCondition 优化规则条件
     * @param wrapper                        包装器
     */
    private void vmRecycleBinConformToOptimizationStrategyRuleResourceIdList(OptimizationRuleFieldCondition optimizationRuleFieldCondition, QueryWrapper<VmCloudServer> wrapper, boolean isAnd) {
        String tableColumnName = ColumnNameUtil.getColumnName(optimizationRuleFieldCondition.getField(), VmCloudServer.class);
        String tableColumnValue = optimizationRuleFieldCondition.getValue();
        if (StringUtils.equalsIgnoreCase(tableColumnName, "instance_status") && StringUtils.equalsIgnoreCase(RecycleBinStatusConstants.ToBeRecycled.name(), tableColumnValue)) {
            QueryWrapper<RecycleBin> recycleQuery = new QueryWrapper<>();
            recycleQuery.eq(true, "recycle_bin.resource_type", ResourceTypeConstants.VM.name());
            recycleQuery.eq(true, "recycle_bin.status", tableColumnValue);
            // 回收站的
            List<RecycleBin> recycleBins = baseRecycleBinMapper.selectList(recycleQuery);
            // 回收站的资源ID
            List<String> resourceIdList = recycleBins.stream().map(RecycleBin::getResourceId).toList();
            if (CollectionUtils.isNotEmpty(resourceIdList)) {
                if (isAnd) {
                    wrapper.and(true, a1 -> vmRecycleBinConformToOptimizationStrategyRuleResourceIdList(optimizationRuleFieldCondition, a1, resourceIdList));
                } else {
                    wrapper.or(true, a1 -> vmRecycleBinConformToOptimizationStrategyRuleResourceIdList(optimizationRuleFieldCondition, a1, resourceIdList));
                }
            }
        }
    }

    private void vmRecycleBinConformToOptimizationStrategyRuleResourceIdList(OptimizationRuleFieldCondition optimizationRuleFieldCondition, QueryWrapper<VmCloudServer> wrapper, List<String> resourceIdList) {
        switch (OptimizationRuleFieldCompare.valueOf(optimizationRuleFieldCondition.getCompare())) {
            case NOT_EQ ->
                    wrapper.notIn(true, ColumnNameUtil.getColumnName(VmCloudServer::getId, true), resourceIdList);
            case EQ -> wrapper.in(true, ColumnNameUtil.getColumnName(VmCloudServer::getId, true), resourceIdList);
            default -> {
            }
        }
    }

    /**
     * 开关机时长计量表符合优化策略规则持续开关机时长条件的云主机ID列表
     *
     * @param optimizationRuleFieldCondition 优化规则现场条件
     * @param wrapper                        查询云主机包装器
     */
    private void vmCloudServerStatusTimingConformToOptimizationStrategyRuleResourceIdList(OptimizationRuleFieldCondition optimizationRuleFieldCondition, QueryWrapper<VmCloudServer> wrapper) {
        String tableColumnName = ColumnNameUtil.getColumnName(optimizationRuleFieldCondition.getField(), VmCloudServerStatusTiming.class);
        boolean condition = StringUtils.isNotEmpty(tableColumnName) && StringUtils.isNotEmpty(optimizationRuleFieldCondition.getValue());
        if (condition) {
            QueryWrapper<VmCloudServerStatusTimingDTO> vmCloudServerStatusTimingQueryWrapper = new QueryWrapper<>();
            // TODO 这添加Having没用。。。后面在看看，先代码过滤吧
//            vmCloudServerStatusTimingQueryWrapper.having("last_" + tableColumnName + OptimizationRuleFieldCompare.valueOf(optimizationRuleFieldCondition.getCompare()).getCompare() + (Long.parseLong( optimizationRuleFieldCondition.getValue())*86400L));
//            LogUtil.debug(vmCloudServerStatusTimingQueryWrapper.getCustomSqlSegment());
            List<VmCloudServerStatusTimingDTO> list = vmCloudServerStatusTimingMapper.listVmCloudServerStatusTiming(vmCloudServerStatusTimingQueryWrapper);
            List<VmCloudServerStatusTimingDTO> filterList = list.stream().filter(v -> {
                Long days = Long.parseLong(optimizationRuleFieldCondition.getValue()) * 86400L;
                if (tableColumnName.toLowerCase().contains("shutdown")) {
                    return compareDuration(v.getLastShutdownDuration(), optimizationRuleFieldCondition.getCompare(), days);
                }
                if (tableColumnName.toLowerCase().contains("running")) {
                    return compareDuration(v.getLastRunningDuration(), optimizationRuleFieldCondition.getCompare(), days);
                }
                return false;
            }).toList();
            List<String> resourceIdList = filterList.stream().map(VmCloudServerStatusTiming::getCloudServerId).toList();
            // 如果没有的话，设置一个不存在的ID,使得条件不通过
            wrapper.in(true, "id", CollectionUtils.isNotEmpty(resourceIdList) ? resourceIdList : List.of("无"));
        }
    }

    private boolean compareDuration(Long duration, String compare, Long days) {
        switch (OptimizationRuleFieldCompare.valueOf(compare)) {
            case EQ -> {
                return duration.longValue() == days.longValue();
            }
            case LE -> {
                return duration <= days;
            }
            case LT -> {
                return duration < days;
            }
            case GE -> {
                return duration >= days;
            }
            case GT -> {
                return duration > days;
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * 构建查询云主机开关机时长查询语句
     * 目前只会通过开机时长、关机时长查询
     * 示例
     * 持续开机时 >= 值
     * last_ 自定义了sql字段
     *
     * @param compare     比较
     * @param columnName  列名
     * @param columnValue 列值 天数
     * @return {@link QueryWrapper }<{@link VmCloudServerStatusTiming }>
     */
    private QueryWrapper toVmCloudServerStatusTimingQueryWrapper(String compare, String columnName, String columnValue) {
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.having(true, "last_" + columnName + OptimizationRuleFieldCompare.valueOf(compare).getCompare() + (Long.parseLong(columnValue) * 86400L));
        return wrapper;
    }
}


