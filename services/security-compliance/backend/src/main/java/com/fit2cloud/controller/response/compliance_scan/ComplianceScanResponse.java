package com.fit2cloud.controller.response.compliance_scan;

import com.fit2cloud.constants.ComplianceStatus;
import com.fit2cloud.dao.constants.RiskLevel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/15  16:56}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceScanResponse {
    @ApiModelProperty(value = "规则id", notes = "规则id")
    private String id;
    @ApiModelProperty(value = "规则名称", notes = "规则名称")
    private String name;
    @ApiModelProperty(value = "云账户id", notes = "云账户id")
    private String cloudAccountId;
    @ApiModelProperty(value = "云账号名称", notes = "云账户名称")
    private String cloudAccountName;
    @ApiModelProperty(value = "供应商", notes = "供应商")
    private String platform;
    @ApiModelProperty(value = "风险等级", notes = "风险等级")
    private RiskLevel riskLevel;
    @ApiModelProperty(value = "检查状态", notes = "检查状态")
    private ComplianceStatus scanStatus;
    @ApiModelProperty(value = "合规数量", notes = "合规数量")
    private Long complianceCount;
    @ApiModelProperty(value = "不合规数量", notes = "不合规数量")
    private Long notComplianceCount;
    @ApiModelProperty(value = "最后同步时间", notes = "最后同步时间")
    private LocalDateTime lastSyncTime;
}
