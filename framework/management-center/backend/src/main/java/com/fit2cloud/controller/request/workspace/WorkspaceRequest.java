package com.fit2cloud.controller.request.workspace;

import com.fit2cloud.common.validator.group.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

/**
 * @author jianneng
 * @date 2022/8/30 17:13
 **/

@Data
public class WorkspaceRequest {

    @Schema(title = "主键id,编辑的时候必填")
    @NotNull(groups = ValidationGroup.UPDATE.class, message = "{i18n.workspace.id.is.not.empty}")
    @Null(groups = ValidationGroup.SAVE.class, message = "{i18n.workspace.id.is.null}")
    private String id;

    @Schema(title = "工作空间名称", required = true)
    @NotNull(groups = {ValidationGroup.SAVE.class, ValidationGroup.UPDATE.class}, message = "{i18n.workspace.name.is.not.empty}")
    private String name;

    @Schema(title = "工作空间描述", required = false)
    private String description;

    @Schema(title = "父级组织ID", required = true)
    @NotNull(groups = {ValidationGroup.SAVE.class, ValidationGroup.UPDATE.class}, message = "{i18n.workspace.organization.is.not.empty}")
    private String organizationId;

}
