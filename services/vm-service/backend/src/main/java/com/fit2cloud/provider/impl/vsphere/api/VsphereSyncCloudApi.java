package com.fit2cloud.provider.impl.vsphere.api;

import com.fit2cloud.base.entity.BillPolicyDetails;
import com.fit2cloud.common.constants.ChargeTypeConstants;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.platform.credential.impl.VsphereCredential;
import com.fit2cloud.common.provider.entity.F2CEntityType;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.provider.exception.SkipPageException;
import com.fit2cloud.common.provider.impl.vsphere.utils.VsphereClient;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.*;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.provider.entity.*;
import com.fit2cloud.provider.entity.request.GetMetricsRequest;
import com.fit2cloud.provider.entity.result.CheckCreateServerResult;
import com.fit2cloud.provider.impl.vsphere.entity.*;
import com.fit2cloud.provider.impl.vsphere.entity.request.*;
import com.fit2cloud.provider.impl.vsphere.util.*;
import com.fit2cloud.utils.ChargingUtil;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Author: LiuDi
 * Date: 2022/9/21 2:39 PM
 */
@Slf4j
public class VsphereSyncCloudApi {
    private static final long MB = 1024 * 1024;
    private static final long GB = MB * 1024;

    public static final String VALID_HOST_REGEX = "^[A-Za-z0-9]+[A-Za-z0-9\\-]*[A-Za-z0-9]$";


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
                    try {
                        F2CVirtualMachine instance = VsphereUtil.toF2CInstance(vm, client, hostCache);
                        if (instance != null) {
                            list.add(instance);
                        }
                    } catch (Exception e) {
                        log.warn("Can not to get vm which name is " + vm.getName() + ". Error message is:" + e.getMessage(), e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to get vm list." + e.getMessage(), e);
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
                try {
                    list.add(VsphereUtil.toF2CHost(hs, client));
                } catch (Exception e) {
                    log.error("同步单台宿主机异常。Host name ：" + hs.getName() + ".异常信息：" + ExceptionUtils.getStackTrace(e), e);
                }
            }
        } catch (Exception e) {
            log.error("同步宿主机异常：" + ExceptionUtils.getStackTrace(e));
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
                            f2cDs.setAllocatedSpace(getDatastoreVmsDiskSizeTotal(ds.getVms()));
                            f2cDs.setDataCenterId(dc.getName());
                            f2cDs.setDataCenterName(dc.getName());
                            f2cDs.setRegion(dc.getName());
                            f2cDs.setClusterId(cluster.getMOR().getVal());
                            f2cDs.setClusterName(cluster.getName());
                            f2cDs.setZone(cluster.getName());
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
                f2cDs.setRegion(dc.getName());
                f2cDs.setCapacity(summary.getCapacity() / GB);
                f2cDs.setDataStoreId(ds.getMOR().getVal());
                f2cDs.setDataStoreName(ds.getName());
                f2cDs.setFreeSpace(summary.getFreeSpace() / GB);
                f2cDs.setType(summary.getType());
                Calendar updated = info.getTimestamp();
                if (updated != null) {
                    f2cDs.setLastUpdate(updated.getTimeInMillis() / 1000);
                }
                f2cDs.setAllocatedSpace(getDatastoreVmsDiskSizeTotal(ds.getVms()));
                datastoreList.add(f2cDs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            closeConnection(client);
        }
        return datastoreList;
    }

