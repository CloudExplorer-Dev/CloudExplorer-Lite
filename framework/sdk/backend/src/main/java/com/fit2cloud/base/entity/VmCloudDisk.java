package com.fit2cloud.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.io.Serial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * <p>
 * 
 * </p>
 *
 *
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("vm_cloud_disk")
public class VmCloudDisk implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 数据中心/区域
     */
    @TableField("region")
    private String region;

    /**
     * 集群/可用区
     */
    @TableField("zone")
    private String zone;

    /**
     * 磁盘UUID
     */
    @TableField("disk_id")
    private String diskId;

    /**
     * 磁盘名称
     */
    @TableField("disk_name")
    private String diskName;

    /**
     * 磁盘置备方式
     */
    @TableField("disk_type")
    private String diskType;

    /**
     * 磁盘类型
     */
    @TableField("category")
    private String category;

    /**
     * 磁盘状态
     */
    @TableField("status")
    private String status;

    /**
     * 计费方式
     */
    @TableField("disk_charge_type")
    private String diskChargeType;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 大小(GB)
     */
    @TableField("size")
    private Long size;

    /**
     * 挂载点
     */
    @TableField("device")
    private String device;

    /**
     * 云账号ID
     */
    @TableField("account_id")
    private String accountId;

    /**
     * 存储器ID
     */
    @TableField("datastore_id")
    private String datastoreId;

    /**
     * 关联虚拟机UUID
     */
    @TableField("instance_uuid")
    private String instanceUuid;

    /**
     * 工作空间ID
     */
    @TableField("workspace_id")
    private String workspaceId;

    /**
     * Project ID
     */
    @TableField("project_id")
    private String projectId;

    /**
     * 是否启动盘
     */
    @TableField("bootable")
    private Boolean bootable;

    /**
     * 镜像ID
     */
    @TableField("image_id")
    private String imageId;

    /**
     * 随实例释放
     */
    @TableField("delete_with_instance")
    private String deleteWithInstance;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
