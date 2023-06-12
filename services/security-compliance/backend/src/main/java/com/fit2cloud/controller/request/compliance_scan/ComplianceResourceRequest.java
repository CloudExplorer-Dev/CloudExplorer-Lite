package com.fit2cloud.controller.request.compliance_scan;

import com.fit2cloud.common.query.annotaion.Query;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.dao.constants.ComplianceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/26  11:22}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceResourceRequest {

    @Schema(title = "资源类型", description = "资源类型")
    @Query(field = "resourceType", compareType = QueryUtil.CompareType.EQ)
    private String resourceType;

    @Schema(title = "云账号id", description = "云账号id")
    @Query(field = "cloudAccountId", compareType = QueryUtil.CompareType.EQ)
    private String cloudAccountId;

    @Schema(title = "合规状态", example = "COMPLIANCE:合规,NOT_COMPLIANCE:不合规")
    private ComplianceStatus complianceStatus;

    @Schema(title = "资源名称")
    @Query(field = "resourceName.keyword", compareType = QueryUtil.CompareType.WILDCARD)
    private String resourceName;
}
