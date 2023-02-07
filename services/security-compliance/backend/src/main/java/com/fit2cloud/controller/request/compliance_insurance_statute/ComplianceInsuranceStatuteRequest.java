package com.fit2cloud.controller.request.compliance_insurance_statute;

import com.fit2cloud.request.pub.OrderRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/30  10:01}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceInsuranceStatuteRequest {
    @ApiModelProperty(value = "基本条款", notes = "基本条款")
    private String baseClause;

    @ApiModelProperty(value = "安全层面", notes = "安全层面")
    private String securityLevel;

    @ApiModelProperty(value = "控制点", notes = "控制点")
    private String controlPoint;

    @ApiModelProperty(value = "改进建议", notes = "改进建议")
    private String improvementProposal;

    @ApiModelProperty(value = "合规规则id", notes = "合规规则id")
    private String complianceRuleId;

    @ApiModelProperty(value = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;

}
