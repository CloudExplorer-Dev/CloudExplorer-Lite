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
import com.fit2cloud.provider.impl.proxmox.entity.ProxmoxActionBaseRequest;
import com.fit2cloud.provider.impl.proxmox.entity.credential.VmProxmoxCredential;
import com.fit2cloud.provider.impl.proxmox.entity.request.ProxmoxBaseRequest;
import com.fit2cloud.provider.impl.proxmox.util.MappingUtil;
import com.fit2cloud.vm.entity.F2CDisk;
import com.fit2cloud.vm.entity.F2CHost;
import com.fit2cloud.vm.entity.F2CImage;
import com.fit2cloud.vm.entity.F2CVirtualMachine;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/10  16:36}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class SyncApi {


    /**
     * 获取镜像模板
     *
     * @param proxmoxBaseRequest 请求对象
     * @return 镜像模板列表
     */
    public static List<F2CImage> listImage(ProxmoxBaseRequest proxmoxBaseRequest) {
        VmProxmoxCredential credential = JsonUtil.parseObject(proxmoxBaseRequest.getCredential(), VmProxmoxCredential.class);
        PveClient client = credential.getClient();
        List<Qemu> qemuList = listQemu(client, proxmoxBaseRequest.getRegionId());
        return qemuList.stream().filter(qemu -> Objects.nonNull(qemu.getTemplate()) && qemu.getTemplate().equals(1)).toList().stream().map(qemu -> {
            Config qemuConfig = getQemuConfig(client, proxmoxBaseRequest.getRegionId(), qemu.getVmid() + "");
            return MappingUtil.toF2CImage(qemu, qemuConfig, proxmoxBaseRequest.getRegionId());
        }).toList();
    }


    public static Config getQemuConfig(ProxmoxBaseRequest request, Qemu qemu) {
        VmProxmoxCredential credential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        return getQemuConfig(credential, request.getRegionId(), qemu.getVmid().toString());
    }


    public static Config getQemuConfig(VmProxmoxCredential credential, String node, String vmId) {
        return getQemuConfig(credential.getClient(), node, vmId);
    }

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
     * 获取虚拟机列表
     *
     * @param request 请求对象
     * @return 虚拟机列表
     */
    public static List<F2CVirtualMachine> listVirtualMachine(ProxmoxBaseRequest request) {
        VmProxmoxCredential credential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        PveClient client = credential.getClient();
        List<Qemu> qemuList = listQemu(client, request.getRegionId())
                .stream()
                .filter(qemu -> Objects.isNull(qemu.getTemplate()) || (Objects.nonNull(qemu.getTemplate()) && !qemu.getTemplate().equals(1)))
                .toList();
        Cluster cluster = getCluster(client);
        ClusterNode clusterNode = getClusterNode(credential, request.getRegionId());
        return qemuList.stream().map(qemu -> {
            Config qemuConfig = getQemuConfig(client, request.getRegionId(), qemu.getVmid() + "");
            List<NetworkInterface> network = getNetwork(client, request.getRegionId(), qemu.getVmid() + "");
            return MappingUtil.toF2CVirtualMachine(qemu, qemuConfig, cluster.getName(), clusterNode, network);
        }).toList();
    }

    /**
     * 获取所有的 虚拟机
     *
     * @param proxmoxBaseRequest 请求对象
     * @return 虚拟机列表
     */
    public static List<Qemu> listQemu(ProxmoxBaseRequest proxmoxBaseRequest) {
        VmProxmoxCredential credential = JsonUtil.parseObject(proxmoxBaseRequest.getCredential(), VmProxmoxCredential.class);
        return listQemu(credential.getClient(), proxmoxBaseRequest.getRegionId());
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
     * 获取虚拟机 根据虚拟机id
     *
     * @param credential 认证对象
     * @param node       节点
     * @param vmId       虚拟机id
     * @return 虚拟机对象
     */
    public static Qemu getQemuById(VmProxmoxCredential credential, String node, String vmId) {
        return getQemuById(credential.getClient(), node, vmId);
    }

    public static Qemu getQemuById(PveClient client, String node, String vmId) {
        Result result = client.getNodes().get(node).getQemu().get(vmId).getStatus().getCurrent().vmStatus();
        if (result.getResponse().isNull("data")) {
            throw new Fit2cloudException(404, "虚拟机不存在");
        } else {
            String data = result.getResponse().getJSONObject("data").toString();
            return JsonUtil.parseObject(data, Qemu.class);
        }
    }

    public static F2CVirtualMachine getF2CVirtualMachineById(VmProxmoxCredential credential, String node, String vmId) {
        PveClient client = credential.getClient();
        return getF2CVirtualMachineById(client, node, vmId);
    }

    public static F2CVirtualMachine getF2CVirtualMachineById(PveClient client, String node, String vmId) {
        Qemu qemu = getQemuById(client, node, vmId);
        Config qemuConfig = getQemuConfig(client, node, vmId);
        Cluster cluster = getCluster(client);
        ClusterNode clusterNode = getClusterNode(client, node);
        List<NetworkInterface> network = getNetwork(client, node, vmId);
        return MappingUtil.toF2CVirtualMachine(qemu, qemuConfig, cluster.getName(), clusterNode, network);

    }

    public static List<NetworkInterface> getNetwork(VmProxmoxCredential credential, String node, String vmId) {
        return getNetwork(credential.getClient(), node, vmId);

    }

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

    public static boolean agentIsRun(PveClient client, String node, String vmId) {
        Result result = client.getNodes().get(node).getQemu().get(vmId).getAgent().getNetworkGetInterfaces().networkGetInterfaces();
        if (result.getStatusCode() == 500) {
            return false;
        }
        return true;
    }

    public static Cluster getCluster(VmProxmoxCredential credential) {
        return getCluster(credential.getClient());
    }

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
        throw new Fit2cloudException(1, "集群不存在");
    }

    public static List<ClusterNode> getClusterNodeList(VmProxmoxCredential credential) {
        return getClusterNodeList(credential.getClient());
    }

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

    public static ClusterNode getClusterNode(VmProxmoxCredential credential, String node) {
        return getClusterNode(credential.getClient(), node);
    }

    private static ClusterNode getClusterNode(PveClient client, String node) {
        List<ClusterNode> clusterNodeList = getClusterNodeList(client);
        return clusterNodeList.stream().filter(clusterNode -> clusterNode.getName().equals(node)).findFirst().orElseThrow(() -> new Fit2cloudException(404, "节点不存在"));

    }


    /**
     * 获取节点列表
     *
     * @param vmProxmoxCredential 认证对象
     * @return 节点列表
     */
    public static List<Node> getNodeList(VmProxmoxCredential vmProxmoxCredential) {
        return getNodeList(vmProxmoxCredential.getClient());
    }

    private static List<Node> getNodeList(PveClient client) {
        PveClient.PVENodes nodes = client.getNodes();
        String data = nodes.index().getResponse().getJSONArray("data").toString();
        return JsonUtil.parseArray(data, Node.class);
    }

    /**
     * 获取集群节点 也就是主机host
     *
     * @param vmProxmoxCredential 认证对象
     * @return 集群(主机节点)列表
     */
    public static List<Host> getHostList(VmProxmoxCredential vmProxmoxCredential) {
        PveClient client = vmProxmoxCredential.getClient();
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

    public static NodeStatus getNodeStatus(VmProxmoxCredential credential, String node) {
        return getNodeStatus(credential.getClient(), node);
    }

    private static NodeStatus getNodeStatus(PveClient client, String node) {
        try {
            Result status = client.getNodes().get(node).getStatus().status();
            return JsonUtil.parseObject(status.getResponse().get("data").toString(), NodeStatus.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<F2CHost> listF2CHost(ProxmoxBaseRequest proxmoxBaseRequest) {
        VmProxmoxCredential vmProxmoxCredential = JsonUtil.parseObject(proxmoxBaseRequest.getCredential(), VmProxmoxCredential.class);
        List<Host> hostList = getHostList(vmProxmoxCredential).stream().filter(host -> StringUtil.equals(host.getNode().getNode(), proxmoxBaseRequest.getRegionId())).toList();
        List<Qemu> qemuList = listQemu(proxmoxBaseRequest);
        return hostList.stream().map(host -> MappingUtil.toF2CHost(host, qemuList)).toList();
    }

    public static List<Disk> listDisk(ProxmoxBaseRequest request) {
        VmProxmoxCredential credential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        PveClient client = credential.getClient();
        return listDataStore(request)
                .stream()
                .map(dataStore -> listDisk(client, request.getRegionId(), dataStore.getStorage())).flatMap(List::stream).toList();
    }

    public static List<Disk> listDisk(PveClient client, String node) {
        return listDataStore(client, node).stream()
                .flatMap(dataStore -> listDisk(client, node, dataStore.getStorage()).stream().peek(disk -> disk.setType(dataStore.getType()))).toList();
    }

    public static Disk getDisk(PveClient client, String node, String volId) {
        List<Disk> disks = SyncApi.listDisk(client, node);
        Optional<Disk> diskF = disks.stream().filter(disk -> StringUtils.equals(disk.getVolid(), volId))
                .findFirst();
        return diskF.orElse(null);
    }

    public static List<Disk> listDisk(PveClient client, String node, String store) {
        Result index = client.getNodes().get(node).getStorage().get(store).getContent().index();
        if (index.getStatusCode() == 200) {
            String data = index.getResponse().getJSONArray("data").toString();
            return JsonUtil.parseArray(data, Disk.class);
        }
        return List.of();
    }


    public static List<F2CDisk> listF2CDisk(ProxmoxBaseRequest request) {
        VmProxmoxCredential credential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        PveClient client = credential.getClient();
        List<Disk> allDisk = listDisk(request);
        List<Qemu> qemuList = listQemu(request);
        List<DiskStatusInfo> mountDiskVolIdList = qemuList.stream().map(qemu -> {
            Config qemuConfig = getQemuConfig(client, request.getRegionId(), qemu.getVmid() + "");
            List<DiskStatusInfo> result = new ArrayList<>();
            result.addAll(qemuConfig.getDisks().stream()
                    .filter(disk -> StringUtils.isEmpty(disk.getMedia()))
                    .map(disk -> new DiskStatusInfo(disk.getVolId(), disk.isBoot(), true, disk.getDevice(), qemu, Objects.isNull(qemuConfig.getMeta()) ? -1 : qemuConfig.getMeta().getCtime())).toList());
            result.addAll(qemuConfig.getUnuseds().stream().map(unUsed -> new DiskStatusInfo(unUsed.getValue(), false, false, null, qemu, Objects.isNull(qemuConfig.getMeta()) ? -1 : qemuConfig.getMeta().getCtime())).toList());
            return result;
        }).flatMap(List::stream).toList();
        return mountDiskVolIdList.stream().map(diskStatusInfo -> allDisk.stream()
                        .filter(disk -> StringUtil.equals(diskStatusInfo.getVolId(), disk.getVolid()))
                        .findFirst()
                        .map(disk -> MappingUtil.toF2CDisk(diskStatusInfo, disk, request.getRegionId()))
                        .orElse(null))
                .filter(Objects::nonNull).toList();
    }

    public static F2CDisk getF2CDisk(PveClient client, String node, String vmId, String configKey) {
        Config qemuConfig = getQemuConfig(client, node, vmId);
        Qemu qemu = getQemuById(client, node, vmId + "");
        com.fit2cloud.common.provider.impl.proxmox.client.entity.config.Disk disk = qemuConfig.getDisks().stream().filter(d -> StringUtils.equals(configKey, d.getKey())).findFirst().orElseThrow(() -> new Fit2cloudException(404, "未找到磁盘"));
        Disk d = getDisk(client, node, disk.getVolId());
        DiskStatusInfo diskStatusInfo = new DiskStatusInfo(disk.getVolId(), disk.isBoot(), true, disk.getDevice(), qemu, Objects.isNull(qemuConfig.getMeta()) ? -1 : qemuConfig.getMeta().getCtime());
        return MappingUtil.toF2CDisk(diskStatusInfo, d, node);
    }


    public static List<DataStore> listDataStore(ProxmoxBaseRequest request) {
        VmProxmoxCredential credential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        return listDataStore(credential.getClient(), request.getRegionId());
    }

    public static List<DataStore> listDataStore(PveClient client, String node) {
        Result index = client.getNodes().get(node).getStorage().index(Map.of("format", 1, "content", "images"));
        if (index.getStatusCode() == 200) {
            return JsonUtil.parseArray(index.getResponse().getJSONArray("data").toString(), DataStore.class).stream().filter(dataStore -> dataStore.getContent().contains("images")).toList();
        }
        return List.of();
    }

    public static DataStore getDataStore(VmProxmoxCredential vmProxmoxCredential, String regionId, String datastore) {
        return getDataStore(vmProxmoxCredential.getClient(), regionId, datastore);
    }

    public static DataStore getDataStore(PveClient client, String regionId, String datastore) {
        return listDataStore(client, regionId).stream().filter(dataStore -> StringUtils.equals(dataStore.getStorage(), datastore))
                .findFirst().orElseThrow(() -> new Fit2cloudException(404, "获取存储器失败"));
    }

    public static List<F2CDisk> getVmF2CDisks(ProxmoxActionBaseRequest request) {
        VmProxmoxCredential credential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        PveClient client = credential.getClient();
        Config config = getQemuConfig(client, request.getRegionId(), request.getUuid().getUuid());
        Qemu qemu = getQemuById(client, request.getRegionId(), request.getUuid().getUuid());
        List<Disk> disks = listDisk(client, request.getRegionId());
        List<DiskStatusInfo> result = new ArrayList<>();
        result.addAll(config.getDisks().stream()
                .filter(disk -> StringUtils.isEmpty(disk.getMedia()))
                .map(disk -> new DiskStatusInfo(disk.getVolId(), disk.isBoot(), true, disk.getDevice(), qemu, Objects.isNull(config.getMeta()) ? -1 : config.getMeta().getCtime())).toList());
        result.addAll(config.getUnuseds().stream().map(unUsed -> new DiskStatusInfo(unUsed.getValue(), false, false, null, qemu, Objects.isNull(config.getMeta()) ? -1 : config.getMeta().getCtime())).toList());
        return result.stream().map(diskStatusInfo -> disks.stream().filter(disk -> StringUtils.equals(disk.getVolid(), diskStatusInfo.getVolId()))
                .findFirst().map(disk -> MappingUtil.toF2CDisk(diskStatusInfo, disk, request.getRegionId()))
                .orElse(null)).filter(Objects::nonNull).toList();
    }
}
