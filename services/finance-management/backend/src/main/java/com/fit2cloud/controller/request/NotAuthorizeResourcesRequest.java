package com.fit2cloud.controller.request;

import com.fit2cloud.common.query.annotaion.Query;
import com.fit2cloud.common.utils.QueryUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NotAuthorizeResourcesRequest {
    @ApiModelProperty(value = "产品名称", notes = "产品名称")
    @Query(compareType = QueryUtil.CompareType.LIKE, field = "productName")
    private String productName;

    @ApiModelProperty(value = "资源名称", notes = "资源名称")
    @Query(compareType = QueryUtil.CompareType.LIKE, field = "resourceName")
    private String resourceName;

    @ApiModelProperty(value = "企业项目名称", notes = "企业项目名称")
    @Query(compareType = QueryUtil.CompareType.LIKE, field = "projectName")
    private String projectName;

    @ApiModelProperty(value = "云账号名称", notes = "云账号名称")
    @Query(compareType = QueryUtil.CompareType.IN, field = "cloudAccountId")
    private String cloudAccountName;
}
