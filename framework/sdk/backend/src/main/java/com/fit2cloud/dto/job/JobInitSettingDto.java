package com.fit2cloud.dto.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.TimeOfDay;

import java.util.Calendar;
import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/17  6:11 PM
 * @Version 1.0
 * @注释:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobInitSettingDto {
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
     * 描述
     */
    private String description = "";
    /**
     * 默认执行间隔
     */
    private int timeInterval = 60;
    /**
     * 默认间隔单位
     */
    private DateBuilder.IntervalUnit unit = DateBuilder.IntervalUnit.MINUTE;
    /**
     * 定时任务执行参数
     */
    private Map<String, Object> params = null;
}
