package com.fit2cloud.common.job.actuator;


import lombok.Data;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/11  10:57}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface JobActuator<T> {
    /**
     * 任务开始执行函数
     */
    boolean start(T context);

    /**
     * 执行函数
     */
    void run();

    /**
     * 执行任务
     *
     * @param stepId 任务步骤id
     */
    void run(String stepId);

    /**
     * 任务步骤id 集合
     *
     * @param stepIds 任务步骤id集合
     */
    void run(String... stepIds);

    void run(List<ExecuteStepData> executeStepData);

    /**
     * 任务结束执行函数
     */
    void end(T context);

    @Data
    class ExecuteStepData {
        /**
         * 需要执行的步骤的唯一标识
         */
        String id;
        /**
         * 步骤上下文
         */
        Object stepContext;
        /**
         * 子步骤
         */
        List<ExecuteStepData> children;

        public static ExecuteStepData of(String id) {
            ExecuteStepData executeStepData = new ExecuteStepData();
            executeStepData.setId(id);
            return executeStepData;
        }
    }


}
