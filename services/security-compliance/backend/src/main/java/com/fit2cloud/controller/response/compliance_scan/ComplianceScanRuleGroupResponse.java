package com.fit2cloud.controller.response.compliance_scan;

import com.fit2cloud.dao.constants.RiskLevel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/21  10:43}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceScanRuleGroupResponse {
    @ApiModelProperty(value = "规则组名称", notes = "规则组名称")
    private String ruleGroupName;
    @ApiModelProperty(value = "合规的规则数量", notes = "合规的规则数量")
    private long complianceRuleCount;
    @ApiModelProperty(value = "不合规的规则数量", notes = "不合规的规则数量")
    private long notComplianceRuleCount;
    @ApiModelProperty(value = "高风险", notes = "高风险")
    private long high;
    @ApiModelProperty(value = "中风险", notes = "中风险")
    private long middle;
    @ApiModelProperty(value = "低风险", notes = "低风险")
    private long low;
}
