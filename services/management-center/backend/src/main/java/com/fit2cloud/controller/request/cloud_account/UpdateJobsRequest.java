package com.fit2cloud.controller.request.cloud_account;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.controller.response.cloud_account.CloudAccountJobDetailsResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/13  11:23 AM
 * @Version 1.0
 * @注释:
 */
@Data
public class UpdateJobsRequest {
    /**
     * 云账号id
     */
    @ApiModelProperty(value = "云账号id", notes = "云账号id")
    private String cloudAccountId;

    /**
     * 云账号同步定时任务
     */
    private List<CloudAccountJobDetailsResponse.ModuleJob> cloudAccountSyncJobs;
    /**
     * 选中的区域
     */
    private List<Credential.Region> selectRegion;
}
