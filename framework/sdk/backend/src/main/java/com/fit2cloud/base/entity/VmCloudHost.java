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
@TableName("vm_cloud_host")
public class VmCloudHost implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @ExcelIgnore
    private String id;

    /**
     * 云账号 ID
     */
    @TableField("account_id")
    @ExcelIgnore
    private String accountId;

    /**
     * 数据中心
     */
    @TableField("region")
    @ExcelProperty(value = "数据中心")
    private String region;

    /**
     * 集群
     */
    @TableField("zone")
    @ExcelProperty(value = "集群")
    private String zone;

    /**
     * 主机ID
     */
    @TableField("host_id")
    @ExcelIgnore
    private String hostId;

    /**
     * 主机名
     */
    @TableField("host_name")
    @ExcelProperty(value = "主机名")
    private String hostName;

    /**
     * CPU 总量
     */
    @TableField("cpu_total")
    @ExcelProperty(value = "CPU 总量(MHz)")
    private Long cpuTotal;

    /**
     * CPU 已分配
     */
    @TableField("cpu_allocated")
    @ExcelProperty(value = "CPU 已分配(MHz)")
    private Long cpuAllocated;

    /**
     * 内存总量
     */
    @TableField("memory_total")
    @ExcelProperty(value = "内存总量(MB)")
    private Long memoryTotal;

    /**
     * 内存已分配
     */
    @TableField("memory_allocated")
    @ExcelProperty(value = "内存已分配(MB)")
    private Long memoryAllocated;

    /**
     * 云主机总数
     */
    @TableField("vm_total")
    @ExcelProperty(value = "云主机总数")
    private Long vmTotal;

    /**
     * 运行中云主机数
     */
    @TableField("vm_running")
    @ExcelProperty(value = "运行中云主机数")
    private Long vmRunning;

    /**
     * 已停止云主机数
     */
    @TableField("vm_stopped")
    @ExcelProperty(value = "已停止云主机数")
    private Long vmStopped;

    /**
     * 主机 IP
     */
    @TableField("host_ip")
    @ExcelProperty(value = "主机 IP")
    private String hostIp;

    /**
     * 主机状态
     */
    @TableField("status")
    @ExcelProperty(value = "主机状态")
    private String status;

    /**
     * 虚拟化类型
     */
    @TableField("hypervisor_type")
    @ExcelIgnore
    private String hypervisorType;

    /**
     * 虚拟化版本
     */
    @TableField("hypervisor_version")
    @ExcelIgnore
    private String hypervisorVersion;

    /**
     * 主机提供商
     */
    @TableField("host_vendor")
    @ExcelIgnore
    private String hostVendor;

    /**
     * 主机型号
     */
    @TableField("host_model")
    @ExcelProperty(value = "主机型号")
    private String hostModel;

    /**
     * CPU 型号
     */
    @TableField("cpu_model")
    @ExcelProperty(value = "CPU 型号")
    private String cpuModel;

    /**
     * 单核 CPU 容量
     */
    @TableField("cpu_mHz_per_one_core")
    @ExcelProperty(value = "单核 CPU 容量(MHz)")
    private Integer cpuMhzPerOneCore;

    /**
     * CPU 总数
     */
    @TableField("num_cpu_cores")
    @ExcelProperty(value = "CPU 总数(核)")
    private Integer numCpuCores;

    /**
     * 维保日期
     */
    @TableField("maintenance_timestamp")
    @ExcelIgnore
    private Long maintenanceTimestamp;

    /**
     * 虚拟核数
     */
    @TableField("vm_cpu_cores")
    @ExcelProperty(value = "虚拟核数")
    private Integer vmCpuCores;

    /**
     * CPU已使用MHZ
     */
    @TableField("cpu_used")
    @ExcelProperty(value = "CPU已使用(MHz)")
    private Long cpuUsed;

    /**
     * 内存已使用MB
     */
    @TableField("memory_used")
    @ExcelProperty(value = "内存已使用(MB)")
    private Long memoryUsed;

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
}
