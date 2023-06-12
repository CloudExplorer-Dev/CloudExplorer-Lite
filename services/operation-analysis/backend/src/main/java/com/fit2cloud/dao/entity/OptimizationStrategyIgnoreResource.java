package com.fit2cloud.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.io.Serial;


/**
 * <p>
 * 优化策略忽略资源
 * </p>
 *
 *
 * @author jianneng
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("operation_analysis_optimization_strategy_ignore_resource")
public class OptimizationStrategyIgnoreResource implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 优化策略ID
     */
    @TableField("optimization_strategy_id")
    private String optimizationStrategyId;

    /**
     * 资源ID
     */
    @TableField("resource_id")
    private String resourceId;
}
