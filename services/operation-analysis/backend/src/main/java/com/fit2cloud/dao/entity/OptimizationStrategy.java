package com.fit2cloud.dao.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fit2cloud.dao.handler.OptimizationRuleHandler;
import com.fit2cloud.dto.optimization.OptimizationRule;
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
 * 资源优化策略
 * </p>
 *
 *
 * @author jianneng
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "operation_analysis_optimization_strategy",autoResultMap = true)
public class OptimizationStrategy implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 优化策略名称
     */
    @TableField(value = "name",updateStrategy = FieldStrategy.IGNORED)
    private String name;

    /**
     * 策略类型，监控策略or云主机相关mysql表字段策略
     */
    @TableField("strategy_type")
    private String strategyType;

    /**
     * 资源类型
     */
    @TableField("resource_type")
    private String resourceType;

    /**
     * 分析数据范围,过去多少天
     */
    @TableField("days")
    private Long days;

    /**
     * 优化规则
     */
    @TableField(value = "optimization_rules", typeHandler = OptimizationRuleHandler.class,updateStrategy = FieldStrategy.IGNORED)
    private OptimizationRule optimizationRules;

    /**
     * 是否针对所有资源
     */
    @TableField(value = "optimization_scope",updateStrategy = FieldStrategy.IGNORED)
    private Boolean optimizationScope;

    /**
     * 组织或者工作空间id
     */
    @TableField("authorize_id")
    private String authorizeId;

    /**
     * 策略是否启用
     */
    @TableField(value = "enabled",updateStrategy = FieldStrategy.IGNORED)
    private Boolean enabled;

    /**
     * 策略是否可以编辑
     */
    @TableField("update_flag")
    private Boolean updateFlag;

    /**
     * 创建用户ID
     */
    @TableField("create_user_id")
    private String createUserId;

    /**
     * 更新用户ID
     */
    @TableField(value = "update_user_id",updateStrategy = FieldStrategy.IGNORED)
    private String updateUserId;

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
    @TableField(value = "update_time",updateStrategy = FieldStrategy.IGNORED)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
