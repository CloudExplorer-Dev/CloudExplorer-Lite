package com.fit2cloud.common.job.step.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fit2cloud.common.job.context.JobContext;
import com.fit2cloud.common.job.result.Result;
import com.fit2cloud.common.job.step.JobStep;
import lombok.Setter;

import java.util.List;
import java.util.function.BiFunction;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/11  9:35}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Setter
public class SimpleJobStep<T> implements JobStep<T> {
    @Override
    public JobStep<T> cloneDeep() {
        SimpleJobStep<T> tSimpleJobStep = new SimpleJobStep<>(this.id, this.name, this.run, this.stepContext);
        tSimpleJobStep.setStatus(this.status);
        tSimpleJobStep.setResult(this.result);
        return tSimpleJobStep;
    }

    /**
     * 步骤唯一标识
     */
    private String id;
    /**
     * 步骤名称
     */
    private String name;

    /**
     * 步骤执行器
     */
    private BiFunction<JobContext<T>, JobStep<T>, Result> run;


    /**
     * 执行结果返回
     */
    private Result result;

    /**
     * 步骤状态
     */
    private StepStatus status;

    /**
     * 子步骤
     */
    private List<JobStep<T>> children;

    /**
     * 步骤上下文,当前步骤的参数
     */
    private Object stepContext;


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getStepContext() {
        return stepContext;
    }

    @Override
    public List<JobStep<T>> getChildren() {
        return children;
    }

    @Override
    public StepStatus getStatus() {
        return status;
    }

    @Override
    public void setChildren(List<JobStep<T>> jobSteps) {
        this.children = jobSteps;
    }

    @Override
    @JsonIgnore
    public BiFunction<JobContext<T>, JobStep<T>, Result> getRun() {
        return run;
    }

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public void setResult(Result result) {
        this.result = result;
    }


    @Override
    public void setStatus(StepStatus status) {
        this.status = status;
    }

    private SimpleJobStep(String id, String name, BiFunction<JobContext<T>, JobStep<T>, Result> run, Object stepContext) {
        this.id = id;
        this.name = name;
        this.run = run;
        this.stepContext = stepContext;
    }

    public static <T> SimpleJobStep<T> of(String id, String name, BiFunction<JobContext<T>, JobStep<T>, Result> run) {
        return new SimpleJobStep<>(id, name, run, null);
    }

    public static <T> SimpleJobStep<T> of(String id, String name, BiFunction<JobContext<T>, JobStep<T>, Result> run, Object stepContext) {
        return new SimpleJobStep<>(id, name, run, stepContext);
    }

}
