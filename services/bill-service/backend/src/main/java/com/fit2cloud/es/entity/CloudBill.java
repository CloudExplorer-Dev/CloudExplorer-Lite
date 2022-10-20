package com.fit2cloud.es.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/14  3:35 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Document(indexName = "ce-cloud-bill")
@Data
public class CloudBill {
    @Id
    private String id;
    /**
     * 组织级别
     */
    @Field(type = FieldType.Object)
    private Map<String, Object> orgLevel;
    /**
     * 工作空间id
     */
    @Field(type = FieldType.Keyword)
    private String workspaceId;
    /**
     * 资源id
     */
    @Field(type = FieldType.Keyword)
    private String reousrceId;
    /**
     * 资源名称
     */
    @Field(type = FieldType.Keyword)
    private String resourceName;
    /**
     * 企业项目
     */
    @Field(type = FieldType.Keyword)
    private String projectId;
    /**
     * 企业项目名称
     */
    @Field(type = FieldType.Auto)
    private String ProjectName;
    /**
     * 付款账号
     */
    @Field(type = FieldType.Keyword)
    private String payAccountId;
    /**
     * 账期
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime billingCycle;
    /**
     * 账单开始时间
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime usageStartDate;
    /**
     * 账单结束时间
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime usageEndDate;
    /**
     * 供应商
     */
    @Field(type = FieldType.Keyword)
    private String provider;
    /**
     * 产品id
     */
    @Field(type = FieldType.Keyword)
    private String productId;
    /**
     * 产品名称
     */
    @Field(type = FieldType.Auto)
    private String productName;
    /**
     * 计费模式
     */
    @Field(type = FieldType.Keyword)
    private String billMode;
    /**
     * 区域
     */
    @Field(type = FieldType.Keyword)
    private String regionId;
    /**
     * 区域名称
     */
    @Field(type = FieldType.Keyword)
    private String regionName;
    /**
     * 可用区
     */
    @Field(type = FieldType.Keyword)
    private String zone;
    /**
     * 原价
     */
    @Field(type = FieldType.Double)
    private BigDecimal totalCost;
    /**
     * 优惠后总价，单位为元
     */
    @Field(type = FieldType.Double)
    private BigDecimal realTotalCost;
    /**
     * 云账号id
     */
    @Field(type = FieldType.Keyword)
    private String cloudAccountId;
    /**
     * 标签
     */
    @Field(type = FieldType.Object)
    private Map<String, Object> tags;
}
