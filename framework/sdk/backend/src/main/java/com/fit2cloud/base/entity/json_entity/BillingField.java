package com.fit2cloud.base.entity.json_entity;

import com.fit2cloud.common.charging.constants.UnitPriceConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/25  16:43}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingField {
    /**
     * 字段
     */
    private String field;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 单位
     */
    private UnitPriceConstants unit;

    public static BillingField of(String field, BigDecimal price, UnitPriceConstants unit) {
        BillingField billingField = new BillingField();
        billingField.field = field;
        billingField.price = price;
        billingField.unit = unit;
        return billingField;
    }
}
