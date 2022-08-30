package com.fit2cloud.request;

import com.fit2cloud.base.mapper.BaseOrganizationMapper;

import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * @Author:张少虎
 * @Date: 2022/8/24  6:33 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class OrganizationRequest {

    @ApiModelProperty("主键id,修改的时候必填")
    @NotNull(groups = ValidationGroup.UPDATE.class, message = "id不能为null")
    @Null(groups = ValidationGroup.SAVE.class, message = "id必须为null")
    @CustomValidated(groups = {ValidationGroup.UPDATE.class },mapper = BaseOrganizationMapper.class,handler = ExistHandler.class,message = "组织id不存在",exist = false)
    private String id;

    @ApiModelProperty(value = "组织名称", required = true)
    @NotNull(groups = ValidationGroup.SAVE.class, message = "组织名称不能为null")
    private String name;

    @ApiModelProperty(value = "组织描述", required = true)
    private String description;

    @ApiModelProperty(value = "组织pid")
    private String pid;

}
