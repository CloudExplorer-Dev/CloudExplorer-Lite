package com.fit2cloud.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(title = "资源id")
    private String resourceId;

    @Schema(title = "资源名称")
    private String resourceName;

    @Schema(title = "企业项目id")
    private String projectId;

    @Schema(title = "企业项目名称")
    private String projectName;

    @Schema(title = "产品id")
    private String productId;

    @Schema(title = "产品名称")
    private String productName;

    @Schema(title = "标签")
    private Map<String, Object> tags;

    @Schema(title = "云账号id")
    private String cloudAccountId;

    @Schema(title = "云账号名称")
    private String cloudAccountName;

    @Schema(title = "云平台")
    private String provider;
}
