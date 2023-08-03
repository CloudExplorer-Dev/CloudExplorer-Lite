package com.fit2cloud.provider.entity.response;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/31 6:18 PM
 */
@Data
public class F2CHost {
    /**
     * 数据中心 ID
     */
    private String dataCenterId;
    /**
     * 数据中心名称
     */
    private String dataCenterName;
    /**
     * 群集ID
     */
    private String clusterId;
    /**
     * 群集名称
     */
    private String clusterName;
    /**
     * 主机 ID
     */
    private String hostId;
    /**
     * 主机名称
     */
    private String hostName;
    /**
     * 主机型号
     */
    private String hostModel;
    /**
     * 主机制造商
     */
    private String hostVendor;
    /**
     * CPU 型号
     */
    private String cpuModel;
    /**
     * 单核 CPU 容量
     */
    private int cpuMHzPerOneCore;
    /**
     * CPU 总数
     */
    private int numCpuCores;
    /**
     * CPU 总量（单位:MHZ)
     */
    private long cpuMHzTotal;
    /**
     * CPU 已分配 （单位:MHZ)
     */
    private long cpuMHzAllocated;
    /**
     * 内存总量 (单位:MB)
     */
    private long memoryTotal;
    /**
     * 内存已分配 (单位:MB)
     */
    private long memoryAllocated;
    /**
     * 云主机总数
     */
    private long vmTotal;
    /**
     * 运行中云主机数
     */
    private long vmRunning;
    /**
     * 已停止云主机数
     */
    private long vmStopped;
    /**
     * 主机 IP
     */
    private String hostIp;
    /**
     * 主机状态
     */
    private String status;
    /**
     * 虚拟化类型
     */
    private String hypervisorType;
    /**
     * 虚拟化版本
     */
    private String hypervisorVersion;
    /**
     * 虚拟核数
     */
    private int vmCpuCores;

    /**
     * 区域
     */
    private String region;
    /**
     * 可用区
     */
    private String zone;
    /**
     * CPU 已使用 （单位:MHZ)
     */
    private long cpuMHzUsed;
    /**
     * 内存已使用 (单位:MB)
     */
    private long memoryUsed;
}
