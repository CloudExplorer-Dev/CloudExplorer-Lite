package com.fit2cloud.controller.request.cloud_account;

import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.dao.mapper.CloudAccountMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateAccountNameRequest {
    @ApiModelProperty(value = "云账号id", notes = "云账号id")
    @NotNull(message = "云账号id不能为null")
    @CustomValidated(mapper = CloudAccountMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.cloud_account.id.is.not.existent}", exist = false)
    private String id;


    @ApiModelProperty(value = "云账号名称", notes = "云账号名称")
    @NotNull(message = "云账号名称不能为null")
    @CustomValidated(mapper = CloudAccountMapper.class, field = "name", handler = ExistHandler.class, message = "{i18n.cloud_account.name.not.repeat}", exist = false)
    private String name;

}
