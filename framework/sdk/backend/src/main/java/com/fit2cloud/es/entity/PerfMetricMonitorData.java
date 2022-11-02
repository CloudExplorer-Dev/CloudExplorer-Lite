package com.fit2cloud.es.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

/**
 * @Author:张少虎
 * @Date: 2022/9/28  9:00 AM
 * @Version 1.0
 * @注释:
 */
@Document(indexName = "ce-perf-metric-monitor-data")
@Data
public class PerfMetricMonitorData {

    @Id
    private String id;
    /**
     * 监控对象类型
     * F2CEntityType
     */
    @Field(type = FieldType.Auto)
    private String entityType;
    /**
     * 对象标识
     */
    @Field(type = FieldType.Auto)
    private String instanceId;
    /**
     * 监控数据时间间隔 秒,默认60
     */
    @Field(type = FieldType.Auto)
    private Integer period;
    /**
     * 磁盘使用率，磁盘标识
     */
    @Field(type = FieldType.Auto)
    private String device;
    /**
     * 监控指标
     */
    @Field(type = FieldType.Auto)
    private String metricName;
    /**
     * 时间点
     */
    @Field(type = FieldType.Auto)
    private Long timestamp;
    /**
     * 最小值
     */
    @Field(type = FieldType.Auto)
    private BigDecimal minimum;
    @Field(type = FieldType.Auto)
    private BigDecimal maximum;
    @Field(type = FieldType.Auto)
    private BigDecimal average;

    @Field(type = FieldType.Auto)
    private String unit;
}
