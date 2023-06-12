package com.fit2cloud.controller.request.compliance_insurance_statute;

import com.fit2cloud.request.pub.OrderRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/30  10:01}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceInsuranceStatuteRequest {

    @Schema(title = "基本条款", description = "基本条款")
    private String baseClause;

    @Schema(title = "安全层面", description = "安全层面")
    private String securityLevel;

    @Schema(title = "控制点", description = "控制点")
    private String controlPoint;

    @Schema(title = "改进建议", description = "改进建议")
    private String improvementProposal;

    @Schema(title = "合规规则id", description = "合规规则id")
    private String complianceRuleId;

    @Schema(title = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;

}
