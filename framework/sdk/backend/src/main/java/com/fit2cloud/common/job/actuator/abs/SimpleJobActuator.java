package com.fit2cloud.common.job.actuator.abs;

import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.job.actuator.JobActuator;
import com.fit2cloud.common.job.job.Job;
import com.fit2cloud.common.job.result.Result;
import com.fit2cloud.common.job.result.impl.SimpleResult;
import com.fit2cloud.common.job.step.JobStep;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/11  10:59}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public abstract class SimpleJobActuator<T> implements JobActuator<T> {
    /**
     * 任务对象
     */
    protected Job<T> job;

    /**
     * 任务类型
     */
    protected JobTypeConstants jobType;

    /**
     * 需要执行的步骤
     */
    protected List<ExecuteStepData> executeStepData;
    /**
     * 运行的Step
     */
    protected List<JobStep<T>> runStep;

    protected SimpleJobActuator(Job<T> job, JobTypeConstants jobType) {
        this.job = job;
        this.jobType = jobType;
    }

    @Override
    public void run() {
        runStep = new ArrayList<>();
        // 任务开始
        if (start(job.getContext().getContext())) {
            // 执行任务步骤
            execJobStep(job.getSteps(), executeStepData, null);
            // 结束任务
            end(job.getContext().getContext());
        }

    }

    @Override
    public void run(String stepId) {
        runStep = new ArrayList<>();
        if (start(job.getContext().getContext())) {
            // 执行任务步骤
            execJobStep(job.getSteps(), Stream.of(stepId).map(ExecuteStepData::of).toList(), null);
            end(job.getContext().getContext());
        }
    }


    @Override
    public void run(String... stepNames) {
        runStep = new ArrayList<>();
        if (start(job.getContext().getContext())) {
            // 执行任务步骤
            execJobStep(job.getSteps(), Arrays.stream(stepNames).map(ExecuteStepData::of).toList(), null);
            end(job.getContext().getContext());
        }
    }

    @Override
    public void run(List<ExecuteStepData> executeStepData) {
        runStep = new ArrayList<>();
        if (start(job.getContext().getContext())) {
            // 执行任务步骤
            execJobStep(job.getSteps(), executeStepData, null);
            end(job.getContext().getContext());
        }
    }

    protected JobStep.StepStatus getJobStatus(List<JobStep<T>> jobSteps) {
        for (JobStep<T> jobStep : jobSteps) {
            if (JobStep.StepStatus.FAILED.equals(jobStep.getStatus())) {
                return JobStep.StepStatus.FAILED;
            }
            if (CollectionUtils.isNotEmpty(jobStep.getChildren())) {
                JobStep.StepStatus jobStatus = getJobStatus(jobStep.getChildren());
                if (JobStep.StepStatus.FAILED.equals(jobStatus)) {
                    return JobStep.StepStatus.FAILED;
                }
            }
        }
        return JobStep.StepStatus.SUCCESS;
    }

    /**
     * @param step 步骤
     * @return 是否终止循环
     */
    private boolean execStep(JobStep<T> step) {
        step.setStatus(JobStep.StepStatus.EXECUTING);
        beforeStep(step);
        // 执行步骤函数
        Result result = proxyExecStep(step);
        List<JobStep<T>> children = step.getChildren();
        if (CollectionUtils.isNotEmpty(children)) {
            for (JobStep<T> child : children) {
                if (execStep(child)) {
                    break;
                }
            }
        }
        step.setStatus(result.getCode() == 200 ? JobStep.StepStatus.SUCCESS : JobStep.StepStatus.FAILED);
        step.setResult(result);
        afterStep(step);
        return result.getTermination();
    }


    private Result proxyExecStep(JobStep<T> step) {
        Result result = null;
        try {
            result = step.getRun().apply(job.getContext(), step);
        } catch (Exception e) {
            result = SimpleResult.of(500, e.getMessage(), false);
        }
        return result;
    }

    /**
     * @param exec 需要执行的步骤
     * @param step 执行的步骤
     * @return 是否中断下面的步骤
     */
    private boolean execStep(ExecuteStepData exec, JobStep<T> step) {
        step.setStatus(JobStep.StepStatus.EXECUTING);
        beforeStep(step);
        // 执行步骤函数
        Result result = proxyExecStep(step);
        if (CollectionUtils.isNotEmpty(step.getChildren())) {
            if (CollectionUtils.isNotEmpty(exec.getChildren())) {
                List<JobStep<T>> filterChildren = step.getChildren()
                        .stream()
                        .filter(sc -> exec.getChildren().stream().anyMatch(e -> StringUtils.equals(e.getId(), sc.getId())))
                        .toList();
                step.setChildren(filterChildren);
            }
        }
        List<JobStep<T>> children = step.getChildren();
        if (CollectionUtils.isNotEmpty(children)) {
            execJobStep(children, CollectionUtils.isNotEmpty(exec.getChildren()) ? exec.getChildren() : new ArrayList<>(), exec);
        }
        step.setStatus(result.getCode() == 200 ? JobStep.StepStatus.SUCCESS : JobStep.StepStatus.FAILED);
        step.setResult(result);
        afterStep(step);
        return result.getTermination();
    }


    private void execJobStep(List<JobStep<T>> steps, List<ExecuteStepData> executeStepList, ExecuteStepData prent) {
        if (Objects.nonNull(prent) && CollectionUtils.isEmpty(executeStepList)) {
            for (JobStep<T> step : steps) {
                if (execStep(step)) {
                    break;
                }
            }
        } else {
            for (ExecuteStepData exec : executeStepList) {
                Optional<JobStep<T>> first = steps.stream().filter(step -> StringUtils.equals(step.getId(), exec.getId())).findFirst();
                if (first.isPresent()) {
                    JobStep<T> step = first.get();
                    if (Objects.isNull(prent)) {
                        runStep.add(step);
                    }
                    if (execStep(exec, step)) {
                        break;
                    }
                }
            }
        }

    }


    /**
     * 步骤前置处理器
     *
     * @param jobStep 步骤对象
     */
    protected abstract void beforeStep(JobStep<T> jobStep);

    /**
     * 步骤后置处理器
     *
     * @param jobStep 任务步骤
     */
    protected abstract void afterStep(JobStep<T> jobStep);

    protected LocalDateTime getSyncTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(now.format(dateTimeFormatter), dateTimeFormatter);
    }


    /**
     * 过滤除正在执行 或者以及执行的步骤
     *
     * @param jobSteps 需要过滤的步骤
     * @return 过滤后的步骤
     */
    protected List<JobStep<T>> filterStep(List<JobStep<T>> jobSteps) {
        return jobSteps.stream().filter(step -> Objects.nonNull(step.getStatus()))
                .map(step -> {
                    JobStep<T> cloneDeep = step.cloneDeep();
                    List<JobStep<T>> children = step.getChildren();
                    if (CollectionUtils.isNotEmpty(children)) {
                        cloneDeep.setChildren(filterStep(children));
                    }
                    return cloneDeep;
                }).toList();
    }
}
