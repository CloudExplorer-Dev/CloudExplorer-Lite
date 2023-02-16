package com.fit2cloud.request.cloud_account;

import com.fit2cloud.common.constants.JobConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.quartz.DateBuilder;

import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/16  1:41 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class CloudAccountJobItem {


    @ApiModelProperty(value = "是否活跃", notes = "是否活跃")
    private Boolean active;

    @ApiModelProperty(value = "定时任务类型", notes = "定时任务类型")
    private JobConstants.JobType jobType;

    @ApiModelProperty(value = "定时任务名称", notes = "定时任务名称")
    private String jobName;

    @ApiModelProperty(value = "定时任务分组", notes = "定时任务分组")
    private String jobGroup;

    @ApiModelProperty(value = "定时任务描述", notes = "定时任务描述")
    private String description;

    @ApiModelProperty(value = "当前任务执行参数", notes = "当前任务执行参数")
    private Map<String, Object> params;

    @ApiModelProperty(value = "定时任务表达式", notes = "定时任务表达式")
    private String cronExpression;

    @ApiModelProperty(value = "间隔单位", notes = "间隔单位")
    private DateBuilder.IntervalUnit unit;

    @ApiModelProperty(value = "间隔时间", notes = "间隔时间")
    private Integer interval;

    @ApiModelProperty(value = "定时任务活跃状态 是否只读", notes = "定时任务活跃状态 是否只读")
    private boolean activeReadOnly;

    @ApiModelProperty(value = "定时任务时间 是否只读", notes = "定时任务时间 是否只读")
    private boolean cronReadOnly;
}
