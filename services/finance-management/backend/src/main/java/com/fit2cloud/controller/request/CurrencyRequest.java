package com.fit2cloud.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/8/8  16:24}
 * {@code @Version 1.0}
 * {@code @注释: }
 */

@Setter
@Getter
@NoArgsConstructor
public class CurrencyRequest implements Serializable {
    @Schema(description = "币种")
    private String code;

    @Schema(description = "汇率")
    private Double exchangeRate;


}
