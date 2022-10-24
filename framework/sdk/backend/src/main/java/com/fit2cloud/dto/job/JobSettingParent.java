package com.fit2cloud.dto.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.quartz.Job;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/20  3:36 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
@AllArgsConstructor
public class JobSettingParent {
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
    Map<String, Object> params;
    /**
     * 是否
     */
    Predicate<String> cloudAccountShow;
}
