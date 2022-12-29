package com.fit2cloud.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fit2cloud.dao.constants.RiskLevel;
import com.fit2cloud.dao.handler.RuleHandler;
import com.fit2cloud.dao.jentity.Rule;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/23  14:59}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceRuleCloudAccount {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 规则名称
     */
    @TableField("cr.name")
    private String name;

    /**
     * 规则组id
     */
    @TableField("rule_group_id")
    private String ruleGroupId;

    /**
     * 云平台
     */
    @TableField("platform")
    private String platform;

    /**
     * 资源类型
     */
    @TableField("resource_type")
    private String resourceType;

    /**
     * 规则条件
     */
    @TableField(value = "rules", typeHandler = RuleHandler.class)
    private List<Rule> rules;

    /**
     * 风险等级
     */
    @TableField("risk_level")
    private RiskLevel riskLevel;
    /**
     * 云账号id
     */
    @TableField("ca.id")
    private String cloudAccountId;

    /**
     * 云账号名称
     */
    @TableField("ca.name")
    private String cloudAccountName;

    /**
     * 规则描述
     */
    @TableField("description")
    private String description;

    /**
     * 是否启用
     */
    @TableField("enable")
    private Boolean enable;

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
