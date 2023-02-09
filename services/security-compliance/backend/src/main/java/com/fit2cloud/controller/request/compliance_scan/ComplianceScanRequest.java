package com.fit2cloud.controller.request.compliance_scan;

import com.fit2cloud.common.query.annotaion.Query;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.request.pub.OrderRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/15  16:41}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceScanRequest {

    @ApiModelProperty(value = "云账号id", notes = "云账号id")
    @Query(field = "cloudAccountId", compareType = QueryUtil.CompareType.EQ)
    private String cloudAccountId;

    @ApiModelProperty(value = "合规规则组id", notes = "合规规则组id")
    private String complianceRuleGroupId;

    @ApiModelProperty(value = "资源类型", notes = "资源类型", required = true)
    private String resourceType;

    @ApiModelProperty(value = "合规规则名称", notes = "合规规则名称")
    private String complianceRuleName;

    @ApiModelProperty(value = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}
