package com.fit2cloud.provider.impl.vsphere.api;

import com.fit2cloud.common.provider.impl.vsphere.utils.VsphereClient;
import com.fit2cloud.provider.entity.response.F2CDatastore;
import com.fit2cloud.provider.entity.response.F2CHost;
import com.fit2cloud.provider.entity.response.F2CVirtualMachine;
import com.fit2cloud.provider.entity.response.VsphereResourcePool;
import com.fit2cloud.provider.impl.vsphere.entity.request.ListDataStoreInstanceRequest;
import com.fit2cloud.provider.impl.vsphere.entity.request.ListEcsInstanceRequest;
import com.fit2cloud.provider.impl.vsphere.entity.request.ListHostInstanceRequest;
import com.fit2cloud.provider.impl.vsphere.entity.request.ListResourcePoolRequest;
import com.fit2cloud.provider.impl.vsphere.entity.response.F2CVsphereHost;
import com.fit2cloud.provider.impl.vsphere.util.VsphereUtil;
import com.fit2cloud.provider.impl.vsphere.util.VsphereVmClient;
import com.vmware.vim25.DatastoreInfo;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.ResourcePoolResourceUsage;
import com.vmware.vim25.ResourcePoolRuntimeInfo;
import com.vmware.vim25.mo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/6  10:45}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Slf4j
public class VsphereApi {
    private static final long MB = 1024 * 1024;
    private static final long GB = MB * 1024;

    /**
     * 获取云主机
     *
     * @param req 请求对象
     * @return 云主机列表
     */
    public static List<F2CVirtualMachine> listVirtualMachine(ListEcsInstanceRequest req) {
        List<F2CVirtualMachine> list = new ArrayList<>();
        VsphereVmClient client = null;
        try {
            client = req.getCredential().getVsphereVmClient(req.getRegionId());
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
            if (Objects.nonNull(client)) {
                client.closeConnection();
            }
        }
        return list;
    }

    /**
     * 获取宿主机
     *
     * @param req 请求对象
     * @return 宿主机列表数据
     */
    public static List<F2CHost> listHost(ListHostInstanceRequest req) {
        VsphereClient client = req.getCredential().getVsphereVmClient(req.getRegionId());
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

    /**
     * 获取存储器
     *
     * @param req 请求对象
     * @return 存储器列表数据
     */
    public static List<F2CDatastore> listDataStore(ListDataStoreInstanceRequest req) {
        List<F2CDatastore> datastoreList = new ArrayList<>();
        VsphereClient client = null;
        try {
            client = req.getCredential().getVsphereVmClient(req.getRegionId());
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
     * 获取资源池列表
     *
     * @param request 请求对象
     * @return 资源池列表
     */
    public static List<VsphereResourcePool> listResourcePool(ListResourcePoolRequest request) {
        VsphereVmClient client = null;
        try {
            List<VsphereResourcePool> list = new ArrayList<>();
            client = request.getCredential().getVsphereVmClient(request.getRegionId());
            List<ClusterComputeResource> clusters = client.listClusters();
            if (CollectionUtils.isNotEmpty(clusters)) {
                for (ClusterComputeResource cluster : clusters) {
                    ResourcePool basePool = cluster.getResourcePool();
                    List<ResourcePool> pools = List.of(basePool.getResourcePools());
                    if (CollectionUtils.isNotEmpty(pools)) {
                        for (ResourcePool pool : pools) {
                            List<VsphereResourcePool> childResourcePools = listChildResourcePool(pool, "");
                            list.addAll(childResourcePools);
                        }
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

    /**
     * 获取指定资源池下面的所有子资源池
     *
     * @param pool   指定资源池
     * @param parent 用于递归记录上级资源池path
     * @return 资源池列表
     * @throws Exception 可能发生异常 在获取子资源池的时候
     */
    public static List<VsphereResourcePool> listChildResourcePool(ResourcePool pool, String parent) throws Exception {
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
                    result.addAll(listChildResourcePool(childPool, parent + pool.getName()));
                }
            }
        }
        return result;
    }

    /**
     * 关闭连接
     *
     * @param client 客户端
     */
    private static void closeConnection(VsphereClient client) {
        if (Objects.nonNull(client)) {
            client.closeConnection();
        }
    }

}
