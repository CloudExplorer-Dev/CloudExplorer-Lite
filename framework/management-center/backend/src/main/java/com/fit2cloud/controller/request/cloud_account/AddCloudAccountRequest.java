package com.fit2cloud.controller.request.cloud_account;

import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.dao.mapper.CloudAccountMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author:张少虎
 * @Date: 2022/9/6  2:14 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class AddCloudAccountRequest extends CloudAccountCredentialRequest {

    @ApiModelProperty(value = "云账号名称", notes = "云账号名称")
    @NotNull(message = "云账号名称不能为null")
    @CustomValidated(mapper = CloudAccountMapper.class, field = "name", handler = ExistHandler.class, message = "{i18n.cloud_account.name.not.repeat}", exist = true)
    private String name;


}
