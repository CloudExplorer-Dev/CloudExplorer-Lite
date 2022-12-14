package com.fit2cloud.controller.request.rule_group;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/9  10:21}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class PageComplianceRuleGroupRequest {
    @ApiModelProperty(value = "规则组名称", notes = "规则组名称")
    private String name;
}
