package com.fit2cloud.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;


/**
 * <p>
 * VIEW
 * </p>
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("org_workspace")
public class OrgWorkspace implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;

    @TableField("NAME")
    private String name;

    @TableField("organization_id")
    private String organizationId;

    @TableField("organization_name")
    private String organizationName;

    @TableField("pid")
    private String pid;

    @TableField("type")
    private String type;
}
