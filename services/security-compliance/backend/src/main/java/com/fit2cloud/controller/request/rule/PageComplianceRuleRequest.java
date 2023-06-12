package com.fit2cloud.controller.request.rule;

import com.fit2cloud.request.pub.OrderRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Pattern;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/9  9:59}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class PageComplianceRuleRequest {

    @Schema(title = "规则名称", description = "规则名称")
    private String name;

    @Schema(title = "云平台", description = "云平台")
    private String platform;

    @Schema(title = "资源类型", description = "资源类型")
    private String resourceType;

    @Schema(title = "风险等级", description = "风险等级")
    @Pattern(regexp = "HIGH|MIDDLE|LOW", message = "风险等级只支持HIGH,MIDDLE,LOW")
    private String riskLevel;

    @Schema(title = "规则组id", description = "规则组id")
    private String ruleGroupId;

    @Schema(title = "规则组名称", description = "规则组名称")
    private String ruleGroupName;

    @Schema(title = "规则描述", description = "规则描述")
    private String description;

    @Schema(title = "是否启用", description = "是否启用")
    private Boolean enable;

    @Schema(title = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}
