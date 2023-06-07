package com.fit2cloud.common.provider.impl.vsphere.utils;

import com.fit2cloud.common.constants.Language;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Author: LiuDi
 * Date: 2022/9/21 3:19 PM
 */
public class VsphereClient {

    private static Logger logger = LoggerFactory.getLogger(VsphereClient.class);

    private static final int connectionTimeout = 60000;
    private static final int readTimeout = 120000;

    private ServiceInstance si;
    private ManagedEntity rootEntity;
    private ManagedEntity mainEntity;
    private String dataCenterId;

    private String vCenterIp;
    private String vCenterVersion;

    private static final String CLUSTER_TYPE_NAME = "ClusterComputeResource";
    public static String FLAG_FOR_NULL_VALUE = "only-a-flag";

    public static final String FOLDER_ROOT = "ROOT";

    public ServiceInstance getSi() {
        return si;
    }

    public VsphereClient(String vcenterIp, String userName, String password,
                         String dataCenterId, Language lang) {
        try {
            synchronized (CLUSTER_TYPE_NAME) {
                si = new ServiceInstance(new URL("https://" + vcenterIp + "/sdk"), userName, password, true, connectionTimeout, readTimeout);
                vCenterVersion = si.getAboutInfo().getVersion();
                rootEntity = si.getRootFolder();
                mainEntity = si.getRootFolder();
                this.vCenterIp = vcenterIp;
                si.getSessionManager().setLocale(lang.name());
            }
        } catch (RemoteException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            if (e.toString().contains("InvalidLogin")) {
                throw new RuntimeException("Account verification failed" + e.getMessage(), e);
            }
            throw new RuntimeException("Failed to connect to Vsphere server!" + e.getMessage(), e);
        } catch (MalformedURLException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("Vsphere server address error!" + e.getMessage(), e);
        }
        updateRootEntity(dataCenterId);
    }

    public void closeConnection() {
        si.getServerConnection().logout();
    }

    public GuestOperationsManager getGuestOperationsManager() {
        return si.getGuestOperationsManager();
    }

    private void updateRootEntity(String dataCenterId) {
        if (dataCenterId != null && dataCenterId.trim().length() > 0) {
            List<Datacenter> dcList = listDataCenters();
            for (Datacenter dc : dcList) {
                if (dc.getName().equals(dataCenterId.trim())) {
                    rootEntity = dc;
                    this.dataCenterId = dataCenterId.trim();
                    break;
                }
            }
        }
    }

    public <T extends ManagedEntity> List<T> listResources(Class<T> resClass) {
        return listResources(resClass, rootEntity);
    }

    private <T extends ManagedEntity> List<T> listResourcesFromAll(Class<T> resClass) {
        return listResources(resClass, mainEntity);
    }

