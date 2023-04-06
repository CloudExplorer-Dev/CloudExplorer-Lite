package com.fit2cloud.controller.request.rule_group;

import com.fit2cloud.common.validator.annnotaion.CustomQueryWrapperValidated;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.common.validator.handler.ExistQueryWrapperValidatedHandler;
import com.fit2cloud.dao.mapper.ComplianceRuleGroupMapper;
import com.fit2cloud.dao.mapper.ComplianceRuleMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/9  9:55}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
@CustomQueryWrapperValidated(groups = ValidationGroup.UPDATE.class,
        handler = ExistQueryWrapperValidatedHandler.class,
        el = "#getQueryWrapper().ne(\"id\",#this.id).eq(\"name\",#this.name)",
        message = "规则组名称不能重复", exist = true,
        mapper = ComplianceRuleGroupMapper.class)
public class ComplianceRuleGroupRequest {

    @ApiModelProperty(value = "规则组id", notes = "规则组id")
    @NotNull(message = "规则组id不能为空", groups = ValidationGroup.UPDATE.class)
    @CustomValidated(groups = ValidationGroup.UPDATE.class, mapper = ComplianceRuleGroupMapper.class, handler = ExistHandler.class, field = "id", message = "规则组id不存在", exist = false)
    @Null(message = "规则组id必须为空", groups = ValidationGroup.SAVE.class)
    private String id;

    @ApiModelProperty(value = "规则组名称", notes = "规则组名称")
    @NotNull(message = "规则组名称不能为空", groups = ValidationGroup.SAVE.class)
    @CustomValidated(groups = ValidationGroup.SAVE.class, mapper = ComplianceRuleGroupMapper.class, field = "name", handler = ExistHandler.class, message = "规则组名称不能重复", exist = true)
    private String name;

    @ApiModelProperty(value = "规则组描述", notes = "规则组描述")
    @NotNull(message = "规则组描述不能为空", groups = ValidationGroup.SAVE.class)
    private String description;
}
