package com.fit2cloud.controller.request;

import com.fit2cloud.common.query.annotaion.Query;
import com.fit2cloud.common.utils.QueryUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/10  3:23 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class AuthorizeResourcesRequest {
    @ApiModelProperty(value = "授权对象id", notes = "授权对象id")
    private String authorizeId;

    @ApiModelProperty(value = "授权对象类型", notes = "授权对象类型")
    private String type;

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
