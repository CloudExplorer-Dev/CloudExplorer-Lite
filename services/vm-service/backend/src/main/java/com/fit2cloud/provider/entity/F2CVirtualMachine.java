package com.fit2cloud.provider.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/19  6:15 PM
 * @Version 1.0
 * @注释:
 */
@Data
@Accessors(chain = true)
public class F2CVirtualMachine {

    /**
     * 数据库中ID
     */
    private String id;
    /**
     * 云主机名称
     */
    private String name;
    /**
     * 镜像id
     */
    private String imageId;
    /**
     * 操作系统
     */
    private String osInfo;
    /**
     * 实例id
     */
    private String instanceId;
    /**
     * 实例状态
     */
    private String instanceStatus;
    /**
     * 实例类型
     */
    private String instanceType;
    /**
     * 实例类型描述
     */
    private String instanceTypeDescription;
    /**
     * 区域
     */
    private String region;
    /**
     * 可用区
     */
    private String zone;
    /**
     * 远程ip
     */
    private String remoteIP;
    /**
     * 本地ip
     */
    private String localIP;
    /**
     * 远程ipv6
     */
    private String remoteIPV6;
    /**
     * 本地ipv6
     */
    private String localIPV6;
    /**
     * 描述
     */
    private String description;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 过期时间
     */
    private Date expired;
    /**
     * 主机名称
     */
    private String hostname;
    /**
     * 消费数据
     */
    private String customData;
    /**
     * 数据中心
     */
    private String dataCenter;
    /**
     * 集群
     */
    private String cluster;
    /**
     * 宿主机ID
     */
    private String hostId;
    /**
     * 宿主机
     */
    private String host;
    /**
     * 资源池ID
     */
    private String resourcePoolId;
    /**
     * 资源池
     */
    private String resourcePool;
    /**
     * cpu
     */
    private int cpu;
    /**
     * 内存
     */
    private int memory;
    /**
     * 磁盘
     */
    private long disk;
    /**
     * cpu使用
     */
    private int cpuUsed;
    /**
     * 内存使用
     */
    private int memoryUsed;
    /**
     * 磁盘使用
     */
    private long diskUsed;
    /**
     * 实例uuid
     */
    private String instanceUUID;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 到期时间
     */
    private Long expiredTime;
    /**
     * mac地址
     */
    private String macAddress;
    /**
     * ip集合
     */
    private List<String> ipArray;
    /**
     * 数据存储名称
     */
    private String datastoreName;
    /**
     * 数据存储类型
     */
    private String datastoreType;
    /**
     * vmtools版本
     */
    private String vmToolsVersion;
    /**
     * vmtools状态
     */
    private String vmToolsStatus;
    /**
     * 键密码
     */
    private Long keypasswordId;
    /**
     * ssh端口
     */
    private int sshPort;
    /**
     * ssh用户
     */
    private String sshUser;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 挂载卷
     */
    private List<String> volumes;
    /**
     * vpcid
     */
    private String vpcId;
    /**
     * 子网id
     */
    private String subnetId;
    /**
     * 网络实例id
     */
    private String networkInterfaceId;
    /**
     * 快照
     */
    private int snapShot;
    /**
     * 备注
     */
    private String remark;
    /**
     * 实例计费类型
     */
    private String instanceChargeType;

    /**
     * 安全组IDS
     */
    private List<String> securityGroupIds;

    /**
     * 监控插件(Agent)状态为运行中
     */
    private boolean agentRunning;
}
