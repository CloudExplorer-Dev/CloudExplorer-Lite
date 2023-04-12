package com.fit2cloud.controller.request;

import com.fit2cloud.common.query.annotaion.Query;
import com.fit2cloud.common.utils.QueryUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NotAuthorizeResourcesRequest {
    @ApiModelProperty(value = "产品名称", notes = "产品名称")
    @Query(compareType = QueryUtil.CompareType.WILDCARD, field = "productName.keyword")
    private String productName;

    @ApiModelProperty(value = "资源名称", notes = "资源名称")
    @Query(compareType = QueryUtil.CompareType.WILDCARD, field = "resourceName.keyword")
    private String resourceName;

    @ApiModelProperty(value = "企业项目名称", notes = "企业项目名称")
    @Query(compareType = QueryUtil.CompareType.WILDCARD, field = "projectName.keyword")
    private String projectName;

    @ApiModelProperty(value = "云账号名称", notes = "云账号名称")
    @Query(compareType = QueryUtil.CompareType.IN, field = "cloudAccountId")
    private String cloudAccountName;

    @Query(compareType = QueryUtil.CompareType.EQ, field = "cloudAccountId")
    @ApiModelProperty(value = "云账号名id", notes = "云账号id")
    private String cloudAccountId;
}
