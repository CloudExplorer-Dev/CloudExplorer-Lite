package com.fit2cloud.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BillRuleRequest {
    @ApiModelProperty(value = "账单规则名称", notes = "账单规则名称")
    private String name;
}
