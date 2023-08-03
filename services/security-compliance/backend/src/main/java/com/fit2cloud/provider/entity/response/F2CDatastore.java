package com.fit2cloud.provider.entity.response;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/31 6:34 PM
 */
@Data
public class F2CDatastore {
    /**
     * 数据中心 ID
     */
    private String dataCenterId;
    /**
     * 数据中心名称
     */
    private String dataCenterName;
    /**
     * 集群 ID
     */
    private String clusterId;
    /**
     * 集群名称
     */
    private String clusterName;
    /**
     * 存储器 ID
     */
    private String dataStoreId;
    /**
     * 存储器名称
     */
    private String dataStoreName;
    /**
     * 容量
     */
    private long capacity;
    /**
     * 剩余
     */
    private long freeSpace;
    /**
     * 状态
     */
    private String status;
    /**
     * 存储类型
     */
    private String type;
    private long lastUpdate;

    /**
     * 区域
     */
    private String region;
    /**
     * 可用区
     */
    private String zone;
    /**
     * 已分配GB
     */
    private long allocatedSpace;
}
