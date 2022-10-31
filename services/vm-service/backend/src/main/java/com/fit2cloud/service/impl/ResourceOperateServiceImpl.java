package com.fit2cloud.service.impl;

import com.fit2cloud.base.entity.JobRecord;
import com.fit2cloud.common.constants.JobStatusConstants;
import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.DateUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.controller.request.CreateJobRecordRequest;
import com.fit2cloud.controller.request.ExecProviderMethodRequest;
import com.fit2cloud.controller.request.ResourceState;
import com.fit2cloud.dto.InitJobRecordDTO;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.service.IResourceOperateService;
import com.fit2cloud.service.JobRecordCommonService;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Author: LiuDi
 * Date: 2022/10/26 11:19 AM
 */
@Service
public class ResourceOperateServiceImpl implements IResourceOperateService {
    @Resource
    JobRecordCommonService jobRecordCommonService;

    @Resource
    ThreadPoolExecutor workThreadPool;

    public <T> void operateWithJobRecord(CreateJobRecordRequest createJobRecordRequest, ExecProviderMethodRequest execProviderMethodRequest, ResourceState<T> resourceState, Consumer<T> updateResource) {
        operateWithJobRecord(createJobRecordRequest, execProviderMethodRequest, resourceState, updateResource, jobRecordCommonService::initJobRecord, jobRecordCommonService::modifyJobRecord);
    }

    private <T> void operateWithJobRecord(CreateJobRecordRequest createJobRecordRequest, ExecProviderMethodRequest execProviderMethodRequest, ResourceState<T> resourceState, Consumer<T> updateResource, Function<InitJobRecordDTO, JobRecord> initJobMethod, Consumer<JobRecord> updateJobRecord) {
        CompletableFuture.runAsync(()->{
            JobTypeConstants jobType = createJobRecordRequest.getJobType();
            String resourceId = createJobRecordRequest.getResourceId();
            ResourceTypeEnum resourceType = createJobRecordRequest.getResourceType();
            OperatedTypeEnum resourceOperateType = createJobRecordRequest.getResourceOperateType();

            LocalDateTime createTime = DateUtil.getSyncTime();
            try {
                // 初始化任务
                JobRecord jobRecord = initJobMethod.apply(
                        InitJobRecordDTO.builder().jobDescription(resourceOperateType.getDescription()).
                                jobStatus(JobStatusConstants.EXECUTION_ING).
                                jobType(jobType).resourceId(resourceId).resourceType(resourceType).createTime(createTime)
                                .build());

                // 更新资源状态为操作中
                updateResource.accept(resourceState.getProcessingResource());

                try {
                    // 执行资源操作方法
                    boolean result = CommonUtil.exec(ICloudProvider.of(execProviderMethodRequest.getPlatform()), JsonUtil.toJSONString(execProviderMethodRequest.getMethodParams()), execProviderMethodRequest.getExecMethod());
                    if (result) {
                        // 更新资源为完成态
                        updateResource.accept(resourceState.getAfterResource());

                        // 设置任务状态为成功
                        jobRecord.setStatus(JobStatusConstants.SUCCESS);
                    }
                } catch (Exception e) {
                    // 还原资源状态
                    updateResource.accept(resourceState.getBeforeResource());

                    // 设置任务状态为失败
                    jobRecord.setStatus(JobStatusConstants.FAILED);
                    jobRecord.setResult(e.getMessage());
                    LogUtil.error("Exec operate failed - {}", e.getMessage());
                }

                // 更新任务记录
                updateJobRecord.accept(jobRecord);
            } catch (Throwable e) {
                LogUtil.error("OperateWithJobRecord failed - {}", e.getMessage());
            }
        },workThreadPool);
    }
}
