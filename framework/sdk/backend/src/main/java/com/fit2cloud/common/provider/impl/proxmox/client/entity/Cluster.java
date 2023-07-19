package com.fit2cloud.common.provider.impl.proxmox.client.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/14  15:24}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class Cluster extends ParentClusterNode {
    /**
     * 节点数量
     */
    private Integer nodes;

    /**
     * 是否具有法定数目的
     */
    private Integer quorate;
    /**
     * 名称
     */
    private String name;
    /**
     * id
     */
    private String id;
    /**
     * 版本
     */
    private Integer version;
}
