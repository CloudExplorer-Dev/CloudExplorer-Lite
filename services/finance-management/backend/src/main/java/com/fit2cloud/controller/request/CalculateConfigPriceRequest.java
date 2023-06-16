package com.fit2cloud.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/16  15:07}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class CalculateConfigPriceRequest {
    /**
     * 云账号
     */
    @Schema(title = "云账号id")
    private String cloudAccountId;
}
