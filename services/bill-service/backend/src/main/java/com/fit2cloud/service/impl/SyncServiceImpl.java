package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.ScriptQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.JobRecord;
import com.fit2cloud.base.entity.JobRecordResourceMapping;
import com.fit2cloud.base.service.IBaseJobRecordResourceMappingService;
import com.fit2cloud.common.constants.JobConstants;
import com.fit2cloud.common.constants.JobStatusConstants;
import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.es.repository.CloudBillRepository;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.service.BaseSyncService;
import com.fit2cloud.service.SyncService;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Consumer;
import org.apache.commons.collections4.MapUtils;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
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
    /**
     * 任务描述
     */
    private static final String jobDescription = "同步账单";
    /**
     * 默认出账日为每月10号
     */
    private static final Integer billDay = 10;

    @Override
    public void syncBill(String cloudAccountId) {
        syncBill(cloudAccountId, getMonths(billDay));
    }

    @Override
    public void syncBill(String cloudAccountId, String... months) {
        syncBill(cloudAccountId, Arrays.stream(months).toList());
    }

    @Override
    public void syncBill(String cloudAccountId, List<String> months) {
        syncBill(cloudAccountId, months, null);
    }

    /**
     * 同步账单
     *
     * @param cloudAccountId 云账号id
     * @param months         月份
     * @param billSetting    账单设置
     */
    private void syncBill(String cloudAccountId, List<String> months, Map<String, Object> billSetting) {
        proxy(cloudAccountId,
                months,
                (p, r) -> syncBill(p, r, cloudAccountId),
                (a, month) -> this.getExecMethodArgs(a, month, billSetting),
                this::saveBatchOrUpdate,
                this::writeJobRecord,
                this::deleteDataSource);
    }

    @Override
    public void syncBill(Map<String, Object> params) {
        if (!params.containsKey(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name())) {
            throw new RuntimeException("必要参数云账号不存在");
        }
        // 云账号id
        String cloudAccountId = params.get(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name()).toString();
        Map<String, Object> billSetting = null;
        if (params.containsKey(JobConstants.CloudAccount.BILL_SETTING.name())) {
            billSetting = JsonUtil.parseObject(JsonUtil.toJSONString(params.get(JobConstants.CloudAccount.BILL_SETTING.name())), Map.class);
        }
        List<String> months = getMonths(billDay);
        if (params.containsKey("MONTHS")) {
            months = JsonUtil.parseArray(JsonUtil.toJSONString(params.get("MONTHS")), String.class);
        }
        syncBill(cloudAccountId, months, billSetting);
    }

    /**
     * 根据云账号id删除数据
     *
     * @param cloudAccountId 云账号id
     */
    private void deleteDataSource(String cloudAccountId) {
        // 如果云账号不存在,删除es对应数据
        elasticsearchTemplate.delete(new NativeQueryBuilder().withQuery(new Query.Builder().term(new TermQuery.Builder().field("cloudAccountId").value(FieldValue.of(cloudAccountId)).build()).build()).build(), CloudBill.class, IndexCoordinates.of(CloudBill.class.getAnnotation(Document.class).indexName()));
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
        return cloudProvider.syncBill(request).stream().map(cloudBill -> appendSystemParams(cloudBill, cloudAccountId)).map(cloudBill -> appendSplitBillParams(cloudBill, cloudAccountId)).toList();
    }

    /**
     * 添加分账信息
     *
     * @param cloudBill      云账单
     * @param cloudAccountId 云账号id
     * @return 云账单
     */
    private CloudBill appendSplitBillParams(CloudBill cloudBill, String cloudAccountId) {
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
                defaultParams = PlatformConstants.valueOf(cloudAccount.getPlatform()).getBillClass().getConstructor().newInstance().getDefaultParams();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("credential", JsonUtil.parseObject(cloudAccount.getCredential()));
        params.put("bill", defaultParams);
        params.put("month", month);
        return JsonUtil.toJSONString(params);
    }

    /**
     * 插入并更新数据
     *
     * @param saveBatchOrUpdateParams 插入更新参数
     */
    private void saveBatchOrUpdate(BiSaveBatchOrUpdateParams<CloudBill> saveBatchOrUpdateParams) {
        //todo 构建删除数据查询条件
        ScriptQuery scriptQuery = new ScriptQuery.Builder().script(s -> s.inline(inlineScript -> inlineScript.lang("painless").source("doc['billingCycle'].value.monthValue==params.month&&doc['billingCycle'].value.year==params.year&&doc['provider'].value==params.platform")
                .params(getQuertParams(saveBatchOrUpdateParams.getRequestParams(), saveBatchOrUpdateParams.getCloudAccount().getPlatform())))).build();
        // todo 删除数据
        elasticsearchTemplate.delete(new NativeQueryBuilder().withQuery(new Query.Builder().script(scriptQuery).build()).build(), CloudBill.class, IndexCoordinates.of(CloudBill.class.getAnnotation(Document.class).indexName()));
        //todo 插入数据
        List<CloudBill> syncRecord = saveBatchOrUpdateParams.getSyncRecord();
        List<List<CloudBill>> lists = CommonUtil.averageAssign(syncRecord, 1000);
        for (List<CloudBill> list : lists) {
            cloudBillRepository.saveAll(list);
        }
    }

    /**
     * 获取脚本查询参数
     *
     * @param requestParams 调用同步查询的请求参数
     * @param platform      云平台
     * @return 查询数据
     */
    private Map<String, JsonData> getQuertParams(String requestParams, String platform) {
        String month = JsonUtil.parseObject(requestParams).get("month").asText();
        String[] split = month.split("-");
        return new HashMap<>() {{
            put("year", JsonData.of(Integer.parseInt(split[0])));
            put("month", JsonData.of(Integer.parseInt(split[1])));
            put("platform", JsonData.of(platform));
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
        DoubleSummaryStatistics summaryStatistics = syncRecord.stream().collect(Collectors.summarizingDouble(b -> b.getRealTotalCost().doubleValue()));
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
        jobRecord.setDescription(jobDescription);
        jobRecord.setStatus(JobStatusConstants.SYNCING);
        jobRecord.setParams(new HashMap<>());
        jobRecord.setType(JobTypeConstants.CLOUD_ACCOUNT_SYNC_BILL_JOB);
        jobRecord.setCreateTime(syncTime);
        // 插入任务数据
        baseJobRecordService.save(jobRecord);
        // 插入关联关系
        JobRecordResourceMapping jobRecordResourceMapping = new JobRecordResourceMapping();
        jobRecordResourceMapping.setResourceId(cloudAccountId);
        jobRecordResourceMapping.setJobType(JobTypeConstants.CLOUD_ACCOUNT_SYNC_BILL_JOB);
        jobRecordResourceMapping.setJobRecordId(jobRecord.getId());
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
    private <T> void proxy(String cloudAccountId,
                           List<String> months,
                           BiFunction<ICloudProvider, String, List<T>> execMethod,
                           BiFunction<CloudAccount, String, String> getExecMethodArgs,
                           Consumer<BiSaveBatchOrUpdateParams<T>> saveBatchOrUpdate,
                           Consumer<BiSaveBatchOrUpdateParams<T>> writeJobRecord,
                           Consumer<String> remote) {
        proxy(cloudAccountId, jobDescription, months, ICloudProvider::of, syncTime -> initJobRecord(syncTime, cloudAccountId), execMethod, getExecMethodArgs, saveBatchOrUpdate, writeJobRecord, remote);
    }

    /**
     * 如果还未到上个月的出账日,还需要同步上个月数据,反之则同步当前月
     *
     * @param billingDay 出账日
     * @return 需要同步的月份
     */
    private static List<String> getMonths(Integer billingDay) {
        Calendar instance = Calendar.getInstance();
        String currentMonth = String.format("%04d-%02d", instance.get(Calendar.YEAR), instance.get(Calendar.MONTH) + 1);
        instance.add(Calendar.MONTH, -1);
        if (instance.get(Calendar.DAY_OF_MONTH) < billingDay) {
            String upMonth = String.format("%04d-%02d", instance.get(Calendar.YEAR), instance.get(Calendar.MONTH));
            return List.of(currentMonth, upMonth);
        }
        return List.of(currentMonth);
    }
}
