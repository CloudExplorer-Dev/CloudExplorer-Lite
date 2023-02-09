package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.NestedProperty;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.JobRecord;
import com.fit2cloud.base.entity.JobRecordResourceMapping;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.base.service.IBaseJobRecordResourceMappingService;
import com.fit2cloud.common.constants.JobStatusConstants;
import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.job_record.JobLink;
import com.fit2cloud.common.job_record.JobLinkTypeConstants;
import com.fit2cloud.common.job_record.JobRecordParam;
import com.fit2cloud.common.provider.exception.SkipPageException;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.constants.SyncDimensionConstants;
import com.fit2cloud.dao.entity.ComplianceRule;
import com.fit2cloud.dao.entity.ComplianceScanResult;
import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.service.*;
import io.reactivex.rxjava3.functions.Action;
import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/6  14:29}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Service
public class SyncServiceImpl extends BaseSyncService implements ISyncService {
    @Resource
    private IBaseCloudAccountService cloudAccountService;
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    @Resource
    private ElasticsearchClient elasticsearchClient;
    @Resource
    private IComplianceScanService complianceScanService;
    @Resource
    private IComplianceRuleService complianceRuleService;
    @Resource
    private IBaseJobRecordResourceMappingService jobRecordResourceMappingService;
    @Resource
    private ThreadPoolExecutor workThreadPool;
    @Resource
    private IComplianceScanResultService complianceScanResultService;

    @SneakyThrows
    @Override
    public void syncInstance(String cloudAccountId, ResourceTypeConstants instanceType) {
        CloudAccount cloudAccount = cloudAccountService.getById(cloudAccountId);
        // 加锁
        RLock lock = redissonClient.getLock(cloudAccountId + instanceType.name());
        // 如果指定时间拿不到锁就不执行同步
        if (!lock.tryLock(5, TimeUnit.SECONDS)) {
            return;
        }
        LocalDateTime syncTime = getSyncTime();
        ArrayList<ResourceInstance> resourceInstancesAll = new ArrayList<>();
        Class<? extends ICloudProvider> iCloudProviderClazz = ICloudProvider.of(cloudAccount.getPlatform());
        Map<ResourceTypeConstants, SyncDimensionConstants> map = CommonUtil.exec(iCloudProviderClazz, ICloudProvider::getResourceSyncDimensionConstants);
        // 如果不存在同步粒度则为不支持的资源类型
        if (!map.containsKey(instanceType)) {
            return;
        }
        JobRecord jobRecord = initJobRecord("同步" + instanceType.getMessage(), syncTime, cloudAccountId, instanceType);
        SyncDimensionConstants syncDimensionConstants = map.get(instanceType);
        List<Map<String, Object>> dimension = syncDimensionConstants.getDimensionExecParams().apply(cloudAccount, null);
        for (Map<String, Object> execParams : dimension) {
            try {
                List<ResourceInstance> resourceInstances = CommonUtil.exec(iCloudProviderClazz, JsonUtil.toJSONString(execParams), instanceType.getExec());
                resourceInstances.forEach(resourceInstance -> resourceInstance.setCloudAccountId(cloudAccountId));
                resourceInstancesAll.addAll(resourceInstances);
                updateJobRecord(jobRecord, resourceInstances, syncDimensionConstants, execParams);
            } catch (Exception e) {
                if (e instanceof SkipPageException) {
                    // todo 跳过当前区域
                    updateJobRecord(jobRecord, new ArrayList<>(), syncDimensionConstants, execParams);
                } else {
                    // todo 记录当前区域同步错误
                    updateJobRecord(jobRecord, getErrorDimensionJobRecordParam(jobRecord, e, syncDimensionConstants, execParams));
                }
            }
        }
        // todo 插入数据
        proxyJob(jobRecord, new JobLink("插入原始数据", JobLinkTypeConstants.SYSTEM_SAVE_DATA), () -> saveOrUpdateData(cloudAccountId, instanceType, resourceInstancesAll), null);
        // todo 更新缓存
        proxyJob(jobRecord, new JobLink("扫描合规资源", JobLinkTypeConstants.SYSTEM_SAVE_DATA), () -> scan(instanceType), null);
        // todo 更新扫描时间
        proxyJob(jobRecord, new JobLink("更新扫描时间", JobLinkTypeConstants.SYSTEM_SAVE_DATA), () ->
                        complianceRuleService.update(new LambdaUpdateWrapper<ComplianceRule>().eq(ComplianceRule::getResourceType, instanceType.name()).set(ComplianceRule::getUpdateTime, syncTime))
                , null);
        // todo 更新任务状态
        JobStatusConstants jobRecordStatus = getJobRecordStatus(jobRecord);
        jobRecord.setStatus(jobRecordStatus);
        updateJobRecord(jobRecord, JobRecordParam.success(new JobLink("任务执行结束", JobLinkTypeConstants.JOB_END), null));
    }


