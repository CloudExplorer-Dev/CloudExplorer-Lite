package com.fit2cloud.common.charging.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/1  16:09}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingFieldMeta {
    /**
     * 按量默认价格
     */
    private BigDecimal defaultOnDemandMPrice;
    /**
     * 包年包月默认价格
     */
    private BigDecimal defaultMonthlyMPrice;
    /**
     * 字段Label
     */
    private String fieldLabel;
    /**
     * 字段单位Label
     */
    private String unitLabel;

    /**
     * 单价单位Label
     * 元
     */
    private String priceLabel;
    /**
     * 其他数据
     */
    private Map<String, Object> meta;

}
