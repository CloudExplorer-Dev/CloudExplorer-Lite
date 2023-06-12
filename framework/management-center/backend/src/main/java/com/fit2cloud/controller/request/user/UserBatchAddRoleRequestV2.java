package com.fit2cloud.controller.request.user;

import com.fit2cloud.base.mapper.BaseRoleMapper;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.handler.ExistHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;


@Data
public class UserBatchAddRoleRequestV2 {

    @NotNull(message = "{i18n.role.id.warn.cannot.null}")
    @CustomValidated(mapper = BaseRoleMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.role.id.warn.not.exist}", exist = false)
    @Schema(title = "角色ID")
    private String roleId;

    @NotEmpty(message = "关联关系不能为空")
    @Schema(title = "用户与组织/工作空间ID关联列表")
    private List<UserBatchAddRoleObjectV2> userSourceMappings;

}
