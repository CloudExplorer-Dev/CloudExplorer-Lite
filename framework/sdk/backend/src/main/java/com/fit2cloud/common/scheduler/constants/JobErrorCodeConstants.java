package com.fit2cloud.common.scheduler.constants;

import com.fit2cloud.common.utils.LocaleUtil;

/**
 * @Author:张少虎
 * @Date: 2022/9/15  10:04 AM
 * @Version 1.0
 * @注释:
 */
public enum JobErrorCodeConstants {
    ADD_JOB_FAIL(200100, "定时任务创建失败"),

    GET_JOB_DETAILS_FAIL(200101, "获取定时任务详情失败"),

    LIST_JOB_DETAILS_FAIL(200102, "获取定时任务列表失败"),

    STOP_JOB_FAIL(200103, "停止定时任务失败"),

    RESUME_JOB_FAIL(200104, "恢复定时任务失败"),

    DELETE_JOB_FAIL(200105, "删除定时任务失败"),

    START_ALL_JOB_FAIL(200106, "启动所有定时任务失败"),

    STOP_ALL_JOB_FAIL(200107, "停止所有定时任务失败"),

    RESUME_ALL_JOB_FAIL(200108, "恢复所有定时任务失败"),

    SHUTDOWN_ALL_JOBS(200109, "关闭定时任务失败");
    /**
     * 提示
     */
    private final String message;
    /**
     * 状态吗
     */
    private final Integer code;

    JobErrorCodeConstants(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取错误提示
     *
     * @return 错误提示文本
     */
    public String getMessage() {
        return LocaleUtil.getMessage(message, message);
    }

    /**
     * 获取错误提示
     *
     * @param args 错误提示参数
     * @return 错误提示文本
     */
    public String getMessage(Object[] args) {
        return LocaleUtil.getMessage(message, args, message);
    }

    /**
     * 获取错误code
     *
     * @return 错误code
     */
    public Integer getCode() {
        return code;
    }
}
