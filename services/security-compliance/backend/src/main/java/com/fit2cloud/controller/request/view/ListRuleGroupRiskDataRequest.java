package com.fit2cloud.controller.request.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/3/27  10:33}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ListRuleGroupRiskDataRequest {

    @Schema(title = "云账号id", description = "云账号id")
    private String cloudAccountId;

}
