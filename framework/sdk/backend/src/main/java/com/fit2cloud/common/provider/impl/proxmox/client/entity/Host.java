package com.fit2cloud.common.provider.impl.proxmox.client.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/14  15:50}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class Host {
    private Node Node;
    private ClusterNode clusterNode;
    private Cluster cluster;
    private NodeStatus nodeStatus;
}
