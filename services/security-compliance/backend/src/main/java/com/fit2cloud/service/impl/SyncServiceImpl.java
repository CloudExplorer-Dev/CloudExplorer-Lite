package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.JobRecord;
import com.fit2cloud.base.entity.JobRecordResourceMapping;
import com.fit2cloud.base.mapper.BaseJobRecordMapper;
import com.fit2cloud.base.mapper.BaseJobRecordResourceMappingMapper;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.base.service.IBaseJobRecordResourceMappingService;
import com.fit2cloud.base.service.IBaseJobRecordService;
import com.fit2cloud.base.service.impl.BaseJobRecordResourceMappingServiceImpl;
import com.fit2cloud.common.constants.JobStatusConstants;
import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.job_record.JobRecordParam;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.exception.SkipPageException;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.constants.SyncDimensionConstants;
import com.fit2cloud.dao.entity.ComplianceRule;
import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.service.*;
import lombok.SneakyThrows;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

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

    @SneakyThrows
    @Override
    public void syncInstance(String cloudAccountId, ResourceTypeConstants instanceType) {
        CloudAccount cloudAccount = cloudAccountService.getById(cloudAccountId);
        LocalDateTime syncTime = getSyncTime();
        ArrayList<ResourceInstance> resourceInstancesAll = new ArrayList<>();
        Class<? extends ICloudProvider> iCloudProviderClazz = ICloudProvider.of(cloudAccount.getPlatform());
        Map<ResourceTypeConstants, SyncDimensionConstants> map = CommonUtil.exec(iCloudProviderClazz, ICloudProvider::getResourceSyncDimensionConstants);
        // 如果不存在同步粒度则为不支持的资源类型
        if (!map.containsKey(instanceType)) {
            return;
        }
        JobRecord jobRecord = initJobRecord("同步" + instanceType.getMessage(), syncTime, cloudAccountId);
        JobStatusConstants jobStatus = JobStatusConstants.SUCCESS;
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
                    // 跳过当前区域
                    updateJobRecord(jobRecord, new ArrayList<>(), syncDimensionConstants, execParams);
                } else {
                    jobStatus = JobStatusConstants.FAILED;
                    // 记录当前区域同步错误
                    updateJobRecord(jobRecord, getErrorDimensionJobRecordParam(e, syncDimensionConstants, execParams));
                }
            }
        }
        jobRecord.setStatus(jobStatus);
        baseJobRecordService.updateById(jobRecord);
        // todo 删除实例历史数据
        Query query = new Query.Builder().bool(new BoolQuery.Builder()
                        .must(new Query.Builder().term(new TermQuery.Builder().field("cloudAccountId").value(cloudAccountId).build()).build(),
                                new Query.Builder().term(new TermQuery.Builder().field("resourceType").value(instanceType.name()).build()).build()).build())
                .build();
        DeleteByQueryRequest build = new DeleteByQueryRequest.Builder().index(ResourceInstance.class.getAnnotation(Document.class).indexName()).query(query).refresh(true).build();
        elasticsearchClient.deleteByQuery(build);
        System.out.println(JsonUtil.toJSONString(resourceInstancesAll));
        // todo 插入数据
        elasticsearchTemplate.save(resourceInstancesAll);
        // todo 更新缓存
        complianceScanService.updateCacheScanComplianceByInstanceType(instanceType);
        // todo 更新扫描时间
        complianceRuleService.update(new LambdaUpdateWrapper<ComplianceRule>().eq(ComplianceRule::getResourceType, instanceType.name()).set(ComplianceRule::getUpdateTime, syncTime));
    }


    /**
     * 初始化任务记录
     *
     * @param jobDescription 任务描述
     * @param syncTime       同步时间
     * @param cloudAccountId 云账户id
     * @return 任务记录对象
     */
    private JobRecord initJobRecord(String jobDescription, LocalDateTime syncTime, String cloudAccountId) {
        JobRecord jobRecord = new JobRecord();
        jobRecord.setDescription(jobDescription);
        jobRecord.setStatus(JobStatusConstants.SYNCING);
        jobRecord.setParams(new HashMap<>());
        jobRecord.setType(JobTypeConstants.SECURITY_COMPLIANCE_CLOUD_ACCOUNT_SYNC_JOB);
        jobRecord.setCreateTime(syncTime);
        // 插入任务数据
        baseJobRecordService.save(jobRecord);
        // 插入关联关系
        JobRecordResourceMapping jobRecordResourceMapping = new JobRecordResourceMapping();
        jobRecordResourceMapping.setResourceId(cloudAccountId);
        jobRecordResourceMapping.setJobType(JobTypeConstants.SECURITY_COMPLIANCE_CLOUD_ACCOUNT_SYNC_JOB);
        jobRecordResourceMapping.setJobRecordId(jobRecord.getId());
        jobRecordResourceMappingService.save(jobRecordResourceMapping);
        return jobRecord;
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
        JobRecordParam<Map<String, Object>> successDimensionJobRecordParam = getSuccessDimensionJobRecordParam(resourceInstances, syncDimensionConstants, execParams);
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
    private JobRecordParam<Map<String, Object>> getSuccessDimensionJobRecordParam(List<ResourceInstance> resourceInstances, SyncDimensionConstants syncDimensionConstants, Map<String, Object> execParams) {
        Map<String, Object> params = syncDimensionConstants.getJobParams().apply(execParams);
        params.put("size", resourceInstances.size());
        return JobRecordParam.success(params);
    }

    /**
     * 获取失败的参数
     *
     * @param e 异常信息
     * @return 失败参数
     */
    private JobRecordParam<Map<String, Object>> getErrorDimensionJobRecordParam(Exception e, SyncDimensionConstants syncDimensionConstants, Map<String, Object> execParams) {
        Map<String, Object> params = syncDimensionConstants.getJobParams().apply(execParams);
        return JobRecordParam.error(params, e.getMessage());
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


}
