package com.fit2cloud.dto;

import com.fit2cloud.common.charging.constants.BillingGranularityConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/7  19:02}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ListInstanceBillRequest {

    @Schema(title = "月份")
    private String month;

    @Schema(title = "生成粒度", example = "DAY HOUR MONTH")
    private BillingGranularityConstants granularity;

    @Schema(title = "云账号id")
    private String cloudAccountId;
}
