package com.fit2cloud.request.cloud_account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
}
