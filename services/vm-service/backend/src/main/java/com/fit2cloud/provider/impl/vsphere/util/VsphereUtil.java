package com.fit2cloud.provider.impl.vsphere.util;

import com.fit2cloud.common.platform.vsphere.utils.VsphereClient;
import com.fit2cloud.provider.constants.DeleteWithInstance;
import com.fit2cloud.provider.constants.F2CDiskStatus;
import com.fit2cloud.provider.constants.F2CInstanceStatus;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.impl.vsphere.entity.F2CVsphereDiskType;
import com.fit2cloud.provider.impl.vsphere.entity.F2CVsphereDatastore;
import com.fit2cloud.provider.impl.vsphere.entity.F2CVsphereHost;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class VsphereUtil {
    private static final long MB = 1024 * 1024;
    private static final long GB = MB * 1024;
    private static Logger logger = LoggerFactory.getLogger(VsphereUtil.class);

    public static F2CVirtualMachine toF2CInstance(VirtualMachine vm, VsphereVmClient client) {
        return toF2CInstance(vm, client, null);
    }

    /**
     * 将 vsphere 虚拟机对象转换为 F2C 云管虚拟机对象
     * @param vm
     * @param client
     * @param hostCache
     * @return
     */
    public static F2CVirtualMachine toF2CInstance(VirtualMachine vm, VsphereVmClient client, Map<String, F2CVsphereHost> hostCache) {
        if (vm == null) {
            return null;
        }

        VirtualMachineConfigInfo vmConfig = vm.getConfig();
        Optional.ofNullable(vmConfig).orElseThrow(() -> new RuntimeException("Virtual Machine config not exists, vmName: " + vm.getName()));

        F2CVirtualMachine instance = new F2CVirtualMachine();
        ManagedObjectReference mor = vm.getMOR();
        VirtualMachineRuntimeInfo runtime = vm.getRuntime();
        Date date = client.getVmCreateDate(mor, runtime);
        Optional.ofNullable(date).ifPresent(theDate -> {
            instance.setCreated(theDate);
            instance.setCreateTime(theDate.getTime());
        });
        Optional.ofNullable(runtime).ifPresent(theRuntime -> {
            instance.setInstanceStatus(getStatus(theRuntime.getPowerState().name()).name());
        });

        try {
            String annotation = vmConfig.getAnnotation();
            if (annotation != null && annotation.startsWith("Created-by-FIT2CLOUD-from-template:")) {
                instance.setImageId(annotation.substring("Created-by-FIT2CLOUD-from-template:".length()));
            } else {
                instance.setImageId("");
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }

        String vmName = vm.getName();
        instance.setInstanceId(vmName);
        VirtualMachineSummary vmSummary = vm.getSummary();
        VirtualMachineConfigSummary conf = vmSummary.getConfig();
        String instanceTypeDescription = "unknown";
        try {
            if (conf != null) {
                Integer numCpu = conf.getNumCpu();
                int memory = (int) Math.ceil((double) conf.getMemorySizeMB() / 1024);
                instanceTypeDescription = numCpu + "vCPU " + memory + "GB";
                instance.setMemory(memory);
                instance.setCpu(numCpu);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        instance.setInstanceTypeDescription(instanceTypeDescription);
        instance.setInstanceType(instanceTypeDescription);

        // disk
        int diskSizeInGB = getDiskSizeInGB(vmConfig);
        instance.setDisk(diskSizeInGB);

        List<String> ipArray = new ArrayList<>();
        // ipv6的地址也要
        GuestInfo vmGuest = vm.getGuest();
        if (vmGuest != null) {
            GuestNicInfo[] nets = vmGuest.getNet();
            if (nets != null && nets.length > 0) {
                for (GuestNicInfo nicInfo : nets) {
                    String[] ips = nicInfo.getIpAddress();
                    String macAddress = nicInfo.getMacAddress();
                    if (macAddress != null) {
                        instance.setMacAddress(macAddress);
                    }
                    if (ips != null && ips.length > 0) {
                        for (String ip : ips) {
                            if (ip.contains(".")) {
                                instance.setLocalIP(ip);
                                ipArray.add(ip);
                            } else if (ip.contains(":")) {
                                instance.setLocalIPV6(ip);
                                ipArray.add(ip);
                            }
                        }
                    }
                }
            }
            instance.setVmtoolsVersion(vmGuest.getToolsVersion());
            instance.setHostname(vmGuest.hostName);
        }
        instance.setIpArray(ipArray);
        instance.setInstanceUUID(vmConfig.getInstanceUuid());

        VirtualMachineQuickStats stats = vmSummary.getQuickStats();
        if (stats != null) {
            Integer cpuUsage = stats.getOverallCpuUsage();
            instance.setCpuUsed(cpuUsage == null ? 0 : cpuUsage);
            Integer memoryUsed = stats.getGuestMemoryUsage();
            instance.setMemoryUsed(memoryUsed == null ? 0 : memoryUsed);
        }
        instance.setDiskUsed(getDiskUsed(vmSummary));
        try {
            Datastore[] dataStores = vm.getDatastores();
            if (dataStores != null && dataStores.length > 0) {
                instance.setDatastoreName(dataStores[0].getName());
                instance.setDatastoreType(dataStores[0].getSummary().getType());
            }
        } catch (Exception e1) {
        }
        instance.setRemark(getVmRemark(vmConfig));
        String name = StringUtils.isEmpty(vmName) ? "" : vmName;
        if (name.length() > 64) {
            name = name.substring(0, 61) + "...";
        }
        instance.setName(name);
        String os = "unknown";
        try {
            os = vmConfig.getGuestFullName();
        } catch (Exception e) {
        }
        instance.setOsInfo(os);
        try {
            F2CVsphereHost f2cVsphereHost;
            if (hostCache == null) {
                f2cVsphereHost = getF2CVsphereHost(client, vm);
            } else {
                String hostMorVal = runtime.getHost().getVal();
                f2cVsphereHost = hostCache.get(hostMorVal);
            }
            if (f2cVsphereHost != null) {
                instance.setHost(f2cVsphereHost.getHostName());
                instance.setDataCenter(f2cVsphereHost.getDataCenterName());
                instance.setRegion(f2cVsphereHost.getDataCenterName());
                instance.setCluster(f2cVsphereHost.getClusterName());
                instance.setZone(f2cVsphereHost.getClusterName());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return instance;
    }

    /**
     * 将 vsphere 磁盘对象转为 F2C 云管磁盘对象
     * @param vm
     * @param disk
     * @param hostSystemMorVal
     * @param hostCache
     * @param datastoreCache
     * @return
     * @throws Exception
     */
    public static F2CDisk toF2CDisk(VirtualMachine vm, VirtualDisk disk, String hostSystemMorVal, Map<String, F2CVsphereHost> hostCache, Map<String, F2CVsphereDatastore> datastoreCache) throws Exception {
        F2CDisk d = new F2CDisk();
        String tmpDiskId = vm.getMOR().getVal() + "-" + (disk.getKey());
        d.setDiskId(tmpDiskId);  //disk.getKey() property is a unique key that distinguishes this device from other devices in the same virtual machine.
        Description info = disk.getDeviceInfo();
        if (info != null) {
            d.setDiskName(info.getLabel());
        }

        F2CVsphereHost hostFromCache = hostCache.get(hostSystemMorVal);
        VirtualMachineConfigInfo config = vm.getConfig();
        d.setInstanceUuid(config.getInstanceUuid());
        d.setRegion(Optional.ofNullable(hostFromCache.getDataCenterName()).orElse("N/A"));
        d.setSize(disk.getCapacityInKB() / MB);
        d.setStatus(F2CDiskStatus.IN_USE);

        VirtualDeviceBackingInfo backing = disk.backing;
        convertDiskType(d, backing);

        if (backing instanceof VirtualDeviceFileBackingInfo) {
            ManagedObjectReference datastoreMor = ((VirtualDeviceFileBackingInfo) backing).getDatastore();
            String datastoreMorVal = datastoreMor.getVal();
            F2CVsphereDatastore datastore = datastoreCache.get(datastoreMorVal);
            // vc 账号权限不够导致虚拟机关联的存储器无法获取
            if (datastore == null) {
                String errorMessage = "Cannot get vm[" + vm.getName() + "]'s datastore";
                throw new Exception(errorMessage);
            }
            d.setDatastoreUniqueId(datastoreMorVal);
            d.setDatastoreName(datastore.getDatastoreName());
        }

        if (hostFromCache.getClusterName() != null) {
            d.setZone(hostFromCache.getClusterName());
        }
        d.setDeleteWithInstance(DeleteWithInstance.YES.name());
        return d;
    }

    /**
     * 设置磁盘类型
     *
     * @param d
     * @param backing
     */
    private static void convertDiskType(F2CDisk d, VirtualDeviceBackingInfo backing) {
        if (backing instanceof VirtualDiskFlatVer2BackingInfo) {
            VirtualDiskFlatVer2BackingInfo virtualDiskFlatVer2BackingInfo = (VirtualDiskFlatVer2BackingInfo) backing;
            if (Boolean.TRUE.equals(virtualDiskFlatVer2BackingInfo.getThinProvisioned())) {// 精简置备
                d.setDiskType(F2CVsphereDiskType.THIN.name());
            } else if (Boolean.TRUE.equals(virtualDiskFlatVer2BackingInfo.getEagerlyScrub())) {// 后置备（快速）置零
                d.setDiskType(F2CVsphereDiskType.THICK_EAGER_ZEROED.name());
            } else { // 后置备延迟置零
                d.setDiskType(F2CVsphereDiskType.THICK_LAZY_ZEROED.name());
            }
            d.setDevice(virtualDiskFlatVer2BackingInfo.getFileName());
            d.setDiskMode(virtualDiskFlatVer2BackingInfo.getDiskMode());
        } else if (backing instanceof VirtualDiskSparseVer2BackingInfo) {
            d.setDiskType(F2CVsphereDiskType.SPARSE.name());
            d.setDevice(((VirtualDiskSparseVer2BackingInfo) backing).getFileName());
            d.setDiskMode(((VirtualDiskSparseVer2BackingInfo) backing).getDiskMode());
        } else {
            d.setDiskType(F2CVsphereDiskType.DEFAULT.name());
        }
    }

    public static F2CVsphereHost getF2CVsphereHost(VsphereClient client, VirtualMachine vm) {
        HostSystem host = client.getHost(vm);
        return transferHostSystem2F2CVsphereHost(client, host);
    }

    /**
     * 将 vsphere 宿主机对象转为 F2C 云管宿主机对象
     *
     * @param client
     * @param hostSystem
     * @return
     */
    private static F2CVsphereHost transferHostSystem2F2CVsphereHost(VsphereClient client, HostSystem hostSystem) {
        F2CVsphereHost host = new F2CVsphereHost();
        host.setHostMorVal(hostSystem.getMOR().getVal());
        host.setHostName(hostSystem.getName());

        ComputeResource resource = client.getComputeResource(hostSystem);
        if (resource instanceof ClusterComputeResource) {
            ClusterComputeResource cluster = (ClusterComputeResource) resource;
            host.setClusterName(cluster.getName());
        }
        Datacenter datacenter = client.getDataCenter(hostSystem);
        if (datacenter != null) {
            host.setDataCenterName(datacenter.getName());
        }
        return host;
    }

    /**
     * 将 vsphere 存储器对象转为 F2C 云管存储器对象
     *
     * @param datastore
     * @return
     */
    private static F2CVsphereDatastore transferDatastore2F2CVsphereDatastore(Datastore datastore) {
        F2CVsphereDatastore f2CVsphereDatastore = new F2CVsphereDatastore();
        f2CVsphereDatastore.setDatastoreName(datastore.getName());
        f2CVsphereDatastore.setDatastoreMorVal(datastore.getMOR().getVal());
        return f2CVsphereDatastore;
    }

    /**
     * 宿主机数据缓存
     *
     * @param client
     * @return
     */
    public static Map<String, F2CVsphereHost> generateHostCache(VsphereClient client) {
        Map<String, F2CVsphereHost> hostSystemCache = new HashMap<>();
        List<HostSystem> hostSystemList = client.listHostsFromAll();
        for (HostSystem hostSystem : hostSystemList) {
            F2CVsphereHost f2CVsphereHost = transferHostSystem2F2CVsphereHost(client, hostSystem);
            hostSystemCache.put(f2CVsphereHost.getHostMorVal(), f2CVsphereHost);
        }
        return hostSystemCache;
    }

    /**
     * 存储器数据缓存
     *
     * @param client
     * @return
     */
    public static Map<String, F2CVsphereDatastore> generateDatastoreCache(VsphereClient client) {
        Map<String, F2CVsphereDatastore> datastoreCache = new HashMap<>();
        List<Datastore> datastoreList = client.listDataStores();
        for (Datastore datastore : datastoreList) {
            F2CVsphereDatastore f2CVsphereDatastore = transferDatastore2F2CVsphereDatastore(datastore);
            datastoreCache.put(f2CVsphereDatastore.getDatastoreMorVal(), f2CVsphereDatastore);
        }
        return datastoreCache;
    }

    /**
     * 获取虚拟机备注信息
     *
     * @param vmConfig 虚拟机信息
     * @return 备注信息
     */
    private static String getVmRemark(VirtualMachineConfigInfo vmConfig) {
        String annotation = vmConfig.getAnnotation();
        if (StringUtils.isNotEmpty(annotation)) {
            return annotation.length() > 100 ? annotation.substring(0, 100) + "..." : annotation;
        }
        return "";
    }

    /**
     * 获取虚拟机状态
     *
     * @param powerState
     * @return
     */
    public static F2CInstanceStatus getStatus(String powerState) {
        if (powerState != null) {
            if ("poweredOff".equals(powerState) || "suspended".equals(powerState)) {
                return F2CInstanceStatus.Stopped;
            } else if ("poweredOn".equals(powerState)) {
                return F2CInstanceStatus.Running;
            }
        }
        return F2CInstanceStatus.Unknown;
    }

    /**
     * 计算虚拟机磁盘大小
     *
     * @param vmConfig
     * @return 单位（GB）
     */
    public static int getDiskSizeInGB(VirtualMachineConfigInfo vmConfig) {
        int diskSizeInGB = 0;
        VirtualHardware hardware = vmConfig.getHardware();
        if (hardware != null) {
            VirtualDevice[] devices = hardware.getDevice();
            if (devices != null && devices.length > 0) {
                for (VirtualDevice device : devices) {
                    if (device instanceof VirtualDisk) {
                        VirtualDisk vd = (VirtualDisk) device;
                        diskSizeInGB += vd.getCapacityInKB() / MB;
                    }
                }
            }
        }
        return diskSizeInGB;
    }

    /**
     * 计算磁盘使用量
     *
     * @param vmSummary
     * @return 单位（GB）
     */
    public static long getDiskUsed(VirtualMachineSummary vmSummary) {
        VirtualMachineStorageSummary storage = vmSummary.getStorage();
        if (storage != null) {
            long diskUsed = storage.getCommitted();
            return diskUsed / GB;
        }
        return 0;
    }

    /**
     * 计算模板磁盘大小
     *
     * @param client
     * @param name
     * @return 单位（GB）
     */
    public static long getTemplateDiskSizeInGB(VsphereVmClient client, String name) {
        long diskSum = 0;
        try {
            VirtualMachine template = client.getTemplateFromAll(name);
            VirtualMachineConfigInfo templateConfig = template.getConfig();
            VirtualDevice[] devices = templateConfig.getHardware().getDevice();
            for (VirtualDevice device : devices) {
                if (device instanceof VirtualDisk) {
                    VirtualDisk vd = (VirtualDisk) device;
                    long capacityInKB = vd.getCapacityInKB();
                    diskSum = diskSum + capacityInKB / MB;
                }
            }
        } catch (Exception ignored) {

        }
        return diskSum;
    }
}
