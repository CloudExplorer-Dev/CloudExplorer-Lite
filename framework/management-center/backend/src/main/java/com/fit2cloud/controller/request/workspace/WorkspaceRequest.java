package com.fit2cloud.controller.request.workspace;

import com.fit2cloud.common.validator.group.ValidationGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * @author jianneng
 * @date 2022/8/30 17:13
 **/

@Data
public class WorkspaceRequest {

    @ApiModelProperty("主键id,修改的时候必填")
    @NotNull(groups = ValidationGroup.UPDATE.class, message = "{i18n.workspace.id.is.not.empty}")
    @Null(groups = ValidationGroup.SAVE.class, message = "{i18n.workspace.id.is.null}")
    private String id;

    @ApiModelProperty(value = "工作空间名称", required = true)
    @NotNull(groups = {ValidationGroup.SAVE.class,ValidationGroup.UPDATE.class}, message = "{i18n.workspace.name.is.not.empty}")
    private String name;

    @ApiModelProperty(value = "工作空间描述", required = false)
    private String description;

    @ApiModelProperty(value = "父级组织ID",required = true)
    @NotNull(groups = {ValidationGroup.SAVE.class,ValidationGroup.UPDATE.class}, message = "{i18n.workspace.organization.is.not.empty}")
    private String organizationId;

}
