package com.fit2cloud.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
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
 * 
 * </p>
 *
 *
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("vm_cloud_server")
public class VmCloudServer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 实例唯一标识
     */
    @TableField("instance_uuid")
    private String instanceUuid;

    /**
     * 工作空间 ID
     */
    @TableField("workspace_id")
    private String workspaceId;

    /**
     * Project ID
     */
    @TableField("project_id")
    private String projectId;

    /**
     * 云账号 ID
     */
    @TableField("account_id")
    private String accountId;

    /**
     * 实例 ID
     */
    @TableField("instance_id")
    private String instanceId;

    /**
     * 实例名称
     */
    @TableField("instance_name")
    private String instanceName;

    /**
     * 镜像 ID
     */
    @TableField("image_id")
    private String imageId;

    /**
     * 实例状态
     */
    @TableField("instance_status")
    private String instanceStatus;

    /**
     * 实例类型
     */
    @TableField("instance_type")
    private String instanceType;

    /**
     * 实例类型描述
     */
    @TableField("instance_type_description")
    private String instanceTypeDescription;

    /**
     * 数据中心/区域
     */
    @TableField("region")
    private String region;

    /**
     * 可用区/集群
     */
    @TableField("zone")
    private String zone;

    /**
     * 宿主机
     */
    @TableField("host")
    private String host;

    /**
     * 公网 IP
     */
    @TableField("remote_ip")
    private String remoteIp;

    /**
     * IP 地址
     */
    @TableField("ip_array")
    private String ipArray;

    /**
     * 操作系统Key
     */
    @TableField("os")
    private String os;

    /**
     * 操作系统版本
     */
    @TableField("os_version")
    private String osVersion;

    /**
     * CPU 核数
     */
    @TableField("cpu")
    private Integer cpu;

    /**
     * 内存容量
     */
    @TableField("memory")
    private Integer memory;

    /**
     * 磁盘容量
     */
    @TableField("disk")
    private Integer disk;

    /**
     * 主机名
     */
    @TableField("hostname")
    private String hostname;

    /**
     * 管理 IP
     */
    @TableField("management_ip")
    private String managementIp;

    /**
     * 管理端口
     */
    @TableField("management_port")
    private Integer managementPort;

    /**
     * 云平台操作系统信息
     */
    @TableField("os_info")
    private String osInfo;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 网络
     */
    @TableField("network")
    private String network;

    /**
     * VPC ID
     */
    @TableField("vpc_id")
    private String vpcId;

    /**
     * 子网 ID
     */
    @TableField("subnet_id")
    private String subnetId;

    /**
     * 网络接口 ID
     */
    @TableField("network_interface_id")
    private String networkInterfaceId;

    /**
     * 管理ip:ipv6
     */
    @TableField("management_ipv6")
    private String managementIpv6;

    /**
     * 公网ipv6
     */
    @TableField("remote_ipv6")
    private String remoteIpv6;

    /**
     * 内网ipv6
     */
    @TableField("local_ipv6")
    private String localIpv6;

    /**
     * ip类型: ipv4、 ipv6 、DualStack
     */
    @TableField("ip_type")
    private String ipType;

    /**
     * 快照
     */
    @TableField("snap_shot")
    private Integer snapShot;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
