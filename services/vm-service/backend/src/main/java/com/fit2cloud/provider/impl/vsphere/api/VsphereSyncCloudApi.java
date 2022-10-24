package com.fit2cloud.provider.impl.vsphere.api;

import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.platform.credential.impl.VsphereCredential;
import com.fit2cloud.common.provider.impl.vsphere.utils.VsphereClient;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.impl.vsphere.entity.*;
import com.fit2cloud.provider.impl.vsphere.entity.request.*;
import com.fit2cloud.provider.impl.vsphere.util.*;
import com.vmware.vim25.*;
import com.fit2cloud.provider.impl.vsphere.entity.*;
import com.fit2cloud.provider.impl.vsphere.entity.request.VsphereNetworkRequest;
import com.fit2cloud.provider.impl.vsphere.entity.request.VsphereVmBaseRequest;
import com.fit2cloud.provider.impl.vsphere.entity.request.VsphereVmPowerRequest;
import com.fit2cloud.provider.impl.vsphere.util.ContentLibraryUtil;
import com.fit2cloud.provider.impl.vsphere.util.ResourceConstants;
import com.fit2cloud.provider.impl.vsphere.util.VsphereUtil;
import com.fit2cloud.provider.impl.vsphere.util.VsphereVmClient;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Author: LiuDi
 * Date: 2022/9/21 2:39 PM
 */
public class VsphereSyncCloudApi {

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
                f2CImageList.add(new F2CImage(id, name, desc, os, dataCenterName, VsphereUtil.getTemplateDiskSizeInGB(client, name), null));
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

    public static List<VsphereTemplate> getTemplates(VsphereVmBaseRequest req) {
        VsphereVmClient client = null;
        try {
            List<VsphereTemplate> templates = new ArrayList<>();

            //req.setRegionId(null);
            client = req.getVsphereVmClient();

            List<VirtualMachine> list = client.listTemplates();
            for (VirtualMachine vm : list) {
                int tmpDiskSize = 0;
                VirtualMachineConfigInfo conf = vm.getConfig();
                if (conf != null) {
                    VirtualDevice[] devices = conf.getHardware().getDevice();
                    for (VirtualDevice device : devices) {
                        if (device instanceof VirtualDisk) {
                            VirtualDisk vd = (VirtualDisk) device;
                            tmpDiskSize += Math.round(vd.getCapacityInKB() / (1024 * 1024.0));
                        }
                    }
                }
                templates.add(new VsphereTemplate(vm.getName(), client.hasVmTools(vm), tmpDiskSize));
            }

            templates.sort((o1, o2) -> {
                if (o1 == null) {
                    return -1;
                }
                if (o2 == null) {
                    return 1;
                }
                return o1.getName().compareTo(o2.getName());
            });
            return templates;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (client != null) {
                client.closeConnection();
            }
        }
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
}
