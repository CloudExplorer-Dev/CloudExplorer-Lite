package com.fit2cloud.base.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("vm_cloud_server")
public class VmCloudServer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @ExcelIgnore
    private String id;

    /**
     * 实例唯一标识
     */
    @TableField("instance_uuid")
    @ExcelIgnore
    private String instanceUuid;

    /**
     * 组织 ID 或工作空间 ID
     */
    @TableField("source_id")
    @ExcelIgnore
    private String sourceId;

    /**
     * Project ID
     */
    @TableField("project_id")
    @ExcelIgnore
    private String projectId;

    /**
     * 云账号 ID
     */
    @TableField("account_id")
    @ExcelIgnore
    private String accountId;

    /**
     * 实例 ID
     */
    @TableField("instance_id")
    @ExcelIgnore
    private String instanceId;

    /**
     * 实例名称
     */
    @TableField("instance_name")
    @ExcelProperty("名称")
    private String instanceName;

    /**
     * 镜像 ID
     */
    @TableField("image_id")
    @ExcelIgnore
    private String imageId;

    /**
     * 实例状态
     */
    @TableField("instance_status")
    @ExcelProperty("状态")
    private String instanceStatus;

    /**
     * 实例类型
     */
    @TableField("instance_type")
    @ExcelIgnore
    private String instanceType;

    /**
     * 实例类型描述
     */
    @TableField("instance_type_description")
    @ExcelProperty("实例规格")
    private String instanceTypeDescription;

    /**
     * 数据中心/区域
     */
    @TableField("region")
    @ExcelIgnore
    private String region;

    /**
     * 可用区/集群
     */
    @TableField("zone")
    @ExcelIgnore
    private String zone;

    /**
     * 宿主机ID
     */
    @TableField("host_id")
    @ExcelIgnore
    private String hostId;

    /**
     * 宿主机
     */
    @TableField("host")
    @ExcelIgnore
    private String host;

    /**
     * 资源池ID
     */
    @TableField("resource_pool_id")
    @ExcelIgnore
    private String resourcePoolId;

    /**
     * 资源池
     */
    @TableField("resource_pool")
    @ExcelIgnore
    private String resourcePool;

    /**
     * 公网 IP
     */
    @TableField("remote_ip")
    @ExcelIgnore
    private String remoteIp;

    /**
     * IP 地址
     */
    @TableField("ip_array")
    @ExcelProperty("IP地址")
    private String ipArray;

    /**
     * 操作系统Key
     */
    @TableField("os")
    @ExcelIgnore
    private String os;

    /**
     * 操作系统版本
     */
    @TableField("os_version")
    @ExcelIgnore
    private String osVersion;

    /**
     * CPU 核数
     */
    @TableField("cpu")
    @ExcelIgnore
    private Integer cpu;

    /**
     * 内存容量
     */
    @TableField("memory")
    @ExcelIgnore
    private Integer memory;

    /**
     * 磁盘容量
     */
    @TableField("disk")
    @ExcelIgnore
    private Integer disk;

    /**
     * 主机名
     */
    @TableField("hostname")
    @ExcelIgnore
    private String hostname;

    /**
     * 管理 IP
     */
    @TableField("management_ip")
    @ExcelIgnore
    private String managementIp;

    /**
     * 管理端口
     */
    @TableField("management_port")
    @ExcelIgnore
    private Integer managementPort;

    /**
     * 云平台操作系统信息
     */
    @TableField("os_info")
    @ExcelIgnore
    private String osInfo;

    /**
     * 备注
     */
    @TableField("remark")
    @ExcelIgnore
    private String remark;

    /**
     * 网络
     */
    @TableField("network")
    @ExcelIgnore
    private String network;

    /**
     * VPC ID
     */
    @TableField("vpc_id")
    @ExcelIgnore
    private String vpcId;

    /**
     * 子网 ID
     */
    @TableField("subnet_id")
    @ExcelIgnore
    private String subnetId;

    /**
     * 网络接口 ID
     */
    @TableField("network_interface_id")
    @ExcelIgnore
    private String networkInterfaceId;

    /**
     * 管理ip:ipv6
     */
    @TableField("management_ipv6")
    @ExcelIgnore
    private String managementIpv6;

    /**
     * 公网ipv6
     */
    @TableField("remote_ipv6")
    @ExcelIgnore
    private String remoteIpv6;

    /**
     * 内网ipv6
     */
    @TableField("local_ipv6")
    @ExcelIgnore
    private String localIpv6;

    /**
     * ip类型: ipv4、 ipv6 、DualStack
     */
    @TableField("ip_type")
    @ExcelIgnore
    private String ipType;

    /**
     * 快照
     */
    @TableField("snap_shot")
    @ExcelIgnore
    private Integer snapShot;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelIgnore
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelIgnore
    private LocalDateTime updateTime;

    /**
     * 最近操作时间
     */
    @TableField("last_operate_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelIgnore
    private LocalDateTime lastOperateTime;

    /**
     * tools版本
     */
    @TableField("vm_tools_version")
    @ExcelIgnore
    private String vmToolsVersion;

    /**
     * tools状态
     */
    @TableField("vm_tools_status")
    @ExcelIgnore
    private String vmToolsStatus;

    /**
     * 计费方式
     */
    @TableField("instance_charge_type")
    @ExcelIgnore
    private String instanceChargeType;

    /**
     * 安全组
     */
    @TableField("security_group_ids")
    @ExcelIgnore
    private String securityGroupIds;

    /**
     * 申请人
     */
    @TableField("apply_user")
    @ExcelIgnore
    private String applyUser;

    /**
     * 包年包月实例的到期时间
     */
    @TableField("expired_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelIgnore
    private LocalDateTime expiredTime;

    /**
     * 是否自动续费
     */
    @TableField("auto_renew")
    @ExcelIgnore
    private Boolean autoRenew;
}
