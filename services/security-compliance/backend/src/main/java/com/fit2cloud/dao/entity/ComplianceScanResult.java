package com.fit2cloud.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fit2cloud.dao.constants.ResourceType;
import com.fit2cloud.dao.constants.ComplianceStatus;

/**
 * <p>
 *
 * </p>
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("compliance_scan_result")
@AllArgsConstructor
@NoArgsConstructor
public class ComplianceScanResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 资源id
     */
    @TableField("resource_id")
    private String resourceId;

    /**
     * 资源类型
     */
    @TableField("resource_type")
    private ResourceType resourceType;

    /**
     * 云账号id
     */
    @TableField("cloud_account_id")
    private String cloudAccountId;

    /**
     * 合规数量
     */
    @TableField("compliance_count")
    private Integer complianceCount;

    /**
     * 不合规数量
     */
    @TableField("not_compliance_count")
    private Integer notComplianceCount;

    /**
     * 资源总数
     */
    @TableField("resource_count")
    private Integer resourceCount;

    /**
     * 扫描状态
     */
    @TableField("status")
    private ComplianceStatus status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
