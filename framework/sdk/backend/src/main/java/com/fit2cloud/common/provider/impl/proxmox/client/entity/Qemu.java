package com.fit2cloud.common.provider.impl.proxmox.client.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/10  15:29}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class Qemu {
    /**
     * 如果该字段为1 那么就是模板
     */
    private Integer template;
    /**
     * stopped  |running
     */
    private String status;

    /**
     * 虚拟机id
     */
    private Integer vmid;

    /**
     * 虚拟机分配的cpu
     */
    private int cpus;

    /**
     * cpu使用率
     */
    private BigDecimal cpu;
    /**
     * 当前配置锁（如果有）
     */
    private String lock;

    /**
     * 启动盘大小
     */
    private Long maxdisk;

    /**
     * 内存大小
     */
    private Long maxmem;
    /**
     * 已使用内存 kb
     */
    private Long mem;
    /**
     * 虚拟机名称
     */
    private String name;
    /**
     * 运行 qemu 进程的 PID。
     */
    private Integer pid;

    /**
     * QEMU QMP 代理状态。
     */
    private String qmpstatus;

    /**
     * 当前正在运行的计算机类型（如果正在运行）
     */
    @JsonProperty("running-machine")
    private String runningMachine;
    /**
     * 运行的Qemu的版本
     */
    @JsonProperty("running-qemu")
    private String runningQemu;
    /**
     * 当前配置的标记
     */
    private String tags;
    /**
     * 运行时间
     */
    private Long uptime;
}
