package com.fit2cloud.controller.request;

import com.fit2cloud.common.query.annotaion.Query;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.request.pub.OrderRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageBillDetailedRequest {
    @ApiModelProperty(value = "月份", notes = "月份")
    private String month;
    @Query(field = "cloudAccountId", compareType = QueryUtil.CompareType.IN)
    @ApiModelProperty("云账号名称")
    private String cloudAccountName;
    @ApiModelProperty("企业项目名称")
    @Query(field = "projectName", compareType = QueryUtil.CompareType.LIKE)
    private String projectName;
    @ApiModelProperty("产品名称")
    @Query(field = "productName", compareType = QueryUtil.CompareType.LIKE)
    private String productName;
    @ApiModelProperty("资源名称")
    @Query(field = "resourceName", compareType = QueryUtil.CompareType.LIKE)
    private String resourceName;
    @ApiModelProperty("排序")
    private OrderRequest order;
}