    /**
     * 扫描原始资源
     *
     * @param instanceType 实例类型
     */
    private void scan(ResourceTypeConstants instanceType) {
        // todo 扫描到实例类型
        List<ComplianceScanResult> complianceScanResults = complianceScanService.scanCompliance(instanceType);
        complianceScanResultService.saveOrUpdate(complianceScanResults);
    }

    /**
     * 插入或者更新数据
     *
     * @param cloudAccountId       云账号id
     * @param instanceType         资源实例类型
     * @param resourceInstancesAll 资源数据
     * @throws IOException 插入可能抛出的异常
     */
    private synchronized void saveOrUpdateData(String cloudAccountId, ResourceTypeConstants instanceType, ArrayList<ResourceInstance> resourceInstancesAll) throws IOException {
        // todo 删除实例历史数据
        Query query = new Query.Builder().bool(new BoolQuery.Builder()
                        .must(new Query.Builder().term(new TermQuery.Builder().field("cloudAccountId").value(cloudAccountId).build()).build(),
                                new Query.Builder().term(new TermQuery.Builder().field("resourceType").value(instanceType.name()).build()).build()).build())
                .build();
        DeleteByQueryRequest build = new DeleteByQueryRequest.Builder().index(ResourceInstance.class.getAnnotation(Document.class).indexName()).query(query).refresh(true).build();
        // todo 处理嵌套数组问题
        List<String> filterArrayKeys = resourceInstancesAll.stream().map(ResourceInstance::getFilterArray).filter(Objects::nonNull).flatMap(f -> f.keySet().stream()).distinct().toList();
        for (String filterArrayKey : filterArrayKeys) {
            elasticsearchClient.indices().putMapping(b -> b.properties(Map.of("filterArray." + filterArrayKey, Property.of(p -> p.nested(NestedProperty.of(n -> n)))))
                    .index(ResourceInstance.class.getAnnotation(Document.class).indexName()));
        }
        // todo 删除数据
        elasticsearchClient.deleteByQuery(build);
        // todo 插入数据
        elasticsearchTemplate.save(resourceInstancesAll);
    }

    /**
     * 代理执行,记录任务节点
     *
     * @param record    任务记录
     * @param jobLink   任务环节
     * @param runnable  任务执行器
     * @param jobParams 当前环节额外参数
     */
    public void proxyJob(JobRecord record, JobLink jobLink, Action runnable, Map<String, Object> jobParams) {
        try {
            runnable.run();
            updateJobRecord(record, JobRecordParam.success(jobLink, jobParams));
        } catch (Throwable e) {
            updateJobRecord(record, JobRecordParam.error(jobLink, e.getMessage()));
        }
    }

    @Override
    public void syncInstance(String cloudAccountId, List<String> instanceType) {
        // todo 每四个任务开启一个异步任务执行
        List<List<String>> jobs = splitArr(instanceType, 5);
        for (List<String> job : jobs) {
            CompletableFuture.runAsync(() -> {
                for (String type : job) {
                    syncInstance(cloudAccountId, ResourceTypeConstants.valueOf(type));
                }
            }, workThreadPool);
        }
    }

