package com.fit2cloud.dto.job;

import com.fit2cloud.common.constants.PlatformConstants;
import lombok.*;
import org.quartz.Job;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/20  3:43 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class JobCronSettingDto extends JobSettingParent {
    /**
     * 指定每一天那些小时执行
     */
    private Integer[] hoursOfDay;

    public JobCronSettingDto(Class<? extends Job> jobHandler, String jobName, String jobGroup, String description, Map<String, Object> params, Integer[] hoursOfDay, Predicate<String> cloudAccountShow) {
        super(jobHandler, jobName, jobGroup, description, params, cloudAccountShow);
        this.hoursOfDay = hoursOfDay;
    }
}
