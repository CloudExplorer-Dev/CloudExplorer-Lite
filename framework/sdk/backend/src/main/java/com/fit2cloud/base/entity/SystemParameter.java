package com.fit2cloud.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.io.Serial;


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
@TableName("system_parameter")
public class SystemParameter implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 参数名称
     */
    @TableId(value = "param_key", type = IdType.ASSIGN_UUID)
    private String paramKey;

    /**
     * 参数值
     */
    @TableField("param_value")
    private String paramValue;

    /**
     * 参数类型
     */
    @TableField("type")
    private String type;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 模块
     */
    @TableField("module")
    private String module;
}
