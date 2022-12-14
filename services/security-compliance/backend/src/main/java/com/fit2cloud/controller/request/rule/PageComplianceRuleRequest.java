package com.fit2cloud.controller.request.rule;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/9  9:59}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class PageComplianceRuleRequest {
    @ApiModelProperty(name = "规则名称", notes = "规则名称")
    private String name;
}
