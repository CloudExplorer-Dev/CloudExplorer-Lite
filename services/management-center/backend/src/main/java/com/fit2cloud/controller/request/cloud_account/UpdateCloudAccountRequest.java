package com.fit2cloud.controller.request.cloud_account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author:张少虎
 * @Date: 2022/9/13  5:06 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class UpdateCloudAccountRequest extends AddCloudAccountRequest {
    @ApiModelProperty(value = "云账号id", notes = "云账号id")
    @NotNull(message = "云账号id不能为null")
    private String id;
}
