package com.fit2cloud.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/7  16:06}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ListCloudAccountByPolicyRequest {
    @ApiModelProperty(value = "计费策略id", required = false, notes = "计费策略id")
    private String billingPolicyId;
}
