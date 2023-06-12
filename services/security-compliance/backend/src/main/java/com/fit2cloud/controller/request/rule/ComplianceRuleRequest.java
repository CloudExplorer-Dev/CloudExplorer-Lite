package com.fit2cloud.controller.request.rule;

import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.dao.constants.RiskLevel;
import com.fit2cloud.dao.jentity.Rules;
import com.fit2cloud.dao.mapper.ComplianceRuleGroupMapper;
import com.fit2cloud.dao.mapper.ComplianceRuleMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/9  9:23}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceRuleRequest {

    @Schema(title = "规则组id", description = "规则组id")
    @NotNull(message = "规则id不能为空", groups = ValidationGroup.UPDATE.class)
    @CustomValidated(groups = ValidationGroup.UPDATE.class, mapper = ComplianceRuleMapper.class, handler = ExistHandler.class, field = "id", message = "规则id不存在", exist = false)
    @Null(message = "规则id必须为空", groups = ValidationGroup.SAVE.class)
    private String id;

    @Schema(title = "规则名称", description = "规则名称")
    @NotNull(message = "规则名称不能为空", groups = ValidationGroup.SAVE.class)
    private String name;

    @Schema(title = "规则组id", description = "规则组id")
    @NotNull(message = "规则组id不能为空", groups = ValidationGroup.SAVE.class)
    @CustomValidated(groups = {ValidationGroup.UPDATE.class, ValidationGroup.SAVE.class}, ifNullPass = true, mapper = ComplianceRuleGroupMapper.class, handler = ExistHandler.class, field = "id", message = "规则组id不存在", exist = false)
    private String ruleGroupId;

    @Schema(title = "云平台", description = "云平台")
    @NotNull(message = "云平台不能为空", groups = ValidationGroup.SAVE.class)
    private String platform;

    @Schema(title = "资源类型", description = "资源类型")
    @NotNull(message = "资源类型不能为空", groups = ValidationGroup.SAVE.class)
    private String resourceType;

    @Schema(title = "等保条例", description = "等保条例")
    @Size(message = "等保条例长度最小为0", min = 0, groups = ValidationGroup.SAVE.class)
    @NotNull(message = "等保条例不能为空", groups = ValidationGroup.SAVE.class)
    private List<Integer> insuranceStatuteIds;

    @Schema(title = "规则条件", description = "规则条件")
    @NotNull(message = "规则组不能为空", groups = ValidationGroup.SAVE.class)
    private Rules rules;

    @Schema(title = "风险等级", description = "风险等级")
    @NotNull(message = "风险等级不能为空", groups = ValidationGroup.SAVE.class)
    private RiskLevel riskLevel;

    @Schema(title = "描述", description = "描述")
    @NotNull(message = "描述不能为空", groups = ValidationGroup.SAVE.class)
    private String description;

    @Schema(title = "是否启用", description = "是否启用")
    private Boolean enable;
}
