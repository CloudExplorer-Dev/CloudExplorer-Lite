package com.fit2cloud.controller.response.cloud_account;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.dto.module.ModuleInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.quartz.DateBuilder;

import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/12  3:45 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class CloudAccountJobDetailsResponse {
    /**
     * 定时任务信息
     */
    @ApiModelProperty(value = "云账号同步任务", notes = "云账号同步任务")
    private List<ModuleJob> cloudAccountSyncJobs;

    /**
     * 定时任务到区域
     */
    @ApiModelProperty(value = "任务区域", notes = "任务区域")
    private List<Credential.Region> selectRegion;

    @Data
    public static class ModuleJob extends ModuleInfo {
        /**
         * 定时任务对象
         */
        @ApiModelProperty(value = "定时任务",notes = "定时任务")
        private List<JobItem> jobDetailsList;
    }

    @Data
    public static class JobItem {

        @ApiModelProperty(value = "是否活跃",notes = "是否活跃")
        private boolean active;
        /**
         * 定时任务名称
         */
        @ApiModelProperty(value = "定时任务名称",notes = "定时任务名称")
        private String jobName;
        /**
         * 定时任务组
         */
        @ApiModelProperty(value = "定时任务分组",notes = "定时任务分组")
        private String jobGroup;
        /**
         * 执行间隔时间
         */
        @ApiModelProperty(value = "定时任务间隔时间",notes = "定时任务间隔时间")
        private Long timeInterval;
        /**
         * 间隔时间单位
         */
        @ApiModelProperty(value = "定时任务间隔时间单位",notes = "定时任务间隔时间单位")
        private DateBuilder.IntervalUnit unit;
        /**
         * 描述
         */
        @ApiModelProperty(value = "定时任务描述",notes = "定时任务描述")
        private String description;
    }

}
