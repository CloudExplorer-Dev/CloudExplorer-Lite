package com.fit2cloud.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/11  9:39 AM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class AuthorizeResourcesResponse {
    @ApiModelProperty("资源id")
    private String resourceId;
    @ApiModelProperty("资源名称")
    private String resourceName;
    @ApiModelProperty("企业项目id")
    private String projectId;
    @ApiModelProperty("企业项目名称")
    private String projectName;
    @ApiModelProperty("产品id")
    private String productId;
    @ApiModelProperty("产品名称")
    private String productName;
    @ApiModelProperty("标签")
    private Map<String, Object> tags;
    @ApiModelProperty("云账号id")
    private String cloudAccountId;
    @ApiModelProperty("云账号名称")
    private String cloudAccountName;
    @ApiModelProperty("云平台")
    private String provider;
}
