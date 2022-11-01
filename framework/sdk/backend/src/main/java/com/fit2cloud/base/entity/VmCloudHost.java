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
@TableName("vm_cloud_host")
public class VmCloudHost implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 云账号 ID
     */
    @TableField("account_id")
    private String accountId;

    /**
     * 数据中心
     */
    @TableField("region")
    private String region;

    /**
     * 集群
     */
    @TableField("zone")
    private String zone;

    /**
     * 主机ID
     */
    @TableField("host_id")
    private String hostId;

    /**
     * 主机名
     */
    @TableField("host_name")
    private String hostName;

    /**
     * CPU 总量
     */
    @TableField("cpu_total")
    private Long cpuTotal;

    /**
     * CPU 已分配
     */
    @TableField("cpu_allocated")
    private Long cpuAllocated;

    /**
     * 内存总量
     */
    @TableField("memory_total")
    private Long memoryTotal;

    /**
     * 内存已分配
     */
    @TableField("memory_allocated")
    private Long memoryAllocated;

    /**
     * 虚拟机总数
     */
    @TableField("vm_total")
    private Long vmTotal;

    /**
     * 运行中虚拟机数
     */
    @TableField("vm_running")
    private Long vmRunning;

    /**
     * 已停止虚拟机数
     */
    @TableField("vm_stopped")
    private Long vmStopped;

    /**
     * 主机 IP
     */
    @TableField("host_ip")
    private String hostIp;

    /**
     * 主机状态
     */
    @TableField("status")
    private String status;

    /**
     * 虚拟化类型
     */
    @TableField("hypervisor_type")
    private String hypervisorType;

    /**
     * 虚拟化版本
     */
    @TableField("hypervisor_version")
    private String hypervisorVersion;

    /**
     * 主机提供商
     */
    @TableField("host_vendor")
    private String hostVendor;

    /**
     * 主机型号
     */
    @TableField("host_model")
    private String hostModel;

    /**
     * CPU 型号
     */
    @TableField("cpu_model")
    private String cpuModel;

    /**
     * 单核 CPU 容量
     */
    @TableField("cpu_mHz_per_one_core")
    private Integer cpuMhzPerOneCore;

    /**
     * CPU 总数
     */
    @TableField("num_cpu_cores")
    private Integer numCpuCores;

    /**
     * 维保日期
     */
    @TableField("maintenance_timestamp")
    private Long maintenanceTimestamp;

    /**
     * 虚拟核数
     */
    @TableField("vm_cpu_cores")
    private Integer vmCpuCores;

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
