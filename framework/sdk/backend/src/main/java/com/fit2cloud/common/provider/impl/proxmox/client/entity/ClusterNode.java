package com.fit2cloud.common.provider.impl.proxmox.client.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/14  15:28}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ClusterNode extends ParentClusterNode {

    /**
     *
     */
    private String level;
    /**
     * ip
     */
    private String ip;
    /**
     * 节点名称
     */
    private String name;
    /**
     * 是否在线
     */
    private Integer online;

    /**
     * 节点id
     */
    private String id;
    /**
     * 集群节点id
     */
    private Integer nodeid;
    /**
     * 是否是本机
     */
    private Integer local;
}
