package com.fit2cloud.provider.impl.vsphere.api;

import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.platform.credential.impl.VsphereCredential;
import com.fit2cloud.common.provider.entity.F2CEntityType;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.provider.impl.vsphere.utils.VsphereClient;
import com.fit2cloud.common.utils.DateUtil;
import com.fit2cloud.common.utils.IpChecker;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.entity.*;
import com.fit2cloud.provider.entity.request.GetMetricsRequest;
import com.fit2cloud.provider.entity.result.CheckCreateServerResult;
import com.fit2cloud.provider.impl.vsphere.entity.*;
import com.fit2cloud.provider.impl.vsphere.entity.request.*;
import com.fit2cloud.provider.impl.vsphere.util.*;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Author: LiuDi
 * Date: 2022/9/21 2:39 PM
 */
@Slf4j
public class VsphereSyncCloudApi {
    private static final long MB = 1024 * 1024;
    private static final long GB = MB * 1024;

    public static final String VALID_HOST_REGEX = "^[A-Za-z]+[A-Za-z0-9\\.\\-]*[A-Za-z0-9]$";


    /**
     * 获取云主机
     *
     * @param req
     * @return 云主机列表
     */
    public static List<F2CVirtualMachine> listVirtualMachine(VsphereVmBaseRequest req) {
        List<F2CVirtualMachine> list = new ArrayList<>();
        VsphereVmClient client = null;
        try {
            client = req.getVsphereVmClient();
            List<VirtualMachine> vmList = client.listVirtualMachines();

            if (!CollectionUtils.isEmpty(vmList)) {
                Map<String, F2CVsphereHost> hostCache = VsphereUtil.generateHostCache(client);
                for (VirtualMachine vm : vmList) {
                    F2CVirtualMachine instance = VsphereUtil.toF2CInstance(vm, client, hostCache);
                    if (instance != null) {
                        list.add(instance);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            closeConnection(client);
        }
        return list;
    }

    /**
     * 获取镜像
     *
     * @param req
     * @return 镜像列表
     */
    public static List<F2CImage> listImage(VsphereVmBaseRequest req) {
        List<F2CImage> f2CImageList = new ArrayList<>();
        VsphereVmClient client = null;
        try {
            client = req.getVsphereVmClient();
            List<VirtualMachine> templateList = client.listTemplates();
            for (VirtualMachine vm : templateList) {
                VirtualMachineConfigInfo config = vm.getConfig();
                String os = config != null ? config.getGuestFullName() : "linux";
                String desc = config != null ? config.getAnnotation() : "";
                String name = vm.getName();
                String id = vm.getName();
                Datacenter dataCenter = client.getDataCenter(vm);
                String dataCenterName = dataCenter == null ? null : dataCenter.getName();
                List<VirtualDisk> diskList = VsphereUtil.getTemplateDisks(client, name);
                f2CImageList.add(new F2CImage(id, name, desc, os, dataCenterName, VsphereUtil.getTemplateDiskSizeInGB(diskList), null).setDiskInfos(JsonUtil.toJSONString(diskList)));
            }

            f2CImageList.addAll(getContentLibrariesImages(req));
            f2CImageList.sort((o1, o2) -> {
                if (o1 == null) {
                    return -1;
                }
                if (o2 == null) {
                    return 1;
                }
                return o1.getName().compareTo(o2.getName());
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            closeConnection(client);
        }
        return f2CImageList;
    }

    /**
     * 获取内容库镜像
     *
     * @param req
     * @return 内容库镜像列表
     */
    private static List<F2CImage> getContentLibrariesImages(VsphereVmBaseRequest req) {
        List<F2CImage> templates = new ArrayList<>();
        VsphereCredential vsphereCredential = req.getVsphereCredential();
        if (!vsphereCredential.isUseContentLibrary()) {
            return templates;
        }
        try {
            List<F2CImage> images = new ContentLibraryUtil().getImages(req);
            if (!CollectionUtils.isEmpty(images)) {
                templates.addAll(images);
            }
            return templates;
        } catch (Exception e) {
            log.error("[Failed to Get Content Library Image]", e);
        }
        return templates;
    }

    /**
     * 获取磁盘
     *
     * @param req
     * @return 磁盘列表
     */
    public static List<F2CDisk> listDisk(VsphereVmBaseRequest req) {
        List<F2CDisk> f2CDiskList = new ArrayList<>();
        VsphereVmClient client = null;
        try {
            client = req.getVsphereVmClient();
            //TODO 这个地方应该查询区域下的主机，不应该查询所有的，会导致查询磁盘的时候重复
            List<HostSystem> hostSystemList = client.listHosts();
            if (!CollectionUtils.isEmpty(hostSystemList)) {
                Map<String, F2CVsphereHost> hostCache = VsphereUtil.generateHostCache(client);
                Map<String, F2CVsphereDatastore> datastoreCache = VsphereUtil.generateDatastoreCache(client);
                for (HostSystem hostSystem : hostSystemList) {
                    VirtualMachine[] vms = hostSystem.getVms();
                    if (vms != null && vms.length > 0) {
                        for (VirtualMachine vm : vms) {
                            VirtualMachineConfigInfo cfg = vm.getConfig();
                            if (cfg != null && cfg.isTemplate()) {
                                continue;
                            }
                            List<VirtualDisk> diskList = client.getVirtualDisks(vm);
                            if (!CollectionUtils.isEmpty(diskList)) {
                                for (VirtualDisk disk : diskList) {
                                    f2CDiskList.add(VsphereUtil.toF2CDisk(vm, disk, hostSystem.getMOR().getVal(), hostCache, datastoreCache));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            closeConnection(client);
        }
        return f2CDiskList;
    }

    public static List<F2CHost> listHost(VsphereVmBaseRequest req) {
        VsphereClient client = req.getVsphereVmClient();
        List<F2CHost> list;
        try {
            List<HostSystem> hosts = client.listHosts();
            list = new ArrayList<>();
            for (HostSystem hs : hosts) {
                list.add(VsphereUtil.toF2CHost(hs, client));
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        } finally {
            closeConnection(client);
        }
        return list;
    }

    public static List<F2CDatastore> listDataStore(VsphereVmBaseRequest req) {
        List<F2CDatastore> datastoreList = new ArrayList<>();
        VsphereClient client = null;
        try {
            client = req.getVsphereVmClient();
            List<ClusterComputeResource> clusterList = client.listClusters();
            for (ClusterComputeResource cluster : clusterList) {
                ComputeResource cr = client.getComputeResource(cluster.getName());
                if (cr instanceof ClusterComputeResource) {
                    Datastore[] list = cr.getDatastores();
                    if (list != null && list.length > 0) {
                        for (Datastore ds : list) {
                            Datacenter dc = client.getDataCenter(ds);
                            F2CDatastore f2cDs = new F2CDatastore();
                            f2cDs.setDataCenterId(dc.getName());
                            f2cDs.setDataCenterName(dc.getName());
                            f2cDs.setClusterId(cluster.getMOR().getVal());
                            f2cDs.setClusterName(cluster.getName());
                            DatastoreSummary dsSummary = ds.getSummary();
                            f2cDs.setCapacity(dsSummary.getCapacity() / GB);
                            f2cDs.setDataStoreId(ds.getMOR().getVal());
                            f2cDs.setDataStoreName(ds.getName());
                            f2cDs.setFreeSpace(dsSummary.getFreeSpace() / GB);
                            f2cDs.setType(dsSummary.getType());
                            datastoreList.add(f2cDs);
                        }
                    }
                }
            }
            List<Datastore> list = client.listDataStores();
            for (Datastore ds : list) {
                boolean exist = false;
                for (F2CDatastore f2CDataStore : datastoreList) {
                    // TODO 这个地方应该是用ID进行比较，而不是名字
                    if (StringUtils.equals(f2CDataStore.getDataStoreId(), ds.getMOR().getVal())) {
                        exist = true;
                        break;
                    }
                }
                if (exist) {
                    continue;
                }
                Datacenter dc = client.getDataCenter(ds);
                DatastoreSummary summary = ds.getSummary();
                DatastoreInfo info = ds.getInfo();
                F2CDatastore f2cDs = new F2CDatastore();
                f2cDs.setDataCenterId(dc.getName());
                f2cDs.setDataCenterName(dc.getName());
                f2cDs.setCapacity(summary.getCapacity() / GB);
                f2cDs.setDataStoreId(ds.getMOR().getVal());
                f2cDs.setDataStoreName(ds.getName());
                f2cDs.setFreeSpace(summary.getFreeSpace() / GB);
                f2cDs.setType(summary.getType());
                Calendar updated = info.getTimestamp();
                if (updated != null) {
                    f2cDs.setLastUpdate(updated.getTimeInMillis() / 1000);
                }
                datastoreList.add(f2cDs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            closeConnection(client);
        }
        return datastoreList;
    }

    /**
     * 关闭连接
     *
     * @param client
     */
    private static void closeConnection(VsphereClient client) {
        if (client != null) {
            client.closeConnection();
        }
    }

    public static boolean powerOff(VsphereVmPowerRequest req) {
        VsphereVmClient client = req.getVsphereVmClient();
        return operate(req.getUuid(), client::powerOff, client::closeConnection);
    }

    public static boolean powerOn(VsphereVmPowerRequest req) {
        VsphereVmClient client = req.getVsphereVmClient();
        return operate(req.getUuid(), client::powerOn, client::closeConnection);
    }

    public static boolean shutdownInstance(VsphereVmPowerRequest req) {
        VsphereVmClient client = req.getVsphereVmClient();
        return operate(req.getUuid(), client::shutdownInstance, client::closeConnection);
    }

    public static boolean reboot(VsphereVmPowerRequest req) {
        VsphereVmClient client = req.getVsphereVmClient();
        return operate(req.getUuid(), client::reboot, client::closeConnection);
    }

    public static boolean deleteInstance(VsphereVmPowerRequest req) {
        VsphereVmClient client = req.getVsphereVmClient();
        return operate(req.getUuid(), client::deleteInstance, client::closeConnection);
    }

    public static boolean hardReboot(VsphereVmPowerRequest req) {
        VsphereVmClient client = req.getVsphereVmClient();
        return operate(req.getUuid(), client::hardReboot, client::closeConnection);
    }

    private static boolean operate(String uuid, Function<String, Boolean> execMethod, Runnable closeConnection) {
        try {
            return execMethod.apply(uuid);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[Failed to operate virtual machine]", e);
        } finally {
            closeConnection.run();
        }
        return false;
    }

    public static List<Map<String, String>> getLocations(VsphereVmCreateRequest req) {
        List<Map<String, String>> locations = new ArrayList<>();
        Map<String, String> hostMap = new HashMap<>();
        hostMap.put("name", "宿主机");
        hostMap.put("value", "host");
        locations.add(hostMap);
        Map<String, String> poolMap = new HashMap<>();
        poolMap.put("name", "资源池");
        poolMap.put("value", "pool");
        locations.add(poolMap);
        if (isRds(req)) {
            HashMap<String, String> drs = new HashMap<>();
            drs.put("name", "Automatic selection");
            drs.put("value", ResourceConstants.DRS);
            locations.add(drs);
        }
        return locations;
    }

    private static boolean isRds(VsphereVmCreateRequest req) {
        VsphereClient client = null;
        try {
            client = req.getVsphereVmClient();
            String cluster = req.getCluster();
            ClusterComputeResource c = client.getCluster(cluster.trim());
            if (c != null) {
                ClusterDrsConfigInfo drsCfg = c.getConfiguration().getDrsConfig();
                return drsCfg.getEnabled();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<F2CVsphereCluster> getClusters(VsphereVmBaseRequest req) {
        VsphereClient client = null;
        try {
            client = req.getVsphereVmClient();
            List<ClusterComputeResource> list = client.listClusters();
            List<F2CVsphereCluster> clusters = new ArrayList<>();
            for (ClusterComputeResource ccr : list) {
                ClusterDrsConfigInfo drsCfg = ccr.getConfiguration().getDrsConfig();
                StringBuilder sb = new StringBuilder();
                if (drsCfg.getEnabled()) {
                    sb.append("DRS已开启");
                    String defaultVmBehavior = drsCfg.getDefaultVmBehavior().name();
                    switch (defaultVmBehavior) {
                        case "partiallyAutomated":
                            sb.append("[半自动]");
                            break;
                        case "fullyAutomated":
                            sb.append("[全自动]");
                            break;
                        case "manual":
                            sb.append("[手动]");
                            break;
                    }
                } else {
                    sb.append("DRS已关闭");
                }
                clusters.add(new F2CVsphereCluster(ccr.getName(), sb.toString(), ccr.getName()));
            }
            return clusters;
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            if (client != null) {
                client.closeConnection();
            }
        }
    }

    private static List<F2CVsphereNetwork> getClusterNetworks(ClusterComputeResource clusterComputeResource) throws Exception {
        List<F2CVsphereNetwork> networks = new ArrayList<>();
        if (clusterComputeResource != null) {
            HostSystem[] hosts = clusterComputeResource.getHosts();
            if (hosts != null) {
                for (HostSystem host : hosts) {
                    networks.addAll(convertToVsphereNetworks(host));
                }
            }
        }
        return networks;
    }

    private static List<F2CVsphereNetwork> convertToVsphereNetworks(HostSystem hostSystem) throws Exception {
        Network[] hostSystemNetworks = hostSystem.getNetworks();
        ComputeResource cr = (ComputeResource) hostSystem.getParent();
        ConfigTarget configTarget = cr.getEnvironmentBrowser().queryConfigTarget(hostSystem);
        DistributedVirtualPortgroupInfo[] distributedVirtualPortgroups = configTarget.getDistributedVirtualPortgroup();
        List<F2CVsphereNetwork> networks = new ArrayList<>();
        for (Network network : hostSystemNetworks) {
            String desc = "";
            boolean uplinkPortgroup = false;
            if (distributedVirtualPortgroups != null) {
                for (DistributedVirtualPortgroupInfo info : distributedVirtualPortgroups) {
                    if (info.uplinkPortgroup && StringUtils.equals(info.getPortgroupName(), network.getName())) {
                        uplinkPortgroup = true;
                        break;
                    }
                }
            }
            if (!StringUtils.equalsIgnoreCase(network.getMOR().getType(), "network")) {
                desc = "dvSwitch";
            }
            if (!uplinkPortgroup) {
                networks.add(new F2CVsphereNetwork()
                        .setName(network.getName())
                        .setDescription(desc)
                        .setMor(network.getMOR().getVal()));
            }
        }
        return networks;
    }

    public static List<F2CDisk> createDisks(VsphereCreateDisksRequest request) {
        VsphereDiskRequest diskRequest = new VsphereDiskRequest();
        BeanUtils.copyProperties(request, diskRequest);
        VsphereClient client = null;
        try {
            List<String> f2CDiskIds = getVmF2CDisks(diskRequest).stream().map(F2CDisk::getDiskId).collect(Collectors.toList());
            client = diskRequest.getVsphereVmClient();
            editDisk(request, client);
            List<F2CDisk> afterF2CDisks = getVmF2CDisks(diskRequest);
            List<F2CDisk> createF2cDisks = new ArrayList();
            for (F2CDisk afterF2CDisk : afterF2CDisks) {
                if (!f2CDiskIds.contains(afterF2CDisk.getDiskId())) {
                    createF2cDisks.add(afterF2CDisk);
                }
            }
            return createF2cDisks;
        } catch (Exception e) {
            throw new RuntimeException("CreateDisks Error!" + e.getMessage(), e);
        } finally {
            closeConnection(client);
        }
    }

    public static F2CDisk createDisk(VsphereCreateDiskRequest request) {
        F2CDisk disk = new F2CDisk();
        List<F2CDisk> disks = createDisks(request.toVsphereCreateDisksRequest());
        if (CollectionUtils.isNotEmpty(disks)) {
            disk = disks.get(0);
        }
        return disk;
    }

    public static boolean enlargeDisk(VsphereResizeDiskRequest resizeDiskRequest) {
        VsphereDiskRequest diskRequest = new VsphereDiskRequest();
        BeanUtils.copyProperties(resizeDiskRequest, diskRequest);
        List<F2CDisk> f2CDisks = getVmF2CDisks(diskRequest);
        List<F2CDisk> toEnlargeDisks = new ArrayList<>();

        for (F2CDisk f2CDisk : f2CDisks) {
            if (resizeDiskRequest.getDiskId().equalsIgnoreCase(f2CDisk.getDiskId())) {
                if (f2CDisk.getSize() > resizeDiskRequest.getNewDiskSize()) {
                    throw new RuntimeException("Disk size can not be reduced!");
                }
                f2CDisk.setSize(resizeDiskRequest.getNewDiskSize());  // 扩容后的size
                toEnlargeDisks.add(f2CDisk);
            }
        }

        VsphereCreateDisksRequest createDiskRequest = new VsphereCreateDisksRequest();
        BeanUtils.copyProperties(resizeDiskRequest, createDiskRequest);
        createDiskRequest.setDisks(toEnlargeDisks);
        VsphereClient client = createDiskRequest.getVsphereVmClient();
        editDisk(createDiskRequest, client);
        return true;
    }

    public static List<F2CDisk> getVmF2CDisks(VsphereDiskRequest request) {
        VsphereVmClient client = null;
        try {
            client = request.getVsphereVmClient();
            return VsphereUtil.getVmDisks(client, request);
        } catch (Exception e) {
            throw new RuntimeException("GetVmF2CDisks Error!Please check the request parameters.");
        } finally {
            closeConnection(client);
        }
    }

    private static void editDisk(VsphereCreateDisksRequest createDiskRequest, VsphereClient client) {
        try {
            VirtualMachine virtualMachine;
            try {
                virtualMachine = client.getVirtualMachineByUuId(createDiskRequest.getInstanceUuid());
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
            if (virtualMachine == null) {
                throw new RuntimeException("Cannot find virtual machine: " + createDiskRequest.getInstanceUuid());
            }

            List<F2CDisk> disks = createDiskRequest.getDisks();
            if (CollectionUtils.isEmpty(disks)) {
                throw new RuntimeException("Parameter error, please specify at least one disk object");
            }

            try {
                List<VsphereDisk> vsphereDisks = VsphereDiskUtil.toVsphereDisk(disks);
                Map<String, Datastore> datastoreMap = new HashMap<>();
                for (VsphereDisk disk : vsphereDisks) {
                    Datastore datastore;
                    if (disk.getDatastoreName() != null && !"".equals(disk.getDatastoreName()) && !VsphereClient.FLAG_FOR_NULL_VALUE.equals(disk.getDatastoreName())) {
                        datastore = datastoreMap.get(disk.getDatastoreName()) == null ? client.getDatastore(disk.getDatastoreName(), client.getDataCenter(virtualMachine)) : datastoreMap.get(disk.getDatastoreName());
                        if (datastore == null) {
                            throw new RuntimeException("Datastore not found: " + disk.getDatastoreName());
                        }
                        disk.setDatastore(datastore);
                        datastoreMap.put(disk.getDatastoreName(), datastore);
                    } else if (!VsphereClient.FLAG_FOR_NULL_VALUE.equals(disk.getDatastoreName())) {
                        throw new RuntimeException("Datastore not specified");
                    }
                }

                VsphereDiskUtil.createDiskForServer(virtualMachine, vsphereDisks);
            } catch (Exception e) {
                throw new RuntimeException(String.format("Virtual machine: %s ", createDiskRequest.getInstanceUuid()) + " create disk error" + ", message:" + e.getMessage());
            }
        } finally {
            closeConnection(client);
        }
    }

    public static List<VsphereHost> getHosts(VsphereVmCreateRequest request) {
        VsphereVmClient client = null;
        try {
            List<VsphereHost> result = new ArrayList<>();
            List<HostSystem> list;
            client = request.getVsphereVmClient();
            if (StringUtils.isNotBlank(request.getCluster())) {
                ClusterComputeResource cluster = client.getCluster(request.getCluster().trim());
                list = List.of(cluster.getHosts());
            } else {
                list = client.listHosts();
            }
            for (HostSystem hostSystem : list) {
                VsphereHost host = new VsphereHost(hostSystem.getMOR().getVal(), hostSystem.getName());
                //使用情况
                HostListSummary summary = hostSystem.getSummary();
                HostHardwareSummary hostHw = summary.getHardware();

                BigDecimal totalCpu = BigDecimal.valueOf(hostHw.getCpuMhz()).multiply(BigDecimal.valueOf(hostHw.getNumCpuCores())).divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP); //GHz
                BigDecimal totalUsedCpu = BigDecimal.valueOf(summary.getQuickStats().getOverallCpuUsage()).divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP); //GHz
                BigDecimal totalMemory = BigDecimal.valueOf(hostHw.getMemorySize()).divide(BigDecimal.valueOf(GB), 2, RoundingMode.HALF_UP);
                BigDecimal totalUsedMemory = BigDecimal.valueOf(summary.getQuickStats().getOverallMemoryUsage()).divide(BigDecimal.valueOf(1024), 2, RoundingMode.HALF_UP); //直接拿到是MB还要再除1024

                host.setTotalCpu(totalCpu)
                        .setTotalMemory(totalMemory)
                        .setUsedCpu(totalUsedCpu)
                        .setUsedMemory(totalUsedMemory);

                result.add(host);
            }
            result.sort(Comparator.comparing(VsphereHost::getName));
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(client);
        }
    }

    public static List<VsphereResourcePool> geResourcePools(VsphereVmCreateRequest request) {
        VsphereVmClient client = null;
        try {
            List<VsphereResourcePool> list = new ArrayList<>();
            client = request.getVsphereVmClient();
            if (StringUtils.isNotBlank(request.getCluster())) {
                ClusterComputeResource cluster = client.getCluster(request.getCluster().trim());
                ResourcePool basePool = cluster.getResourcePool();
                List<ResourcePool> pools = List.of(basePool.getResourcePools());
                if (CollectionUtils.isNotEmpty(pools)) {
                    for (ResourcePool pool : pools) {
                        List<VsphereResourcePool> childResourcePools = getChildResourcePools(pool, "");
                        list.addAll(childResourcePools);
                    }
                }
            }
            list.sort(Comparator.comparing(VsphereResourcePool::getName));
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(client);
        }
    }

    public static List<VsphereResourcePool> getChildResourcePools(ResourcePool pool, String parent) throws Exception {
        List<VsphereResourcePool> result = new ArrayList<>();
        ResourcePool[] subPools = pool.getResourcePools();
        if (!"".equals(parent)) {
            parent = parent + "/";
        }
        VsphereResourcePool root = new VsphereResourcePool(pool.getMOR().getVal(), parent + pool.getName());

        //使用情况
        ResourcePoolRuntimeInfo runtime = pool.getSummary().getRuntime();
        ResourcePoolResourceUsage cpu = runtime.getCpu();
        ResourcePoolResourceUsage memory = runtime.getMemory();

        BigDecimal totalCpu = BigDecimal.valueOf(cpu.getReservationUsedForVm()).add(BigDecimal.valueOf(cpu.getUnreservedForVm())).divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP); //GHz
        BigDecimal totalUsedCpu = BigDecimal.valueOf(cpu.getReservationUsedForVm()).divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP); //GHz
        BigDecimal totalMemory = (BigDecimal.valueOf(memory.getReservationUsedForVm()).add(BigDecimal.valueOf(memory.getUnreservedForVm()))).divide(BigDecimal.valueOf(GB), 2, RoundingMode.HALF_UP); //GB
        BigDecimal totalUsedMemory = BigDecimal.valueOf(memory.getReservationUsedForVm()).divide(BigDecimal.valueOf(GB), 2, RoundingMode.HALF_UP); //GB

        root.setTotalCpu(totalCpu)
                .setUsedCpu(totalUsedCpu)
                .setTotalMemory(totalMemory) //MB
                .setUsedMemory(totalUsedMemory); //MB

        result.add(root);
        if (subPools != null) {
            for (ResourcePool childPool : subPools) {
                if (!StringUtils.equals(childPool.getMOR().getVal(), pool.getMOR().getVal()) &&
                        StringUtils.equals(childPool.getParent().getMOR().getVal(), pool.getMOR().getVal())
                ) {
                    result.addAll(getChildResourcePools(childPool, parent + pool.getName()));
                }
            }
        }
        return result;
    }

    public static List<VsphereFolder> getFolders(VsphereVmCreateRequest request) {
        VsphereVmClient client = null;
        try {
            client = request.getVsphereVmClient();
            List<Folder> folders = client.listFolders();
            List<VsphereFolder> list = new ArrayList<>();

            if (folders != null && folders.size() > 0) {
                for (Folder folder : folders) {
                    List<VsphereFolder> childFolders = VsphereUtil.getChildFolders(client, folder, "");
                    list.addAll(childFolders);
                }
            }
            list.sort(Comparator.comparing(VsphereFolder::getName));

            //根目录
            VsphereFolder rootFolder = new VsphereFolder().setMor(VsphereClient.FOLDER_ROOT).setName(VsphereClient.FOLDER_ROOT);
            list.add(0, rootFolder);

            return list;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            closeConnection(client);
        }
    }

    public static List<VsphereDatastore> getDatastoreListByVm(VsphereDiskRequest request) {
        VsphereVmClient client = request.getVsphereVmClient();
        List<VsphereDatastore> result;
        try {
            VirtualMachine virtualMachine;
            try {
                virtualMachine = client.getVirtualMachineById(request.getInstanceUuid());
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
            if (virtualMachine == null) {
                throw new RuntimeException(String.format("Virtual machine: %s ", request.getInstanceUuid()) + " The host storage failed, the virtual machine was not found.");
            }
            result = new ArrayList<>();
            try {
                if (request.getDiskId() != null) {
                    Datastore datastore = null;
                    VirtualDisk virtualMachineDisk = VsphereDiskUtil.getVirtualMachineDisk(virtualMachine,
                            request.getDiskId());
                    if (virtualMachineDisk == null) {
                        throw new RuntimeException("Failed to get the disk, can't find the virtual machine:" + request.getInstanceUuid() + " disk: " + request.getDiskId());
                    }
                    VirtualDeviceBackingInfo backing = virtualMachineDisk.getBacking();
                    if (backing instanceof VirtualDiskFlatVer2BackingInfo) {
                        datastore = client.getDatastoreByMor(((VirtualDiskFlatVer2BackingInfo) backing).getDatastore().getVal());
                    } else if (backing instanceof VirtualDiskSparseVer2BackingInfo) {
                        datastore = client.getDatastoreByMor(((VirtualDiskSparseVer2BackingInfo) backing).getDatastore().getVal());
                    }
                    if (null == datastore) {
                        throw new RuntimeException("Failed to get datastore, unable to find disk:" + request.getDiskId() + " where the store");
                    }
                    result.add(convertToVsphereDatastore(datastore));
                } else {
                    Datastore[] datastores = virtualMachine.getDatastores();
                    if (datastores != null) {
                        for (Datastore ds : datastores) {
                            result.add(convertToVsphereDatastore(ds));
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(String.format("Virtual machine: %s ", request.getInstanceUuid()) + " where the store" + "，message:" + e.getMessage());
            }
        } finally {
            closeConnection(client);
        }
        return result;
    }

    public static List<VsphereDatastore> getDatastoreList(VsphereVmCreateRequest request) {
        VsphereVmClient client = null;
        List<VsphereDatastore> datastoreList = new ArrayList<>();
        try {
            if (request.getComputeConfig() == null) {
                return new ArrayList<>();
            }
            String location = request.getComputeConfig().getLocation();
            //String mor = request.getComputeConfig().getMor(); //宿主机或者资源池的mor
            String name = request.getComputeConfig().getName(); //宿主机或者资源池的名称，api说vc是拿名称作为快速索引的

            String cluster = request.getCluster();

            if (StringUtils.isBlank(cluster)) {
                return new ArrayList<>();
            }
            if ((!StringUtils.equals(ResourceConstants.DRS, location)) && StringUtils.isBlank(name)) {
                return new ArrayList<>();
            }

            client = request.getVsphereVmClient();
            Map<String, Object> hasAddStorage = new HashMap<>();

            ComputeResource cr = client.getComputeResource(cluster);
            if (!StringUtils.equals("host", location)) {
                if (cr instanceof ClusterComputeResource) {
                    Datastore[] list = cr.getDatastores();

                    if (list != null && list.length > 0) {
                        for (Datastore ds : list) {
                            ManagedEntity parent = ds.getParent();
                            handleDataStoreCluster(datastoreList, hasAddStorage, ds, parent);
                        }
                    }
                }
            } else {
                //HostSystem hs = getHostSystemByHostMor(mor, cr);
                HostSystem hs = client.getHost(name);
                if (hs == null) {
                    return new ArrayList<>();
                }
                Datastore[] datastoreOfHost = hs.getDatastores();
                if (datastoreOfHost != null && datastoreOfHost.length > 0) {
                    for (Datastore ds : datastoreOfHost) {
                        ManagedEntity parent = ds.getParent();
                        handleDataStoreCluster(datastoreList, hasAddStorage, ds, parent);
                    }
                }
            }

            Set<VsphereDatastore> datastoreHashSet = new HashSet<>(datastoreList);
            ArrayList<VsphereDatastore> results = new ArrayList<>(datastoreHashSet);

            results.sort(Comparator.comparing(VsphereDatastore::getInfo));

            return results;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            closeConnection(client);
        }
    }

    private static HostSystem getHostSystemByHostMor(String mor, ComputeResource cr) {
        List<HostSystem> hss = Arrays.stream(cr.getHosts()).filter((host) -> StringUtils.equals(host.getMOR().getVal(), mor)).toList();
        if (CollectionUtils.isEmpty(hss)) {
            return null;
        }
        return hss.get(0);
    }

    private static void handleDataStoreCluster(List<VsphereDatastore> datastoreList, Map<String, Object> hasAddStorage, Datastore ds, ManagedEntity parent) {
        if (parent instanceof StoragePod) {
            if (hasAddStorage.get(parent.getMOR().getVal()) == null) {
                StoragePod storagePod = (StoragePod) parent;
                datastoreList.add(0, convertToVsphereDatastore(storagePod));
                hasAddStorage.put(storagePod.getMOR().getVal(), new Object());
            }
        } else {
            datastoreList.add(convertToVsphereDatastore(ds));
        }
    }

    private static VsphereDatastore convertToVsphereDatastore(StoragePod storagePod) {
        VsphereDatastore vsphereDatastore = new VsphereDatastore().setMor(storagePod.getMOR().getVal()).setName(storagePod.getName());
        BigDecimal freeSpace = BigDecimal.valueOf(storagePod.getSummary().getFreeSpace()).divide(BigDecimal.valueOf(GB), 2, RoundingMode.HALF_UP);
        BigDecimal totalSpace = BigDecimal.valueOf(storagePod.getSummary().getCapacity()).divide(BigDecimal.valueOf(GB), 2, RoundingMode.HALF_UP);
        vsphereDatastore.setFreeDisk(freeSpace);
        vsphereDatastore.setTotalDisk(totalSpace);

        vsphereDatastore.setInfo("Storage Cluster:" + storagePod.getName());
        return vsphereDatastore;
    }

    private static VsphereDatastore convertToVsphereDatastore(Datastore datastore) {
        VsphereDatastore vsphereDatastore = new VsphereDatastore().setMor(datastore.getMOR().getVal()).setName(datastore.getName());
        BigDecimal freeSpace = BigDecimal.valueOf(datastore.getSummary().getFreeSpace()).divide(BigDecimal.valueOf(GB), 2, RoundingMode.HALF_UP);
        BigDecimal totalSpace = BigDecimal.valueOf(datastore.getSummary().getCapacity()).divide(BigDecimal.valueOf(GB), 2, RoundingMode.HALF_UP);
        vsphereDatastore.setFreeDisk(freeSpace);
        vsphereDatastore.setTotalDisk(totalSpace);

        vsphereDatastore.setInfo(datastore.getName());
        return vsphereDatastore;
    }

    public static List<F2CVsphereNetwork> getNetworks(VsphereVmCreateRequest request) {
        VsphereClient client = null;
        List<F2CVsphereNetwork> networks = new ArrayList<>();
        try {
            if (request.getComputeConfig() == null) {
                return new ArrayList<>();
            }
            String location = request.getComputeConfig().getLocation();
            //String mor = request.getComputeConfig().getMor(); //宿主机或者资源池的mor
            String name = request.getComputeConfig().getName(); //宿主机或者资源池的名称，api说vc是拿名称作为快速索引的

            String cluster = request.getCluster();

            if (StringUtils.isBlank(cluster)) {
                return new ArrayList<>();
            }
            if ((!StringUtils.equals(ResourceConstants.DRS, location)) && StringUtils.isBlank(name)) {
                return new ArrayList<>();
            }

            client = request.getVsphereVmClient();

            ComputeResource cr = client.getComputeResource(cluster);

            if (!StringUtils.equals("host", location)) {
                if (cr instanceof ClusterComputeResource) {
                    networks.addAll(getClusterNetworks((ClusterComputeResource) cr));
                }
            } else {
                //HostSystem hs = getHostSystemByHostMor(mor, cr);
                HostSystem hs = client.getHost(name);
                if (hs != null) {
                    networks.addAll(convertToVsphereNetworks(hs));
                }
            }

            Set<F2CVsphereNetwork> temp = new HashSet<>(networks);
            return new ArrayList<>(temp);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            closeConnection(client);
        }

    }

    public static F2CVirtualMachine getSimpleServerByCreateRequest(VsphereVmCreateRequest request) {
        F2CVirtualMachine virtualMachine = new F2CVirtualMachine();

        int index = request.getIndex();
        String instanceType = request.getCpu() + "vCpu " + request.getRam() + "GB";

        virtualMachine
                .setId(request.getId())
                .setName(request.getServerInfos().get(index).getName())
                .setCpu(request.getCpu())
                .setMemory(request.getRam())
                .setIpArray(new ArrayList<>())
                .setInstanceType(instanceType)
                .setInstanceTypeDescription(instanceType);

        return virtualMachine;

    }

    public static F2CVirtualMachine createServer(VsphereVmCreateRequest request) {

        F2CVirtualMachine f2CVirtualMachine = null;

        VsphereVmClient client = request.getVsphereVmClient();
        int index = request.getIndex();

        try {

            if (StringUtils.isBlank(request.getServerInfos().get(index).getName())) {
                request.getServerInfos().get(index).setName("i-" + UUID.randomUUID().toString().substring(0, 8));
            }

            if (StringUtils.isBlank(request.getServerInfos().get(index).getHostname())) {
                request.getServerInfos().get(index).setHostname("i-" + UUID.randomUUID().toString().substring(0, 8));
            }

            Pattern pattern = Pattern.compile(VALID_HOST_REGEX, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(request.getServerInfos().get(index).getHostname());
            if (!matcher.find()) {
                throw new RuntimeException("hostname cannot be empty");
            }
            if (CollectionUtils.isEmpty(request.getNetworkConfigs()) || CollectionUtils.isEmpty(request.getNetworkConfigs().get(index).getAdapters())) {
                throw new RuntimeException("Network Adapter cannot be empty");
            }
            for (VsphereVmCreateRequest.NetworkAdapter networkAdapter : request.getNetworkConfigs().get(index).getAdapters()) {
                if (StringUtils.isBlank(networkAdapter.getVlan())) {
                    throw new RuntimeException("Vlan cannot be empty");
                }
                String ipType = StringUtils.defaultString(networkAdapter.getIpType(), ResourceConstants.ipv4);
                boolean setIpv4 = ipType.equalsIgnoreCase(ResourceConstants.ipv4) || ipType.equalsIgnoreCase(ResourceConstants.DualStack);
                boolean setIpv6 = ipType.equalsIgnoreCase(ResourceConstants.ipv6) || ipType.equalsIgnoreCase(ResourceConstants.DualStack);
                if (!networkAdapter.isDhcp() && setIpv4) {
                    if (StringUtils.isBlank(networkAdapter.getIpAddr())) {
                        throw new RuntimeException("IP cannot be empty");
                    }
                    if (StringUtils.isBlank(networkAdapter.getNetmask())) {
                        throw new RuntimeException("NetMask cannot be empty");
                    }
                    if (StringUtils.isBlank(networkAdapter.getGateway())) {
                        throw new RuntimeException("Gateway cannot be empty");
                    }
                } else if (!networkAdapter.isDhcp() && setIpv6) {
                    if (StringUtils.isBlank(networkAdapter.getIpAddrV6())) {
                        throw new RuntimeException("IP cannot be empty");
                    }
                    if (StringUtils.isBlank(networkAdapter.getNetmaskV6())) {
                        throw new RuntimeException("NetMask cannot be empty");
                    }
                    if (StringUtils.isBlank(networkAdapter.getGatewayV6())) {
                        throw new RuntimeException("Gateway cannot be empty");
                    }
                }

            }

            VirtualMachine vm = client.createVm(request);

            if (vm != null) {
                f2CVirtualMachine = VsphereUtil.toF2CInstance(vm, client);

                client.connectVirtualEthernetCard(vm);

                if (f2CVirtualMachine != null) {
                    int count = 0;
                    while (count++ <= 60) {
                        boolean flag = false; // 处理自定义规范返回了 169.254.x.x 等 IP，不是机器真正的 IP
                        try {
                            Thread.sleep(5000);
                            vm = client.getVirtualMachineById(vm.getConfig().getInstanceUuid());
                            f2CVirtualMachine = VsphereUtil.toF2CInstance(vm, client);
                        } catch (Exception e) {
                            continue;
                        }

                        GuestInfo guest = vm.getGuest();
                        if (guest != null) {
                            //todo 多网卡？ 现在只看了第一张网卡
                            if (!request.getNetworkConfigs().get(index).getAdapters().get(0).isDhcp()) {
                                if ("guestToolsRunning".equalsIgnoreCase(guest.getToolsRunningStatus())) {
                                    break;
                                }
                            }
                            // DHCP 的处理
                            else {
                                GuestNicInfo[] nets = guest.getNet();
                                if (nets != null && nets.length > 0) {
                                    List<String> ipArray = f2CVirtualMachine.getIpArray();
                                    for (GuestNicInfo nicInfo : nets) {
                                        String[] ips = nicInfo.getIpAddress();
                                        if (ips != null && ips.length > 0) {
                                            for (String ip : ips) {
                                                if (ip.startsWith("169.254")) {
                                                    flag = true;
                                                    break;
                                                }
                                                if (ip.contains(".")) {
                                                    f2CVirtualMachine.setLocalIP(ip);
                                                    ipArray.add(ip);
                                                }
                                            }
                                        }
                                        if (flag) {
                                            break;
                                        }
                                    }
                                    // 查询到 169.254.x.x IP 继续查询
                                    if (flag) {
                                        continue;
                                    }
                                    if (ipArray.size() > 0) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (count > 60) {
                        log.error("Get private IP timeout!");
                    }
                }
            }
        } finally {
            closeConnection(client);
        }
        if (f2CVirtualMachine != null) {
            f2CVirtualMachine.setId(request.getId());
        }
        return f2CVirtualMachine;
    }


    public static List<F2CPerfMetricMonitorData> getF2CPerfMetricList(GetMetricsRequest getMetricsRequest) {
        if (StringUtils.isEmpty(getMetricsRequest.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        List<F2CPerfMetricMonitorData> result = new ArrayList<>();
        //设置时间，根据interval,默认一个小时
        getMetricsRequest.setStartTime(String.valueOf(DateUtil.getBeforeHourTime(1)));
        getMetricsRequest.setEndTime(String.valueOf(System.currentTimeMillis()));
//        getMetricsRequest.setStartTime("1667375402125");
//        getMetricsRequest.setEndTime("1667379002125");
        Long cur = System.currentTimeMillis();
        System.out.println("开始时间：" + cur);
        try {
            getMetricsRequest.setRegionId(getMetricsRequest.getRegionId());
            result.addAll(getVmPerfMetric(getMetricsRequest));
        } catch (Exception e) {
            throw new Fit2cloudException(100021, "获取监控数据失败-" + getMetricsRequest.getRegionId() + "-" + e.getMessage());
        }
        Long en = System.currentTimeMillis();
        System.out.println("耗时:" + (cur - en) + "-------------------------------");
        return result;
    }

    /**
     * 获取虚拟机监控指标数据
     *
     * @param getMetricsRequest
     * @return
     */
    private static List<F2CPerfMetricMonitorData> getVmPerfMetric(GetMetricsRequest getMetricsRequest) {
        //vmware默认监控间隔时间最小是300秒(5分钟)
        //理论上我们查询一个小时，最多也只会返回12条数据
        //TODO 磁盘读写数据对应的是VC上的虚拟磁盘，对象是云主机
        getMetricsRequest.setInterval(300);
        VsphereVmBaseRequest vsphereVmBaseRequest = JsonUtil.parseObject(JsonUtil.toJSONString(getMetricsRequest), VsphereVmBaseRequest.class);
        List<F2CPerfMetricMonitorData> result = new ArrayList<>();
        VsphereVmClient client = vsphereVmBaseRequest.getVsphereVmClient();
        //查询所有虚拟机
        List<VirtualMachine> vms = client.listVirtualMachines();
        //VirtualMachine vm = vms.stream().filter(v->StringUtils.equalsIgnoreCase(v.getConfig().getName(),"管理员-CE4.0测试环境")).collect(Collectors.toList()).get(0);
        if (vms.size() != 0) {
            vms.forEach(vm -> {
                Arrays.stream(VspherePerfMetricConstants.CloudServerPerfMetricEnum.values()).sorted().collect(Collectors.toList()).forEach(perfMetric -> {
//                    if(perfMetric.getMetricName()!="181" && perfMetric.getMetricName()!="180"){
//                        return;
//                    }
                    System.out.println("开始：" + perfMetric.getDescription());
                    System.out.println("MetricId：" + perfMetric.getMetricName());
                    Map<String, Map<String, Long>> dataMap = getPerfData(vm, client, getMetricsRequest, perfMetric.getMetricName(), getMetricsRequest.getInterval());
                    System.out.println("结果：" + JsonUtil.toJSONString(dataMap));
                    dataMap.values().forEach((data) -> {
                        F2CPerfMetricMonitorData f2CEntityPerfMetric = new F2CPerfMetricMonitorData();
                        f2CEntityPerfMetric.setTimestamp(data.get("timestamp"));
                        f2CEntityPerfMetric.setAverage(new BigDecimal(data.get("Average")).divide(new BigDecimal(perfMetric.getDivisor())));
                        /// TODO 没有最大最小值，可能需要自己算？
//                        f2CEntityPerfMetric.setMinimum(new BigDecimal(v.get("Minimum").doubleValue()).setScale(3, RoundingMode.HALF_UP));
//                        f2CEntityPerfMetric.setMaximum(new BigDecimal(v.get("Maximum").doubleValue()).setScale(3, RoundingMode.HALF_UP));
                        f2CEntityPerfMetric.setEntityType(F2CEntityType.VIRTUAL_MACHINE.name());
                        f2CEntityPerfMetric.setMetricName(perfMetric.name());
                        f2CEntityPerfMetric.setPeriod(getMetricsRequest.getPeriod());
                        f2CEntityPerfMetric.setInstanceId(vm.getConfig().getInstanceUuid());
                        f2CEntityPerfMetric.setUnit(perfMetric.getUnit());
                        result.add(f2CEntityPerfMetric);
                    });
                });
            });
        }
        return result;
    }

    /**
     * 根据指标获取监控数据
     *
     * @param virtualMachine
     * @param client
     * @param request
     * @param metricName
     * @param interval
     * @return
     */
    public static Map<String, Map<String, Long>> getPerfData(VirtualMachine virtualMachine, VsphereVmClient client,
                                                             GetMetricsRequest request, String metricName, Integer interval) {
        Calendar calBegin = Calendar.getInstance();
        //如果是虚拟磁盘读写监控，开始时间+40分钟，因为只能查询20分钟内的数据
        if (StringUtils.equalsIgnoreCase(metricName, "181") || StringUtils.equalsIgnoreCase(metricName, "180")) {
            calBegin.setTime(new Date(Long.valueOf(request.getStartTime()) + 2400000));
        } else {
            calBegin.setTime(new Date(Long.valueOf(request.getStartTime())));
        }
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(new Date(Long.valueOf(request.getEndTime())));
        try {
            PerformanceManager performanceManager = client.getSi().getPerformanceManager();
            PerfQuerySpec qSpec = new PerfQuerySpec();
            qSpec.setEntity(virtualMachine.getMOR());
            PerfMetricId perfMetricId = new PerfMetricId();
            perfMetricId.setCounterId(Integer.parseInt(metricName));
            //这个是必须的，不然查询会报错
            perfMetricId.setInstance("");
            PerfMetricId[] perfMetricIds = new PerfMetricId[1];
            perfMetricIds[0] = perfMetricId;
            qSpec.setStartTime(calBegin);
            qSpec.setEndTime(calEnd);
            qSpec.setMetricId(perfMetricIds);
            qSpec.setIntervalId(interval);
            qSpec.setFormat("normal");
            PerfQuerySpec[] querySpecs = {qSpec};
            PerfEntityMetricBase[] pValues = performanceManager.queryPerf(querySpecs);
            if (pValues != null) {
                return printPerfMetric(pValues[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    /**
     * 组数据，key:时间，value:数据
     * 因为vmware时间与数据分开两个数组返回，我们处理方式就是按照两个数组的下标对应关系
     *
     * @param pemBase
     * @return
     */
    static Map<String, Map<String, Long>> printPerfMetric(PerfEntityMetricBase pemBase) {
        Map<String, Map<String, Long>> perfEntityMetric = new LinkedHashMap<>();
        if (pemBase instanceof PerfEntityMetric) {
            PerfEntityMetric pem = (PerfEntityMetric) pemBase;
            //时间数据
            PerfSampleInfo[] infos = pem.getSampleInfo();
            if (!ObjectUtils.isEmpty(infos)) {
                //数据值
                long[] values = ((PerfMetricIntSeries) pem.getValue()[0]).getValue();
                System.out.println("时间数据数量：" + infos.length);
                System.out.println("数据数量：" + values.length);
                System.out.println("\nSample values:");
                F2CPerfMetricMonitorData data = new F2CPerfMetricMonitorData();
                for (int j = 0; values != null && j < values.length; ++j) {
                    Map<String, Long> map = new HashMap<>();
                    map.put("timestamp", infos[j].getTimestamp().getTimeInMillis());
                    map.put("Average", values[j]);
                    perfEntityMetric.put(String.valueOf(infos[j].getTimestamp().getTimeInMillis()), map);
                }
            }
        }
        return perfEntityMetric;
    }

    public static CheckCreateServerResult validateServerCreateRequest(VsphereVmCreateRequest request) {
        VsphereVmClient client = null;
        try {
            int count = request.getCount();
            if (count <= 0) {
                return CheckCreateServerResult.fail("申请个数不能为空");
            }
            if (CollectionUtils.isEmpty(request.getNetworkConfigs()) || request.getNetworkConfigs().size() != count) {
                return CheckCreateServerResult.fail("网络配置中主机个数与申请主机数不匹配");
            }

            String template = request.getTemplate();
            if (StringUtils.isBlank(template)) {
                return CheckCreateServerResult.fail("模版不能为空");
            }

            for (VsphereVmCreateRequest.NetworkConfig networkConfig : request.getNetworkConfigs()) {
                if (CollectionUtils.isEmpty(networkConfig.getAdapters())) {
                    return CheckCreateServerResult.fail("主机网卡不能为空");
                }
                //判断dns是否为ip格式
                if (StringUtils.isNotBlank(networkConfig.getDns1()) && !IpChecker.isIpv4(networkConfig.getDns1())) {
                    return CheckCreateServerResult.fail("DNS1: " + networkConfig.getDns1() + " 格式不正确");
                }
                if (StringUtils.isNotBlank(networkConfig.getDns2()) && !IpChecker.isIpv4(networkConfig.getDns2())) {
                    return CheckCreateServerResult.fail("DNS2: " + networkConfig.getDns2() + " 格式不正确");
                }
                for (VsphereVmCreateRequest.NetworkAdapter adapter : networkConfig.getAdapters()) {
                    if (StringUtils.isBlank(adapter.getVlan())) {
                        return CheckCreateServerResult.fail("网络不能为空");
                    }
                    if (!adapter.isDhcp()) {
                        if (StringUtils.isBlank(adapter.getIpAddr())) {
                            return CheckCreateServerResult.fail("IP地址不能为空");
                        }
                        if (StringUtils.isBlank(adapter.getGateway())) {
                            return CheckCreateServerResult.fail("网关不能为空");
                        }
                        if (StringUtils.isBlank(adapter.getNetmask())) {
                            return CheckCreateServerResult.fail("Netmask不能为空");
                        }
                        if (!IpChecker.isIpv4(adapter.getIpAddr())) {
                            return CheckCreateServerResult.fail("IP地址: " + adapter.getIpAddr() + " 格式不正确");
                        }
                        if (!IpChecker.isIpv4(adapter.getGateway())) {
                            return CheckCreateServerResult.fail("网关: " + adapter.getGateway() + " 格式不正确");
                        }
                        if (!IpChecker.isNetmask(adapter.getNetmask())) {
                            return CheckCreateServerResult.fail("Netmask: " + adapter.getNetmask() + " 格式不正确");
                        }
                    }
                }

            }
            if (CollectionUtils.isEmpty(request.getServerInfos()) || request.getServerInfos().size() != count) {
                return CheckCreateServerResult.fail("主机配置中主机个数与申请主机数不匹配");
            }

            Set<String> names = new HashSet<>();

            Pattern pattern = Pattern.compile(VALID_HOST_REGEX, Pattern.CASE_INSENSITIVE);

            for (VsphereVmCreateRequest.ServerInfo serverInfo : request.getServerInfos()) {
                if (StringUtils.isBlank(serverInfo.getName())) {
                    return CheckCreateServerResult.fail("主机名称不能为空");
                }
                if (StringUtils.isBlank(serverInfo.getHostname())) {
                    return CheckCreateServerResult.fail("Hostname称不能为空");
                }
                Matcher matcher = pattern.matcher(serverInfo.getHostname());
                if (!matcher.find()) {
                    return CheckCreateServerResult.fail("Hostname: " + serverInfo.getHostname() + " 不正确");
                }

                names.add(serverInfo.getName());
            }
            if (names.size() != count) {
                return CheckCreateServerResult.fail("主机名称不能重复");
            }

            client = request.getVsphereVmClient();

            for (String name : names) {
                //去查询该名称主机是否存在
                client = request.getVsphereVmClient();
                VirtualMachine vm = client.getVirtualMachine(name);
                if (vm != null) {
                    return CheckCreateServerResult.fail("主机: " + name + " 已存在");
                }
            }

            String folderName = request.getFolder();
            if (StringUtils.isNotBlank(folderName) && !VsphereClient.FOLDER_ROOT.equals(folderName)) {
                boolean result = VsphereUtil.validateFolder(client, folderName);
                if (!result) {
                    return CheckCreateServerResult.fail("文件夹: " + folderName + " 不存在");
                }
            }

            //todo 校验底层资源是否足够？


            return CheckCreateServerResult.success();

        } catch (Exception e) {
            return CheckCreateServerResult.fail(e.getMessage());
        } finally {
            closeConnection(client);
        }
    }
}
