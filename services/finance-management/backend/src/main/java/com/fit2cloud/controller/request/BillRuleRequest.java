package com.fit2cloud.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BillRuleRequest {
    @Schema(title = "账单规则名称", description = "账单规则名称")
    private String name;
}
