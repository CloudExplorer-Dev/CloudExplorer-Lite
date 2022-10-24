package com.fit2cloud.dto.job;

import lombok.*;
import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.TimeOfDay;

import java.util.Calendar;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @Author:张少虎
 * @Date: 2022/9/17  6:11 PM
 * @Version 1.0
 * @注释:
 */
@Getter
@Setter
public class JobInitSettingDto extends JobSettingParent {
    /**
     * 每天开始时间
     */
    private TimeOfDay startTimeDay = null;
    /**
     * 每天结束时间
     */
    private TimeOfDay endTimeDay = null;
    /**
     * 每天执行次数, -1 无限执行
     */
    private Integer repeatCount = -1;
    /**
     * 指定每周执行那几天
     * SUNDAY    周日
     * MONDAY    周一
     * TUESDAY   周二
     * WEDNESDAY 周三
     * THURSDAY  周四
     * FRIDAY    周五
     * SATURDAY  周六
     */
    private Integer[] weeks = new Integer[]{Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY};
    /**
     * 默认执行间隔
     */
    private int timeInterval = 60;
    /**
     * 默认间隔单位
     */
    private DateBuilder.IntervalUnit unit = DateBuilder.IntervalUnit.MINUTE;

    /**
     * 全参 构造器
     *
     * @param jobHandler       定时任务处理器
     * @param jobName          定时任务名称
     * @param jobGroup         定时任务分组
     * @param description      定时任务描述
     * @param params           定时任务参数
     * @param cloudAccountShow 定时任务是否在云账号页面展示
     * @param startTimeDay     开始时间
     * @param endTimeDay       结束时间
     * @param repeatCount      重复次数
     * @param weeks            每周那几天触发
     * @param timeInterval     时间间隔
     * @param unit             单位
     */
    public JobInitSettingDto(Class<? extends Job> jobHandler, String jobName, String jobGroup, String description, Map<String, Object> params, Predicate<String> cloudAccountShow, TimeOfDay startTimeDay, TimeOfDay endTimeDay, Integer repeatCount, Integer[] weeks, int timeInterval, DateBuilder.IntervalUnit unit) {
        super(jobHandler, jobName, jobGroup, description, params, cloudAccountShow);
        this.startTimeDay = startTimeDay;
        this.endTimeDay = endTimeDay;
        this.repeatCount = repeatCount;
        this.weeks = weeks;
        this.timeInterval = timeInterval;
        this.unit = unit;
    }

    /**
     * @param jobHandler       定时任务处理器
     * @param jobName          定时任务名称
     * @param jobGroup         定时任务分组
     * @param description      定时任务描述
     * @param params           定时任务参数
     * @param cloudAccountShow 是否展示
     */
    public JobInitSettingDto(Class<? extends Job> jobHandler, String jobName, String jobGroup, String description, Map<String, Object> params, Predicate<String> cloudAccountShow) {
        super(jobHandler, jobName, jobGroup, description, params, cloudAccountShow);
    }


    /**
     * @param jobHandler       定时任务处理器
     * @param jobName          定时任务名称
     * @param jobGroup         定时任务分组
     * @param description      定时任务描述
     * @param params           定时任务参数
     * @param cloudAccountShow 示范展示
     * @param timeInterval     间隔时间
     * @param unit             间隔时间单位
     */
    public JobInitSettingDto(Class<? extends Job> jobHandler, String jobName, String jobGroup, String description, Map<String, Object> params, Predicate<String> cloudAccountShow, int timeInterval, DateBuilder.IntervalUnit unit) {
        super(jobHandler, jobName, jobGroup, description, params, cloudAccountShow);
        this.timeInterval = timeInterval;
        this.unit = unit;
    }
}
