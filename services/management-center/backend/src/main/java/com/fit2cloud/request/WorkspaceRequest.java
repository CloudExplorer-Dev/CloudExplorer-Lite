package com.fit2cloud.request;

import com.fit2cloud.base.mapper.BaseOrganizationMapper;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * @author jianneng
 * @date 2022/8/30 17:13
 **/

@Data
public class WorkspaceRequest {

    @ApiModelProperty("主键id,修改的时候必填")
    @NotNull(groups = ValidationGroup.UPDATE.class, message = "id不能为null")
    @Null(groups = ValidationGroup.SAVE.class, message = "id必须为null")
    private String id;

    @ApiModelProperty(value = "工作空间名称", required = true)
    @NotNull(groups = ValidationGroup.SAVE.class, message = "工作空间名称不能为空")
    private String name;

    @ApiModelProperty(value = "工作空间描述", required = false)
    private String description;

    @ApiModelProperty(value = "父级组织ID",required = false)
    private String organizationId;

}
