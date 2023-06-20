package com.fit2cloud.common.charging.instance;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/26  20:22}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface InstanceStateRecorder {
    /**
     * 记录实例状态 和 实例变更
     */
    void run();

    /**
     * 记录实例状态
     */
    void runRecordState();

    /**
     * 记录实例变更
     */
    void runRecordInstanceChange();
}
