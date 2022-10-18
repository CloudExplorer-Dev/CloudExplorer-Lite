package com.fit2cloud.es.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

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
    private Map<String, Object> orgLevel;
    /**
     * 工作空间id
     */
    private String workspaceId;
    /**
     * 资源id
     */
    private String reousrceId;
    /**
     * 资源名称
     */
    private String resourceName;
    /**
     * 企业项目
     */
    private String projectId;
    /**
     * 企业项目名称
     */
    private String ProjectName;
    /**
     * 付款账号
     */
    private String payAccountId;
    /**
     * 账期
     */
    private LocalDateTime billingCycle;
    /**
     * 账单开始时间
     */
    private LocalDateTime usageStartDate;
    /**
     * 账单结束时间
     */
    private LocalDateTime usageEndDate;
    /**
     * 供应商
     */
    private String provider;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 计费模式
     */
    private String billMode;
    /**
     * 区域
     */
    private String region;
    /**
     * 可用区
     */
    private String zone;
    /**
     * 原价
     */
    private BigDecimal totalCost;
    /**
     * 优惠后总价，单位为元
     */
    private BigDecimal realTotalCost;
    /**
     * 云账号id
     */
    private String cloudAccountId;
    /**
     * 标签
     */
    private Map<String, Object> tags;
}
