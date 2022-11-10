package com.fit2cloud.provider.impl.vsphere.api;

import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.platform.credential.impl.VsphereCredential;
import com.fit2cloud.common.provider.entity.F2CEntityType;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.provider.impl.vsphere.utils.VsphereClient;
import com.fit2cloud.common.utils.DateUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.entity.*;
import com.fit2cloud.provider.entity.request.GetMetricsRequest;
import com.fit2cloud.provider.impl.vsphere.entity.*;
import com.fit2cloud.provider.impl.vsphere.entity.request.*;
import com.fit2cloud.provider.impl.vsphere.util.*;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Author: LiuDi
 * Date: 2022/9/21 2:39 PM
 */
public class VsphereSyncCloudApi {
    private static final long MB = 1024 * 1024;
    private static final long GB = MB * 1024;
    private static Logger logger = LoggerFactory.getLogger(VsphereSyncCloudApi.class);

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
            logger.error("[Failed to Get Content Library Image]", e);
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
            List<HostSystem> hostSystemList = client.listHostsFromAll();
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
            List<HostSystem> hosts = client.listHostsFromAll();
            list = new ArrayList<>();
            for (HostSystem hs : hosts) {
                list.add(VsphereUtil.toF2CHost(hs, client));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
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
                            f2cDs.setClusterId(cluster.getName());
                            f2cDs.setClusterName(cluster.getName());
                            DatastoreSummary dsSummary = ds.getSummary();
                            f2cDs.setCapacity(dsSummary.getCapacity() / GB);
                            f2cDs.setDataStoreId(ds.getName());
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
                    if (StringUtils.equals(f2CDataStore.getDataStoreId(), ds.getName())) {
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
                f2cDs.setDataStoreId(ds.getName());
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
        return operate(req.getUuId(), client::powerOff, client::closeConnection);
    }

    public static boolean powerOn(VsphereVmPowerRequest req) {
        VsphereVmClient client = req.getVsphereVmClient();
        return operate(req.getUuId(), client::powerOn, client::closeConnection);
    }

    public static boolean shutdownInstance(VsphereVmPowerRequest req) {
        VsphereVmClient client = req.getVsphereVmClient();
        return operate(req.getUuId(), client::shutdownInstance, client::closeConnection);
    }

    public static boolean reboot(VsphereVmPowerRequest req) {
        VsphereVmClient client = req.getVsphereVmClient();
        return operate(req.getUuId(), client::reboot, client::closeConnection);
    }

    public static boolean deleteInstance(VsphereVmPowerRequest req) {
        VsphereVmClient client = req.getVsphereVmClient();
        return operate(req.getUuId(), client::deleteInstance, client::closeConnection);
    }

    public static boolean hardReboot(VsphereVmPowerRequest req) {
        VsphereVmClient client = req.getVsphereVmClient();
        return operate(req.getUuId(), client::hardReboot, client::closeConnection);
    }

    private static boolean operate(String uuId, Function<String, Boolean> execMethod, Runnable closeConnection) {
        try {
            return execMethod.apply(uuId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("[Failed to operate virtual machine]", e);
        } finally {
            closeConnection.run();
        }
        return false;
    }

    public static List<Map<String, String>> getLocations(VsphereVmCreateRequest req) {
        List<Map<String, String>> locations = new ArrayList<>();
        Map<String, String> hostMap = new HashMap<>();
        hostMap.put("name", "主机");
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


    public static List<F2CVsphereNetwork> getNetworks(VsphereNetworkRequest req) {

        VsphereVmClient client = null;
        try {
            client = req.getVsphereVmClient();

            List<F2CVsphereNetwork> networks = new ArrayList<>();
            networks.add(new F2CVsphereNetwork().setName("Template default").setId(ResourceConstants.DEFAULT_TEMPLATE_NETWORK));

            String location = req.getLocation();
            List<String> locationValues = req.getHosts();
            String cluster = req.getCluster();
            ClusterComputeResource clusterComputeResource = null;
            if (cluster != null && cluster.trim().length() > 0) {
                clusterComputeResource = client.getCluster(cluster);
            }
            if (CollectionUtils.isEmpty(locationValues)) {
                networks.addAll(getClusterNetworks(clusterComputeResource));
            } else {
                if (StringUtils.equals("host", location)) {
                    if (locationValues.contains(ResourceConstants.DRS) || locationValues.contains(ResourceConstants.ALL_COMPUTE_RESOURCE)) {
                        networks.addAll(getClusterNetworks(clusterComputeResource));
                    } else {
                        for (String host : locationValues) {
                            HostSystem hostSystem = client.getHost(host);
                            if (hostSystem != null) {
                                networks.addAll(convertToVsphereNetworks(hostSystem));
                            }
                        }
                    }
                } else {
                    networks.addAll(getClusterNetworks(clusterComputeResource));
                }
            }
            Set<F2CVsphereNetwork> temp = new HashSet<>(networks);
            return new ArrayList<>(temp);

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
                        .setId(network.getMOR().getVal()));
            }
        }
        return networks;
    }

    public static List<F2CDisk> createDisks(VsphereCreateDiskRequest request) {
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

        VsphereCreateDiskRequest createDiskRequest = new VsphereCreateDiskRequest();
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

    private static void editDisk(VsphereCreateDiskRequest createDiskRequest, VsphereClient client) {
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
                long totalMemory = hostHw.getMemorySize() / GB;
                long totalCpu = (long) hostHw.getCpuMhz() * hostHw.getNumCpuCores();
                long totalUsedCpu = summary.getQuickStats().getOverallCpuUsage();
                long totalUsedMemory = summary.getQuickStats().getOverallMemoryUsage();

                host.setTotalCpu(totalCpu)
                        .setTotalMemory(totalMemory)
                        .setUsedCpu(totalUsedCpu)
                        .setUsedMemory(totalUsedMemory);

                result.add(host);
            }

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

        root.setTotalCpu(cpu.getReservationUsedForVm() + cpu.getUnreservedForVm())
                .setUsedCpu(cpu.getReservationUsedForVm())
                .setTotalMemory((memory.getReservationUsedForVm() + memory.getUnreservedForVm()) / GB)
                .setUsedMemory(memory.getReservationUsedForVm() / GB);

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

    public static List<F2CPerfMetricMonitorData> getF2CPerfMetricList(GetMetricsRequest getMetricsRequest){
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
        System.out.println("开始时间："+cur);
        try{
            getMetricsRequest.setRegionId(getMetricsRequest.getRegionId());
            result.addAll(getVmPerfMetric(getMetricsRequest));
        }catch (Exception e){
            throw new Fit2cloudException(100021, "获取监控数据失败-"+getMetricsRequest.getRegionId()+"-" + e.getMessage());
        }
        Long en = System.currentTimeMillis();
        System.out.println("耗时:"+(cur-en)+"-------------------------------");
        return result;
    }

    /**
     * 获取虚拟机监控指标数据
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
        if( vms.size()!=0 ){
            vms.forEach(vm->{
                Arrays.stream(VspherePerfMetricConstants.CloudServerPerfMetricEnum.values()).sorted().collect(Collectors.toList()).forEach(perfMetric->{
//                    if(perfMetric.getMetricName()!="181" && perfMetric.getMetricName()!="180"){
//                        return;
//                    }
                    System.out.println("开始："+perfMetric.getDescription());
                    System.out.println("MetricId："+perfMetric.getMetricName());
                    Map<String, Map<String,Long>> dataMap = getPerfData(vm,client,getMetricsRequest,perfMetric.getMetricName(),getMetricsRequest.getInterval());
                    System.out.println("结果："+JsonUtil.toJSONString(dataMap));
                    dataMap.values().forEach((data)->{
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
     * @param virtualMachine
     * @param client
     * @param request
     * @param metricName
     * @param interval
     * @return
     */
    public static Map<String, Map<String,Long>> getPerfData(VirtualMachine virtualMachine, VsphereVmClient client,
                                                            GetMetricsRequest request, String metricName, Integer interval) {
        Calendar calBegin = Calendar.getInstance();
        //如果是虚拟磁盘读写监控，开始时间+40分钟，因为只能查询20分钟内的数据
        if(StringUtils.equalsIgnoreCase(metricName,"181") || StringUtils.equalsIgnoreCase(metricName,"180")){
            calBegin.setTime(new Date(Long.valueOf(request.getStartTime())+2400000));
        }else{
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
            PerfQuerySpec[] querySpecs = { qSpec };
            PerfEntityMetricBase[] pValues = performanceManager.queryPerf(querySpecs);
            if(pValues != null){
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
     * @param pemBase
     * @return
     */
    static Map<String, Map<String,Long>> printPerfMetric(PerfEntityMetricBase pemBase){
        Map<String, Map<String,Long>> perfEntityMetric = new LinkedHashMap<>();
        if(pemBase instanceof PerfEntityMetric){
            PerfEntityMetric pem = (PerfEntityMetric) pemBase;
            //时间数据
            PerfSampleInfo[]  infos = pem.getSampleInfo();
            if(!ObjectUtils.isEmpty(infos) ){
                //数据值
                long[] values = ((PerfMetricIntSeries) pem.getValue()[0]).getValue();
                System.out.println("时间数据数量："+infos.length);
                System.out.println("数据数量："+values.length);
                System.out.println("\nSample values:");
                F2CPerfMetricMonitorData data = new F2CPerfMetricMonitorData();
                for(int j=0; values!=null && j<values.length; ++j){
                    Map<String,Long> map = new HashMap<>();
                    map.put("timestamp",infos[j].getTimestamp().getTimeInMillis());
                    map.put("Average",values[j]);
                    perfEntityMetric.put(String.valueOf(infos[j].getTimestamp().getTimeInMillis()),map);
                }
            }
        }
        return perfEntityMetric;
    }
}
