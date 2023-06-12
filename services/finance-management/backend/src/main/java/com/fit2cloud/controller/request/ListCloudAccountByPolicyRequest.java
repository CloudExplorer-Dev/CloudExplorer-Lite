package com.fit2cloud.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/7  16:06}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ListCloudAccountByPolicyRequest {

    @Schema(title = "计费策略id", required = false)
    private String billingPolicyId;
}
