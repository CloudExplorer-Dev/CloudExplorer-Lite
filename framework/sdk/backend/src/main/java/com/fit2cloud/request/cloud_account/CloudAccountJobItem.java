package com.fit2cloud.request.cloud_account;

import com.fit2cloud.common.platform.credential.Credential;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.quartz.DateBuilder;

import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/16  1:41 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class CloudAccountJobItem {

    @ApiModelProperty(value = "是否活跃", notes = "是否活跃")
    private boolean active;

    @ApiModelProperty(value = "定时任务名称", notes = "定时任务名称")
    private String jobName;

    @ApiModelProperty(value = "定时任务分组", notes = "定时任务分组")
    private String jobGroup;

    @ApiModelProperty(value = "定时任务间隔时间", notes = "定时任务间隔时间")
    private Long timeInterval;

    @ApiModelProperty(value = "定时任务间隔时间单位", notes = "定时任务间隔时间单位")
    private DateBuilder.IntervalUnit unit;

    @ApiModelProperty(value = "定时任务描述", notes = "定时任务描述")
    private String description;

    @ApiModelProperty(value = "当前任务执行的区域", notes = "当前任务区域")
    private List<Credential.Region> regions;
}
