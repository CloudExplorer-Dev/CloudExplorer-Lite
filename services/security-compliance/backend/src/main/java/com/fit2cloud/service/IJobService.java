package com.fit2cloud.service;

import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.job.actuator.JobActuator;
import com.fit2cloud.common.job.job.Job;
import com.fit2cloud.constants.ResourceTypeConstants;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/12  20:31}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface IJobService {
    /**
     * 构建一个任务Job
     *
     * @param jobName 任务名称
     * @return 任务
     */
    Job<IJobStepService.Context> of(String jobName);

    /**
     * 获取任务执行器
     *
     * @param job             任务对象
     * @param jobType         任务类型
     * @param cloudAccountId  云账号id
     * @param instanceType    实例类型
     * @param executeStepData 需要执行的步骤
     * @return 任务执行器
     */
    JobActuator<IJobStepService.Context> ofJobActuator(Job<IJobStepService.Context> job,
                                                       JobTypeConstants jobType,
                                                       String cloudAccountId,
                                                       ResourceTypeConstants instanceType,
                                                       List<JobActuator.ExecuteStepData> executeStepData
    );

}
