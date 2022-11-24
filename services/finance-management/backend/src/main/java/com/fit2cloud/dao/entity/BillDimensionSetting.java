package com.fit2cloud.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fit2cloud.constants.AuthorizeTypeConstants;
import com.fit2cloud.dao.handler.AuthorizeRuleGrouHandler;
import com.fit2cloud.dao.jentity.BillAuthorizeRule;
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
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "bill_dimension_setting", autoResultMap = true)
public class BillDimensionSetting implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 组建id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 账单规则组
     */
    @TableField(value = "authorize_rule", typeHandler = AuthorizeRuleGrouHandler.class)
    private BillAuthorizeRule authorizeRule;

    /**
     * 组织或者工作空间id
     */
    @TableField("authorize_id")
    private String authorizeId;

    /**
     * 0组织1工作空间
     */
    @TableField("type")
    private AuthorizeTypeConstants type;

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
