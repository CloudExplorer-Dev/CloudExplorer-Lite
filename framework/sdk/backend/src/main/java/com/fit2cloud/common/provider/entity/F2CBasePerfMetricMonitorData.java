package com.fit2cloud.common.provider.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 性能指标监控数据
 *
 * @author jianneng
 **/
@Getter
@Setter
public class F2CBasePerfMetricMonitorData {
    /**
     * 最小值
     */
    private BigDecimal minimum;
    /**
     * 最大值
     */
    private BigDecimal maximum;
    /**
     * 平均值
     */
    private BigDecimal average;

    public F2CBasePerfMetricMonitorData() {

    }

    public F2CBasePerfMetricMonitorData(BigDecimal minimum, BigDecimal maximum, BigDecimal average) {
        this.average = average;
        this.maximum = maximum;
        this.minimum = minimum;
    }
}
