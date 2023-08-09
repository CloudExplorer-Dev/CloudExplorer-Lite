package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import co.elastic.clients.json.JsonData;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fit2cloud.autoconfigure.PluginsContextHolder;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.JobRecord;
import com.fit2cloud.base.entity.JobRecordResourceMapping;
import com.fit2cloud.base.service.IBaseJobRecordResourceMappingService;
import com.fit2cloud.common.constants.JobConstants;
import com.fit2cloud.common.constants.JobStatusConstants;
import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.IBaseCloudProvider;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.util.MonthUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.constants.EsWriteLockConstants;
import com.fit2cloud.dao.entity.BillDimensionSetting;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.es.repository.CloudBillRepository;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.service.BaseSyncService;
import com.fit2cloud.service.IBillDimensionSettingService;
import com.fit2cloud.service.SyncService;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Consumer;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/18  4:02 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Service
public class SyncServiceImpl extends BaseSyncService implements SyncService {
    @Resource
    private IBaseJobRecordResourceMappingService jobRecordResourceMappingService;
    @Resource
    private CloudBillRepository cloudBillRepository;
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    @Resource
    private ElasticsearchClient elasticsearchClient;
    @Resource
    private IBillDimensionSettingService billDimensionSettingService;
    /**
     * 分账字段分割
     */
    private static final String ledgerSeparator = "::";
    /**
     * 任务描述
     */
    private static final String jobDescription = "同步账单";
    /**
     * 默认出账日为每月10号
     */
    private static final Integer billDay = 10;

