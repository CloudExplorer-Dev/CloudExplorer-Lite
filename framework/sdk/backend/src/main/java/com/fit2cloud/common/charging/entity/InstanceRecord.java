package com.fit2cloud.common.charging.entity;

import com.fit2cloud.common.charging.constants.BillModeConstants;
import lombok.Data;

import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/30  11:35}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class InstanceRecord {
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
     * 工作空间id
     */
    private String workspaceId;

    /**
     * 工作空间名称
     */
    private String workspaceName;

    /**
     * 组织id
     */
    private String organizationId;

    /**
     * 组织名称
     */
    private String organizationName;

    /**
     * 计费模式 ON_DEMAND 按需 MONTHLY包年包月
     */
    private BillModeConstants billMode;

    /**
     * 元数据
     */
    private Map<String, Integer> meta;
    /**
     * 实例状态
     */
    private InstanceState.State state;
}