    private static int getDatastoreVmsDiskSizeTotal(VirtualMachine[] vms) {
        int vmDiskSize = 0;
        if (vms != null) {
            for (VirtualMachine vm : vms) {
                VirtualMachineConfigInfo vmConfig = vm.getConfig();
                if (vmConfig != null && vmConfig.isTemplate()) {
                    continue;
                }
                vmDiskSize += VsphereUtil.getDiskSizeInGB(vmConfig);
            }
        }
        return vmDiskSize;
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
            throw e;
        } finally {
            closeConnection.run();
        }
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
                    //根据数据中心过滤
                    if (StringUtils.isNotBlank(request.getRegion())) {
                        ManagedEntity p = folder.getParent();
                        if (p != null) {
                            ManagedEntity pp = p.getParent();
                            if (pp == null || !StringUtils.equals(pp.getName(), request.getRegion())) {
                                continue;
                            }
                        } else {
                            continue;
                        }
                    }

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
                .setInstanceTypeDescription(instanceType)
                .setRemark(request.getServerInfos().get(index).getRemark())
                .setInstanceChargeType(request.getInstanceChargeType());
        if (StringUtils.equals(ChargeTypeConstants.PREPAID.getCode(), request.getInstanceChargeType())) {
            virtualMachine.setExpiredTime(CommonUtil.getExpiredTimeEpochMilli(request.getPeriodNum()));
        }
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
                                // 都是这种fe80开头的ipv6链路本地地址，继续查询
                                // TODO 多网卡时，取到一个真实IP就返回，都是DHCP的话继续查询，直到获取到自动分配的IP为止
                                List<String> ipList = ipArray.stream().filter(v -> !v.startsWith("fe80")).collect(Collectors.toList());
                                if (ipArray.size() > 0 && ipList.size() > 0) {
                                    break;
                                }
                            }
                        }
                    }
                    if (count > 60) {
                        log.error("Get private IP timeout!");
                    }
                    // 超时都没拿到IP,就直接从参数中获取IP,DHCP除外
                    if (f2CVirtualMachine.getIpArray().size() == 0) {
                        // 从参数中获取IP地址
                        List<VsphereVmCreateRequest.NetworkAdapter> networkAdapters = request.getNetworkConfigs().get(index).getAdapters();
                        if (networkAdapters != null && networkAdapters.size() > 0) {
                            List<String> ipArray = f2CVirtualMachine.getIpArray();
                            // 多网卡的IP地址
                            for (VsphereVmCreateRequest.NetworkAdapter networkAdapter : networkAdapters) {
                                // 手动设置的IP地址
                                if (!networkAdapter.isDhcp()) {
                                    // 参数中的IP地址
                                    ipArray.add(networkAdapter.getIpAddr());
                                }
                            }
                        }
                    }
                    // 去重
                    f2CVirtualMachine.setIpArray(f2CVirtualMachine.getIpArray().stream().filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList()));
                }
            }
        } finally {
            closeConnection(client);
        }
        if (f2CVirtualMachine != null) {
            f2CVirtualMachine.setId(request.getId());
            f2CVirtualMachine.setInstanceChargeType(request.getInstanceChargeType());
        }
        return f2CVirtualMachine;
    }

    public static boolean resetPassword(VsphereVmResetPasswordRequest request) {

        if (request.getPasswordSetting() == null || !request.getPasswordSetting().getType().equals(VsphereVmCreateRequest.PasswordObject.TYPE.LINUX)) {
            log.info("skip reset password");
            return false;
        }

        if (StringUtils.isBlank(request.getPasswordSetting().getLoginUser()) || StringUtils.isBlank(request.getPasswordSetting().getLoginPassword()) || StringUtils.isBlank(request.getPasswordSetting().getImageUser()) || StringUtils.isBlank(request.getPasswordSetting().getImagePassword())) {
            throw new RuntimeException("用户名或密码不能为空");
        }
        if (StringUtils.isBlank(request.getPasswordSetting().getProgramPath()) || StringUtils.isBlank(request.getPasswordSetting().getScript())) {
            throw new RuntimeException("脚本执行路径或脚本参数不能为空");
        }

        VsphereVmClient client = request.getVsphereVmClient();
        try {
            VirtualMachine vm = client.getVirtualMachineById(request.getServerId());
            if (vm == null) {
                throw new RuntimeException("ID为[" + request.getServerId() + "]的vm不存在");
            }

            //先校验原密码是否正确
            int count = 160;
            while (count-- > 0) {
                try {
                    boolean validLoginInfo;
                    log.debug("validate OS username and password ::: user :: " + request.getPasswordSetting().getImageUser() + ", vmName: " + vm.getName());
                    validLoginInfo = client.validateOsUserAndPassword(vm, request.getPasswordSetting().getImageUser(), request.getPasswordSetting().getImagePassword());
                    if (validLoginInfo) {
                        break;
                    }
                } catch (Exception e) {
                    log.info(e.toString());
                } finally {
                    Thread.sleep(3000);
                }
            }

            String script = request.getPasswordSetting().getScript();

            script = script.replace(VsphereVmCreateRequest.PasswordObject.LOGIN_USER_PLACEHOLDER, request.getPasswordSetting().getLoginUser());
            script = script.replace(VsphereVmCreateRequest.PasswordObject.NEW_PASSWORD_PLACEHOLDER, request.getPasswordSetting().getLoginPassword());
            client.startProgramInGuest(request.getServerId(), request.getPasswordSetting().getProgramPath(), script, request.getPasswordSetting().getImageUser(), request.getPasswordSetting().getImagePassword(), 300);
        } catch (Exception e) {
            boolean valid = false;
            try {
                //抛出异常前再判断一次密码是否设置上了
                if (client != null) {
                    valid = client.validateOsUserAndPassword(client.getVirtualMachineById(request.getServerId()), request.getPasswordSetting().getLoginUser(), request.getPasswordSetting().getLoginPassword());
                }
            } catch (Exception ignore) {
            }
            if (!valid) {
                throw new RuntimeException(e);
            }
        } finally {
            if (client != null) {
                client.closeConnection();
            }
        }
        return true;
    }

    /**
     * 得到vsphere客户
     *
     * @param getMetricsRequest 获取监控数据参数
     * @return {@link VsphereClient }
     */
    private static VsphereVmClient getVsphereClientByGetMetricsRequest(GetMetricsRequest getMetricsRequest) {
        VsphereVmBaseRequest vsphereVmBaseRequest = JsonUtil.parseObject(JsonUtil.toJSONString(getMetricsRequest), VsphereVmBaseRequest.class);
        return vsphereVmBaseRequest.getVsphereVmClient();
    }

    /**
     * 获取vSphere查询监控数据的时间格式
     *
     * @param time 时间戳
     * @return {@link Calendar }
     */
    private static Calendar getCalendarTime(Long time) {
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(new Date(time));
        return calBegin;
    }

    /**
     * 获取云主机监控指标数据
     * 开始时间跟结束时间
     * syncTimeStampStr 开始触发监控数据同步的时间点
     * 根据syncTimeStampStr获取过去一个小时
     * 开始时间:syncTimeStampStr过去一个小时的时间点
     * 结束时间:syncTimeStampStr
     * 注：vmware没办法获取大于一个小时的
     *
     * @param getMetricsRequest 参数
     * @return {@link List }<{@link F2CPerfMetricMonitorData }>
     */
    public static List<F2CPerfMetricMonitorData> getF2CPerfMetricList(GetMetricsRequest getMetricsRequest) {
        getMetricsRequest.setStartTime(DateUtil.beforeOneHourToTimestamp(Long.valueOf(getMetricsRequest.getSyncTimeStampStr())));
        getMetricsRequest.setEndTime(Long.parseLong(getMetricsRequest.getSyncTimeStampStr()));
        try {
            return getVmMetricsData(getMetricsRequest, getVsphereClientByGetMetricsRequest(getMetricsRequest));
        } catch (Exception e) {
            throw new SkipPageException(100021, "获取云主机监控数据失败-" + getMetricsRequest.getRegionId() + "-" + e.getMessage());
        }
    }

    /**
     * 云主机指标监控数据
     * 批量获取云主机指标的数据
     * 云主机查询监控数据时
     * 查询数据时的interval为20秒
     *
     * @param getMetricsRequest 请求参数
     * @return 监控数据
     */
    private static List<F2CPerfMetricMonitorData> getVmMetricsData(GetMetricsRequest getMetricsRequest, VsphereVmClient client) {
        List<F2CPerfMetricMonitorData> result = new ArrayList<>();
        // 查询所有云主机
        List<VirtualMachine> vms = client.listVirtualMachines();
        if (CollectionUtils.isEmpty(vms)) {
            return result;
        }
        vms.forEach(vm -> {
            // 这个间隔是查询数据时参数
            int interval = 20;
            // 获取要查询的监控指标
            List<PerfMetricId> metricIds = getMetricIds(client);
            // 查询结果
            List<PerfEntityMetricBase> response = getMetricsData(client, vm.getMOR(), metricIds, getCalendarTime(getMetricsRequest.getStartTime()), getCalendarTime(getMetricsRequest.getEndTime()), interval);
            if (CollectionUtils.isNotEmpty(response)) {
                ConvertToPerfMetricDataVO convertToPerfMetricDataVO = new ConvertToPerfMetricDataVO();
                convertToPerfMetricDataVO.setEntityType(F2CEntityType.VIRTUAL_MACHINE);
                convertToPerfMetricDataVO.setInstanceId(vm.getConfig().getInstanceUuid());
                convertToPerfMetricDataVO.setHostId(vm.getRuntime().getHost().getVal());
                // convertApiResponse 会将间隔20秒的数据转为60秒，所以这里直接设置为60
                convertToPerfMetricDataVO.setPeriod(60);
                // 处理结果
                handleData(convertToPerfMetricDataVO, client, convertApiResponse(response, interval), result);
            }
        });
        return result;
    }


    /**
     * 获得宿主机性能指标监控数据
     *
     * @param getMetricsRequest 得到指标要求
     * @return {@link List }<{@link F2CPerfMetricMonitorData }>
     */
    public static List<F2CPerfMetricMonitorData> getF2CHostPerfMetricList(GetMetricsRequest getMetricsRequest) {
        getMetricsRequest.setStartTime(DateUtil.beforeOneHourToTimestamp(Long.valueOf(getMetricsRequest.getSyncTimeStampStr())));
        getMetricsRequest.setEndTime(Long.parseLong(getMetricsRequest.getSyncTimeStampStr()));
        try {
            return getHostMetricData(getMetricsRequest, getVsphereClientByGetMetricsRequest(getMetricsRequest));
        } catch (Exception e) {
            throw new SkipPageException(100021, "获取宿主机监控数据失败-" + getMetricsRequest.getRegionId() + "-" + e.getMessage());
        }
    }


    /**
     * 获得宿主机监控数据
     *
     * @param getMetricsRequest 查询宿主机指标监控数据参数
     * @param client            客户端
     * @return {@link List }<{@link F2CPerfMetricMonitorData }>
     */
    private static List<F2CPerfMetricMonitorData> getHostMetricData(GetMetricsRequest getMetricsRequest, VsphereVmClient client) {
        List<F2CPerfMetricMonitorData> result = new ArrayList<>();
        // 查询所有宿主机
        List<HostSystem> hosts = client.listHosts();
        if (hosts.size() != 0) {
            int interval = 300;
            hosts.forEach(host -> {
                // 指标数据
                List<PerfMetricId> metricIds = getMetricIds(client);
                // 查询结果
                List<PerfEntityMetricBase> response = getMetricsData(client, host.getMOR(), metricIds, getCalendarTime(getMetricsRequest.getStartTime()), getCalendarTime(getMetricsRequest.getEndTime()), interval);
                if (CollectionUtils.isNotEmpty(response)) {
                    ConvertToPerfMetricDataVO convertToPerfMetricDataVO = new ConvertToPerfMetricDataVO();
                    convertToPerfMetricDataVO.setEntityType(F2CEntityType.HOST);
                    convertToPerfMetricDataVO.setInstanceId(host.getMOR().getVal());
                    convertToPerfMetricDataVO.setPeriod(interval);
                    ComputeResource clusterResource = client.getComputeResource(host);
                    //设置集群名称
                    if (clusterResource instanceof ClusterComputeResource) {
                        ClusterComputeResource cluster = (ClusterComputeResource) clusterResource;
                        convertToPerfMetricDataVO.setClusterName(cluster.getName());
                    }
                    // 处理结果
                    handleData(convertToPerfMetricDataVO, client, convertApiResponse(response, interval), result);
                }
            });
        }
        return result;
    }


    /**
     * 获得存储器性能指标监控数据
     *
     * @param getMetricsRequest 参数
     * @return {@link List }<{@link F2CPerfMetricMonitorData }>
     */
    public static List<F2CPerfMetricMonitorData> getF2CDatastorePerfMetricList(GetMetricsRequest getMetricsRequest) {
        getMetricsRequest.setStartTime(DateUtil.beforeOneHourToTimestamp(Long.valueOf(getMetricsRequest.getSyncTimeStampStr())));
        getMetricsRequest.setEndTime(Long.parseLong(getMetricsRequest.getSyncTimeStampStr()));
        try {
            return getDatastoreMetricData(getMetricsRequest, getVsphereClientByGetMetricsRequest(getMetricsRequest));
        } catch (Exception e) {
            throw new SkipPageException(100021, "获取存储器监控数据失败-" + getMetricsRequest.getRegionId() + "-" + e.getMessage());
        }
    }


    /**
     * 获取存储器监控数据
     *
     * @param getMetricsRequest 查询主机参数指标监测数据
     * @param client            客户端
     * @return {@link List }<{@link F2CPerfMetricMonitorData }>
     * @author jianneng
     * @date 2023/05/25
     */
    private static List<F2CPerfMetricMonitorData> getDatastoreMetricData(GetMetricsRequest getMetricsRequest, VsphereVmClient client) {
        List<F2CPerfMetricMonitorData> result = new ArrayList<>();
        VsphereVmBaseRequest vsphereVmBaseRequest = JsonUtil.parseObject(JsonUtil.toJSONString(getMetricsRequest), VsphereVmBaseRequest.class);
        //查询数据中心下所有存储器
        List<DatastoreMor> datastores = listDataStoreMor(vsphereVmBaseRequest);
        if (datastores.size() != 0) {
            int interval = 300;
            datastores.forEach(datastoreMor -> {
                // 指标数据
                List<PerfMetricId> metricIds = getMetricIds(client);
                // 查询结果
                List<PerfEntityMetricBase> response = getMetricsData(client, datastoreMor.getMor(), metricIds, getCalendarTime(getMetricsRequest.getStartTime()), getCalendarTime(getMetricsRequest.getEndTime()), interval);
                if (CollectionUtils.isNotEmpty(response)) {
                    // 存储的结果需要单位换算，因为查询到的是实际使用的容量，而不是具体的比例值
                    List<MetricTimeValue> metricTimeValueList = convertApiResponse(response, interval);
                    metricTimeValueList.forEach(metricTimeValue -> {
                        BigDecimal useAvg = metricTimeValue.getAverage().divide(new BigDecimal(1024), 2, RoundingMode.HALF_UP).divide(new BigDecimal(1024), 2, RoundingMode.HALF_UP);
                        BigDecimal totalBig = new BigDecimal(datastoreMor.getCapacity());
                        metricTimeValue.setAverage(useAvg.multiply(new BigDecimal(100)).divide(totalBig, 2, RoundingMode.HALF_UP));
                    });
                    ConvertToPerfMetricDataVO convertToPerfMetricDataVO = new ConvertToPerfMetricDataVO();
                    convertToPerfMetricDataVO.setEntityType(F2CEntityType.DATASTORE);
                    convertToPerfMetricDataVO.setInstanceId(datastoreMor.getDataStoreId());
                    convertToPerfMetricDataVO.setPeriod(interval);
                    // 处理结果
                    handleData(convertToPerfMetricDataVO, client, metricTimeValueList, result);
                }
            });
        }
        return result;
    }


    /**
     * 数据存储列表铁道部
     *
     * @param req 获取存储器信息参数
     * @return {@link List }<{@link DatastoreMor }>
     */
    public static List<DatastoreMor> listDataStoreMor(VsphereVmBaseRequest req) {
        List<DatastoreMor> datastoreList = new ArrayList<>();
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
                            DatastoreMor f2cDs = new DatastoreMor();
                            f2cDs.setDataCenterId(dc.getName());
                            f2cDs.setDataCenterName(dc.getName());
                            f2cDs.setClusterId(cluster.getMOR().getVal());
                            f2cDs.setClusterName(cluster.getName());
                            DatastoreSummary dsSummary = ds.getSummary();
                            f2cDs.setCapacity(dsSummary.getCapacity() / GB);
                            f2cDs.setDataStoreId(ds.getMOR().getVal());
                            f2cDs.setMor(ds.getMOR());
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
                DatastoreMor f2cDs = new DatastoreMor();
                f2cDs.setDataCenterId(dc.getName());
                f2cDs.setDataCenterName(dc.getName());
                f2cDs.setCapacity(summary.getCapacity() / GB);
                f2cDs.setDataStoreId(ds.getMOR().getVal());
                f2cDs.setDataStoreName(ds.getName());
                f2cDs.setFreeSpace(summary.getFreeSpace() / GB);
                f2cDs.setMor(ds.getMOR());
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
     * 根据vSphere版本处理数据
     * 处理了两个版本的，如果不是6.x,那么就使用7.x 8.x的
     * 版本不一样，使用的指标不一样
     *
     * @param convertToPerfMetricDataVO {@link ConvertToPerfMetricDataVO} 统一的转换参数
     * @param client                    客户端
     * @param metricTimeValues          监控数据对象集合
     * @param result                    结果集
     */
    private static void handleData(ConvertToPerfMetricDataVO convertToPerfMetricDataVO, VsphereVmClient client, List<MetricTimeValue> metricTimeValues, List<F2CPerfMetricMonitorData> result) {
        String vSphereVersion = client.getSi().getAboutInfo().getVersion();
        if (vSphereVersion.startsWith("6")) {
            Arrays.stream(VspherePerfMetricConstants.PerfMetricForSixEnum.values()).forEach(perfMetric -> {
                convertToPerfMetricDataVO.setName(perfMetric.name());
                convertToPerfMetricDataVO.setUnit(perfMetric.getUnit());
                convertToPerfMetricDataVO.setMetricName(perfMetric.getMetricName());
                convertToPerfMetricDataVO.setDivisor(perfMetric.getDivisor());
                tof2CPerfMetricMonitorData(convertToPerfMetricDataVO, metricTimeValues, result);
            });
        } else {
            Arrays.stream(VspherePerfMetricConstants.PerfMetricForSevenAndEightEnum.values()).forEach(perfMetric -> {
                convertToPerfMetricDataVO.setName(perfMetric.name());
                convertToPerfMetricDataVO.setUnit(perfMetric.getUnit());
                convertToPerfMetricDataVO.setMetricName(perfMetric.getMetricName());
                convertToPerfMetricDataVO.setDivisor(perfMetric.getDivisor());
                tof2CPerfMetricMonitorData(convertToPerfMetricDataVO, metricTimeValues, result);
            });
        }
    }


    /**
     * 将数据转为平台数据结构并设置到返回结果中
     *
     * @param convertToPerfMetricDataVO {@link ConvertToPerfMetricDataVO} 统一的转换参数
     * @param metricTimeValues          监控数据
     * @param result                    结果
     */
    private static void tof2CPerfMetricMonitorData(ConvertToPerfMetricDataVO convertToPerfMetricDataVO, List<MetricTimeValue> metricTimeValues, List<F2CPerfMetricMonitorData> result) {
        List<MetricTimeValue> metricDataList = metricTimeValues.stream().filter(metricTimeValue -> StringUtils.equalsIgnoreCase(convertToPerfMetricDataVO.getMetricName(), metricTimeValue.getMetric())).toList();
        if (CollectionUtils.isNotEmpty(metricDataList)) {
            // 最大最小值去时间段内的，因为接口拿不到
            BigDecimal max = metricDataList.stream().toList().stream().map(MetricTimeValue::getAverage)
                    .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
            BigDecimal min = metricDataList.stream().toList().stream().map(MetricTimeValue::getAverage)
                    .min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
            metricDataList.forEach(metricData -> {
                F2CPerfMetricMonitorData f2CEntityPerfMetric = new F2CPerfMetricMonitorData();
                f2CEntityPerfMetric.setTimestamp(metricData.getTimestamp());
                f2CEntityPerfMetric.setAverage(metricData.getAverage().divide(new BigDecimal(convertToPerfMetricDataVO.getDivisor()), 2, RoundingMode.HALF_UP));
                f2CEntityPerfMetric.setMinimum(min.divide(new BigDecimal(convertToPerfMetricDataVO.getDivisor()), 2, RoundingMode.HALF_UP));
                f2CEntityPerfMetric.setMaximum(max.divide(new BigDecimal(convertToPerfMetricDataVO.getDivisor()), 2, RoundingMode.HALF_UP));
                f2CEntityPerfMetric.setEntityType(convertToPerfMetricDataVO.getEntityType().name());
                f2CEntityPerfMetric.setMetricName(convertToPerfMetricDataVO.getName());
                f2CEntityPerfMetric.setPeriod(convertToPerfMetricDataVO.getPeriod());
                f2CEntityPerfMetric.setInstanceId(convertToPerfMetricDataVO.getInstanceId());
                f2CEntityPerfMetric.setUnit(convertToPerfMetricDataVO.getUnit());
                f2CEntityPerfMetric.setHostId(convertToPerfMetricDataVO.getHostId());
                f2CEntityPerfMetric.setClusterName(convertToPerfMetricDataVO.getClusterName());
                result.add(f2CEntityPerfMetric);
            });
        }
    }


    /**
     * 转换数据
     * 将时间数据数组与指标值数据数组中相同下标的元素组合成新对象的集合
     * 如果interval为20时，数据间隔是20秒一条数据，会将数据转为一分钟一条数据，然后计算同一分钟的数据的平均值作为当前分钟的值
     * 如果interval大于20时，直接返回新的对象数据
     *
     * @param response API接口数据
     * @param interval 数据间隔
     * @return 返回每分钟一条数据
     */
    private static List<MetricTimeValue> convertApiResponse(List<PerfEntityMetricBase> response, int interval) {
        List<MetricTimeValue> result = new ArrayList<>();
        PerfEntityMetricBase perfEntityMetricBase = response.get(0);
        if (perfEntityMetricBase instanceof PerfEntityMetric) {
            PerfEntityMetric pem = (PerfEntityMetric) perfEntityMetricBase;
            // 时间数据
            PerfSampleInfo[] sampleInfo = pem.getSampleInfo();
            // 指标数据值
            PerfMetricSeries[] metricsValues = pem.getValue();
            for (PerfMetricSeries perfMetricSeries : metricsValues) {
                PerfMetricIntSeries perfMetricIntSeries = (PerfMetricIntSeries) perfMetricSeries;
                long[] values = perfMetricIntSeries.getValue();
                // 将时间数据数组与指标值数据数组中相同下标的元素组合成新对象的集合
                List<MetricTimeValue> metricTimeValues = IntStream.range(0, Math.min(sampleInfo.length, values.length)).mapToObj((index) -> new MetricTimeValue(String.valueOf(perfMetricIntSeries.getId().getCounterId()), sampleInfo[index].getTimestamp().getTimeInMillis(), BigDecimal.valueOf(values[index]))).collect(Collectors.toList());
                // 如果查询数据的数据间隔大于20秒，就直接返回数据，默认除了云主机查询时时20秒的间隔外，其他都是300
                if (interval > 20) {
                    result.addAll(metricTimeValues);
                } else {
                    // 根据时间戳以分钟分组，并计算每组数据值的平均值
                    List<MetricTimeValue> computerValueList = metricTimeValues.stream()
                            .collect(Collectors.groupingBy(mtv -> LocalDateTime.ofEpochSecond(mtv.getTimestamp() / 1000, 0, ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES)))
                            .entrySet().stream()
                            .map(entry -> new MetricTimeValue(String.valueOf(perfMetricIntSeries.getId().getCounterId()), entry.getKey().toEpochSecond(ZoneOffset.UTC) * 1000, calculateAverageValue(entry.getValue()))).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(computerValueList)) {
                        result.addAll(computerValueList.stream().sorted(Comparator.comparing(MetricTimeValue::getTimestamp)).collect(Collectors.toList()));
                    }
                }
            }
        }
        return result;
    }

    /**
     * 计算同一分钟的平均值
     *
     * @param metricTimeValues 同一分钟的数据集合
     * @return 集合平均值的平均值 取证
     */
    private static BigDecimal calculateAverageValue(List<MetricTimeValue> metricTimeValues) {
        BigDecimal total = metricTimeValues.stream().map(MetricTimeValue::getAverage).reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf(metricTimeValues.size()), 4, RoundingMode.HALF_UP);
    }


    /**
     * 根据vSphere版本获取指标
     * 目前6.x与7.x、8.x,有部分指标ID不一样
     * 6.x的虚拟磁盘读写速率的指标分别为173、174 而7.x 8.x的是180、181
     * 其他指标暂定是一样的，如果有发现获取不到数据的，检查一下指标是否一样，不一样就得修改对应版本的指标
     *
     * @param client 客户端
     * @return 监控指标集合
     */
    private static List<PerfMetricId> getMetricIds(VsphereVmClient client) {
        List<PerfMetricId> metricIds = new ArrayList<>();
        String vSphereVersion = client.getSi().getAboutInfo().getVersion();
        if (vSphereVersion.startsWith("6")) {
            Arrays.stream(VspherePerfMetricConstants.PerfMetricForSixEnum.values()).forEach(v -> {
                metricIds.add(createMetricId(v.getMetricName(), v.name()));
            });
        } else {
            Arrays.stream(VspherePerfMetricConstants.PerfMetricForSevenAndEightEnum.values()).forEach(v -> {
                metricIds.add(createMetricId(v.getMetricName(), v.name()));
            });
        }
        return metricIds;
    }

    /**
     * 创建查询指标对象
     *
     * @param metricName 指标
     * @param name       枚举名称
     * @return PerfMetricId
     */
    private static PerfMetricId createMetricId(String metricName, String name) {
        PerfMetricId metricId = new PerfMetricId();
        metricId.setCounterId(Integer.parseInt(metricName));
        metricId.setInstance("");
        // 磁盘指标枚举的话，只获取第一个盘的指标，冒号后面的0为系统盘，标识硬盘1
        if (name.startsWith("DISK_")) {
            metricId.setInstance("scsi0:0");
        }
        return metricId;
    }


    /**
     * 批量查询指标监控数据
     *
     * @param client    客户端
     * @param entities  查询对象
     * @param metricIds 指标集合
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 指标数据集合
     */
    private static List<PerfEntityMetricBase> getMetricsData(VsphereVmClient client, ManagedObjectReference entities, List<PerfMetricId> metricIds,
                                                             Calendar startTime, Calendar endTime, int interval) {
        List<PerfEntityMetricBase> metricsData = new ArrayList<>();
        if (ObjectUtils.isEmpty(entities) || metricIds.isEmpty()) {
            return metricsData;
        }
        try {
            PerfQuerySpec querySpec = new PerfQuerySpec();
            // 开始时间
            querySpec.setStartTime(startTime);
            // 结束时间
            querySpec.setEndTime(endTime);
            // 时间间隔
            querySpec.setIntervalId(interval);
            // 查询对象
            querySpec.setEntity(entities);
            // 指标集合
            querySpec.setMetricId(metricIds.toArray(new PerfMetricId[0]));
            PerfEntityMetricBase[] entityMetrics = client.getSi().getPerformanceManager().queryPerf(new PerfQuerySpec[]{querySpec});
            if (entityMetrics != null) {
                metricsData.addAll(Arrays.asList(entityMetrics));
            }
        } catch (Exception e) {
            LogUtil.error("查询vSphere - " + entities.getType() + "-" + entities.getVal() + " - 指标监控失败:" + e.getMessage());
            e.printStackTrace();
        }
        return metricsData;
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
                return CheckCreateServerResult.fail("模板不能为空");
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

    /**
     * 云主机配置变更
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static F2CVirtualMachine changeVmConfig(VsphereUpdateConfigRequest request) {
        VsphereVmClient client = request.getVsphereVmClient();
        String instanceId = request.getInstanceUuid();
        VirtualMachine vm;
        try {
            vm = client.getVirtualMachineById(instanceId);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        Optional.ofNullable(vm).orElseThrow(() -> new RuntimeException("Vm does not exist!InstanceId: " + instanceId));

        int cpuCount = request.getCpu();
        int cpuSockets = 1;
        long memory = request.getMemory() * 1024L;
        HostSystem host = client.getHost(vm);
        Optional.ofNullable(host).orElseThrow(() -> new RuntimeException("Host does not exist!"));
        cpuSockets = host.getSummary().getHardware().getNumCpuPkgs();

        F2CVirtualMachine currentInstance = VsphereUtil.toF2CInstance(vm, client);

        VirtualMachineConfigSpec vmConfigSpec = new VirtualMachineConfigSpec();
        if (StringUtils.isNotEmpty(request.getAnnotation())) {
            vmConfigSpec.setAnnotation(request.getAnnotation());
        }
        if (memory > 0 && memory != currentInstance.getMemory() * 1024) {
            vmConfigSpec.setMemoryMB(memory);
        }
        if (cpuCount > 0 && cpuCount != currentInstance.getCpu()) {
            vmConfigSpec.setNumCPUs(cpuCount);
            vmConfigSpec.setNumCoresPerSocket((int) Math.ceil(1.0 * cpuCount / cpuSockets));
        }

        // 获取虚机运行状态，以保证虚机运行状态不变
        VirtualMachineRuntimeInfo vmRuntime = vm.getRuntime();
        String vmStatus = VsphereUtil.getStatus(vmRuntime.getPowerState().name()).name();

        try {
            Task task = vm.reconfigVM_Task(vmConfigSpec);
            String status = task.waitForTask();
            if (!Task.SUCCESS.equals(status)) {
                throw new RuntimeException("Virtual configuration modification failed! Error message:" + task.getTaskInfo().getError().getLocalizedMessage());
            }
        } catch (Exception e) {
            // 直接修改失败，尝试关机后修改
            log.info("Change vm config error, shutdown os and try again: " + instanceId + ", error: ", e);
            if ("running".equalsIgnoreCase(vmStatus) && client.shutdownInstance(instanceId)) {
                if (client.stopVm(instanceId)) {
                    Task task;
                    String status;
                    try {
                        task = vm.reconfigVM_Task(vmConfigSpec);
                        status = task.waitForTask();
                        if (!Task.SUCCESS.equals(status)) {
                            throw new RuntimeException("Virtual configuration modification failed! Error message:" + task.getTaskInfo().getError().getLocalizedMessage());
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex.getMessage(), e);
                    }
                } else {
                    throw new RuntimeException("Failed to stop vm! InstanceId: " + instanceId);
                }
            } else {
                throw new RuntimeException("Failed to close guestOS! InstanceId: " + instanceId);
            }
        }
        if ("running".equalsIgnoreCase(vmStatus) && !client.startVm(instanceId)) {
            throw new RuntimeException("Failed to start vm! InstanceId: " + instanceId);
        }
        try {
            return VsphereUtil.toF2CInstance(client.getVirtualMachineById(instanceId), client);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String calculateConfigPrice(VsphereCalculateConfigPriceRequest request) {
        RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
        Set<String> servicesExcludeGateway = ServiceUtil.getServicesExcludeGateway();
        if (servicesExcludeGateway.contains("finance-management")) {
            String url = ServiceUtil.getHttpUrl("finance-management", "api/billing_policy/calculate_config_price/" + request.getAccountId());
            ResponseEntity<ResultHolder<List<BillPolicyDetails>>> exchange = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
            });

            List<BillPolicyDetails> billPolicyDetailsList = Objects.requireNonNull(exchange.getBody()).getData();

            String instanceChargeType = request.getInstanceChargeType();

            Optional<BigDecimal> ecs = billPolicyDetailsList.stream()
                    .map(billPolicyDetails -> {
                        if ("ECS".equals(billPolicyDetails.getResourceType())) {
                            return ChargingUtil.getBigDecimal(instanceChargeType, Map.of("cpu", request.getCpu(), "memory", request.getRam()), billPolicyDetails);
                        }
                        if ("DISK".equals(billPolicyDetails.getResourceType())) {
                            List<VsphereVmCreateRequest.DiskConfig> disks = request.getDisks();
                            return disks.stream().map(disk -> ChargingUtil.getBigDecimal(instanceChargeType, Map.of("size", disk.getSize()), billPolicyDetails))
                                    .reduce(BigDecimal::add).orElse(new BigDecimal(0));
                        }
                        return new BigDecimal(0);
                    }).reduce(BigDecimal::add);
            return ecs.map(bigDecimal -> {
                        if (ChargeTypeConstants.PREPAID.getCode().equals(instanceChargeType) && StringUtils.isNotEmpty(request.getPeriodNum())) {
                            return bigDecimal.multiply(new BigDecimal(request.getPeriodNum())).multiply(new BigDecimal(request.getCount()));
                        }
                        return bigDecimal.multiply(new BigDecimal(request.getCount()));
                    }).map(bigDecimal -> bigDecimal.setScale(3, RoundingMode.HALF_UP) +
                            (ChargeTypeConstants.POSTPAID.getCode().equals(instanceChargeType) ? "元/小时" : "元"))
                    .orElse("--");
        }
        return "-";
    }


}
