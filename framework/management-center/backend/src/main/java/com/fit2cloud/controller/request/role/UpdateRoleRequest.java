package com.fit2cloud.controller.request.role;

import com.fit2cloud.base.mapper.BaseRoleMapper;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Accessors(chain = true)
@Data
public class UpdateRoleRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -5146630802315452873L;

    @NotNull(groups = ValidationGroup.UPDATE.class, message = "{i18n.role.id.warn.cannot.null}")
    @CustomValidated(groups = {ValidationGroup.UPDATE.class}, mapper = BaseRoleMapper.class, handler = ExistHandler.class, message = "{i18n.role.id.warn.not.exist}", exist = false)
    @Schema(title = "角色ID")
    private String id;

    @Schema(title = "角色名称")
    private String name;

    @Schema(title = "描述")
    private String description;

    @Schema(title = "权限")
    private List<String> permissions;

}
