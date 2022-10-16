package com.fit2cloud.common.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author:张少虎
 * @Date: 2022/10/8  3:36 PM
 * @Version 1.0
 * @注释: 定时任务状态常量
 */
public enum JobStatusConstants {
    /**
     * 成功
     */
    SUCCESS(0),
    /**
     * 失败
     */
    FAILED(1),
    /**
     * 同步中
     */
    SYNCING(2),
    /**
     * 执行中
     */
    EXECUTION_ING(3);

    @EnumValue
    private final int code;

    JobStatusConstants(int code) {
        this.code = code;
    }
}