    private <T extends ManagedEntity> List<T> listResources(Class<T> resClass, ManagedEntity rootEntity) {
        List<T> list = new ArrayList<T>();
        try {
            ManagedEntity[] mes = new InventoryNavigator(rootEntity).searchManagedEntities(resClass.getSimpleName());
            if (mes != null) {
                for (ManagedEntity m : mes) {
                    list.add((T) m);
                }
            }
        } catch (InvalidProperty e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("invalid parameters!" + e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("Error getting resources!" + e.getLocalizedMessage(), e);
        }
        return list;
    }

    private <T extends ManagedEntity> T getResource(Class<T> resClass, String resName) {
        return getResource(resClass, resName, rootEntity);
    }

    public <T extends ManagedEntity> T getResourceFromAll(Class<T> resClass, String resName) {
        return getResource(resClass, resName, mainEntity);
    }

    private <T extends ManagedEntity> T getResource(Class<T> resClass, String resName, ManagedEntity rootEntity) {
        try {
            ManagedEntity entity = new InventoryNavigator(rootEntity).searchManagedEntity(resClass.getSimpleName(), resName);
            if (entity != null) {
                return (T) entity;
            }
        } catch (InvalidProperty e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Invalid parameters!" + e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("Error getting resources!" + e.getLocalizedMessage(), e);
        }
        return null;
    }

    public <T extends ManagedEntity> Datacenter getDataCenter(T t, Class<T> classOfT) {
        ManagedEntity parent = classOfT.cast(t).getParent();
        while (parent != null) {
            if (parent instanceof Datacenter) {
                return (Datacenter) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    /**
     * 获取全部数据中心
     *
     * @return 数据中心列表
     */
    public List<Datacenter> listDataCenters() {
        return listResources(Datacenter.class);
    }

    /**
     * 获取全部存储
     *
     * @return
     */
    public List<Datastore> listDataStores() {
        return listResources(Datastore.class);
    }

    /**
     * 根据名称获取数据中心
     *
     * @param name 名称
     * @return 数据中心实例
     */
    public Datacenter getDataCenter(String name) {
        return getResource(Datacenter.class, name);
    }

    /**
     * 根据虚拟机获取数据中心
     *
     * @param vm 虚拟机实例
     * @return 数据中心实例
     */
    public Datacenter getDataCenter(VirtualMachine vm) {
        return getDataCenter(vm, VirtualMachine.class);
    }

    /**
     * 根据宿主机获取数据中心
     *
     * @param host 宿主机实例
     * @return 数据中心实例
     */
    public Datacenter getDataCenter(HostSystem host) {
        return getDataCenter(host, HostSystem.class);
    }

    /**
     * 根据存储器获取数据中心
     *
     * @param datastore 存储器实例
     * @return 数据中心实例
     */
    public Datacenter getDataCenter(Datastore datastore) {
        ManagedEntity parent = datastore.getParent();
        while (parent != null) {
            if (parent instanceof Datacenter) {
                return (Datacenter) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    /**
     * 获取全部宿主机
     *
     * @return 宿主机列表
     */
    public List<HostSystem> listHostsFromAll() {
        return listResourcesFromAll(HostSystem.class);
    }

    public List<HostSystem> listHosts() {
        return listResources(HostSystem.class);
    }

    /**
     * 根据虚拟机获取宿主机
     *
     * @param vm 虚拟机实例
     * @return 宿主机实例
     */
    public HostSystem getHost(VirtualMachine vm) {
        if (vm == null) {
            return null;
        }
        ManagedObjectReference hostMor = vm.getRuntime().getHost();
        if (hostMor != null) {
            List<HostSystem> hosts = listHostsFromAll();
            for (HostSystem host : hosts) {
                if (host.getMOR() == null || hostMor.getVal() == null) {
                    return host;
                }
                if (hostMor.getVal().equals(host.getMOR().getVal())) {
                    return host;
                }
            }
        }
        return null;
    }

    /**
     * 获取计算资源
     *
     * @param name 名称
     * @return
     */
    public ComputeResource getComputeResource(String name) {
        return getResource(ComputeResource.class, name);
    }

    /**
     * 获取计算资源
     *
     * @param host 宿主机实例
     * @return ComputeResource 实例
     */
    public ComputeResource getComputeResource(HostSystem host) {
        ComputeResource cr = getComputeResource(host.getName());
        if (cr == null) {
            ManagedEntity parent = host.getParent();
            while (parent != null) {
                if (parent instanceof ComputeResource) {
                    return (ComputeResource) parent;
                }
                parent = parent.getParent();
            }
        }
        return cr;
    }

    /**
     * 根据UUID查询资源
     *
     * @param instanceUuid
     * @return
     */
    public VirtualMachine getVirtualMachineByUuId(String instanceUuid) {
        VimPortType vimService = si.getServerConnection().getVimService();
        ServiceContent serviceContent = si.getServiceContent();
        try {
            ManagedObjectReference vmMor = vimService.findByUuid(serviceContent.getSearchIndex(), null, instanceUuid, true, true);
            if (vmMor == null) {
                throw new RuntimeException("No such machine found! - " + instanceUuid);
            }
            return new VirtualMachine(si.getServerConnection(), vmMor);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public ClusterComputeResource getCluster(String name) {
        return getResource(ClusterComputeResource.class, name);
    }

    public List<ClusterComputeResource> listClusters() {
        return listResources(ClusterComputeResource.class);
    }

    public List<Network> listNetworks() {
        return listResources(Network.class);
    }

    public HostSystem getHost(String hostName) {
        return getResource(HostSystem.class, hostName);
    }

    public Datastore getDatastore(String name, Datacenter datacenter) throws RuntimeException {
        return getResource(Datastore.class, name, datacenter);
    }

    public VirtualMachine getVirtualMachine(String name) throws RuntimeException {
        return getResource(VirtualMachine.class, name);
    }


    public StoragePod getStoragePod(String name) {
        return getResource(StoragePod.class, name);
    }

    public Folder getFolder(String name) {
        name = StringUtils.strip(name, "/");
        if (!StringUtils.contains(name, "/")) {
            return getResource(Folder.class, name);
        }
        String s = StringUtils.substringBefore(name, "/");
        Folder root = getResource(Folder.class, s);

        StringTokenizer stringTokenizer = new StringTokenizer(name, "/");
        stringTokenizer.nextToken();
        while (stringTokenizer.hasMoreTokens()) {
            Folder resource = getResource(Folder.class, stringTokenizer.nextToken(), root);
            if (resource == null) {
                return null;
            }
            root = resource;
        }
        return root;
    }

    public ResourcePool getResourcePool(String name) {
        name = StringUtils.strip(name, "/");
        if (!StringUtils.contains(name, "/")) {
            return getResource(ResourcePool.class, name);
        }
        String s = StringUtils.substringBefore(name, "/");
        ResourcePool root = getResource(ResourcePool.class, s);

        StringTokenizer stringTokenizer = new StringTokenizer(name, "/");
        stringTokenizer.nextToken();
        while (stringTokenizer.hasMoreTokens()) {
            ResourcePool resource = getResource(ResourcePool.class, stringTokenizer.nextToken(), root);
            if (resource == null) {
                return null;
            }
            root = resource;
        }
        return root;
    }

    public VirtualMachine getVirtualMachineById(String instanceId) throws Exception {
        // 正则判断虚拟机的ID是不是uuid
        if (!instanceId.matches("([0-9a-f]{8}(-?[0-9a-f]{4}){3}-?[0-9a-f]{12}?)")) {
            return getVirtualMachine(instanceId);
        }
        VimPortType vimService = this.getSi().getServerConnection().getVimService();
        ServiceContent serviceContent = this.getSi().getServiceContent();
        ManagedObjectReference vmMor = vimService.findByUuid(serviceContent.getSearchIndex(), null, instanceId, true, true);
        if (vmMor == null) {
            return null;
        }
        return new VirtualMachine(this.getSi().getServerConnection(), vmMor);
    }

    public List<VirtualDisk> getVirtualDisks(VirtualMachine vm) {
        List<VirtualDisk> disks = new ArrayList<>();
        VirtualMachineConfigInfo config = vm.getConfig();
        if (config != null) {
            VirtualHardware hardware = config.getHardware();
            VirtualDevice[] devices = hardware.getDevice();
            if (devices != null && devices.length > 0) {
                for (VirtualDevice device : devices) {
                    if (device instanceof VirtualDisk) {
                        disks.add((VirtualDisk) device);
                    }
                }
            }
        }
        return disks;
    }

    public boolean hasVmTools(VirtualMachine vm) {
        GuestInfo guest = vm.getGuest();
        return guest != null;
    }

    public List<Folder> listFolders() {
        List<Folder> folderList = new ArrayList<>();
        for (Folder f : listResources(Folder.class)) {
            if (f.getParent() != null && f.getParent().getName().equalsIgnoreCase("vm")) {
                folderList.add(f);
            }
        }
        return folderList;
    }

    public <T> T[] getChildResource(Class<T> resClass, ManagedEntity parentEntity) {
        try {
            ManagedEntity[] entities = new InventoryNavigator(parentEntity).searchManagedEntities(resClass.getSimpleName());
            if (entities != null && entities.length > 0) {
                return Arrays.asList(entities).toArray((T[]) Array.newInstance(resClass, entities.length));
            }
        } catch (InvalidProperty e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("invalid parameters!" + e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("Error getting resources!" + e.getLocalizedMessage(), e);
        }
        return null;
    }

    public String getVCenterVersion() {
        return si.getAboutInfo().getVersion();
    }

    public Network getNetworkByMor(String networkMor) {
        List<Network> nets = listNetworks();
        if (nets != null) {
            for (Network net : nets) {
                if (net.getMOR().getVal().equals(networkMor)) {
                    return net;
                }
            }
        }
        return null;
    }

    public double version() {
        String[] split = StringUtils.split(vCenterVersion, ".");
        ArrayUtils.reverse(split);
        double versionNum = 0;
        for (int i = 0, length = split.length; i < length; i++) {
            versionNum += Double.valueOf(split[i]) * Math.pow(10, i);
        }
        return versionNum;
    }

    public boolean isUseCustomSpec() {
        return version() >= 550;
    }
}
