package com.fit2cloud.request.cloud_account;

import com.fit2cloud.common.constants.JobConstants;
import com.fit2cloud.common.form.vo.FormObject;
import io.swagger.v3.oas.annotations.media.Schema;
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


    @Schema(title = "是否活跃", description = "是否活跃")
    private Boolean active;

    @Schema(title = "定时任务类型", description = "定时任务类型")
    private JobConstants.JobType jobType;

    @Schema(title = "定时任务名称", description = "定时任务名称")
    private String jobName;

    @Schema(title = "定时任务分组", description = "定时任务分组")
    private String jobGroup;

    @Schema(title = "定时任务描述", description = "定时任务描述")
    private String description;

    @Schema(title = "当前任务执行参数", description = "当前任务执行参数")
    private Map<String, Object> params;

    @Schema(title = "定时任务表达式", description = "定时任务表达式")
    private String cronExpression;

    @Schema(title = "间隔单位", description = "间隔单位")
    private DateBuilder.IntervalUnit unit;

    @Schema(title = "间隔时间", description = "间隔时间")
    private Integer interval;

    @Schema(title = "定时任务活跃状态 是否只读", description = "定时任务活跃状态 是否只读")
    private boolean activeReadOnly;

    @Schema(title = "定时任务时间 是否只读", description = "定时任务时间 是否只读")
    private boolean cronReadOnly;

    @Schema(title = "表单收集对象", description = "表单收集对象")
    private FormObject paramsForm;
}
