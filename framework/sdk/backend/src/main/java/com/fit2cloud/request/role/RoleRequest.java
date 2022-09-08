package com.fit2cloud.request.role;

import com.fit2cloud.base.mapper.BaseRoleMapper;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Accessors(chain = true)
@Data
public class RoleRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 8484185034593083260L;

    @ApiModelProperty("角色")
    @NotNull(groups = ValidationGroup.UPDATE.class, message = "{i18n.role.warn.cannot.null}")
    @CustomValidated(groups = {ValidationGroup.UPDATE.class}, mapper = BaseRoleMapper.class, handler = ExistHandler.class, message = "{i18n.role.warn.not.exist}", exist = false)
    private String id;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("角色分类")
    private RoleConstants.Type type;

    @ApiModelProperty("继承角色")
    private RoleConstants.ROLE parentRoleId;


}
