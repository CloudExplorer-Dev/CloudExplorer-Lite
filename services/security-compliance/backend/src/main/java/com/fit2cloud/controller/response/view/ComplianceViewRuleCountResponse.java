package com.fit2cloud.controller.response.view;

import com.fit2cloud.dao.constants.RiskLevel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/3/22  14:52}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceViewRuleCountResponse {

    @ApiModelProperty(value = "风险等级", notes = "风险等级")
    private RiskLevel riskLevel;

    @ApiModelProperty(value = "合规数量", notes = "合规数量")
    private Long complianceCount;

    @ApiModelProperty(value = "不合规数量", notes = "不合规数量")
    private Long notComplianceCount;

    @ApiModelProperty(value = "总数", notes = "总数")
    private Long total;
}
