package com.fit2cloud.controller.response.compliance_scan_result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/21  10:43}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceScanRuleGroupResultResponse {

    @Schema(title = "规则组名称", description = "规则组名称")
    private String ruleGroupName;

    @Schema(title = "合规的规则数量", description = "合规的规则数量")
    private long complianceRuleCount;

    @Schema(title = "不合规的规则数量", description = "不合规的规则数量")
    private long notComplianceRuleCount;

    @Schema(title = "高风险", description = "高风险")
    private long high;

    @Schema(title = "中风险", description = "中风险")
    private long middle;

    @Schema(title = "低风险", description = "低风险")
    private long low;
}
