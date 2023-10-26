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
@TableName("vm_cloud_datastore")
public class VmCloudDatastore implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @ExcelIgnore
    private String id;

    /**
     * 云账号 ID
     */
    @TableField("account_id")
    @ExcelIgnore
    private String accountId;

    /**
     * 存储 ID
     */
    @TableField("datastore_id")
    @ExcelIgnore
    private String datastoreId;

    /**
     * 存储名称
     */
    @TableField("datastore_name")
    @ExcelProperty("名称")
    private String datastoreName;

    /**
     * 容量
     */
    @TableField("capacity")
    @ExcelProperty("总容量(GB)")
    private Long capacity;

    /**
     * 剩余
     */
    @TableField("free_space")
    @ExcelProperty("剩余(GB)")
    private Long freeSpace;

    /**
     * 状态
     */
    @TableField("status")
    @ExcelIgnore
    private String status;

    /**
     * 存储类型
     */
    @TableField("type")
    @ExcelProperty("存储类型")
    private String type;

    /**
     * 数据中心
     */
    @TableField("region")
    @ExcelProperty("数据中心")
    private String region;

    /**
     * 集群
     */
    @TableField("zone")
    @ExcelProperty("集群")
    private String zone;

    /**
     * 已分配容量
     */
    @TableField("allocated_space")
    @ExcelProperty("已分配(GB)")
    private Long allocatedSpace;

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
