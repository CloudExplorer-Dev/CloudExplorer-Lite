package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.autoconfigure.PluginsContextHolder;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudDatastore;
import com.fit2cloud.base.entity.VmCloudHost;
import com.fit2cloud.base.mapper.BaseVmCloudDatastoreMapper;
import com.fit2cloud.base.mapper.BaseVmCloudHostMapper;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.base.service.IBaseVmCloudDatastoreService;
import com.fit2cloud.base.service.IBaseVmCloudHostService;
import com.fit2cloud.common.es.ElasticsearchProvide;
import com.fit2cloud.common.es.constants.IndexConstants;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.provider.IBaseCloudProvider;
import com.fit2cloud.common.provider.entity.F2CEntityType;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.DateUtil;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.constants.SpecialAttributesConstants;
import com.fit2cloud.controller.request.base.resource.analysis.ResourceAnalysisRequest;
import com.fit2cloud.controller.request.base.resource.analysis.ResourceUsedTrendRequest;
import com.fit2cloud.controller.request.datastore.PageDatastoreRequest;
import com.fit2cloud.controller.request.host.PageHostRequest;
import com.fit2cloud.controller.response.ChartData;
import com.fit2cloud.controller.response.ResourceAllocatedInfo;
import com.fit2cloud.dto.AnalysisDatastoreDTO;
import com.fit2cloud.dto.AnalysisHostDTO;
import com.fit2cloud.dto.KeyValue;
import com.fit2cloud.es.entity.PerfMetricMonitorData;
import com.fit2cloud.service.IBaseResourceAnalysisService;
import com.fit2cloud.utils.OperationUtils;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jianneng
 * {@code @date} 2022/12/11 18:43
 **/
@Service
public class BaseResourceAnalysisServiceImpl implements IBaseResourceAnalysisService {
    @Resource
    private BaseVmCloudHostMapper baseVmCloudHostMapper;
    @Resource
    private BaseVmCloudDatastoreMapper baseVmCloudDatastoreMapper;
    @Resource
    private IBaseCloudAccountService iBaseCloudAccountService;
    @Resource
    private IBaseVmCloudHostService iBaseVmCloudHostService;
    @Resource
    private IBaseVmCloudDatastoreService iBaseVmCloudDatastoreService;
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    @Resource
    private ElasticsearchProvide elasticsearchProvide;
    @Resource
    private CurrentUserResourceService currentUserResourceService;

    /**
     * @param request 宿主机分页查询参数
     */
    @Override
    public IPage<AnalysisHostDTO> pageHost(PageHostRequest request) {
        Page<AnalysisHostDTO> page = PageUtil.of(request, AnalysisHostDTO.class, null, true);
        // 构建查询参数
        MPJLambdaWrapper<VmCloudHost> wrapper = new MPJLambdaWrapper<VmCloudHost>().selectAll(VmCloudHost.class);
        wrapper.like(StringUtils.isNotBlank(request.getHostName()), VmCloudHost::getHostName, request.getHostName());
        wrapper.selectAs(CloudAccount::getName, AnalysisHostDTO::getAccountName);
        wrapper.selectAs(CloudAccount::getPlatform, AnalysisDatastoreDTO::getPlatform);
        wrapper.leftJoin(CloudAccount.class, CloudAccount::getId, AnalysisDatastoreDTO::getAccountId);
        wrapper.orderByDesc(CloudAccount::getCreateTime);
        return baseVmCloudHostMapper.selectJoinPage(page, AnalysisHostDTO.class, wrapper);
    }

    @Override
    public long countHost() {
        return iBaseVmCloudHostService.count();
    }

    @Override
    public long countHost(String cloudAccountId) {
        MPJLambdaWrapper<VmCloudHost> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(StringUtils.isNotEmpty(cloudAccountId), VmCloudHost::getAccountId, cloudAccountId);
        return iBaseVmCloudHostService.count(wrapper);
    }

