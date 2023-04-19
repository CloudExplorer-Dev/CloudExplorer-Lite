package com.fit2cloud.controller.response.cloud_account;

import com.fit2cloud.request.cloud_account.CloudAccountModuleJob;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    private List<CloudAccountModuleJob> cloudAccountModuleJobs;
}
