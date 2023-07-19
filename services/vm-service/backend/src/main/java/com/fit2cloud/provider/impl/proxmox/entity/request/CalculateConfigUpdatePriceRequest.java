package com.fit2cloud.provider.impl.proxmox.entity.request;

import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/19  15:28}
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
     * CPU插槽
     */
    private int cpuSlot;
    /**
     * 内存
     */
    private int mem;
    /**
     * 实例付费类型
     */
    private String instanceChargeType;
}
