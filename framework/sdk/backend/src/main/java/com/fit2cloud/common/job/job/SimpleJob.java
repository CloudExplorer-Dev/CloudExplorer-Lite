package com.fit2cloud.common.job.job;

import com.fit2cloud.common.job.context.JobContext;
import com.fit2cloud.common.job.step.JobStep;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/11  10:10}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class SimpleJob<T> implements Job<T > {
    /**
     * 任务名称
     */
    private String name;
    /**
     * 任务上下文
     */
    private JobContext  context;
    /**
     * 任务步骤
     */
    private List<JobStep<T>> steps;

    private SimpleJob(
            String name, JobContext<T> context,
            List<JobStep<T>> steps) {
        this.name = name;
        this.context = context;
        this.steps = steps;
    }

    /**
     * 构建任务
     *
     * @param name    任务名称
     * @param context 任务上下文
     * @param steps   任务步骤
     * @param <T>     上下文对象泛型
     * @return 任务执行对象
     */
    public static <T> SimpleJob<T> of(String name, JobContext<T> context, List<JobStep<T>> steps) {
        return new SimpleJob<>(name, context, steps);
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public JobContext<T> getContext() {
        return context;
    }

    @Override
    public List<JobStep<T>> getSteps() {
        return steps;
    }
}
