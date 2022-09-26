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
@TableName("vm_cloud_image")
public class VmCloudImage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 云账号 ID
     */
    @TableField("account_id")
    private String accountId;

    /**
     * 镜像 ID
     */
    @TableField("image_id")
    private String imageId;

    /**
     * 数据中心/区域
     */
    @TableField("region")
    private String region;

    /**
     * 数据中心/区域
     */
    @TableField("region_name")
    private String regionName;

    /**
     * 镜像名
     */
    @TableField("image_name")
    private String imageName;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 操作系统
     */
    @TableField("os")
    private String os;

    /**
     * 镜像磁盘大小
     */
    @TableField("disk_size")
    private Long diskSize;

    /**
     * 磁盘状态,normal:正常，delete:删除
     */
    @TableField("status")
    private String status;

    /**
     * 工作空间 ID
     */
    @TableField("workspace_id")
    private String workspaceId;

    /**
     * 更新时间
     */
    @TableField("create_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 上次同步时间
     */
    @TableField("update_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
