package com.fit2cloud.common.provider.impl.proxmox.client.entity;

import java.math.BigDecimal;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/7  14:36}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class Node {
    /**
     * 主键id
     */
    private String id;
    /**
     * 支持级别。
     */
    private String level;
    /**
     * CPU 已使用cpu
     */
    private BigDecimal cpu;
    /**
     * 最大可使用cpu
     */
    private int maxcpu;
    /**
     * 类型
     */
    private String type;
    /**
     * 节点正常运行时间（以秒为单位）
     */
    private long uptime;
    /**
     * 群集节点名称
     */
    private String node;
    /**
     * 磁盘使用率
     */
    private long disk;
    /**
     * 最大可用磁盘大小
     */
    private long maxdisk;
    /**
     * 节点证书的 SSL 指纹。
     */
    private String ssl_fingerprint;
    /**
     * 内存使用率
     */
    private long mem;
    /**
     * 最大可用内存
     */
    private long maxmem;
    /**
     * 节点状态。
     */
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public BigDecimal getCpu() {
        return cpu;
    }

    public void setCpu(BigDecimal cpu) {
        this.cpu = cpu;
    }

    public int getMaxcpu() {
        return maxcpu;
    }

    public void setMaxcpu(int maxcpu) {
        this.maxcpu = maxcpu;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public long getDisk() {
        return disk;
    }

    public void setDisk(long disk) {
        this.disk = disk;
    }

    public long getMaxdisk() {
        return maxdisk;
    }

    public void setMaxdisk(long maxdisk) {
        this.maxdisk = maxdisk;
    }

    public String getSsl_fingerprint() {
        return ssl_fingerprint;
    }

    public void setSsl_fingerprint(String ssl_fingerprint) {
        this.ssl_fingerprint = ssl_fingerprint;
    }

    public long getMem() {
        return mem;
    }

    public void setMem(long mem) {
        this.mem = mem;
    }

    public long getMaxmem() {
        return maxmem;
    }

    public void setMaxmem(long maxmem) {
        this.maxmem = maxmem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
