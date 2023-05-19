package com.fit2cloud.provider.impl.vsphere.api;

import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.provider.entity.InstanceFieldType;
import com.fit2cloud.provider.entity.InstanceSearchField;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/6  18:22}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class VsphereInstanceSearchApi {
    /**
     * 获取vsphere ecs云主机查询字段
     *
     * @return vsphere 云主机查询字段
     */
    public static List<InstanceSearchField> listEcsInstanceSearchField() {
        InstanceSearchField cpu = new InstanceSearchField("cpu", "cpu", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.ECS);

        InstanceSearchField memory = new InstanceSearchField("内存(单位: GB)", "memory", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.ECS);

        InstanceSearchField status = new InstanceSearchField("实例状态", "instanceStatus", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("运行中", "Running"),
                        new DefaultKeyValue<>("停止", "Stopped"),
                        new DefaultKeyValue<>("未知", "Unknown")))
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.ECS);

        InstanceSearchField osInfo = new InstanceSearchField("操作系统", "osInfo", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.ECS);

        return List.of(cpu, memory, status, osInfo);
    }

    public static List<InstanceSearchField> listDataStoreInstanceSearchField() {
        InstanceSearchField capacity = new InstanceSearchField("容量(单位: GB)", "capacity", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.DATA_STORE);

        InstanceSearchField freeSpace = new InstanceSearchField("剩余容量(单位: GB)", "freeSpace", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.DATA_STORE);

        InstanceSearchField status = new InstanceSearchField("状态", "status", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.DATA_STORE);
        return List.of(capacity, freeSpace, status);

    }

    public static List<InstanceSearchField> listResourcePoolInstanceSearchField() {
        InstanceSearchField name = new InstanceSearchField("资源池名称", "name", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.RESOURCE_POOL);

        InstanceSearchField totalCpu = new InstanceSearchField("CPU总量(单位: GHz)", "totalCpu", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.RESOURCE_POOL);

        InstanceSearchField totalMemory = new InstanceSearchField("内存总量(单位: GB)", "totalMemory", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.RESOURCE_POOL);

        InstanceSearchField usedCpu = new InstanceSearchField("已使用CPU(单位: GHz)", "usedCpu", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.RESOURCE_POOL);

        InstanceSearchField usedMemory = new InstanceSearchField("已使用内存(单位: GB)", "usedMemory", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.RESOURCE_POOL);
        return List.of(name, totalCpu, totalMemory, usedCpu, usedMemory);
    }

    public static List<InstanceSearchField> listHostInstanceSearchField() {
        InstanceSearchField hostModel = new InstanceSearchField("主机型号", "hostModel", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.HOST);

        InstanceSearchField hostVendor = new InstanceSearchField("主机制造商", "hostVendor", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.HOST);

        InstanceSearchField cpuModel = new InstanceSearchField("CPU 型号", "cpuModel", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.HOST);

        InstanceSearchField cpuMHzPerOneCore = new InstanceSearchField("单核 CPU 容量(单位:MHZ)", "cpuMHzPerOneCore", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.HOST);

        InstanceSearchField numCpuCores = new InstanceSearchField("CPU 核数", "numCpuCores", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.HOST);

        InstanceSearchField cpuMHzTotal = new InstanceSearchField("CPU 总量(单位:MHZ)", "cpuMHzTotal", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.HOST);

        InstanceSearchField cpuMHzAllocated = new InstanceSearchField("CPU 已分配(单位:MHZ)", "cpuMHzAllocated", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.HOST);

        InstanceSearchField memoryTotal = new InstanceSearchField("内存总量 (单位:MB)", "memoryTotal", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.HOST);

        InstanceSearchField memoryAllocated = new InstanceSearchField("内存已分配 (单位:MB)", "memoryAllocated", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.HOST);

        InstanceSearchField vmTotal = new InstanceSearchField("虚拟机总数", "vmTotal", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.HOST);

        InstanceSearchField vmRunning = new InstanceSearchField("运行中虚拟机数", "vmRunning", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.HOST);

        InstanceSearchField vmStopped = new InstanceSearchField("已停止虚拟机数", "vmStopped", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.HOST);

        InstanceSearchField vmCpuCores = new InstanceSearchField("虚拟核数", "vmCpuCores", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.HOST);

        return List.of(hostModel, hostVendor, cpuModel, cpuMHzPerOneCore, numCpuCores, cpuMHzTotal, cpuMHzAllocated, memoryTotal, memoryAllocated,
                vmTotal, vmRunning, vmStopped, vmCpuCores);
    }

}
