package com.fit2cloud.controller.response.view;

import com.fit2cloud.dao.constants.RiskLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/3/22  14:52}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceViewRuleCountResponse {

    @Schema(title = "风险等级", description = "风险等级")
    private RiskLevel riskLevel;

    @Schema(title = "合规数量", description = "合规数量")
    private Long complianceCount;

    @Schema(title = "不合规数量", description = "不合规数量")
    private Long notComplianceCount;

    @Schema(title = "总数", description = "总数")
    private Long total;
}
