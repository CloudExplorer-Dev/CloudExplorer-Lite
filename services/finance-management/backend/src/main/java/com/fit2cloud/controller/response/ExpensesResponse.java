package com.fit2cloud.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/4/3  11:44}
 * {@code @Version 1.0}
 * {@code @注释: 花销实体类}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpensesResponse {

    @Schema(title = "当前月/年", description = "当前月/年")
    private BigDecimal current;

    /**
     * 需要注意 上月数据根据当前日进行
     */
    @Schema(title = "上月/年", description = "上月/年")
    private BigDecimal up;
}
