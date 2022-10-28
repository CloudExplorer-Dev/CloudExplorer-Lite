package com.fit2cloud.common.scheduler.entity;

import lombok.Data;

import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/27  1:44 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class QuzrtzJobDetail {
    /**
     * 定时任务模块名称
     */
    private String schedName;
    /**
     * 触发器名称
     */
    private String triggerName;
    /**
     * 触发器分组
     */
    private String triggerGroup;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务分组
     */
    private String jobGroup;
    /**
     * 任务描述
     */
    private String description;
    /**
     * 下一次触发时间
     */
    private Long nextFireTime;
    /**
     * 上一次触发时间
     */
    private Long prevFireTime;
    /**
     * 优先级
     */
    private Integer priority;
    /**
     * 触发器状态
     */
    private String triggerState;
    /**
     * 触发器类型
     */
    private String triggerType;
    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 日程表名称，表qrtz_calendars的CALENDAR_NAME字段的值
     */
    private String calendarName;

    /**
     * 措施或者是补偿执行的策略
     */
    private Integer misfireInstr;
    /**
     * 触发器数据
     */
    private Map<String, Object> triggerJobData;
    /**
     * 任务执行处理类
     */
    private String jobClassName;
    /**
     * 是否持久化，把该属性设置为1，quartz会把job持久化到数据库中
     */
    private Integer isDurable;
    /**
     * 是否并发
     */
    private Integer isNonconcurrent;
    /**
     * 是否更新数据
     */
    private Integer isUpdateData;
    /**
     * 是否接收恢复执行
     */
    private Integer requestsRecovery;
    /**
     * 定时任务详情数据
     */
    private Map<String, Object> jobDetailsJobData;
    /**
     * 定时任务时间间隔单位
     */
    private String unit;
    /**
     * 一周内那几天执行
     */
    private String days_of_week;
    /**
     * 一天内,几点到几点执行定时任务
     */
    private String startTimeOfDayAndEndTimeOfDay;
    /**
     * 时间间隔
     */
    private Integer interval;
    /**
     * 执行次数 -1 永不停止
     */
    private Integer repeatCount;
    /**
     * 触发器策略
     */
    private Integer misfireInstruction;
    /**
     * cron定时任务呢时间zone
     */
    private String cronTimeZone;
    /**
     * 定时任务cron表达式
     */
    private String cronExpressopn;
}
