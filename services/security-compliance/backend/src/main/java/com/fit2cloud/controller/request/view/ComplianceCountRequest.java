package com.fit2cloud.controller.request.view;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/10  12:07}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceCountRequest {
    @ApiModelProperty(value = "云账号id", notes = "云账号id")
    private String cloudAccountId;
}
