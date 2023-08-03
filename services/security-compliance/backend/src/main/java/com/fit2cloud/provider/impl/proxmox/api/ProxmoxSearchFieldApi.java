package com.fit2cloud.provider.impl.proxmox.api;

import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.provider.constants.F2CDiskStatus;
import com.fit2cloud.provider.entity.InstanceFieldType;
import com.fit2cloud.provider.entity.InstanceSearchField;
import com.fit2cloud.provider.impl.proxmox.ProxmoxProvider;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/8/3  15:43}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class ProxmoxSearchFieldApi {
    public static List<InstanceSearchField> listEcsInstanceSearchField() {
        InstanceSearchField cpu = new InstanceSearchField("cpu", "cpu", InstanceFieldType.Number)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.ECS);

        InstanceSearchField memory = new InstanceSearchField("内存(单位: GB)", "memory", InstanceFieldType.Number)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.ECS);

        InstanceSearchField status = new InstanceSearchField("实例状态", "instanceStatus", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("运行中", "Running"),
                        new DefaultKeyValue<>("停止", "Stopped"),
                        new DefaultKeyValue<>("未知", "Unknown")))
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.ECS);

        InstanceSearchField osInfo = new InstanceSearchField("操作系统", "osInfo", InstanceFieldType.String)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.ECS);

        return List.of(cpu, memory, status, osInfo);
    }

    public static List<InstanceSearchField> listDataStoreInstanceSearchField() {
        InstanceSearchField capacity = new InstanceSearchField("容量(单位: GB)", "capacity", InstanceFieldType.Number)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.DATA_STORE);

        InstanceSearchField freeSpace = new InstanceSearchField("剩余容量(单位: GB)", "freeSpace", InstanceFieldType.Number)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.DATA_STORE);

        InstanceSearchField status = new InstanceSearchField("状态", "status", InstanceFieldType.String)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.DATA_STORE);
        return List.of(capacity, freeSpace, status);

    }


    public static List<InstanceSearchField> listHostInstanceSearchField() {
        InstanceSearchField hostModel = new InstanceSearchField("主机型号", "hostModel", InstanceFieldType.String)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.HOST);

        InstanceSearchField hostVendor = new InstanceSearchField("主机制造商", "hostVendor", InstanceFieldType.String)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.HOST);

        InstanceSearchField cpuModel = new InstanceSearchField("CPU 型号", "cpuModel", InstanceFieldType.String)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.HOST);

        InstanceSearchField cpuMHzPerOneCore = new InstanceSearchField("单核 CPU 容量(单位:MHZ)", "cpuMHzPerOneCore", InstanceFieldType.Number)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.HOST);

        InstanceSearchField numCpuCores = new InstanceSearchField("CPU 核数", "numCpuCores", InstanceFieldType.Number)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.HOST);

        InstanceSearchField cpuMHzTotal = new InstanceSearchField("CPU 总量(单位:MHZ)", "cpuMHzTotal", InstanceFieldType.Number)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.HOST);

        InstanceSearchField cpuMHzAllocated = new InstanceSearchField("CPU 已分配(单位:MHZ)", "cpuMHzAllocated", InstanceFieldType.Number)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.HOST);

        InstanceSearchField memoryTotal = new InstanceSearchField("内存总量 (单位:MB)", "memoryTotal", InstanceFieldType.Number)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.HOST);

        InstanceSearchField memoryAllocated = new InstanceSearchField("内存已分配 (单位:MB)", "memoryAllocated", InstanceFieldType.Number)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.HOST);

        InstanceSearchField vmTotal = new InstanceSearchField("云主机总数", "vmTotal", InstanceFieldType.Number)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.HOST);

        InstanceSearchField vmRunning = new InstanceSearchField("运行中云主机数", "vmRunning", InstanceFieldType.Number)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.HOST);

        InstanceSearchField vmStopped = new InstanceSearchField("已停止云主机数", "vmStopped", InstanceFieldType.Number)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.HOST);

        InstanceSearchField vmCpuCores = new InstanceSearchField("虚拟核数", "vmCpuCores", InstanceFieldType.Number)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.HOST);

        return List.of(hostModel, hostVendor, cpuModel, cpuMHzPerOneCore, numCpuCores, cpuMHzTotal, cpuMHzAllocated, memoryTotal, memoryAllocated,
                vmTotal, vmRunning, vmStopped, vmCpuCores);
    }

    /**
     * 获取华为云 云磁盘 oss 实例查询字段
     *
     * @return 华为云 云磁盘 实例查询字段
     */
    public static List<InstanceSearchField> listDiskInstanceSearchField() {
        InstanceSearchField status = new InstanceSearchField("云硬盘状态", "status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("使用中", F2CDiskStatus.IN_USE),
                        new DefaultKeyValue<>("可用", F2CDiskStatus.AVAILABLE)))
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.DISK);

        InstanceSearchField volume_type = new InstanceSearchField("硬盘类型", "diskType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("原始磁盘映像 (raw)", "raw"),
                        new DefaultKeyValue<>("QEMU映像格式 (qcow2)", "qcow2"),
                        new DefaultKeyValue<>("VMware映像格式 (vmdk)", "vmdk")))
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.DISK);

        InstanceSearchField size = new InstanceSearchField("云磁盘大小,单位为GiB", "size", InstanceFieldType.Number)
                .resetInstanceField(ProxmoxProvider.proxmoxBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.DISK);

        return List.of(status, volume_type, size);
    }
}
