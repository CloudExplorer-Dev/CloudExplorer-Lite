package com.fit2cloud.controller.request;

import com.fit2cloud.common.query.annotaion.Query;
import com.fit2cloud.common.utils.QueryUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NotAuthorizeResourcesRequest {
    @Schema(title = "产品名称", description = "产品名称")
    @Query(compareType = QueryUtil.CompareType.WILDCARD, field = "productName.keyword")
    private String productName;

    @Schema(title = "资源名称", description = "资源名称")
    @Query(compareType = QueryUtil.CompareType.WILDCARD, field = "resourceName.keyword")
    private String resourceName;

    @Schema(title = "企业项目名称", description = "企业项目名称")
    @Query(compareType = QueryUtil.CompareType.WILDCARD, field = "projectName.keyword")
    private String projectName;

    @Schema(title = "云账号名称", description = "云账号名称")
    @Query(compareType = QueryUtil.CompareType.IN, field = "cloudAccountId")
    private String cloudAccountName;

    @Query(compareType = QueryUtil.CompareType.EQ, field = "cloudAccountId")
    @Schema(title = "云账号名id", description = "云账号id")
    private String cloudAccountId;
}
