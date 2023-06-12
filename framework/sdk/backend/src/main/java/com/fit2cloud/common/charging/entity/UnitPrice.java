package com.fit2cloud.common.charging.entity;

import com.fit2cloud.common.charging.constants.UnitPriceConstants;

import java.math.BigDecimal;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/25  20:17}
 * {@code @Version 1.0}
 * {@code @注释: }
 *
 * @param unit  单位
 * @param price 价钱
 */

public record UnitPrice(UnitPriceConstants unit, BigDecimal price) {
}