    private <T> List<List<T>> splitArr(List<T> array, int num) {
        int count = array.size() % num == 0 ? array.size() / num : array.size() / num + 1;
        List<List<T>> arrayList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int index = i * num;
            List<T> list = new ArrayList<>();
            int j = 0;
            while (j < num && index < array.size()) {
                list.add(array.get(index++));
                j++;
            }
            arrayList.add(list);
        }
        return arrayList;
    }


    /**
     * 初始化任务记录
     *
     * @param jobDescription 任务描述
     * @param syncTime       同步时间
     * @param cloudAccountId 云账户id
     * @return 任务记录对象
     */
    private JobRecord initJobRecord(String jobDescription, LocalDateTime syncTime, String cloudAccountId, ResourceTypeConstants instanceType) {
        JobRecord jobRecord = new JobRecord();
        jobRecord.setDescription(jobDescription);
        jobRecord.setStatus(JobStatusConstants.SYNCING);
        jobRecord.setParams(getStartJobParams());
        jobRecord.setType(JobTypeConstants.SECURITY_COMPLIANCE_CLOUD_ACCOUNT_SYNC_JOB);
        jobRecord.setCreateTime(syncTime);
        // 插入任务数据
        baseJobRecordService.save(jobRecord);
        // 插入关联关系
        JobRecordResourceMapping jobRecordResourceMapping = new JobRecordResourceMapping();
        jobRecordResourceMapping.setResourceId(cloudAccountId);
        jobRecordResourceMapping.setJobType(JobTypeConstants.SECURITY_COMPLIANCE_CLOUD_ACCOUNT_SYNC_JOB);
        jobRecordResourceMapping.setJobRecordId(jobRecord.getId());
        jobRecordResourceMapping.setResourceType(instanceType.name());
        jobRecordResourceMappingService.save(jobRecordResourceMapping);
        return jobRecord;
    }

    /**
     * 获取开始任务参数
     *
     * @return 任务开始参数
     */
    public HashMap<String, Object> getStartJobParams() {
        HashMap<String, Object> jobParams = new HashMap<>();
        List<JobRecordParam<Map<String, Object>>> jobLink = new ArrayList<>();
        jobLink.add(JobRecordParam.success(new JobLink("任务开始执行", JobLinkTypeConstants.JOB_START), null));
        jobParams.put(JobTypeConstants.SECURITY_COMPLIANCE_CLOUD_ACCOUNT_SYNC_JOB.name(), jobLink);
        return jobParams;
    }

    /**
     * 修改任务记录
     *
     * @param record                 任务记录
     * @param resourceInstances      资源列表
     * @param syncDimensionConstants JobRecord
     * @param execParams             执行参数
     */
    private void updateJobRecord(JobRecord record, List<ResourceInstance> resourceInstances, SyncDimensionConstants syncDimensionConstants, Map<String, Object> execParams) {
        JobRecordParam<Map<String, Object>> successDimensionJobRecordParam = getSuccessDimensionJobRecordParam(record, resourceInstances, syncDimensionConstants, execParams);
        updateJobRecord(record, successDimensionJobRecordParam);
    }

    /**
     * 获取成功的粒度任务参数
     *
     * @param resourceInstances      资源实例
     * @param syncDimensionConstants 同步粒度对象
     * @param execParams             执行参数
     * @return 当前粒度参数
     */
    private JobRecordParam<Map<String, Object>> getSuccessDimensionJobRecordParam(JobRecord record, List<ResourceInstance> resourceInstances, SyncDimensionConstants syncDimensionConstants, Map<String, Object> execParams) {
        Map<String, Object> params = syncDimensionConstants.getJobParams().apply(execParams);
        JobLink jobLink = syncDimensionConstants.getJobLink().apply(record, execParams);
        params.put("size", resourceInstances.size());
        return JobRecordParam.success(jobLink, params);
    }

    /**
     * 获取失败的参数
     *
     * @param e 异常信息
     * @return 失败参数
     */
    private JobRecordParam<Map<String, Object>> getErrorDimensionJobRecordParam(JobRecord record, Exception e, SyncDimensionConstants syncDimensionConstants, Map<String, Object> execParams) {
        Map<String, Object> params = syncDimensionConstants.getJobParams().apply(execParams);
        JobLink jobLink = syncDimensionConstants.getJobLink().apply(record, execParams);
        return JobRecordParam.error(jobLink, params, e.getMessage());
    }

    /**
     * 更新任务记录
     *
     * @param jobRecord               任务记录
     * @param jobDimensionRecordParam 任务记录参数
     */
    private void updateJobRecord(JobRecord jobRecord, JobRecordParam<Map<String, Object>> jobDimensionRecordParam) {
        Map<String, Object> jobParams = jobRecord.getParams();
        if (Objects.isNull(jobParams.get(JobTypeConstants.SECURITY_COMPLIANCE_CLOUD_ACCOUNT_SYNC_JOB.name()))) {
            List<JobRecordParam<Map<String, Object>>> resourceJobParams = new ArrayList<>();
            resourceJobParams.add(jobDimensionRecordParam);
            jobParams.put(JobTypeConstants.SECURITY_COMPLIANCE_CLOUD_ACCOUNT_SYNC_JOB.name(), resourceJobParams);
        } else {
            List<JobRecordParam<Map<String, Object>>> resourceJobParams = (List<JobRecordParam<Map<String, Object>>>) jobParams.get(JobTypeConstants.SECURITY_COMPLIANCE_CLOUD_ACCOUNT_SYNC_JOB.name());
            resourceJobParams.add(jobDimensionRecordParam);
        }
        baseJobRecordService.updateById(jobRecord);
    }

    /**
     * 获取任务状态
     *
     * @param jobRecord 任务记录
     * @return 任务状态
     */
    private JobStatusConstants getJobRecordStatus(JobRecord jobRecord) {
        Map<String, Object> jobParams = jobRecord.getParams();
        List<JobRecordParam<Map<String, Object>>> resourceJobParams = (List<JobRecordParam<Map<String, Object>>>) jobParams.get(JobTypeConstants.SECURITY_COMPLIANCE_CLOUD_ACCOUNT_SYNC_JOB.name());
        boolean error = resourceJobParams.stream().anyMatch(link -> link.getCode().equals(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        return error ? JobStatusConstants.FAILED : JobStatusConstants.SUCCESS;
    }


}
