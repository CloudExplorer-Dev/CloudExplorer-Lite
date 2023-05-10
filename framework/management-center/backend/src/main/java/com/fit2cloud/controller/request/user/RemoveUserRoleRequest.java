package com.fit2cloud.controller.request.user;

import com.fit2cloud.base.mapper.BaseRoleMapper;
import com.fit2cloud.base.mapper.BaseUserMapper;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.handler.ExistHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RemoveUserRoleRequest {

    @ApiModelProperty(value = "用户ID")
    @NotNull(message = "{i18n.user.id.cannot.be.null}")
    @CustomValidated(mapper = BaseUserMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.primary.key.not.exist}", exist = false)
    private String userId;

    @ApiModelProperty(value = "角色ID")
    @NotNull(message = "{i18n.role.id.warn.cannot.null}")
    @CustomValidated(mapper = BaseRoleMapper.class, field = "id", handler = ExistHandler.class, message = "角色ID不存在", exist = false)
    private String roleId;

    @ApiModelProperty(value = "组织/工作空间ID")
    private String sourceId;

}
