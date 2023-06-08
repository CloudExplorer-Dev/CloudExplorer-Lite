package com.fit2cloud.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fit2cloud.base.handler.MapTypeHandler;
import com.fit2cloud.common.charging.constants.BillModeConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * <p>
 *
 * </p>
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "resource_instance", autoResultMap = true)
public class ResourceInstance implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id 用于区别实例
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 资源id
     */
    @TableField("resource_id")
    private String resourceId;

    /**
     * 资源名称
     */
    @TableField("resource_name")
    private String resourceName;

    /**
     * 资源类型
     */
    @TableField("resource_type")
    private String resourceType;

    /**
     * 云账号id
     */
    @TableField("cloud_account_id")
    private String cloudAccountId;

    /**
     * 产品id
     */
    @TableField("product_id")
    private String productId;

    /**
     * 产品名称
     */
    @TableField("product_name")
    private String productName;

    /**
     * 产品详情
     */
    @TableField("product_detail")
    private String productDetail;

    /**
     * 区域
     */
    @TableField("region")
    private String region;

    /**
     * 可用区
     */
    @TableField("zone")
    private String zone;

    /**
     * 工作空间id
     */
    @TableField("workspace_id")
    private String workspaceId;

    /**
     * 工作空间名称
     */
    @TableField("workspace_name")
    private String workspaceName;

    /**
     * 组织id
     */
    @TableField("organization_id")
    private String organizationId;

    /**
     * 组织名称
     */
    @TableField("organization_name")
    private String organizationName;

    /**
     * 计费模式 ON_DEMAND 按需 MONTHLY包年包月
     */
    @TableField("bill_mode")
    private BillModeConstants billMode;

    /**
     * 元数据
     */
    @TableField(value = "meta", typeHandler = MapTypeHandler.class)
    private Map<String, Integer> meta;

    @TableField("create_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableField("update_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
