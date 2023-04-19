package com.fit2cloud.provider.impl.vsphere.util;

import com.fit2cloud.common.provider.impl.vsphere.utils.VsphereClient;
import com.fit2cloud.provider.constants.F2CInstanceStatus;
import com.fit2cloud.provider.entity.response.F2CHost;
import com.fit2cloud.provider.entity.response.F2CVirtualMachine;
import com.fit2cloud.provider.impl.vsphere.entity.response.F2CVsphereHost;
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
     * @param vm        虚拟机对象
     * @param client    vm客户端
     * @param hostCache 用于存储host宿主机信息
     * @return 虚拟机信息
     */
    public static F2CVirtualMachine toF2CInstance(VirtualMachine vm, VsphereVmClient client, Map<String, F2CVsphereHost> hostCache) {
        if (Objects.isNull(vm)) {
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
            instance.setVmToolsVersion(vmGuest.getToolsVersion());
            instance.setVmToolsStatus(vmGuest.getToolsRunningStatus());
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
     * 将 vsphere 宿主机对象转为 F2C 云管宿主机对象
     *
     * @param hs     宿主机
     * @param client vc客户端
     * @return 系统宿主机对象
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
            f2cHost.setZone(cluster.getName());
        }

        f2cHost.setCpuMHzAllocated(totalUsedCpu);
        f2cHost.setCpuMHzTotal(totalCpu);

        Datacenter dc = client.getDataCenter(hs);
        f2cHost.setDataCenterId(dc.getName());
        f2cHost.setDataCenterName(dc.getName());
        f2cHost.setRegion(dc.getName());

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
     * 获取虚拟机的宿主机
     *
     * @param client 客户端
     * @param vm     虚拟机
     * @return 虚拟机对应的客户端
     */
    public static F2CVsphereHost getF2CVsphereHost(VsphereClient client, VirtualMachine vm) {
        HostSystem host = client.getHost(vm);
        return transferHostSystem2F2CVsphereHost(client, host);
    }

    /**
     * 将 vsphere 宿主机对象转为 F2C 云管宿主机对象
     *
     * @param client     vc客户端
     * @param hostSystem 宿主机设置
     * @return 宿主机信息
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
     * @param powerState 云主机状态
     * @return 云主机状态
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
     * @param vmConfig 虚拟机Config
     * @return 单位（GB）
     */
    public static int getDiskSizeInGB(VirtualMachineConfigInfo vmConfig) {
        int diskSizeInGB = 0;
        VirtualHardware hardware = vmConfig.getHardware();
        if (hardware != null) {
            VirtualDevice[] devices = hardware.getDevice();
            if (devices != null && devices.length > 0) {
                long l = Arrays.stream(devices)
                        .filter(d -> d instanceof VirtualDisk)
                        .map(d -> (VirtualDisk) d)
                        .mapToLong(VirtualDisk::getCapacityInKB)
                        .sum();
                long mb = l / MB;
                return (int) mb;
            }
        }
        return diskSizeInGB;
    }

    /**
     * 计算磁盘使用量
     *
     * @param vmSummary 虚拟机存储数据
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
     * 宿主机数据缓存
     *
     * @param client 客户端
     * @return 宿主机数据 key host value: 宿主机信息
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


}
