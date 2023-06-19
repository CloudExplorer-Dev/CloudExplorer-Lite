package com.fit2cloud.provider.impl.vsphere.entity.request;

import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/19  11:24}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class CalculateConfigUpdatePriceRequest {
    /**
     * 云账号id
     */
    private String cloudAccountId;
    /**
     * cpu
     */
    private int cpu;
    /**
     * 内存
     */
    private int memory;
    /**
     * 实例付费类型
     */
    private String instanceChargeType;
}
