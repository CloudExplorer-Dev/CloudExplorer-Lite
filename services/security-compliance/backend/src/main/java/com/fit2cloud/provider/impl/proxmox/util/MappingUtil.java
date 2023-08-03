package com.fit2cloud.provider.impl.proxmox.util;

import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.*;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.config.Config;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.config.DiskStatusInfo;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.config.NetworkInterface;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.constants.OsType;
import com.fit2cloud.provider.constants.F2CDiskStatus;
import com.fit2cloud.provider.constants.F2CInstanceStatus;
import com.fit2cloud.provider.entity.response.F2CDisk;
import com.fit2cloud.provider.entity.response.F2CHost;
import com.fit2cloud.provider.entity.response.F2CVirtualMachine;
import jodd.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/10  15:55}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class MappingUtil {

    public static String toVmId(String vmId, String vmGenId) {
        String temp = "{\"uuid\":\"%s\",\"vmGenId\":\"%s\"}";
        return temp.formatted(vmId, vmGenId);
    }

    public static String toInstanceId(String uuid, String vmGenId) {
        String temp = "{\"uuid\":\"%s\",\"vmGenId\":\"%s\"}";
        return temp.formatted(uuid, vmGenId);
    }

    public static F2CVirtualMachine toF2CVirtualMachine(Qemu qemu,
                                                        Config config,
                                                        String cluster,
                                                        ClusterNode clusterNode,
                                                        List<NetworkInterface> networkInterfaces) {
        F2CVirtualMachine f2CVirtualMachine = new F2CVirtualMachine();
        String instanceId = toVmId(qemu.getVmid().toString(), StringUtil.isEmpty(config.getVmGenId()) ? "" : config.getVmGenId());
        f2CVirtualMachine.setInstanceUUID(instanceId);
        f2CVirtualMachine.setInstanceId(instanceId);
        f2CVirtualMachine.setRegion(clusterNode.getName());
        f2CVirtualMachine.setMemory((int) Math.ceil(qemu.getMaxmem() / 1024.f / 1024 / 1024));
        f2CVirtualMachine.setCpu(qemu.getCpus());
        f2CVirtualMachine.setCluster(cluster);
        f2CVirtualMachine.setHostId(clusterNode.getName());
        f2CVirtualMachine.setHost(clusterNode.getName());
        f2CVirtualMachine.setHostname(qemu.getName());
        f2CVirtualMachine.setMemoryUsed((int) Math.ceil(qemu.getMem() / 1024.0 / 1024 / 1024));
        f2CVirtualMachine.setInstanceStatus(getF2CInstanceStatus(qemu.getStatus()).name());
        f2CVirtualMachine.setDescription(Objects.isNull(config.getDescription()) ? "" : config.getDescription().getDescription());
        f2CVirtualMachine.setRemark(Objects.isNull(config.getDescription()) ? "" : config.getDescription().getDescription());
        f2CVirtualMachine.setName(qemu.getName());
        f2CVirtualMachine.setInstanceType(getInstanceType(f2CVirtualMachine.getCpu(), f2CVirtualMachine.getMemory()));
        f2CVirtualMachine.setInstanceTypeDescription(getInstanceType(f2CVirtualMachine.getCpu(), f2CVirtualMachine.getMemory()));
        f2CVirtualMachine.setZone(clusterNode.getName());
        f2CVirtualMachine.setOsInfo(StringUtil.isEmpty(config.getOsType()) ? "Other" : OsType.osType.getOrDefault(config.getOsType(), "Other"));
        if (CollectionUtils.isNotEmpty(networkInterfaces)) {
            List<String> ipArray = networkInterfaces
                    .stream()
                    .filter(networkInterface -> !StringUtil.equals(networkInterface.getName(), "lo") && CollectionUtils.isNotEmpty(networkInterface.getIpAddresses()))
                    .flatMap(networkInterface -> networkInterface.getIpAddresses()
                            .stream()
                            .map(NetworkInterface.IpAddresses::getIpAddress))
                    .toList();
            f2CVirtualMachine.setIpArray(ipArray);
            f2CVirtualMachine.setAgentRunning(true);
        } else {
            f2CVirtualMachine.setIpArray(List.of());
            f2CVirtualMachine.setAgentRunning(false);
        }
        return f2CVirtualMachine;


    }

    private static F2CInstanceStatus getF2CInstanceStatus(String status) {
        if (StringUtil.equals(status, "stopped")) {
            return F2CInstanceStatus.Stopped;
        }
        if (StringUtil.equals(status, "running")) {
            return F2CInstanceStatus.Running;
        }
        return F2CInstanceStatus.Unknown;
    }

    private static String getInstanceType(int cpu, int mem) {
        return cpu + "vCPU" + mem + "GB";
    }

    public static F2CHost toF2CHost(Host host, List<Qemu> qemuList) {
        F2CHost f2CHost = new F2CHost();
        f2CHost.setHostId(host.getClusterNode().getName());
        f2CHost.setHostIp(host.getClusterNode().getIp());
        f2CHost.setHostName(host.getClusterNode().getName());
        f2CHost.setClusterName(Objects.isNull(host.getCluster()) ? null : host.getCluster().getName());
        f2CHost.setClusterId(Objects.isNull(host.getCluster()) ? null : host.getCluster().getName());
        DefaultKeyValue<Integer, String> cpuMHzPerOneCore = getCpuMHzPerOneCore(host.getNodeStatus());
        f2CHost.setCpuMHzPerOneCore(cpuMHzPerOneCore.getKey());
        f2CHost.setCpuModel(cpuMHzPerOneCore.getValue());
        f2CHost.setRegion(host.getClusterNode().getName());
        long cpuTotal = (long) host.getNode().getMaxcpu() * cpuMHzPerOneCore.getKey();
        long cpuUsed = host.getNode().getCpu().multiply(BigDecimal.valueOf(cpuTotal)).longValue();
        f2CHost.setCpuMHzTotal(cpuTotal);
        f2CHost.setCpuMHzUsed(cpuUsed);
        f2CHost.setCpuMHzAllocated(cpuTotal - cpuUsed);
        long memTotal = host.getNode().getMaxmem();
        long memUsed = host.getNode().getMem();
        f2CHost.setMemoryUsed(memUsed);
        f2CHost.setMemoryTotal(memTotal);
        f2CHost.setMemoryAllocated(memTotal - memUsed);
        f2CHost.setStatus(host.getNode().getStatus());
        f2CHost.setNumCpuCores(host.getNode().getMaxcpu());
        int sum = qemuList.stream().mapToInt(Qemu::getCpus).sum();
        f2CHost.setVmCpuCores(sum);
        f2CHost.setVmRunning(qemuList.stream().filter(qemu -> StringUtil.equals(qemu.getStatus(), "running")).count());
        f2CHost.setVmStopped(qemuList.stream().filter(qemu -> StringUtil.equals(qemu.getStatus(), "stopped")).count());
        f2CHost.setVmTotal(qemuList.size());
        f2CHost.setZone(Objects.nonNull(host.getCluster()) ? host.getCluster().getName() : null);
        return f2CHost;
    }


    public static DefaultKeyValue<Integer, String> getCpuMHzPerOneCore(NodeStatus nodeStatus) {
        if (Objects.isNull(nodeStatus) || Objects.isNull(nodeStatus.getCpuinfo()) || Objects.isNull(nodeStatus.getCpuinfo().getMhz())) {
            return new DefaultKeyValue<>(1000, "未知");
        }
        return new DefaultKeyValue<>(nodeStatus.getCpuinfo().getMhz().intValue(), nodeStatus.getCpuinfo().getModel());
    }


    public static F2CDisk toF2CDisk(DiskStatusInfo diskStatusInfo, Disk disk, String node) {
        F2CDisk f2CDisk = new F2CDisk();
        String diskId = toInstanceId(disk.getVolid(), diskStatusInfo.getVmGenId());
        String instanceId = toVmId(disk.getVmId().toString(), diskStatusInfo.getVmGenId());
        f2CDisk.setDiskId(diskId);
        f2CDisk.setCreateTime(Objects.isNull(disk.getCtime()) ? System.currentTimeMillis() : disk.getCtime() * 1000);
        f2CDisk.setDiskMode(disk.getContent());
        f2CDisk.setStatus(diskStatusInfo.isUsed() ? F2CDiskStatus.IN_USE : F2CDiskStatus.AVAILABLE);
        f2CDisk.setBootable(diskStatusInfo.isBoot());
        f2CDisk.setDiskName(disk.getDiskName());
        f2CDisk.setInstanceName(diskStatusInfo.getQemu().getName());
        f2CDisk.setInstanceUuid(instanceId);
        f2CDisk.setSize((long) Math.ceil(disk.getSize() / (1024 * 1024 * 1024.0)));
        f2CDisk.setDatastoreName(disk.getStorage());
        f2CDisk.setDatastoreUniqueId(disk.getStorage());
        f2CDisk.setDeleteWithInstance("YES");
        f2CDisk.setRegion(node);
        f2CDisk.setDiskType(disk.getFormat());
        f2CDisk.setDevice(diskStatusInfo.getDevice());
        f2CDisk.setZone(node);
        return f2CDisk;
    }

    public static <T> T ofError(Supplier<T> supplier, String errorMessage) {
        try {
            return supplier.get();
        } catch (Exception e) {
            LogUtil.error(StringUtil.isEmpty(errorMessage) ? e.getMessage() : errorMessage + "[" + e.getMessage() + "]");
            return null;
        }
    }
}
