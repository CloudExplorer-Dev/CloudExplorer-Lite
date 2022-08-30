package com.fit2cloud.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId("id")
    private String id;

    /**
     * 角色分类：系统内置角色、继承角色
     */
    @TableField("_type")
    private String type;

    @TableField("_name")
    private String name;

    @TableField("_description")
    private String description;

    /**
     * 父角色ID
     */
    @TableField("parent_role_id")
    private String parentRoleId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
