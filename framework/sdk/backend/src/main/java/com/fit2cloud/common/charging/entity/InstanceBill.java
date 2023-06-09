package com.fit2cloud.common.charging.entity;

import com.fit2cloud.common.charging.constants.BillModeConstants;
import lombok.Data;

import java.math.BigDecimal;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/25  15:50}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class InstanceBill {
    /**
     * 账期
     * yyyy-MM
     */
    private String billingCycle;
    /**
     * 资源id
     */
    private String resourceId;
    /**
     * 资源名称
     */
    private String resourceName;
    /**
     * 资源类型
     */
    private String resourceType;
    /**
     * 云账号id
     */
    private String cloudAccountId;
    /**
     * 所属平台
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
     * 产品详情
     */
    private String productDetail;
    /**
     * 区域
     */
    private String region;

    /**
     * 可用区
     */
    private String zone;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 扣费时间
     */
    private String deductionTime;

    /**
     * 付费id
     */
    private String payAccountId;

    /**
     * 组织id
     */
    private String organizationId;

    /**
     * 组织名称
     */
    private String organizationName;

    /**
     * 工作空间id
     */
    private String workspaceId;

    /**
     * 工作空间名称
     */
    private String workspaceName;

    /**
     * 付费类型
     */
    private BillModeConstants billMode;
}
