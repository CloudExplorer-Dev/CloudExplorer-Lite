package com.fit2cloud.service.impl;

import com.fit2cloud.base.entity.JobRecord;
import com.fit2cloud.base.entity.JobRecordResourceMapping;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.base.service.IBaseJobRecordResourceMappingService;
import com.fit2cloud.base.service.IBaseJobRecordService;
import com.fit2cloud.common.constants.JobStatusConstants;
import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.job.actuator.JobActuator;
import com.fit2cloud.common.job.actuator.abs.SimpleJobActuator;
import com.fit2cloud.common.job.context.impl.SimpleJobContext;
import com.fit2cloud.common.job.job.Job;
import com.fit2cloud.common.job.job.SimpleJob;
import com.fit2cloud.common.job.step.JobStep;
import com.fit2cloud.common.job.step.impl.SimpleJobStep;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.constants.SyncDimensionConstants;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.service.IJobService;
import com.fit2cloud.service.IJobStepService;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/12  20:39}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Service
public class JobServiceImpl implements IJobService {

    @Resource
    private IJobStepService jobStepService;
    @Resource
    private IBaseJobRecordService jobRecordService;
    @Resource
    private IBaseCloudAccountService cloudAccountService;
    @Resource
    private IBaseJobRecordResourceMappingService jobRecordResourceMappingService;

    @Override
    public Job<IJobStepService.Context> of(String jobName) {
        SimpleJobContext<IJobStepService.Context> context = SimpleJobContext.of(new IJobStepService.Context());
        SimpleJobStep<IJobStepService.Context> check = SimpleJobStep.of("VERIFICATION::CLOUD_ACCOUNT", "云账号校验", jobStepService::checkCloudAccount);
        SimpleJobStep<IJobStepService.Context> buildSyncData = SimpleJobStep.of("SYNC::RESOURCE", "同步资源", jobStepService::syncResource);
        SimpleJobStep<IJobStepService.Context> saveOrUpdateDataStep = SimpleJobStep.of("SAVE::RESOURCE_DATA", "插入原始数据", jobStepService::saveOrUpdateData);
        SimpleJobStep<IJobStepService.Context> scanStep = SimpleJobStep.of("SCAN::RESOURCE", "扫描合规资源", jobStepService::scan);
        SimpleJobStep<IJobStepService.Context> updateScanTimeStep = SimpleJobStep.of("UPDATE::SCAN_TIME", "更新扫描时间", jobStepService::updateScanTime);
        return SimpleJob.of(jobName, context, List.of(check, buildSyncData, saveOrUpdateDataStep, scanStep, updateScanTimeStep));
    }

    @Override
    public JobActuator<IJobStepService.Context> ofJobActuator(Job<IJobStepService.Context> job, JobTypeConstants jobType, String cloudAccountId, ResourceTypeConstants instanceType, List<JobActuator.ExecuteStepData> executeStepData) {
        return new SyncJobActuator(job, jobType, cloudAccountId, instanceType, executeStepData);
    }

    public class SyncJobActuator extends SimpleJobActuator<IJobStepService.Context> {

        private String cloudAccountId;
        private ResourceTypeConstants instanceType;


        public SyncJobActuator(Job<IJobStepService.Context> job, JobTypeConstants jobType, String cloudAccountId, ResourceTypeConstants instanceType, List<ExecuteStepData> executeStepData) {
            super(job, jobType);
            this.cloudAccountId = cloudAccountId;
            this.instanceType = instanceType;
            this.executeStepData = executeStepData;
            IJobStepService.Context context = job.getContext().getContext();
            context.instanceType = instanceType;
            context.cloudAccountId = cloudAccountId;
            context.cloudAccount = cloudAccountService.getById(cloudAccountId);
            context.syncTime = getSyncTime();
        }

        @Override
        public boolean start(IJobStepService.Context context) {
            LogUtil.info(job.getName() + "开始执行");
            return true;
        }

        @Override
        public void end(IJobStepService.Context context) {
            LogUtil.info(job.getName() + "执行结束");
        }

        /**
         * 校验当前云账号是否支持扫描资源
         *
         * @param context 上下文
         * @return 是否支持
         */
        private boolean checkResourceSupport(IJobStepService.Context context) {
            Class<? extends ICloudProvider> iCloudProviderClazz = ICloudProvider.of(context.cloudAccount.getPlatform());
            List<DefaultKeyValue<ResourceTypeConstants, SyncDimensionConstants>> map = CommonUtil.exec(iCloudProviderClazz, ICloudProvider::getResourceSyncDimensionConstants);
            // 如果不存在同步粒度则为不支持的资源类型
            Optional<DefaultKeyValue<ResourceTypeConstants, SyncDimensionConstants>> first = map.stream().filter(item -> item.getKey().equals(context.instanceType)).findFirst();
            if (first.isPresent()) {
                DefaultKeyValue<ResourceTypeConstants, SyncDimensionConstants> resourceTypeConstantsSyncDimensionConstantsDefaultKeyValue = first.get();
                context.syncDimensionConstants = resourceTypeConstantsSyncDimensionConstantsDefaultKeyValue.getValue();
            }
            return first.isPresent();
        }


        @Override
        protected void beforeStep(JobStep<IJobStepService.Context> jobStep) {
            Map<String, Object> params = job.getContext().getContext().jobRecord.getParams();
            // 全量的步骤执行
            params.put("steps", filterStep(runStep));
            jobRecordService.updateById(job.getContext().getContext().jobRecord);
        }

        @Override
        protected void afterStep(JobStep<IJobStepService.Context> jobStep) {
            Map<String, Object> params = job.getContext().getContext().jobRecord.getParams();
            // 全量的步骤执行
            params.put("steps", filterStep(runStep));
            jobRecordService.updateById(job.getContext().getContext().jobRecord);
        }

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
        jobRecord.setParams(new HashMap<>());
        jobRecord.setType(JobTypeConstants.SECURITY_COMPLIANCE_CLOUD_ACCOUNT_SYNC_JOB);
        jobRecord.setCreateTime(syncTime);
        // 插入任务数据
        jobRecordService.save(jobRecord);
        // 插入关联关系
        JobRecordResourceMapping jobRecordResourceMapping = new JobRecordResourceMapping();
        jobRecordResourceMapping.setResourceId(cloudAccountId);
        jobRecordResourceMapping.setJobType(JobTypeConstants.SECURITY_COMPLIANCE_CLOUD_ACCOUNT_SYNC_JOB);
        jobRecordResourceMapping.setJobRecordId(jobRecord.getId());
        jobRecordResourceMapping.setResourceType(instanceType.name());
        jobRecordResourceMappingService.save(jobRecordResourceMapping);
        return jobRecord;
    }

}
