package com.fit2cloud.request.cloud_account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/30  3:22 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class SyncRequest {
    /**
     * 云账号id
     */
    @Schema(title = "云账号id", description = "云账号id")
    private String cloudAccountId;

    /**
     * 任务
     */
    @Schema(title = "同步任务", description = "同步任务")
    private List<Job> syncJob;

    /**
     * 同步参数
     */
    private Map<String, Object> params;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Job {
        /**
         * 模块名称
         */
        @Schema(title = "模块名称", description = "模块名称")
        private String module;
        /**
         * 任务名称
         */
        @Schema(title = "任务名称", description = "任务名称")
        private String jobName;

        @Schema(title = "任务组", description = "任务组")
        private String jobGroup;
    }
}