    /**
     * @param request 存储器分页查询参数
     */
    @Override
    public IPage<AnalysisDatastoreDTO> pageDatastore(PageDatastoreRequest request) {
        Page<AnalysisDatastoreDTO> page = PageUtil.of(request, AnalysisDatastoreDTO.class, null, false);
        // 构建查询参数
        MPJLambdaWrapper<VmCloudDatastore> wrapper = addQueryDatastore(request);
        wrapper.selectAs(CloudAccount::getName, AnalysisDatastoreDTO::getAccountName);
        wrapper.selectAs(CloudAccount::getPlatform, AnalysisDatastoreDTO::getPlatform);
        wrapper.leftJoin(CloudAccount.class, CloudAccount::getId, AnalysisDatastoreDTO::getAccountId);
        wrapper.orderByDesc(VmCloudDatastore::getCreateTime);
        IPage<AnalysisDatastoreDTO> result = baseVmCloudDatastoreMapper.selectJoinPage(page, AnalysisDatastoreDTO.class, wrapper);
        //计算
        result.getRecords().forEach(v -> {
            v.setAllocated(String.valueOf(v.getAllocatedSpace()));
            BigDecimal useRate = new BigDecimal(v.getCapacity()).subtract(new BigDecimal(v.getFreeSpace())).multiply(new BigDecimal(100)).divide(new BigDecimal(v.getCapacity()), 2, RoundingMode.UP);
            v.setUseRate(String.valueOf(useRate));
            BigDecimal freeRate = new BigDecimal(v.getFreeSpace()).multiply(new BigDecimal(100)).divide(new BigDecimal(v.getCapacity()), 2, RoundingMode.UP);
            v.setFreeRate(String.valueOf(freeRate));
        });
        return result;
    }

    @Override
    public long countDatastore() {
        return iBaseVmCloudDatastoreService.count();
    }

    @Override
    public long countDatastore(String cloudAccountId) {
        MPJLambdaWrapper<VmCloudDatastore> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(StringUtils.isNotEmpty(cloudAccountId), VmCloudDatastore::getAccountId, cloudAccountId);
        return iBaseVmCloudDatastoreService.count(wrapper);
    }

    private MPJLambdaWrapper<VmCloudDatastore> addQueryDatastore(PageDatastoreRequest request) {
        MPJLambdaWrapper<VmCloudDatastore> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(VmCloudDatastore.class);
        wrapper.like(StringUtils.isNotBlank(request.getDatastoreName()), VmCloudDatastore::getDatastoreName, request.getDatastoreName());
        return wrapper;
    }

    /**
     * 私有云账号，VMWare vSphere、OpenStack
     *
     * @return List<CloudAccount>
     */
    @Override
    public List<CloudAccount> getAllPrivateCloudAccount() {
        QueryWrapper<CloudAccount> queryWrapper = new QueryWrapper<>();
        List<String> strings = PluginsContextHolder.getExtensions(IBaseCloudProvider.class)
                .stream().filter(p -> !p.getCloudAccountMeta().publicCloud)
                .map(p -> p.getCloudAccountMeta().platform).distinct()
                .toList();
        queryWrapper.in(ColumnNameUtil.getColumnName(CloudAccount::getPlatform, true)
                , strings);
        return iBaseCloudAccountService.list(queryWrapper);
    }

