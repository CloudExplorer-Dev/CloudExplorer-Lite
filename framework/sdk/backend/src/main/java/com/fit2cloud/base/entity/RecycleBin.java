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
import com.fit2cloud.common.constants.ResourceTypeConstants;
import com.fit2cloud.common.constants.RecycleBinStatusConstants;

/**
 * <p>
 *
 * </p>
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("recycle_bin")
public class RecycleBin implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 资源 ID
     */
    @TableField("resource_id")
    private String resourceId;

    /**
     * 资源类型：云主机、云磁盘
     */
    @TableField("resource_type")
    private ResourceTypeConstants resourceType;

    /**
     * 资源状态：待回收、已删除、已恢复
     */
    @TableField("status")
    private RecycleBinStatusConstants status;

    /**
     * 创建时间（资源放入回收站时间）
     */
    @TableField("create_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 删除时间（资源彻底删除时间）
     */
    @TableField("delete_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deleteTime;

    /**
     * 恢复时间
     */
    @TableField("recover_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime recoverTime;

    /**
     * 发起回收、恢复或者删除操作的用户ID
     */
    @TableField("user_id")
    private String userId;
}
