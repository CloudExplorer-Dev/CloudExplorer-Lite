package com.fit2cloud.provider.impl.proxmox.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.provider.impl.proxmox.client.PveClient;
import com.fit2cloud.common.provider.impl.proxmox.client.Result;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.*;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.config.Config;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.config.DiskStatusInfo;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.config.NetworkInterface;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.entity.response.F2CDatastore;
import com.fit2cloud.provider.entity.response.F2CDisk;
import com.fit2cloud.provider.entity.response.F2CHost;
import com.fit2cloud.provider.entity.response.F2CVirtualMachine;
import com.fit2cloud.provider.impl.proxmox.entity.credential.ProxmoxComplianceCredential;
import com.fit2cloud.provider.impl.proxmox.entity.request.ProxmoxBaseRequest;
import com.fit2cloud.provider.impl.proxmox.util.MappingUtil;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/8/3  14:34}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class ProxmoxApi {

    /**
     * 获取虚拟机列表
     *
     * @param request 请求对象
     * @return 虚拟机列表
     */
    public static List<F2CVirtualMachine> listVirtualMachine(ProxmoxBaseRequest request) {
        ProxmoxComplianceCredential credential = request.getCredential();
        PveClient client = credential.getClient();
        List<Qemu> qemuList = listVmQemu(client, request.getRegionId());
        Cluster cluster = getCluster(client);
        ClusterNode clusterNode = getClusterNode(client, request.getRegionId());
        return qemuList.stream().map(qemu -> {
            Config qemuConfig = MappingUtil.ofError(() -> getQemuConfig(client, request.getRegionId(), qemu.getVmid() + ""), "获取虚拟机配置失败:{" + qemu.getVmid() + "},跳过当前虚拟机同步");
            if (Objects.isNull(qemuConfig)) {
                return null;
            }
            List<NetworkInterface> network = MappingUtil.ofError(() -> getNetwork(client, request.getRegionId(), qemu.getVmid() + ""), "获取网卡失败:{" + qemu.getVmid() + "},跳过当前虚拟机同步");
            return MappingUtil.ofError(() -> MappingUtil.toF2CVirtualMachine(qemu, qemuConfig, Objects.isNull(cluster) ? null : cluster.getName(), clusterNode, network), "转换虚拟机对象是失败:{" + qemu.getVmid() + "},跳过当前虚拟机同步");
        }).filter(Objects::nonNull).toList();
    }

    /**
     * 获取磁盘列表
     *
     * @param request 请求对象
     * @return 磁盘列表
     */
    public static List<Disk> listDisk(ProxmoxBaseRequest request) {
        ProxmoxComplianceCredential credential = request.getCredential();
        PveClient client = credential.getClient();
        return listDataStore(client, request.getRegionId())
                .stream()
                .map(dataStore -> listDisk(client, request.getRegionId(), dataStore.getStorage())).flatMap(List::stream).toList();
    }

    /**
     * 获取 磁盘详情列表
     *
     * @param request 请求对象
     * @return 磁盘详情列表
     */
    public static List<F2CDisk> listF2CDisk(ProxmoxBaseRequest request) {
        ProxmoxComplianceCredential credential = request.getCredential();
        PveClient client = credential.getClient();
        List<Disk> allDisk = listDisk(request);
        List<Qemu> qemuList = listQemu(client, request.getRegionId());
        List<DiskStatusInfo> mountDiskVolIdList = qemuList.stream().map(qemu -> {
            Config qemuConfig = getQemuConfig(client, request.getRegionId(), qemu.getVmid() + "");
            List<DiskStatusInfo> result = new ArrayList<>();
            result.addAll(qemuConfig.getDisks().stream()
                    .filter(disk -> StringUtils.isEmpty(disk.getMedia()))
                    .map(disk -> new DiskStatusInfo(disk.getVolId(), disk.isBoot(), true, disk.getDevice(), qemu, Objects.isNull(qemuConfig.getMeta()) ? "" : qemuConfig.getVmGenId())).toList());
            result.addAll(qemuConfig.getUnuseds().stream().map(unUsed -> new DiskStatusInfo(unUsed.getValue(), false, false, null, qemu, Objects.isNull(qemuConfig.getMeta()) ? "" : qemuConfig.getVmGenId())).toList());
            return result;
        }).flatMap(List::stream).toList();
        return mountDiskVolIdList.stream().map(diskStatusInfo -> allDisk.stream()
                        .filter(disk -> StringUtil.equals(diskStatusInfo.getVolId(), disk.getVolid()))
                        .findFirst()
                        .map(disk -> MappingUtil.ofError(() -> MappingUtil.toF2CDisk(diskStatusInfo, disk, request.getRegionId()), null))
                        .orElse(null))
                .filter(Objects::nonNull).toList();
    }

    /**
     * 获取存储器列表
     *
     * @param proxmoxBaseRequest 请求对象
     * @return 存储器列表
     */
    public static List<F2CDatastore> listF2CDatastore(ProxmoxBaseRequest proxmoxBaseRequest) {
        ProxmoxComplianceCredential credential = proxmoxBaseRequest.getCredential();
        PveClient client = credential.getClient();
        List<DataStore> dataStores = listDataStore(client, proxmoxBaseRequest.getRegionId());
        Cluster cluster = getCluster(client);
        return dataStores.stream().map(dataStore -> {
                    List<Disk> disks = listDisk(client, proxmoxBaseRequest.getRegionId(), dataStore.getStorage());
                    F2CDatastore f2CDatastore = new F2CDatastore();
                    f2CDatastore.setType(dataStore.getType());
                    f2CDatastore.setClusterName(Objects.nonNull(cluster) ? cluster.getName() : null);
                    f2CDatastore.setClusterId(Objects.nonNull(cluster) ? cluster.getName() : null);
                    f2CDatastore.setCapacity(dataStore.getTotal() / 1024 / 1024 / 1024);
                    f2CDatastore.setFreeSpace(dataStore.getAvail() / 1024 / 1024 / 1024);
                    f2CDatastore.setRegion(proxmoxBaseRequest.getRegionId());
                    f2CDatastore.setAllocatedSpace(disks.stream().map(Disk::getSize).mapToLong(Long::longValue).sum() / 1024 / 1024 / 1024);
                    f2CDatastore.setZone(proxmoxBaseRequest.getRegionId());
                    f2CDatastore.setDataStoreId(dataStore.getStorage());
                    f2CDatastore.setDataStoreName(dataStore.getStorage());
                    return f2CDatastore;
                })
                .toList();

    }

    /**
     * 获取磁盘 列表
     *
     * @param client 客户端
     * @param node   节点
     * @param store  存储
     * @return 磁盘列表
     */
    public static List<Disk> listDisk(PveClient client, String node, String store) {
        Result index = client.getNodes().get(node).getStorage().get(store).getContent().index();
        if (index.getStatusCode() == 200) {
            String data = index.getResponse().getJSONArray("data").toString();
            return JsonUtil.parseArray(data, Disk.class);
        }
        return List.of();
    }

    /**
     * 获取存储器列表
     *
     * @param client 客户端
     * @param node   节点
     * @return 存储器列表
     */
    public static List<DataStore> listDataStore(PveClient client, String node) {
        Result index = client.getNodes().get(node).getStorage().index(Map.of("format", 1, "content", "images"));
        if (index.getStatusCode() == 200) {
            return JsonUtil.parseArray(index.getResponse().getJSONArray("data").toString(), DataStore.class).stream().filter(dataStore -> dataStore.getContent().contains("images")).toList();
        }
        return List.of();
    }

    /**
     * 获取网络信息
     *
     * @param client 客户端
     * @param node   节点
     * @param vmId   虚拟机id
     * @return 网络信息
     */
    public static List<NetworkInterface> getNetwork(PveClient client, String node, String vmId) {
        Result result = client.getNodes().get(node).getQemu().get(vmId).getAgent().getNetworkGetInterfaces().networkGetInterfaces();
        if (result.getStatusCode() == 500) {
            return List.of();
        } else {
            try {
                JSONArray jsonArray = result.getResponse().getJSONObject("data").getJSONArray("result");
                return JsonUtil.parseArray(jsonArray.toString(), NetworkInterface.class);
            } catch (Exception e) {
                return List.of();
            }
        }
    }

    /**
     * 获取虚拟机信息列表
     *
     * @param client 客户端
     * @param node   节点
     * @return 虚拟机信息列表
     */
    public static List<Qemu> listVmQemu(PveClient client, String node) {
        return listQemu(client, node)
                .stream()
                .filter(qemu -> Objects.isNull(qemu.getTemplate()) || (Objects.nonNull(qemu.getTemplate()) && !qemu.getTemplate().equals(1)))
                .toList();
    }

    /**
     * 获取指定节点的所有虚拟机
     *
     * @param client 客户端
     * @param node   节点
     * @return 虚拟机列表
     */
    private static List<Qemu> listQemu(PveClient client, String node) {
        Result vmList = client.getNodes().get(node).getQemu().vmlist();
        JSONObject response = vmList.getResponse();
        if (!response.isNull("data")) {
            String data = response.getJSONArray("data").toString();
            return JsonUtil.parseArray(data, Qemu.class);
        }
        return List.of();
    }

    /**
     * 获取集群信息
     *
     * @param client 客户端
     * @return 集群信息
     */
    private static Cluster getCluster(PveClient client) {
        Result status = client.getCluster().getStatus().getStatus();
        JSONObject response = status.getResponse();
        if (!response.isNull("data")) {
            for (Object data : response.getJSONArray("data")) {
                String dataString = data.toString();
                ParentClusterNode parentClusterNode = JsonUtil.parseObject(dataString, ParentClusterNode.class);
                if (StringUtil.equals(parentClusterNode.getType(), "cluster")) {
                    return JsonUtil.parseObject(dataString, Cluster.class);
                }
            }
        }
        return null;
    }

    /**
     * 获取集群节点详情
     *
     * @param client 客户端
     * @param node   节点
     * @return 节点详情
     */
    private static ClusterNode getClusterNode(PveClient client, String node) {
        List<ClusterNode> clusterNodeList = getClusterNodeList(client);
        return clusterNodeList.stream().filter(clusterNode -> clusterNode.getName().equals(node)).findFirst().orElseThrow(() -> new Fit2cloudException(404, "节点不存在"));

    }

    /**
     * 获取节点列表
     *
     * @param client 客户端
     * @return 节点列表
     */
    public static List<ClusterNode> getClusterNodeList(PveClient client) {
        List<ClusterNode> clusterNodes = new ArrayList<>();
        Result status = client.getCluster().getStatus().getStatus();
        JSONObject response = status.getResponse();
        if (!response.isNull("data")) {
            for (Object data : response.getJSONArray("data")) {
                String dataString = data.toString();
                ParentClusterNode parentClusterNode = JsonUtil.parseObject(dataString, ParentClusterNode.class);
                if (StringUtil.equals(parentClusterNode.getType(), "node")) {
                    ClusterNode clusterNode = JsonUtil.parseObject(dataString, ClusterNode.class);
                    clusterNodes.add(clusterNode);
                }
            }
        }
        return clusterNodes;
    }

    /**
     * 获取虚拟机 配置
     *
     * @param client 客户端
     * @param node   节点
     * @param vmId   虚拟机id
     * @return 虚拟机配置
     */
    public static Config getQemuConfig(PveClient client, String node, String vmId) {
        JSONObject response = client.getNodes().get(node).getQemu().get(vmId).getConfig().vmConfig().getResponse();
        if (!response.isNull("data")) {
            String data = response.getJSONObject("data").toString();
            return new Config(JsonUtil.parseObject(data, new TypeReference<>() {
            }));

        }
        throw new Fit2cloudException(404, "获取主机配置失败");
    }

    /**
     * 获取 宿主机详情列表
     *
     * @param request 请求对象
     * @return 宿主机详情列表
     */
    public static List<F2CHost> listF2CHost(ProxmoxBaseRequest request) {
        ProxmoxComplianceCredential credential = request.getCredential();
        PveClient client = credential.getClient();
        List<Host> hostList = getHostList(client).stream().filter(host -> StringUtil.equals(host.getNode().getNode(), request.getRegionId())).toList();
        List<Qemu> qemuList = listVmQemu(client, request.getRegionId());
        return hostList.stream().map(host -> MappingUtil.toF2CHost(host, qemuList)).toList();
    }

    /**
     * 获取集群节点 也就是主机host
     *
     * @param client 客户端
     * @return 集群(主机节点)列表
     */
    public static List<Host> getHostList(PveClient client) {
        List<ClusterNode> clusterNodeList = getClusterNodeList(client);
        Cluster cluster = getCluster(client);
        List<Node> nodeList = getNodeList(client);
        return clusterNodeList.stream().map(clusterNode -> nodeList.stream().filter(node -> StringUtil.equals(node.getId(), clusterNode.getId())).findFirst().map(item -> {
            NodeStatus nodeStatus = getNodeStatus(client, item.getNode());
            if (Objects.isNull(nodeStatus)) {
                return null;
            }
            Host host = new Host();
            host.setClusterNode(clusterNode);
            host.setCluster(cluster);
            host.setNode(item);
            host.setNodeStatus(nodeStatus);
            return host;
        }).orElse(null)).filter(Objects::nonNull).toList();

    }

    /**
     * 获取pve节点
     *
     * @param client pve客户端
     * @return 节点
     */
    private static List<Node> getNodeList(PveClient client) {
        PveClient.PVENodes nodes = client.getNodes();
        String data = nodes.index().getResponse().getJSONArray("data").toString();
        return JsonUtil.parseArray(data, Node.class);
    }

    /**
     * 获取节点详情信息
     *
     * @param client 客户端
     * @param node   节点id
     * @return 节点详情信息
     */
    private static NodeStatus getNodeStatus(PveClient client, String node) {
        try {
            Result status = client.getNodes().get(node).getStatus().status();
            return JsonUtil.parseObject(status.getResponse().get("data").toString(), NodeStatus.class);
        } catch (Exception e) {
            return null;
        }
    }

}
