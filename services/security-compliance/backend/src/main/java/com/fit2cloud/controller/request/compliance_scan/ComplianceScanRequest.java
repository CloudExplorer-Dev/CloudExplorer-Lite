package com.fit2cloud.controller.request.compliance_scan;

import com.fit2cloud.common.query.annotaion.Query;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.dao.constants.ComplianceStatus;
import com.fit2cloud.request.pub.OrderRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/15  16:41}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceScanRequest {

    @Schema(title = "云账号id", description = "云账号id")
    @Query(field = "cloudAccountId", compareType = QueryUtil.CompareType.EQ)
    private String cloudAccountId;

    @Schema(title = "合规规则组id", description = "合规规则组id")
    private String complianceRuleGroupId;

    @Schema(title = "资源类型", description = "资源类型", required = true)
    private String resourceType;

    @Schema(title = "合规规则名称", description = "合规规则名称")
    private String complianceRuleName;

    @Schema(title = "供应商", description = "供应商")
    private String platform;

    @Schema(title = "风险等级", description = "风险等级")
    private String riskLevel;

    @Schema(title = "检测状态", description = "检测状态")
    private ComplianceStatus scanStatus;

    @Schema(title = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}
