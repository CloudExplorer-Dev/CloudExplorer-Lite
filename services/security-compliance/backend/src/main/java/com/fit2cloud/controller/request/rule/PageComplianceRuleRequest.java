package com.fit2cloud.controller.request.rule;

import com.fit2cloud.request.pub.OrderRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/9  9:59}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class PageComplianceRuleRequest {
    @ApiModelProperty(name = "规则名称", notes = "规则名称")
    private String name;
    @ApiModelProperty(name = "云平台", notes = "云平台")
    private String platform;
    @ApiModelProperty(name = "资源类型", notes = "资源类型")
    private String resourceType;
    @ApiModelProperty(name = "风险等级", notes = "风险等级")
    @Pattern(regexp = "HIGH|MIDDLE|LOW",message = "风险等级只支持HIGH,MIDDLE,LOW")
    private String riskLevel;
    @ApiModelProperty(name = "规则组id", notes = "规则组id")
    private String ruleGroupId;
    @ApiModelProperty(name = "规则组名称", notes = "规则组名称")
    private String ruleGroupName;
    @ApiModelProperty(name = "规则描述", notes = "规则描述")
    private String description;
    @ApiModelProperty(name = "是否启用", notes = "是否启用")
    private Boolean enable;
    @ApiModelProperty(value = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}
