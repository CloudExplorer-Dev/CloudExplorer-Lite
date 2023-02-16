package com.fit2cloud.dto.job;

import com.fit2cloud.common.constants.JobConstants;
import com.fit2cloud.common.scheduler.util.CronUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.quartz.DateBuilder;
import org.quartz.Job;

import java.util.Calendar;
import java.util.Map;
import java.util.function.Predicate;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/20  3:36 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
@AllArgsConstructor
public class JobSetting {
    /**
     * 定时任务类型 cron 或者 Interval
     */
    private JobConstants.JobType jobType;

    /**
     * 间隔单位
     */
    private DateBuilder.IntervalUnit unit;
    /**
     * 间隔时间
     */
    private int interval;

    /**
     * 任务处理器
     */
    private Class<? extends Job> jobHandler;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务分组
     */
    private String jobGroup;
    /**
     * 描述
     */
    private String description = "";
    /**
     * 任务参数
     */
    private Map<String, Object> params;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 当前云平台 是否支持
     */
    private Predicate<String> cloudAccountShow;
    /**
     * 当前云平台 定时任务是否不可修改状态 状态:启动,停止
     */
    private Predicate<String> activeReadOnly;
    /**
     * 当前云平台 定时任务是否可修改定时时间
     */
    private Predicate<String> cronReadOnly;

    public JobSetting(Class<? extends Job> jobHandler, String jobName, String jobGroup, String description, Map<String, Object> params, Predicate<String> cloudAccountShow) {
        this.jobHandler = jobHandler;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.description = description;
        this.params = params;
        this.cloudAccountShow = cloudAccountShow;
        this.cronExpression = CronUtils.create(new Integer[]{0}, Calendar.MINUTE);
        this.jobType = JobConstants.JobType.CRON;
        this.activeReadOnly = (p) -> false;
        this.cronReadOnly = (p) -> false;
    }

    /**
     * 定时任务设置
     *
     * @param jobHandler       任务处理器
     * @param jobName          任务名称
     * @param jobGroup         任务分组
     * @param description      任务描述
     * @param params           任务携带参数
     * @param cronExpression   cron表达式
     * @param cloudAccountShow 云平台是否支持
     * @param activeReadOnly   云平台是否可修改状态
     * @param cronReadOnly     云平台是否可修改时间
     */
    public JobSetting(Class<? extends Job> jobHandler,
                      String jobName,
                      String jobGroup,
                      String description,
                      Map<String, Object> params,
                      String cronExpression,
                      Predicate<String> cloudAccountShow,
                      Predicate<String> activeReadOnly,
                      Predicate<String> cronReadOnly
    ) {
        this.jobHandler = jobHandler;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.description = description;
        this.params = params;
        this.cloudAccountShow = cloudAccountShow;
        this.cronExpression = cronExpression;
        this.jobType = JobConstants.JobType.CRON;
        this.activeReadOnly = activeReadOnly;
        this.cronReadOnly = cronReadOnly;
    }

    /**
     * @param jobHandler       定时任务处理器
     * @param jobName          任务名称
     * @param jobGroup         任务分组
     * @param description      任务描述
     * @param params           任务参数
     * @param interval         任务间隔时间
     * @param unit             任务间隔时间单位
     * @param cloudAccountShow 云平台是否支持
     * @param activeReadOnly   云平台是否可修改状态
     * @param cronReadOnly     云平台是否可修改时间
     */
    public JobSetting(Class<? extends Job> jobHandler,
                      String jobName, String jobGroup,
                      String description,
                      Map<String, Object> params,
                      int interval,
                      DateBuilder.IntervalUnit unit,
                      Predicate<String> cloudAccountShow,
                      Predicate<String> activeReadOnly,
                      Predicate<String> cronReadOnly) {
        this.jobHandler = jobHandler;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.description = description;
        this.params = params;
        this.cloudAccountShow = cloudAccountShow;
        this.interval = interval;
        this.unit = unit;
        this.jobType = JobConstants.JobType.INTERVAL;
        this.activeReadOnly = activeReadOnly;
        this.cronReadOnly = cronReadOnly;
    }
}