    /**
     * 集群
     *
     * @return List<Map < String, String>>
     */
    @Override
    public List<Map<String, String>> getCluster(ResourceAnalysisRequest request) {
        allPrivateCloudAccount(request);
        List<Map<String, String>> result = new ArrayList<>();
        QueryWrapper<VmCloudHost> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()), ColumnNameUtil.getColumnName(VmCloudHost::getAccountId, true), request.getAccountIds());
        List<VmCloudHost> vmCloudHosts = iBaseVmCloudHostService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(vmCloudHosts)) {
            Map<String, List<VmCloudHost>> zoneMap = vmCloudHosts.stream().filter(v -> StringUtils.isNotEmpty(v.getZone())).collect(Collectors.groupingBy(VmCloudHost::getZone));
            zoneMap.forEach((k, v) -> {
                if (CollectionUtils.isNotEmpty(v)) {
                    Map<String, String> map = new HashMap<>(2);
                    map.put("id", v.get(0).getZone());
                    map.put("name", v.get(0).getZone());
                    map.put("accountId", v.get(0).getAccountId());
                    result.add(map);
                }
            });
        }
        return result;
    }

    private void allPrivateCloudAccount(ResourceAnalysisRequest request) {
        if (CollectionUtils.isEmpty(request.getAccountIds())) {
            request.setAccountIds(getAllPrivateCloudAccount().stream().map(CloudAccount::getId).toList());
        }
    }

    /**
     * 宿主机
     *
     * @return List<VmCloudHost>
     */
    @Override
    public List<VmCloudHost> getVmHost(ResourceAnalysisRequest request) {
        allPrivateCloudAccount(request);
        QueryWrapper<VmCloudHost> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()), ColumnNameUtil.getColumnName(VmCloudHost::getAccountId, true), request.getAccountIds());
        queryWrapper.in(CollectionUtils.isNotEmpty(request.getClusterIds()), ColumnNameUtil.getColumnName(VmCloudHost::getZone, true), request.getClusterIds());
        return iBaseVmCloudHostService.list(queryWrapper);
    }

    /**
     * 存储器
     *
     * @return List<VmCloudDatastore>
     */
    @Override
    public List<VmCloudDatastore> getVmCloudDatastore(ResourceAnalysisRequest request) {
        allPrivateCloudAccount(request);
        QueryWrapper<VmCloudDatastore> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()), ColumnNameUtil.getColumnName(VmCloudDatastore::getAccountId, true), request.getAccountIds());
        queryWrapper.in(CollectionUtils.isNotEmpty(request.getClusterIds()), ColumnNameUtil.getColumnName(VmCloudDatastore::getZone, true), request.getClusterIds());
        queryWrapper.groupBy(ColumnNameUtil.getColumnName(VmCloudDatastore::getDatastoreId, true));
        return iBaseVmCloudDatastoreService.list(queryWrapper);
    }

    /**
     * 分配情况
     *
     * @return Map<String, ResourceAllocatedInfo>
     */
    @Override
    public Map<String, ResourceAllocatedInfo> getResourceAllocatedInfo(ResourceAnalysisRequest request) {
        Map<String, ResourceAllocatedInfo> result = new HashMap<>(3);
        List<VmCloudHost> hosts = getVmHost(request);
        if (CollectionUtils.isNotEmpty(hosts)) {
            hosts = hosts.stream().filter(v -> !CollectionUtils.isNotEmpty(request.getHostIds()) || request.getHostIds().contains(v.getId())).toList();
            // 取core
            BigDecimal cpuTotal = new BigDecimal(hosts.stream().filter(v -> Objects.nonNull(v.getNumCpuCores())).mapToLong(VmCloudHost::getNumCpuCores).sum());
            BigDecimal cpuAllocated = new BigDecimal(hosts.stream().filter(v -> Objects.nonNull(v.getVmCpuCores())).mapToLong(VmCloudHost::getVmCpuCores).sum());
            BigDecimal cpuAllocatedRate = cpuAllocated.divide(cpuTotal, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            result.put(SpecialAttributesConstants.ResourceField.CPU, ResourceAllocatedInfo.builder()
                    .total(cpuTotal)
                    .allocated(cpuAllocated)
                    .allocatedRate(cpuAllocatedRate.compareTo(new BigDecimal(100)) > 0 ? new BigDecimal(100) : cpuAllocatedRate)
                    .build());
            BigDecimal memoryTotal = new BigDecimal(hosts.stream().filter(v -> Objects.nonNull(v.getMemoryTotal())).mapToLong(VmCloudHost::getMemoryTotal).sum());
            BigDecimal memoryAllocated = new BigDecimal(hosts.stream().filter(v -> Objects.nonNull(v.getMemoryAllocated())).mapToLong(VmCloudHost::getMemoryAllocated).sum());
            BigDecimal memoryAllocatedRate = memoryAllocated.divide(memoryTotal, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            result.put(SpecialAttributesConstants.ResourceField.MEMORY, ResourceAllocatedInfo.builder()
                    .total(memoryTotal.divide(new BigDecimal(1024), RoundingMode.HALF_UP))
                    .allocated(memoryAllocated.divide(new BigDecimal(1024), RoundingMode.HALF_UP))
                    .allocatedRate(memoryAllocatedRate.compareTo(new BigDecimal(100)) > 0 ? new BigDecimal(100) : memoryAllocatedRate)
                    .build());

        }
        List<VmCloudDatastore> datastoreList = getVmCloudDatastore(request);
        if (CollectionUtils.isNotEmpty(datastoreList)) {
            datastoreList = datastoreList.stream().filter(v -> !CollectionUtils.isNotEmpty(request.getDatastoreIds()) || request.getDatastoreIds().contains(v.getId())).toList();
            BigDecimal capacity = new BigDecimal(datastoreList.stream().filter(v -> Objects.nonNull(v.getCapacity())).mapToLong(VmCloudDatastore::getCapacity).sum());
            BigDecimal allocatedSpace = new BigDecimal(datastoreList.stream().filter(v -> Objects.nonNull(v.getAllocatedSpace())).mapToLong(VmCloudDatastore::getAllocatedSpace).sum());
            BigDecimal allocatedRate = allocatedSpace.divide(capacity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            result.put(SpecialAttributesConstants.ResourceField.DATASTORE, ResourceAllocatedInfo.builder()
                    .total(capacity)
                    .allocated(allocatedSpace)
                    .allocatedRate(allocatedRate.compareTo(new BigDecimal(100)) > 0 ? new BigDecimal(100) : allocatedRate)
                    .build());
        }
        return result;
    }

    /**
     * 资源分布情况
     *
     * @return Map<String, List < KeyValue>>
     */
    @Override
    public Map<String, List<KeyValue>> getResourceSpreadInfo(ResourceAnalysisRequest request) {
        Map<String, List<KeyValue>> result = new HashMap<>(3);
        List<CloudAccount> accountList = getAllPrivateCloudAccount();
        Map<String, CloudAccount> accountMap = accountList.stream().collect(Collectors.toMap(CloudAccount::getId, v -> v, (k1, k2) -> k1));
        if (accountMap.size() == 0) {
            return result;
        }
        List<VmCloudHost> hosts = getVmHost(request);
        if (CollectionUtils.isEmpty(hosts)) {
            return result;
        }
        hosts = hosts.stream().filter(v -> !CollectionUtils.isNotEmpty(request.getHostIds()) || request.getHostIds().contains(v.getId())).toList();
        // 主机在云账号上面的分布情况
        Map<String, Long> hostSpread = hosts.stream().filter(v -> StringUtils.isNotEmpty(v.getAccountId())).collect(Collectors.groupingBy(VmCloudHost::getAccountId, Collectors.counting()));
        result.put("host", hostSpread.entrySet().stream().map(c -> new KeyValue(StringUtils.isEmpty(accountMap.get(c.getKey()).getName()) ? c.getKey() : accountMap.get(c.getKey()).getName(), c.getValue())).toList());
        List<VmCloudDatastore> datastoreList = getVmCloudDatastore(request);
        datastoreList = datastoreList.stream().filter(v -> !CollectionUtils.isNotEmpty(request.getDatastoreIds()) || request.getDatastoreIds().contains(v.getId())).toList();
        // 存储器在云账号上面的分布情况
        Map<String, Long> datastoreSpread = datastoreList.stream().filter(v -> StringUtils.isNotEmpty(v.getAccountId())).collect(Collectors.groupingBy(VmCloudDatastore::getAccountId, Collectors.counting()));
        result.put("datastore", datastoreSpread.entrySet().stream().map(c -> new KeyValue(StringUtils.isEmpty(accountMap.get(c.getKey()).getName()) ? c.getKey() : accountMap.get(c.getKey()).getName(), c.getValue())).toList());
        List<KeyValue> vms = new ArrayList<>();
        // 云主机在宿主机上的分布情况
        hosts.forEach(v -> {
            KeyValue kv = new KeyValue();
            kv.setName(v.getHostName());
            switch (StringUtils.isEmpty(request.getVmStatus()) ? "" : request.getVmStatus()) {
                case "running" -> kv.setValue(v.getVmRunning());
                case "stopped" -> kv.setValue(v.getVmStopped());
                default -> kv.setValue(v.getVmTotal());
            }
            vms.add(kv);
        });
        result.put("vm", vms);
        return result;
    }

    /**
     * 资源使用趋势数据
     */
    @Override
    public List<ChartData> getResourceUsedTrendData(ResourceUsedTrendRequest request) {
        List<ChartData> result = new ArrayList<>();
        if (!elasticsearchProvide.existsIndex(IndexConstants.CE_PERF_METRIC_MONITOR_DATA.getCode())) {
            return result;
        }
        CalendarInterval intervalUnit = OperationUtils.getCalendarIntervalUnit(request.getStartTime(), request.getEndTime());
        try {
            request.setIntervalPosition(intervalUnit);
            if (CollectionUtils.isEmpty(request.getAccountIds())) {
                request.setAccountIds(currentUserResourceService.currentUserCloudAccountList().stream().map(CloudAccount::getId).toList());
            }
            SearchHits<PerfMetricMonitorData> response = elasticsearchTemplate.search(getSearchResourceTrendDataQuery(request), PerfMetricMonitorData.class, IndexCoordinates.of(IndexConstants.CE_PERF_METRIC_MONITOR_DATA.getCode()));
            return convertToTrendData(response, intervalUnit);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("获取云主机资源使用趋势异常:" + e.getMessage());
        }
        return result;
    }

    /**
     * 使用趋势数据转换
     */
    @Override
    public List<ChartData> convertToTrendData(SearchHits<PerfMetricMonitorData> response, CalendarInterval intervalUnit) {
        List<ChartData> result = new ArrayList<>();
        try {
            ElasticsearchAggregations aggregations = (ElasticsearchAggregations) response.getAggregations();
            assert aggregations != null;
            ElasticsearchAggregation aggregation = aggregations.aggregations().get(0);
            List<DateHistogramBucket> dateHistogramBucketList = aggregation.aggregation().getAggregate().dateHistogram().buckets().array();
            Map<String, Map<String, Long>> groupDateAndRange = new HashMap<>(5);
            dateHistogramBucketList.forEach(dateHistogramBucket -> Stream.of(0, 20, 40, 60, 80).forEach(interval -> {
                String rangeKey = interval + "~" + (interval + 20) + "%";
                String key = dateHistogramBucket.key() + "-" + rangeKey;
                Map<String, Long> map = new HashMap<>(5);
                map.put(rangeKey, 0L);
                List<StringTermsBucket> averageRanges = dateHistogramBucket.aggregations().get("instanceIds").sterms().buckets().array();
                if (!groupDateAndRange.containsKey(key) && CollectionUtils.isNotEmpty(averageRanges)) {
                    Long count = 0L;
                    //资源时间段内平均值
                    for (StringTermsBucket termsBucket : averageRanges) {
                        double avgValue = termsBucket.aggregations().get(SpecialAttributesConstants.SpecialField.AVERAGE).max().value();
                        //在区间里面
                        if (avgValue >= interval && avgValue <= (interval + 20)) {
                            count++;
                        }
                    }
                    map.put(rangeKey, count);
                }
                groupDateAndRange.put(key, map);
            }));
            groupDateAndRange.keySet().forEach(k -> {
                Map<String, Long> map = groupDateAndRange.get(k);
                map.keySet().forEach(r -> {
                    ChartData chartData = new ChartData();
                    chartData.setXAxis(OperationUtils.getTimeFormat(DateUtil.dateToString(Long.parseLong(k.split("-")[0]), null), intervalUnit));
                    chartData.setGroupName(r);
                    chartData.setYAxis(new BigDecimal(map.get(r)));
                    result.add(chartData);
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("获取云主机资源使用趋势异常:" + e.getMessage());
        }
        return result;
    }

    /**
     * 设置查询数据条件
     */
    private org.springframework.data.elasticsearch.core.query.Query getSearchResourceTrendDataQuery(ResourceUsedTrendRequest request) {
        List<QueryUtil.QueryCondition> queryConditions = elasticsearchProvide.getDefaultQueryConditions(request.getAccountIds(), request.getMetricName(), request.getEntityType(), request.getStartTime(), request.getEndTime());

        if (CollectionUtils.isNotEmpty(request.getClusterIds())) {
            QueryUtil.QueryCondition clusterNames = new QueryUtil.QueryCondition(true, "clusterName.keyword", request.getClusterIds(), QueryUtil.CompareType.IN);
            queryConditions.add(clusterNames);
        }
        if (CollectionUtils.isNotEmpty(request.getResourceIds())) {
            QueryUtil.QueryCondition resourceIds = new QueryUtil.QueryCondition(true, "instanceId.keyword", getResourceIds(request), QueryUtil.CompareType.IN);
            queryConditions.add(resourceIds);
        }
        return getRangeQuery(queryConditions, request.getIntervalPosition());
    }

    /**
     * 聚合查询，时间、资源ID、平均使用率
     */
    @Override
    public Query getRangeQuery(List<QueryUtil.QueryCondition> queryConditions, CalendarInterval intervalPosition) {
        BoolQuery.Builder boolQuery = QueryUtil.getQuery(queryConditions);
        NativeQueryBuilder query = new NativeQueryBuilder()
                .withPageable(PageRequest.of(0, 1))
                .withQuery(new co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder().bool(boolQuery.build()).build())
                .withSourceFilter(new FetchSourceFilter(new String[]{}, new String[]{"@version", "@timestamp", "host", "tags"}))
                .withAggregation("timestamp", new Aggregation.Builder().dateHistogram(new DateHistogramAggregation.Builder().field("timestamp").calendarInterval(intervalPosition).build())
                        .aggregations("instanceIds", new Aggregation.Builder().terms(new TermsAggregation.Builder().field("instanceId.keyword").size(Integer.MAX_VALUE).build())
                                .aggregations("average", new Aggregation.Builder().max(new MaxAggregation.Builder().field("average").build()).build()).build()).build());
        return query.build();
    }

    /**
     * 获取资源真实ID
     *
     * @return List<String>
     */
    private List<String> getResourceIds(ResourceUsedTrendRequest request) {
        List<String> resourceIds = new ArrayList<>();
        try {
            switch (F2CEntityType.valueOf(request.getEntityType())) {
                case HOST -> {
                    QueryWrapper<VmCloudHost> queryHostWrapper = new QueryWrapper<>();
                    queryHostWrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()), ColumnNameUtil.getColumnName(VmCloudHost::getAccountId, true), request.getAccountIds());
                    queryHostWrapper.in(true, ColumnNameUtil.getColumnName(VmCloudHost::getId, true), request.getResourceIds());
                    List<VmCloudHost> vmCloudHosts = iBaseVmCloudHostService.list(queryHostWrapper);
                    if (CollectionUtils.isNotEmpty(vmCloudHosts)) {
                        resourceIds = vmCloudHosts.stream().map(VmCloudHost::getHostId).toList();
                    }
                }
                case DATASTORE -> {
                    QueryWrapper<VmCloudDatastore> queryDatastoreWrapper = new QueryWrapper<>();
                    queryDatastoreWrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()), ColumnNameUtil.getColumnName(VmCloudHost::getAccountId, true), request.getAccountIds());
                    queryDatastoreWrapper.in(true, ColumnNameUtil.getColumnName(VmCloudDatastore::getId, true), request.getResourceIds());
                    List<VmCloudDatastore> vmCloudDatastoreList = iBaseVmCloudDatastoreService.list(queryDatastoreWrapper);
                    if (CollectionUtils.isNotEmpty(vmCloudDatastoreList)) {
                        resourceIds = vmCloudDatastoreList.stream().map(VmCloudDatastore::getDatastoreId).toList();
                    }
                }
                default -> {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CollectionUtils.isEmpty(resourceIds) ? request.getResourceIds() : resourceIds;
    }

    /**
     * 使用情况
     *
     * @return Map<String, ResourceAllocatedInfo>
     */
    @Override
    public Map<String, ResourceAllocatedInfo> getResourceUsedInfo(ResourceAnalysisRequest request) {
        Map<String, ResourceAllocatedInfo> result = new HashMap<>(3);
        List<VmCloudHost> hosts = getVmHost(request);
        if (CollectionUtils.isNotEmpty(hosts)) {
            hosts = hosts.stream().filter(v -> !CollectionUtils.isNotEmpty(request.getHostIds()) || request.getHostIds().contains(v.getId())).toList();
            BigDecimal cpuTotal = new BigDecimal(hosts.stream().filter(v -> Objects.nonNull(v.getCpuTotal())).mapToLong(VmCloudHost::getCpuTotal).sum());
            BigDecimal cpuUsed = new BigDecimal(hosts.stream().filter(v -> Objects.nonNull(v.getCpuUsed())).mapToLong(VmCloudHost::getCpuUsed).sum());
            BigDecimal cpuUsedRate = cpuUsed.divide(cpuTotal, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            result.put("cpu", ResourceAllocatedInfo.builder()
                    .total(cpuTotal)
                    .used(cpuUsed)
                    .usedRate(cpuUsedRate.compareTo(new BigDecimal(100)) > 0 ? new BigDecimal(100) : cpuUsedRate)
                    .build());
            BigDecimal memoryTotal = new BigDecimal(hosts.stream().filter(v -> Objects.nonNull(v.getMemoryTotal())).mapToLong(VmCloudHost::getMemoryTotal).sum());
            BigDecimal memoryUsed = new BigDecimal(hosts.stream().filter(v -> Objects.nonNull(v.getMemoryUsed())).mapToLong(VmCloudHost::getMemoryUsed).sum());
            BigDecimal memoryUsedRate = memoryUsed.divide(memoryTotal, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            result.put("memory", ResourceAllocatedInfo.builder()
                    .total(memoryTotal.divide(new BigDecimal(1024), RoundingMode.HALF_UP))
                    .used(memoryUsed.divide(new BigDecimal(1024), RoundingMode.HALF_UP))
                    .usedRate(memoryUsedRate.compareTo(new BigDecimal(100)) > 0 ? new BigDecimal(100) : memoryUsedRate)
                    .build());

        }
        List<VmCloudDatastore> datastoreList = getVmCloudDatastore(request);
        if (CollectionUtils.isNotEmpty(datastoreList)) {
            datastoreList = datastoreList.stream().filter(v -> !CollectionUtils.isNotEmpty(request.getDatastoreIds()) || request.getDatastoreIds().contains(v.getId())).toList();
            BigDecimal capacity = new BigDecimal(datastoreList.stream().filter(v -> Objects.nonNull(v.getCapacity())).mapToLong(VmCloudDatastore::getCapacity).sum());
            BigDecimal freeSpace = new BigDecimal(datastoreList.stream().filter(v -> Objects.nonNull(v.getFreeSpace())).mapToLong(VmCloudDatastore::getFreeSpace).sum());
            BigDecimal usedRate = capacity.subtract(freeSpace).divide(capacity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            result.put("datastore", ResourceAllocatedInfo.builder()
                    .total(capacity)
                    .used(capacity.subtract(freeSpace))
                    .usedRate(usedRate)
                    .build());
        } else {
            result.put("datastore", ResourceAllocatedInfo.builder()
                    .total(BigDecimal.ZERO)
                    .used(BigDecimal.ZERO)
                    .usedRate(BigDecimal.ZERO)
                    .build());
        }
        return result;
    }

}
