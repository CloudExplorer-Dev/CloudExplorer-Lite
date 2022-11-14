package com.fit2cloud.provider.impl.vsphere.util;

import com.fit2cloud.common.provider.impl.vsphere.utils.VsphereClient;
import com.fit2cloud.provider.constants.DeleteWithInstance;
import com.fit2cloud.provider.constants.F2CDiskStatus;
import com.fit2cloud.provider.constants.F2CInstanceStatus;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CHost;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.impl.vsphere.entity.F2CVsphereDatastore;
import com.fit2cloud.provider.impl.vsphere.entity.F2CVsphereHost;
import com.fit2cloud.provider.impl.vsphere.entity.VsphereFolder;
import com.fit2cloud.provider.impl.vsphere.entity.constants.VsphereDiskType;
import com.fit2cloud.provider.impl.vsphere.entity.request.VsphereDiskRequest;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.*;

@Slf4j
public class VsphereUtil {
    private static final long MB = 1024 * 1024;
    private static final long GB = MB * 1024;

    public static F2CVirtualMachine toF2CInstance(VirtualMachine vm, VsphereVmClient client) {
        return toF2CInstance(vm, client, null);
    }

    /**
     * 将 vsphere 云主机对象转换为 F2C 云管云主机对象
     *
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
            log.error(ExceptionUtils.getStackTrace(e));
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
            log.error(ExceptionUtils.getStackTrace(e));
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
            instance.setVmtoolsStatus(vmGuest.getToolsRunningStatus());
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
            String hostMorVal = null;
            if (hostCache == null) {
                f2cVsphereHost = getF2CVsphereHost(client, vm);
            } else {
                hostMorVal = runtime.getHost().getVal();
                f2cVsphereHost = hostCache.get(hostMorVal);
            }
            if (f2cVsphereHost != null) {
                instance.setHostId(hostMorVal);
                instance.setHost(f2cVsphereHost.getHostName());
                instance.setDataCenter(f2cVsphereHost.getDataCenterName());
                instance.setRegion(f2cVsphereHost.getDataCenterName());
                instance.setCluster(f2cVsphereHost.getClusterName());
                instance.setZone(f2cVsphereHost.getClusterName());
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

        try {
            ResourcePool pool = vm.getResourcePool();
            if (pool != null) {
                instance.setResourcePoolId(pool.getMOR().getVal());
                instance.setResourcePool(pool.getName());
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

        return instance;
    }

    /**
     * 将 vsphere 磁盘对象转为 F2C 云管磁盘对象
     *
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
            // vc 账号权限不够导致云主机关联的存储器无法获取
            if (datastore == null) {
                String errorMessage = "Cannot get vm[" + vm.getName() + "]'s datastore";
                log.error(errorMessage);
            } else {
                d.setDatastoreUniqueId(datastoreMorVal);
                d.setDatastoreName(datastore.getDatastoreName());
            }
        }

        if (hostFromCache.getClusterName() != null) {
            d.setZone(hostFromCache.getClusterName());
        }
        d.setDeleteWithInstance(DeleteWithInstance.YES.name());
        return d;
    }

    /**
     * 将 vsphere 宿主机对象转为 F2C 云管宿主机对象
     *
     * @param hs
     * @param client
     * @return
     * @throws Exception
     */
    public static F2CHost toF2CHost(HostSystem hs, VsphereClient client) throws Exception {
        HostSystemInfo hostSystemInfo = hs.getHardware().getSystemInfo();
        String hostModel = hostSystemInfo.getModel();
        String hostVendor = hostSystemInfo.getVendor();
        HostListSummary summary = hs.getSummary();
        HostHardwareSummary hostHw = summary.getHardware();
        long totalMemory = hostHw.getMemorySize() / MB;
        int cpuMHzOneCore = hostHw.getCpuMhz();
        int numCpuCores = hostHw.getNumCpuCores();
        String cpuModel = hostHw.getCpuModel();
        long totalCpu = cpuMHzOneCore * numCpuCores;
        long totalUsedCpu = 0;
        long totalUsedMemory = 0;
        try {
            HostListSummaryQuickStats quickStats = summary.getQuickStats();
            totalUsedCpu = quickStats.getOverallCpuUsage();
            totalUsedMemory = quickStats.getOverallMemoryUsage();
        } catch (Exception e) {
            //unknown host
        }
        VirtualMachine[] vms = hs.getVms();
        long vmRunning = 0;
        long vmStopped = 0;
        long vmTotal = 0;
        int vmCpuCores = 0;
        if (vms != null) { //todo 后面可以替换掉，不同步所有机器
            for (VirtualMachine vm : vms) {
                VirtualMachineConfigInfo vmConfig = vm.getConfig();
                if (vmConfig != null && vmConfig.isTemplate()) {
                    continue;
                }
                vmTotal++;
                VirtualMachineRuntimeInfo vmRuntime = vm.getRuntime();
                if (vmRuntime != null) {
                    String vmStatus = VsphereUtil.getStatus(vmRuntime.getPowerState().name()).name();
                    if ("running".equalsIgnoreCase(vmStatus)) {
                        vmRunning++;
                    } else {
                        if ("stopped".equalsIgnoreCase(vmStatus)) {
                            vmStopped++;
                        }
                        continue;
                    }
                }
                VirtualMachineSummary vmSummary = vm.getSummary();
                if (vmSummary != null) {
                    VirtualMachineConfigSummary vmConf = vmSummary.getConfig();
                    if (vmConf != null) {
                        vmCpuCores += vmConf.getNumCpu();
                    }
                }
            }
        }
        AboutInfo product = summary.getConfig().getProduct();
        F2CHost f2cHost = new F2CHost();
        f2cHost.setHypervisorType(product.getName());
        f2cHost.setHypervisorVersion(product.getVersion());
        f2cHost.setHostModel(hostModel);
        f2cHost.setHostVendor(hostVendor);
        f2cHost.setCpuModel(cpuModel);
        f2cHost.setCpuMHzPerOneCore(cpuMHzOneCore);
        f2cHost.setNumCpuCores(numCpuCores);
        f2cHost.setVmCpuCores(vmCpuCores);

        ComputeResource resource = client.getComputeResource(hs);
        if (resource instanceof ClusterComputeResource) {
            ClusterComputeResource cluster = (ClusterComputeResource) resource;
            f2cHost.setClusterId(cluster.getMOR().getVal());
            f2cHost.setClusterName(cluster.getName());
        }

        f2cHost.setCpuMHzAllocated(totalUsedCpu);
        f2cHost.setCpuMHzTotal(totalCpu);

        Datacenter dc = client.getDataCenter(hs);
        f2cHost.setDataCenterId(dc.getMOR().getVal());
        f2cHost.setDataCenterName(dc.getName());

        f2cHost.setHostId(hs.getMOR().getVal());
        f2cHost.setHostName(hs.getName());

        f2cHost.setMemoryAllocated(totalUsedMemory);
        f2cHost.setMemoryTotal(totalMemory);

        f2cHost.setStatus(hs.getRuntime().getPowerState().name());
        f2cHost.setVmRunning(vmRunning);
        f2cHost.setVmStopped(vmStopped);
        f2cHost.setVmTotal(vmTotal);

        HostConfigInfo hsConfig = hs.getConfig();
        if (hsConfig != null && hsConfig.getVirtualNicManagerInfo() != null) {
            VirtualNicManagerNetConfig[] config = hsConfig.getVirtualNicManagerInfo().getNetConfig();
            if (config != null) {
                for (VirtualNicManagerNetConfig managerNetConfig : config) {
                    if (managerNetConfig.getNicType() != null && StringUtils.equals("management", managerNetConfig.getNicType())) {
                        String selectedVnic = managerNetConfig.getSelectedVnic()[0];
                        HostVirtualNic[] hostVirtualNics = managerNetConfig.getCandidateVnic();
                        for (HostVirtualNic vnic : hostVirtualNics) {
                            HostVirtualNicSpec vnicSpec = vnic.getSpec();
                            if (vnic.getKey().equalsIgnoreCase(selectedVnic) && vnicSpec != null
                                    && vnicSpec.getIp() != null) {
                                f2cHost.setHostIp(vnicSpec.getIp().getIpAddress());
                            }
                        }
                    }
                }
            }
        }
        return f2cHost;
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
                d.setDiskType(VsphereDiskType.THIN.name());
            } else if (Boolean.TRUE.equals(virtualDiskFlatVer2BackingInfo.getEagerlyScrub())) {// 后置备（快速）置零
                d.setDiskType(VsphereDiskType.THICK_EAGER_ZEROED.name());
            } else { // 后置备延迟置零
                d.setDiskType(VsphereDiskType.THICK_LAZY_ZEROED.name());
            }
            d.setDevice(virtualDiskFlatVer2BackingInfo.getFileName());
            d.setDiskMode(virtualDiskFlatVer2BackingInfo.getDiskMode());
        } else if (backing instanceof VirtualDiskSparseVer2BackingInfo) {
            d.setDiskType(VsphereDiskType.SPARSE.name());
            d.setDevice(((VirtualDiskSparseVer2BackingInfo) backing).getFileName());
            d.setDiskMode(((VirtualDiskSparseVer2BackingInfo) backing).getDiskMode());
        } else {
            d.setDiskType(VsphereDiskType.NA.name());
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
     * 获取云主机备注信息
     *
     * @param vmConfig 云主机信息
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
     * 获取云主机状态
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
     * 计算云主机磁盘大小
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

    public static List<VirtualDisk> getTemplateDisks(VsphereVmClient client, String name) {
        List<VirtualDisk> disks = new ArrayList<>();
        try {
            VirtualMachine template = client.getTemplateFromAll(name);
            VirtualMachineConfigInfo templateConfig = template.getConfig();
            VirtualDevice[] devices = templateConfig.getHardware().getDevice();
            for (VirtualDevice device : devices) {
                if (device instanceof VirtualDisk) {
                    disks.add((VirtualDisk) device);
                }
            }
        } catch (Exception ignored) {
        }
        return disks;
    }


    /**
     * 计算模板磁盘大小
     *
     * @return 单位（GB）
     */
    public static long getTemplateDiskSizeInGB(List<VirtualDisk> devices) {
        long diskSum = 0;
        for (VirtualDisk device : devices) {
            long capacityInKB = device.getCapacityInKB();
            diskSum = diskSum + capacityInKB / MB;

        }
        return diskSum;
    }

    public static List<F2CDisk> getVmDisks(VsphereClient client, VsphereDiskRequest request) {
        VirtualMachine vm = client.getVirtualMachineByUuId(request.getInstanceUuid());
        if (vm == null) {
            return null;
        }
        List<VirtualDisk> diskList = client.getVirtualDisks(vm);
        List<F2CDisk> disks = new ArrayList<F2CDisk>();
        ServerConnection serverConnection = client.getSi().getServerConnection();
        if (diskList != null && diskList.size() > 0) {
            for (VirtualDisk disk : diskList) {
                F2CDisk d = new F2CDisk();
                String tmpDiskId = vm.getMOR().getVal() + "-" + Integer.toString(disk.getKey());
                d.setDiskId(tmpDiskId);
                Description info = disk.getDeviceInfo();
                if (info != null) {
                    d.setDiskName(info.getLabel());
                }

                VirtualMachineConfigInfo config = vm.getConfig();
                d.setInstanceUuid(config.getInstanceUuid());
                d.setInstanceName(vm.getName());
                if (client.getDataCenter(vm) == null) {
                    d.setRegion("N/A");
                } else {
                    d.setRegion(client.getDataCenter(vm).getName());
                }
                d.setSize(disk.getCapacityInKB() / MB);
                d.setStatus(F2CDiskStatus.IN_USE);

                VirtualDeviceBackingInfo backing = disk.backing;
                convertDiskType(d, backing);

                if (backing instanceof VirtualDeviceFileBackingInfo) {
                    ManagedObjectReference datastoreMor = ((VirtualDeviceFileBackingInfo) backing).getDatastore();
                    Datastore datastore = new Datastore(serverConnection, datastoreMor);
                    d.setDatastoreName(datastore.getName());
                    d.setDatastoreUniqueId(datastoreMor.getVal());
                }

                HostSystem host = client.getHost(vm);
                if (host != null) {
                    d.setZone(host.getName());
                }
                d.setDeleteWithInstance(DeleteWithInstance.YES.name());
                disks.add(d);
            }
        }
        return disks;
    }

    public static List<VsphereFolder> getChildFolders(VsphereClient client, Folder folder, String parent) {
        List<VsphereFolder> result = new ArrayList<>();
        Folder[] subFolders = client.getChildResource(Folder.class, folder);
        if (!"".equals(parent)) {
            parent = parent + "/";
        }
        VsphereFolder root = new VsphereFolder().setMor(folder.getMOR().getVal()).setName(parent + folder.getName());
        result.add(root);
        if (subFolders != null) {
            for (Folder childFolder : subFolders) {
                if (!StringUtils.equals(childFolder.getMOR().getVal(), folder.getMOR().getVal()) &&
                        StringUtils.equals(childFolder.getParent().getMOR().getVal(), folder.getMOR().getVal())
                ) {
                    result.addAll(getChildFolders(client, childFolder, parent + folder.getName()));
                }
            }
        }
        return result;
    }

    public static List<VirtualEthernetCard> getVirtualEthernetCardsByVm(VirtualMachine virtualMachine) {
        VirtualDevice[] virtualDevices = virtualMachine.getConfig().getHardware().getDevice();
        List<VirtualEthernetCard> virtualEthernetCards = new ArrayList<>();
        for (VirtualDevice virtualDevice : virtualDevices) {
            if (virtualDevice instanceof VirtualEthernetCard) {
                virtualEthernetCards.add((VirtualEthernetCard) virtualDevice);
            }
        }
        return virtualEthernetCards;
    }

    public static boolean validateFolder(VsphereClient client, String folderName) {
        if (StringUtils.isNotBlank(folderName)) {
            folderName = StringUtils.strip(folderName, "/");
            Folder folder = client.getFolder(folderName);
            return folder != null;
        }
        return false;
    }
}
