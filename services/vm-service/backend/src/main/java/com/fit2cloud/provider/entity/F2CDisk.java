package com.fit2cloud.provider.entity;

import lombok.Data;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  10:43 AM
 * @Version 1.0
 * @注释: 磁盘对象
 */
@Data
public class F2CDisk {
    /**
     * 区域
     */
    private String region;
    /**
     * 可用区
     */
    private String zone;
    /**
     * 磁盘id
     */
    private String diskId;
    /**
     * 磁盘名称
     */
    private String diskName;
    /**
     * 实例Uuid
     */
    private String instanceUuid;
    /**
     * 磁盘类型
     */
    private String diskType;
    /**
     * 磁盘模型
     */
    private String diskMode;
    /**
     * 磁盘类别
     */
    private String category;
    /**
     * 磁盘状态
     */
    private String status;
    /**
     * 磁盘付费类型
     */
    private String diskChargeType;
    /**
     * 磁盘描述
     */
    private String description;
    /**
     * 磁盘大小
     */
    private long size;
    /**
     * 磁盘设备
     */
    private String device;
    /**
     * 磁盘数据存储唯一id
     */
    private String datastoreUniqueId;
    /**
     * 创建时间
     */
    private long createTime;
    /**
     * 实例名称
     */
    private String instanceName;
    /**
     * 存储名称
     */
    private String datastoreName;
    /**
     * 新的大小
     */
    private long newSize;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 是否启动挂载
     */
    private boolean bootable;
    /**
     * 删除时是否删除实例
     */
    private String deleteWithInstance;
    /**
     * 磁盘挂载点
     */
    private String mountPoint;
    /**
     * 文件系统类型ext4、xfs
     */
    private String fileSystemType;
}
