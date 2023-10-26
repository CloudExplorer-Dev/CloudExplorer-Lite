package com.fit2cloud.base.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("vm_cloud_disk")
public class VmCloudDisk implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @ExcelIgnore
    private String id;

    /**
     * 数据中心/区域
     */
    @TableField("region")
    @ExcelProperty(value = "数据中心/区域")
    private String region;

    /**
     * 集群/可用区
     */
    @TableField("zone")
    @ExcelProperty(value = "集群/可用区")
    private String zone;

    /**
     * 磁盘UUID
     */
    @TableField("disk_id")
    @ExcelIgnore
    private String diskId;

    /**
     * 磁盘名称
     */
    @TableField("disk_name")
    @ExcelProperty(value = "名称")
    private String diskName;

    /**
     * 磁盘置备方式
     */
    @TableField("disk_type")
    @ExcelIgnore
    private String diskType;

    /**
     * 磁盘类型
     */
    @TableField("category")
    @ExcelIgnore
    private String category;

    /**
     * 磁盘状态
     */
    @TableField("status")
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 计费方式
     */
    @TableField("disk_charge_type")
    @ExcelProperty(value = "付费方式")
    private String diskChargeType;

    /**
     * 描述
     */
    @TableField("description")
    @ExcelIgnore
    private String description;

    /**
     * 大小(GB)
     */
    @TableField("size")
    @ExcelProperty(value = "大小(GB)")
    private Long size;

    /**
     * 挂载点
     */
    @TableField("device")
    @ExcelIgnore
    private String device;

    /**
     * 云账号ID
     */
    @TableField("account_id")
    @ExcelIgnore
    private String accountId;

    /**
     * 存储器ID
     */
    @TableField("datastore_id")
    @ExcelIgnore
    private String datastoreId;

    /**
     * 关联云主机UUID
     */
    @TableField("instance_uuid")
    @ExcelIgnore
    private String instanceUuid;

    /**
     * 组织 ID 或工作空间 ID
     */
    @TableField("source_id")
    @ExcelIgnore
    private String sourceId;

    /**
     * Project ID
     */
    @TableField("project_id")
    @ExcelIgnore
    private String projectId;

    /**
     * 是否启动盘
     */
    @TableField("bootable")
    @ExcelIgnore
    private Boolean bootable;

    /**
     * 镜像ID
     */
    @TableField("image_id")
    @ExcelIgnore
    private String imageId;

    /**
     * 随实例释放
     */
    @TableField("delete_with_instance")
    @ExcelIgnore
    private String deleteWithInstance;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelIgnore
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelIgnore
    private LocalDateTime updateTime;

}
