package com.fit2cloud.controller.request;

import com.fit2cloud.common.query.annotaion.Query;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.request.pub.OrderRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PageBillDetailedRequest {
    @Schema(title = "月份", description = "月份")
    private String month;
    @Query(field = "cloudAccountId", compareType = QueryUtil.CompareType.IN)
    @Schema(title = "云账号名称")
    private String cloudAccountName;
    @Query(field = "cloudAccountId", compareType = QueryUtil.CompareType.EQ)
    @Schema(title = "云账号id")
    private String cloudAccountId;
    @Schema(title = "企业项目名称")
    @Query(field = "projectName.keyword", compareType = QueryUtil.CompareType.WILDCARD)
    private String projectName;
    @Schema(title = "产品名称")
    @Query(field = "productName.keyword", compareType = QueryUtil.CompareType.WILDCARD)
    private String productName;
    @Schema(title = "资源名称")
    @Query(field = "resourceName.keyword", compareType = QueryUtil.CompareType.WILDCARD)
    private String resourceName;
    @Schema(title = "排序")
    private OrderRequest order;
}
