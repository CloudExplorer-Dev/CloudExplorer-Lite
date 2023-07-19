package com.fit2cloud.common.provider.impl.proxmox.client.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/14  20:40}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class NodeStatus {

    private CpuInfo cpuinfo;


    @Getter
    @Setter
    public static class CpuInfo {
        private int cpus;

        private BigDecimal mhz;

        private int cores;
        private String model;

    }

}
