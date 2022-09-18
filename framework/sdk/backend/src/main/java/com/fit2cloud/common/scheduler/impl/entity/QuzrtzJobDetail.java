package com.fit2cloud.common.scheduler.impl.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.quartz.DateBuilder;
import org.quartz.Trigger;

import java.util.Date;
import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/8  4:34 PM
 * @Version 1.0
 * @注释:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuzrtzJobDetail {

    /**
     * 定时任务名称
     */
    private String name;
    /**
     * 定时任务组
     */
    private String group;
    /**
     * 定时任务描述
     */
    private String description;

    /**
     * 定时任务间隔时间单位
     */
    private DateBuilder.IntervalUnit unit;

    /**
     * 定时任务间隔时间
     */
    private Long repeatInterval;

    /**
     * 定时任务执行参数
     */
    private Map<String, Object> params;

    /**
     * 下一次执行时间
     */
    private Date nextFireTime;
    /**
     * 上一次触发时间
     */
    private Date previousFireTime;
    /**
     * 任务状态
     * NONE      无状态
     * NORMAL    正常
     * PAUSED    暂停
     * COMPLETE  完成
     * ERROR     错误
     * BLOCKED   阻塞
     */
    private Trigger.TriggerState triggerState;
    /**
     * 定时任务执行器
     */
    private String jobClass;
    /**
     * 定时器昵称
     */
    private String jobClassName;

    /**
     * cron 表达式
     */
    private String cronE;

}
