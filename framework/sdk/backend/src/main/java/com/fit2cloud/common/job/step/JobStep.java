package com.fit2cloud.common.job.step;

import com.fit2cloud.common.job.context.JobContext;
import com.fit2cloud.common.job.result.Result;

import java.util.List;
import java.util.function.BiFunction;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/10  15:10}
 * {@code @Version 1.0}
 * {@code @注释: }
 */

public interface JobStep<T> {
    /**
     * 唯一标识
     *
     * @return 任务Step唯一标识
     */
    String getId();

    /**
     * 步骤名称
     *
     * @return 步骤名称
     */
    String getName();

    /**
     * 步骤内置上下文
     *
     * @return 步骤上下文
     */
    Object getStepContext();

    /**
     * 子步骤
     *
     * @return 子步骤
     */
    List<JobStep<T>> getChildren();

    StepStatus getStatus();


    void setStatus(StepStatus status);

    /**
     * 设置子步骤
     *
     * @param jobSteps 子步骤
     */
    void setChildren(List<JobStep<T>> jobSteps);


    /**
     * 获取Step执行器
     *
     * @return 任务步骤执行器
     */
    BiFunction<JobContext<T>, JobStep<T>, Result> getRun();

    Result getResult();

    void setResult(Result result);

    JobStep<T> cloneDeep();

    public static enum StepStatus {
        /**
         * 执行中
         */
        EXECUTING,
        /**
         * 失败
         */
        FAILED,
        /**
         * 成功
         */
        SUCCESS,
    }


}
