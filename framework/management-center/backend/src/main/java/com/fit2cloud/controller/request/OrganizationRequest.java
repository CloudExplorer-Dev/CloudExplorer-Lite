package com.fit2cloud.controller.request;

import com.fit2cloud.base.mapper.BaseOrganizationMapper;
import com.fit2cloud.common.validator.annnotaion.CustomQueryWrapperValidated;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.common.validator.handler.ExistQueryWrapperValidatedHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

/**
 * @Author:张少虎
 * @Date: 2022/8/24  6:33 PM
 * @Version 1.0
 * @注释:
 */
@Data
@CustomQueryWrapperValidated(groups = ValidationGroup.UPDATE.class,
        handler = ExistQueryWrapperValidatedHandler.class,
        el = "#getQueryWrapper().ne(\"id\",#this.id).eq(\"name\",#this.name)",
        message = "组织名称不能重复", exist = true,
        mapper = BaseOrganizationMapper.class)
public class OrganizationRequest {

    @Schema(title = "主键id,编辑的时候必填")
    @NotNull(groups = ValidationGroup.UPDATE.class, message = "{i18n.organization.id.is.not.empty}")
    @Null(groups = ValidationGroup.SAVE.class, message = "{i18n.organization.id.is.null}")
    @CustomValidated(groups = {ValidationGroup.UPDATE.class}, mapper = BaseOrganizationMapper.class, handler = ExistHandler.class, message = "{i18n.organization.id.is.not.existent}", exist = false)
    private String id;

    @Schema(title = "组织名称", required = true)
    @NotNull(groups = ValidationGroup.SAVE.class, message = "{i18n.organization.name.is.not.empty}")
    private String name;

    @Schema(title = "组织描述", required = true)
    private String description;

    @Schema(title = "组织pid")
    private String pid;

}
