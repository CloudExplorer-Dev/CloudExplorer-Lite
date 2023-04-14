package com.fit2cloud.common.provider.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 性能指标监控数据
 *
 * @author jianneng
 **/
@Data
public class F2CPerfMetricMonitorData {
    /**
     * 监控对象类型
     * F2CEntityType
     */
    private String entityType;
    /**
     * 对象标识
     */
    private String instanceId;
    /**
     * 监控数据时间间隔 秒,默认60
     */
    private Integer period;
    /**
     * 磁盘使用率，磁盘标识
     */
    private String device;
    /**
     * 监控指标
     */
    private String metricName;
    /**
     * 时间点
     */
    private Long timestamp;
    /**
     * 最小值
     */
    private BigDecimal minimum;
    private BigDecimal maximum;
    private BigDecimal average;

    /**
     * 单位
     */
    private String unit;

    /**
     * 集群名称
     */
    private String clusterName;
    /**
     * 宿主机ID
     */
    private String hostId;
}
