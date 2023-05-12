package com.fit2cloud.common.job.job;

import com.fit2cloud.common.job.context.JobContext;
import com.fit2cloud.common.job.step.JobStep;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/11  10:08}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface Job<T> {

    /**
     * 获取任务名称
     *
     * @return 任务名称
     */
    String getName();

    /**
     * 获取任务上下文
     *
     * @return 任务上下文
     */
    JobContext<T> getContext();

    /**
     * 获取任务执行步骤
     *
     * @return 任务执行步骤
     */
    List<JobStep<T>> getSteps();

}
