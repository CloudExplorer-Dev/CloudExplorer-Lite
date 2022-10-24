package com.fit2cloud.request.cloud_account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.dto.job.JobModuleInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.quartz.DateBuilder;

import java.util.List;
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

    @ApiModelProperty(value = "定时任务间隔时间", notes = "定时任务间隔时间")
    private Long timeInterval;

    @ApiModelProperty(value = "定时任务间隔时间单位", notes = "定时任务间隔时间单位")
    private DateBuilder.IntervalUnit unit;

    @ApiModelProperty(value = "定时任务描述", notes = "定时任务描述")
    private String description;

    @ApiModelProperty(value = "当前任务执行参数", notes = "当前任务执行参数")
    private Map<String, Object> params;

    @ApiModelProperty(value = "指定每天的那几个小时运行", notes = "指定每天的那几个小时运行")
    private List<Integer> hoursOfDay;

    @ApiModelProperty(value = "定时任务类型,Interval时间间隔执行,SpecifyHour指定在那几个小时执行", notes = "定时任务类型,Interval时间间隔执行,SpecifyHour指定在那几个小时执行")
    private JobType jobType;

    public enum JobType {
        /**
         * 时间间隔
         */
        Interval,
        /**
         * 指定每一天那几个小时执行
         */
        SpecifyHour
    }

}
