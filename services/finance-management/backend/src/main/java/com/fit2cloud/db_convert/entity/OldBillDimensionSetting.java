package com.fit2cloud.db_convert.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fit2cloud.constants.AuthorizeTypeConstants;
import com.fit2cloud.db_convert.entity.json_entity.OldBillAuthorizeRule;
import com.fit2cloud.db_convert.handler.OldAuthorizeRuleHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * <p>
 *
 * </p>
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "bill_dimension_setting")
public class OldBillDimensionSetting implements Serializable {

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
    @TableField(value = "authorize_rule", typeHandler = OldAuthorizeRuleHandler.class)
    private OldBillAuthorizeRule authorizeRule;

    @TableField(value = "authorize_rule", typeHandler = OldAuthorizeRuleHandler.class)
    private HashMap<String, Object> currentAuthorizeRule;

    /**
     * 组织或者工作空间id
     */
    @TableField("authorize_id")
    private String authorizeId;

    /**
     * 当前规则组是否修改
     */
    @TableField("update_flag")
    private Boolean updateFlag;

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
