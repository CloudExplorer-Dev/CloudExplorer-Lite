package com.fit2cloud.controller.request.cloud_account;

import com.fit2cloud.common.validator.annnotaion.CustomQueryWrapperValidated;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.common.validator.handler.ExistQueryWrapperValidatedHandler;
import com.fit2cloud.dao.mapper.CloudAccountMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * { @Author:张少虎}
 * { @Date: 2022/9/13  5:06 PM}
 * { @Version 1.0}
 * { @注释:}
 */
@Data
@CustomQueryWrapperValidated(groups = ValidationGroup.UPDATE.class,
        handler = ExistQueryWrapperValidatedHandler.class,
        el = "#getQueryWrapper().ne(\"id\",#this.id).eq(\"name\",#this.name)",
        message = "云账号名称不能重复", exist = true,
        mapper = CloudAccountMapper.class)
public class UpdateCloudAccountRequest extends AddCloudAccountRequest {
    @ApiModelProperty(value = "云账号id", notes = "云账号id")
    @NotNull(message = "云账号id不能为空")
    @CustomValidated(groups = ValidationGroup.UPDATE.class, mapper = CloudAccountMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.cloud_account.id.is.not.existent}", exist = false)
    private String id;
}