    @Override
    @Caching(evict = {@CacheEvict(value = "bill_view", allEntries = true),
            @CacheEvict(value = "dimension_setting", allEntries = true),
            @CacheEvict(value = "bill_rule", allEntries = true)})
    public void syncBill(String cloudAccountId) {
        syncBill(cloudAccountId, MonthUtil.getMonths(billDay));
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "bill_view", allEntries = true),
            @CacheEvict(value = "dimension_setting", allEntries = true),
            @CacheEvict(value = "bill_rule", allEntries = true)})
    public void syncBill(String cloudAccountId, String... months) {
        syncBill(cloudAccountId, Arrays.stream(months).toList());
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "bill_view", allEntries = true),
            @CacheEvict(value = "dimension_setting", allEntries = true),
            @CacheEvict(value = "bill_rule", allEntries = true)})
    public void syncBill(String cloudAccountId, List<String> months) {
        syncBill(cloudAccountId, months, null);
    }


    @Override
    @Caching(evict = {@CacheEvict(value = "bill_view", allEntries = true),
            @CacheEvict(value = "dimension_setting", allEntries = true),
            @CacheEvict(value = "bill_rule", allEntries = true)})
    public void syncBill(Map<String, Object> params) {
        if (!params.containsKey(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name())) {
            throw new Fit2cloudException(ErrorCodeConstants.BILL_SYNC_REQUIRED_PARAMS_NOT_EXIST.getCode(), ErrorCodeConstants.BILL_SYNC_REQUIRED_PARAMS_NOT_EXIST.getMessage(new Object[]{"云账户id"}));
        }
        // 云账号id
        String cloudAccountId = params.get(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name()).toString();
        CloudAccount cloudAccount = cloudAccountService.getById(cloudAccountId);
        Map<String, Object> billSetting = null;
        if (params.containsKey(JobConstants.CloudAccount.BILL_SETTING.name())) {
            billSetting = JsonUtil.parseObject(JsonUtil.toJSONString(params.get(JobConstants.CloudAccount.BILL_SETTING.name())), new TypeReference<>() {
            });
        }
        // 获取默认同步月份
        List<String> months = MonthUtil.getMonths(billDay);
        // 如果参数有传输 则使用参数月份
        if (params.containsKey("MONTHS")) {
            months = JsonUtil.parseArray(JsonUtil.toJSONString(params.get("MONTHS")), String.class);
        }
        // 如果是桶 则获取桶中月份
        if (Objects.nonNull(cloudAccount) && Objects.nonNull(billSetting) && params.containsKey("BUCKET_CYCLE") && StringUtils.equals((String) params.get("BUCKET_CYCLE"), "all")) {
            Class<? extends ICloudProvider> of = ICloudProvider.of(cloudAccount.getPlatform());
            String execMethodArgs = getExecMethodArgs(cloudAccount, "", billSetting);
            months = CommonUtil.exec(of, execMethodArgs, ICloudProvider::listBucketFileMonth);
        }
        // 如果没有同步过,则同步历史12个月的账单
        long count = jobRecordResourceMappingService.count(new LambdaQueryWrapper<JobRecordResourceMapping>()
                .eq(JobRecordResourceMapping::getJobType, JobTypeConstants.CLOUD_ACCOUNT_SYNC_BILL_JOB)
                .eq(JobRecordResourceMapping::getResourceId, cloudAccountId)
                .eq(JobRecordResourceMapping::getResourceType, com.fit2cloud.constants.JobConstants.JobSyncResourceType.BILL.name()));
        if (count == 0) {
            months = MonthUtil.getHistoryMonth(12);
        }
        // 删除不属于当前已有云账号的数据
        deleteNotFountCloudAccountData();
        syncBill(cloudAccountId, months, billSetting);
    }


    /**
     * 同步账单
     *
     * @param cloudAccountId 云账号id
     * @param months         月份
     * @param billSetting    账单设置
     */
    private void syncBill(String cloudAccountId, List<String> months, Map<String, Object> billSetting) {
        proxy(cloudAccountId, months, (p, r) -> syncBill(p, r, cloudAccountId), (a, month) -> this.getExecMethodArgs(a, month, billSetting), this::saveBatchOrUpdate, this::writeJobRecord, this::deleteDataSource);
    }

    /**
     * 根据云账号id删除数据
     *
     * @param cloudAccountId 云账号id
     */
    @Override
    public void deleteDataSource(String cloudAccountId) {
        synchronized (EsWriteLockConstants.WRITE_LOCK) {
            // 如果云账号不存在,删除es对应数据
            elasticsearchTemplate.delete(new NativeQueryBuilder()
                    .withQuery(new Query.Builder()
                            .term(new TermQuery.Builder().field("cloudAccountId")
                                    .value(FieldValue.of(cloudAccountId)).build()).build()).build(), CloudBill.class, IndexCoordinates.of(CloudBill.class.getAnnotation(Document.class).indexName()));
        }
    }

    /**
     * 清理不存在的云账号数据
     */
    @Override
    public void deleteNotFountCloudAccountData() {
        // 所有的云账号
        List<CloudAccount> cloudAccounts = cloudAccountService.list();
        synchronized (EsWriteLockConstants.WRITE_LOCK) {
            Query query = new BoolQuery.Builder().mustNot(new Query.Builder().terms(new TermsQuery.Builder()
                    .terms(new TermsQueryField.Builder().value(cloudAccounts.stream().map(CloudAccount::getId)
                            .map(FieldValue::of).toList()).build()).field("cloudAccountId").build()).build()).build()._toQuery();
            DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest.Builder().query(query).refresh(Boolean.TRUE)
                    .index(CloudBill.class.getAnnotation(Document.class).indexName()).build();
            try {
                elasticsearchClient.deleteByQuery(deleteByQueryRequest);
            } catch (Exception ignored) {
            }

        }
    }


    public List<CloudBill> appendParams(List<CloudBill> cloudBills, String cloudAccountId) {
        return cloudBills.stream().map(cloudBill -> appendSystemParams(cloudBill, cloudAccountId))
                .map(cloudBill -> appendAggregationsParams(cloudBill, cloudAccountId)).toList();
    }

    /**
     * 同步账单
     *
     * @param cloudProvider  云平台处理器
     * @param request        请求函数
     * @param cloudAccountId 云账号id
     * @return 账单数据
     */
    private List<CloudBill> syncBill(ICloudProvider cloudProvider, String request, String cloudAccountId) {
        return cloudProvider.syncBill(request).stream()
                .map(cloudBill -> appendSystemParams(cloudBill, cloudAccountId))
                .map(cloudBill -> appendAggregationsParams(cloudBill, cloudAccountId)).toList();
    }

    /**
     * 添加聚合参数 用于冗余聚合 注意 使用runtime_mapping聚合很慢 所以将需要聚合的冗余起来
     *
     * @param cloudBill      账单对象
     * @param cloudAccountId 云账号id
     * @return 账单对象
     */
    private CloudBill appendAggregationsParams(CloudBill cloudBill, String cloudAccountId) {
        // 分账资源
        String ledgerResource =
                cloudBill.getResourceId() + ledgerSeparator +
                        cloudAccountId + ledgerSeparator +
                        cloudBill.getProjectId() + ledgerSeparator +
                        cloudBill.getTags().values().stream()
                                .filter(Objects::nonNull)
                                .map(Objects::toString)
                                .sorted(String::compareTo)
                                .collect(Collectors.joining(","));
        cloudBill.setAggregations(Map.of("ledgerResource", ledgerResource));
        return cloudBill;
    }

    /**
     * 添加系统数据
     *
     * @param cloudBill      账单对象
     * @param cloudAccountId 云账号id
     * @return 账单对象
     */
    private CloudBill appendSystemParams(CloudBill cloudBill, String cloudAccountId) {
        cloudBill.setCloudAccountId(cloudAccountId);
        return cloudBill;
    }

    /**
     * 获取执行函数参数
     *
     * @param cloudAccount 云账号
     * @param month        月份
     * @return 执行函数参数
     */
    private String getExecMethodArgs(CloudAccount cloudAccount, String month, Map<String, Object> billSetting) {
        Map<String, Object> defaultParams = null;
        try {
            if (MapUtils.isEmpty(billSetting)) {
                defaultParams = Map.of();
            } else {
                defaultParams = billSetting;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("credential", JsonUtil.parseObject(cloudAccount.getCredential()));
        params.put("bill", defaultParams);
        params.put("month", month);
        params.put("cloudAccountId", cloudAccount.getId());
        return JsonUtil.toJSONString(params);
    }

    /**
     * 插入并更新数据
     *
     * @param saveBatchOrUpdateParams 插入更新参数
     */
    private void saveBatchOrUpdate(BiSaveBatchOrUpdateParams<CloudBill> saveBatchOrUpdateParams) {
        //todo 构建删除数据查询条件
        ScriptQuery scriptQuery = new ScriptQuery.Builder().script(s -> s.inline(inlineScript -> inlineScript.lang("painless").source("doc['billingCycle'].value.monthValue==params.month&&doc['billingCycle'].value.year==params.year&&doc['cloudAccountId'].value==params.cloudAccountId").params(getQueryParams(saveBatchOrUpdateParams.getRequestParams(), saveBatchOrUpdateParams.getCloudAccount().getId())))).build();
        synchronized (EsWriteLockConstants.WRITE_LOCK) {
            // todo 删除数据
            elasticsearchTemplate.delete(new NativeQueryBuilder().withQuery(new Query.Builder().script(scriptQuery).build()).build(), CloudBill.class, IndexCoordinates.of(CloudBill.class.getAnnotation(Document.class).indexName()));
            //todo 插入数据
            List<CloudBill> syncRecord = saveBatchOrUpdateParams.getSyncRecord();
            List<List<CloudBill>> lists = CommonUtil.averageAssign(syncRecord, 5000);
            for (List<CloudBill> list : lists) {
                cloudBillRepository.saveAll(list);
            }
        }

    }

    /**
     * 获取脚本查询参数
     *
     * @param requestParams  调用同步查询的请求参数
     * @param cloudAccountId 云平台
     * @return 查询数据
     */
    private Map<String, JsonData> getQueryParams(String requestParams, String cloudAccountId) {
        String month = JsonUtil.parseObject(requestParams).get("month").asText();
        String[] split = month.split("-");
        return new HashMap<>() {{
            put("year", JsonData.of(Integer.parseInt(split[0])));
            put("month", JsonData.of(Integer.parseInt(split[1])));
            put("cloudAccountId", JsonData.of(cloudAccountId));
        }};
    }

    /**
     * 插入任务记录
     *
     * @param saveBatchOrUpdateParams 写入任务记录
     */
    private void writeJobRecord(BiSaveBatchOrUpdateParams<CloudBill> saveBatchOrUpdateParams) {
        JobRecord jobRecord = saveBatchOrUpdateParams.getJobRecord();
        List<CloudBill> syncRecord = saveBatchOrUpdateParams.getSyncRecord();
        DoubleSummaryStatistics summaryStatistics = syncRecord.stream()
                .collect(Collectors.summarizingDouble(b -> b.getCost().getOrDefault("payableAmount", BigDecimal.ZERO).doubleValue()));
        HashMap<String, Object> params = new HashMap<>();
        params.put("month", JsonUtil.parseObject(saveBatchOrUpdateParams.getRequestParams()).get("month").toString());
        params.put("size", summaryStatistics.getCount());
        params.put("sum", summaryStatistics.getSum());
        params.put("max", summaryStatistics.getMax());
        params.put("min", summaryStatistics.getMin());
        params.put("average", summaryStatistics.getAverage());
        params.put("platform", saveBatchOrUpdateParams.getCloudAccount().getPlatform());
        if (!jobRecord.getParams().containsKey(JobTypeConstants.CLOUD_ACCOUNT_SYNC_BILL_JOB.name())) {
            jobRecord.getParams().put(JobTypeConstants.CLOUD_ACCOUNT_SYNC_BILL_JOB.name(), new ArrayList<Map<String, Object>>() {{
                add(params);
            }});
        } else {
            ArrayList<Map<String, Object>> maps = (ArrayList<Map<String, Object>>) jobRecord.getParams().get(JobTypeConstants.CLOUD_ACCOUNT_SYNC_BILL_JOB.name());
            maps.add(params);
        }
        baseJobRecordService.updateById(jobRecord);
    }

    /**
     * 初始化任务记录
     *
     * @param syncTime       同步时间
     * @param cloudAccountId 云账户id
     * @return 任务记录对象
     */
    private JobRecord initJobRecord(LocalDateTime syncTime, String cloudAccountId) {
        JobRecord jobRecord = new JobRecord();
        jobRecord.setDescription(com.fit2cloud.constants.JobConstants.JobSyncResourceType.BILL.getMessage());
        jobRecord.setStatus(JobStatusConstants.SYNCING);
        jobRecord.setParams(new HashMap<>());
        jobRecord.setType(JobTypeConstants.CLOUD_ACCOUNT_SYNC_BILL_JOB);
        jobRecord.setCreateTime(syncTime);
        jobRecord.setUpdateTime(syncTime);
        // 插入任务数据
        baseJobRecordService.save(jobRecord);
        // 插入关联关系
        JobRecordResourceMapping jobRecordResourceMapping = new JobRecordResourceMapping();
        jobRecordResourceMapping.setResourceId(cloudAccountId);
        jobRecordResourceMapping.setJobType(JobTypeConstants.CLOUD_ACCOUNT_SYNC_BILL_JOB);
        jobRecordResourceMapping.setResourceType(com.fit2cloud.constants.JobConstants.JobSyncResourceType.BILL.name());
        jobRecordResourceMapping.setJobRecordId(jobRecord.getId());
        jobRecordResourceMapping.setUpdateTime(syncTime);
        jobRecordResourceMapping.setCreateTime(syncTime);
        jobRecordResourceMappingService.save(jobRecordResourceMapping);
        return jobRecord;
    }

    /**
     * 代理同步函数
     *
     * @param cloudAccountId    云账号id
     * @param months            月份
     * @param execMethod        执行函数
     * @param getExecMethodArgs 执行函数参数
     * @param saveBatchOrUpdate 插入并且更新数据
     * @param writeJobRecord    写入任务记录
     * @param remote            如果云账号不存在则删除
     * @param <T>               账单
     */
    private <T> void proxy(String cloudAccountId, List<String> months, BiFunction<ICloudProvider, String, List<T>> execMethod, BiFunction<CloudAccount, String, String> getExecMethodArgs, Consumer<BiSaveBatchOrUpdateParams<T>> saveBatchOrUpdate, Consumer<BiSaveBatchOrUpdateParams<T>> writeJobRecord, Consumer<String> remote) {
        proxy(cloudAccountId, jobDescription, months, p -> PluginsContextHolder.getPlatformExtension(ICloudProvider.class, p), syncTime -> initJobRecord(syncTime, cloudAccountId), execMethod, getExecMethodArgs, saveBatchOrUpdate, writeJobRecord, () -> auth(months, cloudAccountId), remote);
    }

    /**
     * 账单授权
     *
     * @param months         需要授权的月份
     * @param cloudAccountId 需要授权的云账号id
     */
    private void auth(List<String> months, String cloudAccountId) {
        List<BillDimensionSetting> billDimensionSettings = billDimensionSettingService.list()
                .stream()
                .sorted(Comparator.comparing(BillDimensionSetting::getUpdateTime))
                .toList();
        for (BillDimensionSetting billDimensionSetting : billDimensionSettings) {
            for (String month : months) {
                if (billDimensionSetting.getUpdateFlag()) {
                    billDimensionSettingService.clearAuthorize(billDimensionSetting, month, cloudAccountId);
                }
                billDimensionSettingService.authorize(billDimensionSetting, month, cloudAccountId);
            }
        }

    }

    @Override
    protected Credential getCredential(CloudAccount cloudAccount) {
        IBaseCloudProvider platformExtension = PluginsContextHolder.getPlatformExtension(IBaseCloudProvider.class, cloudAccount.getPlatform());
        return JsonUtil.parseObject(cloudAccount.getCredential(), platformExtension.getCloudAccountMeta().credential);
    }
}
