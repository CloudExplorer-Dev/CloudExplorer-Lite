package com.fit2cloud.provider.impl.vsphere.api;

import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.platform.credential.impl.VsphereCredential;
import com.fit2cloud.common.provider.impl.vsphere.utils.VsphereClient;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.impl.vsphere.entity.F2CVsphereDatastore;
import com.fit2cloud.provider.impl.vsphere.entity.F2CVsphereHost;
import com.fit2cloud.provider.impl.vsphere.entity.F2CVsphereNetwork;
import com.fit2cloud.provider.impl.vsphere.entity.request.VsphereNetworkRequest;
import com.fit2cloud.provider.impl.vsphere.entity.request.VsphereVmBaseRequest;
import com.fit2cloud.provider.impl.vsphere.entity.request.VsphereVmPowerRequest;
import com.fit2cloud.provider.impl.vsphere.util.ContentLibraryUtil;
import com.fit2cloud.provider.impl.vsphere.util.ResourceConstants;
import com.fit2cloud.provider.impl.vsphere.util.VsphereUtil;
import com.fit2cloud.provider.impl.vsphere.util.VsphereVmClient;
import com.vmware.vim25.ConfigTarget;
import com.vmware.vim25.DistributedVirtualPortgroupInfo;
import com.vmware.vim25.VirtualDisk;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.mo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Author: LiuDi
 * Date: 2022/9/21 2:39 PM
 */
public class VsphereSyncCloudApi {

    private static Logger logger = LoggerFactory.getLogger(VsphereSyncCloudApi.class);

    /**
     * 获取虚拟机
     *
     * @param req
     * @return 虚拟机列表
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
            if (client != null) {
                client.closeConnection();
            }
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
        VsphereVmClient client = null;
        try {
            client = req.getVsphereVmClient();
            return client.powerOff(req.getUuid());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            if (client != null) {
                client.closeConnection();
            }
        }
    }

    public static boolean powerOn(VsphereVmPowerRequest req) {
        VsphereVmClient client = null;
        try {
            client = req.getVsphereVmClient();
            return client.powerOn(req.getUuid());
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            if (client != null) {
                client.closeConnection();
            }
        }
    }

    public static boolean shutdownInstance(VsphereVmPowerRequest req) {
        VsphereVmClient client = null;
        try {
            client = req.getVsphereVmClient();
            client.shutdownInstance(req.getUuid());
            return true;
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            if (client != null) {
                client.closeConnection();
            }
        }
    }

    public static boolean reboot(VsphereVmPowerRequest req) {
        VsphereVmClient client = null;
        try {
            client = req.getVsphereVmClient();
            client.reboot(req.getUuid());
            return true;
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            if (client != null) {
                client.closeConnection();
            }
        }
    }

    public static boolean deleteInstance(VsphereVmPowerRequest req) {
        VsphereVmClient client = null;
        try {
            client = req.getVsphereVmClient();
            client.deleteInstance(req.getUuid());
            return true;
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
            if (!network.getMOR().getType().toLowerCase().equals("network")) {
                desc = "(dvSwitch)";
            }
            if (!uplinkPortgroup) {
                networks.add(new F2CVsphereNetwork().setName(network.getName()).setDescription(desc).setId(network.getMOR().getVal()));
            }
        }
        return networks;
    }
}
