package com.fit2cloud.provider.impl.vsphere.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 指标监控数据对象
 *
 * @author jianneng
 * @date 2023/5/24 13:40
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetricTimeValue {

    /**
     * 指标ID
     */
    private String metric;
    /**
     * 时间戳
     */
    private long timestamp;
    /**
     * 值
     */
    private BigDecimal average;
}
