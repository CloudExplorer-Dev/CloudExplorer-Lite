package com.fit2cloud.dto.job;

import com.fit2cloud.common.scheduler.util.CronUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
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
     * 是否
     */
    private Predicate<String> cloudAccountShow;

    public JobSetting(Class<? extends Job> jobHandler, String jobName, String jobGroup, String description, Map<String, Object> params, Predicate<String> cloudAccountShow) {
        this.jobHandler = jobHandler;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.description = description;
        this.params = params;
        this.cloudAccountShow = cloudAccountShow;
        this.cronExpression = CronUtils.create(new Integer[]{0}, Calendar.MINUTE);
    }
}
