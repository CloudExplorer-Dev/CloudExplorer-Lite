package com.fit2cloud.controller.request.role;

import com.fit2cloud.base.mapper.BaseRoleMapper;
import com.fit2cloud.common.constants.RoleConstants;
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
public class CreateRoleRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -1000182047768023081L;

    @NotNull(groups = ValidationGroup.SAVE.class, message = "{i18n.role.name.warn.cannot.null}")
    @CustomValidated(groups = {ValidationGroup.SAVE.class}, field = "_name", mapper = BaseRoleMapper.class, handler = ExistHandler.class, message = "{i18n.role.name.warn.not.duplicated}", exist = true)
    @Schema(title = "角色名称")
    private String name;

    @NotNull(groups = ValidationGroup.SAVE.class, message = "{i18n.role.parent.warn.cannot.null}")
    @Schema(title = "继承角色")
    private RoleConstants.ROLE parentRoleId;

    @Schema(title = "描述")
    private String description;

    @Schema(title = "权限")
    private List<String> permissions;

}
