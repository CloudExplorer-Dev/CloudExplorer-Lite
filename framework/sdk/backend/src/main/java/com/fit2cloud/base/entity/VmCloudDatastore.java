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
@TableName("vm_cloud_datastore")
public class VmCloudDatastore implements Serializable {

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
     * 存储 ID
     */
    @TableField("datastore_id")
    private String datastoreId;

    /**
     * 存储名称
     */
    @TableField("datastore_name")
    private String datastoreName;

    /**
     * 容量
     */
    @TableField("capacity")
    private Long capacity;

    /**
     * 剩余
     */
    @TableField("free_space")
    private Long freeSpace;

    /**
     * 状态
     */
    @TableField("status")
    private String status;

    /**
     * 存储类型
     */
    @TableField("type")
    private String type;

    /**
     * 数据中心
     */
    @TableField("region")
    private String region;

    /**
     * 集群
     */
    @TableField("zone")
    private String zone;

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
