package com.fit2cloud.provider.impl.proxmox.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.provider.entity.F2CBasePerfMetricMonitorData;
import com.fit2cloud.common.provider.entity.F2CEntityType;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.provider.impl.proxmox.client.PveClient;
import com.fit2cloud.common.provider.impl.proxmox.client.Result;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.*;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.config.Config;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.config.DiskStatusInfo;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.config.NetworkInterface;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.impl.proxmox.constants.ProxmoxPerfMetricConstants;
import com.fit2cloud.provider.impl.proxmox.entity.ProxmoxActionBaseRequest;
import com.fit2cloud.provider.impl.proxmox.entity.credential.VmProxmoxCredential;
import com.fit2cloud.provider.impl.proxmox.entity.request.ProxmoxBaseRequest;
import com.fit2cloud.provider.impl.proxmox.util.MappingUtil;
import com.fit2cloud.vm.entity.*;
import com.fit2cloud.vm.entity.request.GetMetricsRequest;
import jodd.util.StringUtil;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

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

    /**
     * 获取虚拟机 配置
     *
     * @param credential 认证信息
     * @param node       节点
     * @param vmId       虚拟机 id
     * @return 虚拟机配置
     */
    public static Config getQemuConfig(VmProxmoxCredential credential, String node, String vmId) {
        return getQemuConfig(credential.getClient(), node, vmId);
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
     * 获取虚拟机列表
     *
     * @param request 请求对象
     * @return 虚拟机列表
     */
    public static List<F2CVirtualMachine> listVirtualMachine(ProxmoxBaseRequest request) {
        VmProxmoxCredential credential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        PveClient client = credential.getClient();
        List<Qemu> qemuList = listVmQemu(client, request.getRegionId());
        Cluster cluster = getCluster(client);
        ClusterNode clusterNode = getClusterNode(credential, request.getRegionId());
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

    /**
     * 根据虚拟机id 获取虚拟机信息
     *
     * @param client 客户端
     * @param node   节点
     * @param vmId   虚拟机id
     * @return 虚拟机信息
     */
    public static Qemu getQemuById(PveClient client, String node, String vmId) {
        Result result = client.getNodes().get(node).getQemu().get(vmId).getStatus().getCurrent().vmStatus();
        if (result.getResponse().isNull("data")) {
            throw new Fit2cloudException(404, "虚拟机不存在");
        } else {
            String data = result.getResponse().getJSONObject("data").toString();
            return JsonUtil.parseObject(data, Qemu.class);
        }
    }

    /**
     * 根据虚拟机id 获取虚拟机详情
     *
     * @param credential 认证信息
     * @param node       节点
     * @param vmId       虚拟机id
     * @return 虚拟机详情
     */
    public static F2CVirtualMachine getF2CVirtualMachineById(VmProxmoxCredential credential, String node, String vmId) {
        PveClient client = credential.getClient();
        return getF2CVirtualMachineById(client, node, vmId);
    }

    /**
     * 根据虚拟机id 获取虚拟机详情
     *
     * @param client 客户端
     * @param node   节点
     * @param vmId   虚拟机id
     * @return 虚拟机详情
     */
    public static F2CVirtualMachine getF2CVirtualMachineById(PveClient client, String node, String vmId) {
        Qemu qemu = getQemuById(client, node, vmId);
        Config qemuConfig = getQemuConfig(client, node, vmId);
        Cluster cluster = getCluster(client);
        ClusterNode clusterNode = getClusterNode(client, node);
        List<NetworkInterface> network = getNetwork(client, node, vmId);
        return MappingUtil.toF2CVirtualMachine(qemu, qemuConfig, cluster.getName(), clusterNode, network);

    }

    /**
     * 获取网络信息
     *
     * @param credential 认证信息
     * @param node       节点
     * @param vmId       虚拟机id
     * @return 网络信息
     */
    public static List<NetworkInterface> getNetwork(VmProxmoxCredential credential, String node, String vmId) {
        return getNetwork(credential.getClient(), node, vmId);

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
     * agent是否运行
     *
     * @param client 客户端
     * @param node   节点
     * @param vmId   机器id
     * @return 是否运行
     */
    public static boolean agentIsRun(PveClient client, String node, String vmId) {
        Result result = client.getNodes().get(node).getQemu().get(vmId).getAgent().getNetworkGetInterfaces().networkGetInterfaces();
        return result.getStatusCode() != 500;
    }

    /**
     * 获取集群信息
     *
     * @param credential 认证对象
     * @return 集群信息
     */
    public static Cluster getCluster(VmProxmoxCredential credential) {
        return getCluster(credential.getClient());
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
     * 获取节点列表
     *
     * @param credential 认证信息
     * @return 节点列表
     */
    public static List<ClusterNode> getClusterNodeList(VmProxmoxCredential credential) {
        return getClusterNodeList(credential.getClient());
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
     * 获取集群节点详情
     *
     * @param credential 认证信息
     * @param node       节点
     * @return 节点详情
     */
    public static ClusterNode getClusterNode(VmProxmoxCredential credential, String node) {
        return getClusterNode(credential.getClient(), node);
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
     * @param vmProxmoxCredential 认证对象
     * @return 节点列表
     */
    public static List<Node> getNodeList(VmProxmoxCredential vmProxmoxCredential) {
        return getNodeList(vmProxmoxCredential.getClient());
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

    /**
     * 获取节点详情信息
     *
     * @param credential 认证信息
     * @param node       节点id
     * @return 节点详情信息
     */
    public static NodeStatus getNodeStatus(VmProxmoxCredential credential, String node) {
        return getNodeStatus(credential.getClient(), node);
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

    /**
     * 获取 宿主机详情列表
     *
     * @param proxmoxBaseRequest 请求对象
     * @return 宿主机详情列表
     */
    public static List<F2CHost> listF2CHost(ProxmoxBaseRequest proxmoxBaseRequest) {
        VmProxmoxCredential vmProxmoxCredential = JsonUtil.parseObject(proxmoxBaseRequest.getCredential(), VmProxmoxCredential.class);
        List<Host> hostList = getHostList(vmProxmoxCredential).stream().filter(host -> StringUtil.equals(host.getNode().getNode(), proxmoxBaseRequest.getRegionId())).toList();
        List<Qemu> qemuList = listVmQemu(vmProxmoxCredential.getClient(), proxmoxBaseRequest.getRegionId());
        return hostList.stream().map(host -> MappingUtil.toF2CHost(host, qemuList)).toList();
    }

    /**
     * 获取磁盘列表
     *
     * @param request 请求对象
     * @return 磁盘列表
     */
    public static List<Disk> listDisk(ProxmoxBaseRequest request) {
        VmProxmoxCredential credential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        PveClient client = credential.getClient();
        return listDataStore(request)
                .stream()
                .map(dataStore -> listDisk(client, request.getRegionId(), dataStore.getStorage())).flatMap(List::stream).toList();
    }

    /**
     * 获取磁盘列表
     *
     * @param client 客户端
     * @param node   节点
     * @return 磁盘
     */
    public static List<Disk> listDisk(PveClient client, String node) {
        return listDataStore(client, node).stream()
                .flatMap(dataStore -> listDisk(client, node, dataStore.getStorage()).stream().peek(disk -> disk.setType(dataStore.getType()))).toList();
    }

    /**
     * 获取磁盘信息
     *
     * @param client 客户端
     * @param node   节点
     * @param volId  volId
     * @return 磁盘信息
     */
    public static Disk getDisk(PveClient client, String node, String volId) {
        List<Disk> disks = SyncApi.listDisk(client, node);
        Optional<Disk> diskF = disks.stream().filter(disk -> StringUtils.equals(disk.getVolid(), volId))
                .findFirst();
        return diskF.orElse(null);
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
     * 获取 磁盘详情列表
     *
     * @param request 请求对象
     * @return 磁盘详情列表
     */
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
     * 获取磁盘详情
     *
     * @param client    客户端
     * @param node      节点
     * @param vmId      虚拟机
     * @param configKey 配置key
     * @return 磁盘详情
     */
    public static F2CDisk getF2CDisk(PveClient client, String node, String vmId, String configKey) {
        Config qemuConfig = getQemuConfig(client, node, vmId);
        Qemu qemu = getQemuById(client, node, vmId + "");
        com.fit2cloud.common.provider.impl.proxmox.client.entity.config.Disk disk = qemuConfig.getDisks().stream().filter(d -> StringUtils.equals(configKey, d.getKey())).findFirst().orElseThrow(() -> new Fit2cloudException(404, "未找到磁盘"));
        Disk d = getDisk(client, node, disk.getVolId());
        DiskStatusInfo diskStatusInfo = new DiskStatusInfo(disk.getVolId(), disk.isBoot(), true, disk.getDevice(), qemu, Objects.isNull(qemuConfig.getMeta()) ? "" : qemuConfig.getVmGenId());
        return MappingUtil.toF2CDisk(diskStatusInfo, d, node);
    }


    /**
     * 获取存储器列表
     *
     * @param request 请求对象
     * @return 存储器列表
     */
    public static List<DataStore> listDataStore(ProxmoxBaseRequest request) {
        VmProxmoxCredential credential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        return listDataStore(credential.getClient(), request.getRegionId());
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
     * 获取存储器
     *
     * @param vmProxmoxCredential 认证对象
     * @param regionId            节点
     * @param datastore           存储器
     * @return 存储器信息
     */
    public static DataStore getDataStore(VmProxmoxCredential vmProxmoxCredential, String regionId, String datastore) {
        return getDataStore(vmProxmoxCredential.getClient(), regionId, datastore);
    }

    /**
     * 获取存储器
     *
     * @param client    客户端
     * @param regionId  区域id
     * @param datastore 存储器
     * @return 存储器信息
     */
    public static DataStore getDataStore(PveClient client, String regionId, String datastore) {
        return listDataStore(client, regionId).stream().filter(dataStore -> StringUtils.equals(dataStore.getStorage(), datastore))
                .findFirst().orElseThrow(() -> new Fit2cloudException(404, "获取存储器失败"));
    }

    /**
     * 获取虚拟机磁盘列表
     *
     * @param request 请求对象
     * @return 磁盘列表
     */
    public static List<F2CDisk> getVmF2CDisks(ProxmoxActionBaseRequest request) {
        VmProxmoxCredential credential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        PveClient client = credential.getClient();
        Config config = getQemuConfig(client, request.getRegionId(), request.getUuid().getUuid());
        Qemu qemu = getQemuById(client, request.getRegionId(), request.getUuid().getUuid());
        List<Disk> disks = listDisk(client, request.getRegionId());
        List<DiskStatusInfo> result = new ArrayList<>();
        result.addAll(config.getDisks().stream()
                .filter(disk -> StringUtils.isEmpty(disk.getMedia()))
                .map(disk -> new DiskStatusInfo(disk.getVolId(), disk.isBoot(), true, disk.getDevice(), qemu, Objects.isNull(config.getMeta()) ? "" : config.getVmGenId())).toList());
        result.addAll(config
                .getUnuseds()
                .stream()
                .map(unUsed -> new DiskStatusInfo(unUsed.getValue(), false, false, null, qemu, Objects.isNull(config.getMeta()) ? "" : config.getVmGenId()))
                .toList());
        return result.stream()
                .map(diskStatusInfo -> disks.stream().filter(disk -> StringUtils.equals(disk.getVolid(), diskStatusInfo.getVolId()))
                        .findFirst()
                        .map(disk -> MappingUtil.toF2CDisk(diskStatusInfo, disk, request.getRegionId()))
                        .orElse(null)
                ).filter(Objects::nonNull).toList();
    }


    /**
     * 获取云主机监控数据
     *
     * @param parseObject 获取监控请求对象
     * @return 返回值
     */
    public static List<F2CPerfMetricMonitorData> getF2CPerfMetricMonitorData(GetMetricsRequest parseObject) {
        VmProxmoxCredential vmProxmoxCredential = JsonUtil.parseObject(parseObject.getCredential(), VmProxmoxCredential.class);
        PveClient client = vmProxmoxCredential.getClient();
        return listQemu(client, parseObject.getRegionId())
                .stream()
                .filter(qemu -> Objects.isNull(qemu.getTemplate()) || (Objects.nonNull(qemu.getTemplate()) && !qemu.getTemplate().equals(1)))
                .map(qemu -> getF2CPerfMetricMonitorData(client, parseObject.getRegionId(), qemu.getVmid().toString()))
                .flatMap(List::stream).toList();
    }

    /**
     * 获取云主机监控数据
     *
     * @param pveClient 客户端
     * @param node      节点
     * @param vmId      虚拟机id
     * @return 云主机监控数据
     */
    public static List<F2CPerfMetricMonitorData> getF2CPerfMetricMonitorData(PveClient pveClient, String node, String vmId) {
        Config qemuConfig = getQemuConfig(pveClient, node, vmId);
        Result rrdData = pveClient.getNodes().get(node)
                .getQemu().get(vmId)
                .getRrddata().rrddata("hour", "AVERAGE");
        return getF2CPerfMetricMonitorData(rrdData, F2CEntityType.VIRTUAL_MACHINE, qemuConfig, node, vmId);
    }


    /**
     * 获取 云主机监控数据
     *
     * @param rrdData     监控数据
     * @param entityTypes 类型也就是云主机
     * @param config      云主机配置
     * @param node        节点
     * @param vmId        虚拟机id
     * @return 云主机监控数据
     */
    public static List<F2CPerfMetricMonitorData> getF2CPerfMetricMonitorData(Result rrdData,
                                                                             F2CEntityType entityTypes,
                                                                             Config config,
                                                                             String node,
                                                                             String vmId) {
        return getF2CPerfMetricMonitorData(rrdData, Arrays.stream(ProxmoxPerfMetricConstants.CloudServerPerfMetricEnum.values()).toList(), f2CPerfMetricMonitorData -> {
            f2CPerfMetricMonitorData.setEntityType(entityTypes.name());
            f2CPerfMetricMonitorData.setInstanceId(MappingUtil.toInstanceId(vmId, config.getVmGenId()));
            f2CPerfMetricMonitorData.setHostId(node);
        });
    }

    /**
     * 获取宿主机监控数据
     *
     * @param parseObject 请求对象
     * @return 宿主机监控数据
     */
    public static List<F2CPerfMetricMonitorData> getF2CHostPerfMetricMonitorData(GetMetricsRequest parseObject) {
        VmProxmoxCredential vmProxmoxCredential = JsonUtil.parseObject(parseObject.getCredential(), VmProxmoxCredential.class);
        PveClient client = vmProxmoxCredential.getClient();
        Cluster cluster = getCluster(client);
        return getClusterNodeList(client).stream()
                .map(node -> getF2CHostPerfMetricMonitorData(client, node, cluster))
                .flatMap(List::stream)
                .toList();
    }

    /**
     * 获取宿主机监控数据
     *
     * @param pveClient 客户端
     * @param node      节点
     * @param cluster   集群
     * @return 宿主机监控数据
     */
    public static List<F2CPerfMetricMonitorData> getF2CHostPerfMetricMonitorData(PveClient pveClient, ClusterNode node, Cluster cluster) {
        Result rrdData = pveClient.getNodes().get(node.getName())
                .getRrddata().rrddata("hour", "AVERAGE");
        return getF2CHostPerfMetricMonitorData(rrdData, F2CEntityType.HOST, node, cluster);

    }

    /**
     * 获取宿主机监控数据
     *
     * @param rrdData     监控数据
     * @param entityTypes 类型也就是宿主机类型
     * @param clusterNode 集群节点对象
     * @param cluster     集群
     * @return 宿主机监控数据
     */
    public static List<F2CPerfMetricMonitorData> getF2CHostPerfMetricMonitorData(Result rrdData,
                                                                                 F2CEntityType entityTypes,
                                                                                 ClusterNode clusterNode, Cluster cluster) {
        return getF2CPerfMetricMonitorData(rrdData, Arrays.stream(ProxmoxPerfMetricConstants.CloudHostPerfMetricEnum.values()).toList(), f2CPerfMetricMonitorData -> {
            f2CPerfMetricMonitorData.setEntityType(entityTypes.name());
            f2CPerfMetricMonitorData.setInstanceId(clusterNode.getName());
            f2CPerfMetricMonitorData.setClusterName(Objects.nonNull(cluster) ? cluster.getName() : null);
            f2CPerfMetricMonitorData.setHostId(clusterNode.getName());
        });
    }

    /**
     * 获取监控数据
     *
     * @param rrdData                    监控数据
     * @param perfMetricConstantsParents 需要获取的指标
     * @param consumer                   后处理数据
     * @return 监控数据
     */
    public static List<F2CPerfMetricMonitorData> getF2CPerfMetricMonitorData(Result rrdData,
                                                                             List<? extends ProxmoxPerfMetricConstants.PerfMetricConstantsParent> perfMetricConstantsParents, Consumer<F2CPerfMetricMonitorData> consumer) {
        if (rrdData.isSuccessStatusCode()) {
            return perfMetricConstantsParents.stream()
                    .flatMap(item -> {
                        JSONArray data = rrdData.getResponse().getJSONArray("data");
                        return IntStream.range(0, data.length()).boxed().map(data::getJSONObject)
                                .filter(Objects::nonNull)
                                .map(jsonObject -> {
                                    if (jsonObject.isNull("time")) {
                                        return null;
                                    }
                                    F2CBasePerfMetricMonitorData f2CBasePerfMetricMonitorData = item.getF2CBasePerfMetricMonitorData().apply(jsonObject);
                                    F2CPerfMetricMonitorData f2CPerfMetricMonitorData = new F2CPerfMetricMonitorData();
                                    BeanUtils.copyProperties(f2CBasePerfMetricMonitorData, f2CPerfMetricMonitorData);
                                    f2CPerfMetricMonitorData.setMetricName(item.name());
                                    f2CPerfMetricMonitorData.setUnit(item.getUnit());
                                    f2CPerfMetricMonitorData.setPeriod(60);
                                    f2CPerfMetricMonitorData.setTimestamp(jsonObject.getLong("time") * 1000);
                                    consumer.accept(f2CPerfMetricMonitorData);
                                    return f2CPerfMetricMonitorData;
                                }).filter(Objects::nonNull);
                    }).toList();
        }
        return List.of();
    }

    /**
     * 获取存储器监控数据
     *
     * @param parseObject 请求对象
     * @return 存储就监控数据
     */
    public static List<F2CPerfMetricMonitorData> getF2CDatastorePerfMetricMonitorData(GetMetricsRequest parseObject) {
        VmProxmoxCredential vmProxmoxCredential = JsonUtil.parseObject(parseObject.getCredential(), VmProxmoxCredential.class);
        PveClient client = vmProxmoxCredential.getClient();
        return getClusterNodeList(client)
                .stream().flatMap(clusterNode -> listDataStore(client, clusterNode.getName()).stream().map(dataStore -> new DefaultKeyValue<>(clusterNode, dataStore)))
                .map(kv -> getF2CDatastorePerfMetricMonitorData(client, kv.getKey(), kv.getValue()))
                .flatMap(List::stream)
                .toList();


    }

    /**
     * 获取存储器监控数据
     *
     * @param client      存储器
     * @param clusterNode 集群节点
     * @param dataStore   存储器
     * @return 存储器监控数据
     */
    public static List<F2CPerfMetricMonitorData> getF2CDatastorePerfMetricMonitorData(PveClient client, ClusterNode clusterNode, DataStore dataStore) {
        Result rrdData = client.getNodes().get(clusterNode.getName())
                .getStorage()
                .get(dataStore.getStorage())
                .getRrddata().rrddata("hour", "AVERAGE");
        return getF2CDatastorePerfMetricMonitorData(rrdData, clusterNode, dataStore);

    }

    /**
     * 获取存储器监控数据
     *
     * @param rrdData     存储器监控数据
     * @param clusterNode 存储器节点
     * @param dataStore   存储器
     * @return 存储器监控数据
     */
    public static List<F2CPerfMetricMonitorData> getF2CDatastorePerfMetricMonitorData(Result rrdData, ClusterNode clusterNode, DataStore dataStore) {
        return getF2CPerfMetricMonitorData(rrdData, Arrays.stream(ProxmoxPerfMetricConstants.CloudDatastorePerfMetricEnum.values()).toList(), f2CPerfMetricMonitorData -> {
            f2CPerfMetricMonitorData.setEntityType(F2CEntityType.DATASTORE.name());
            f2CPerfMetricMonitorData.setInstanceId(dataStore.getStorage());
            f2CPerfMetricMonitorData.setClusterName(clusterNode.getName());
            f2CPerfMetricMonitorData.setHostId(clusterNode.getId());
        });
    }

    /**
     * 获取存储器列表
     *
     * @param proxmoxBaseRequest 请求对象
     * @return 存储器列表
     */
    public static List<F2CDatastore> listF2CDatastore(ProxmoxBaseRequest proxmoxBaseRequest) {
        VmProxmoxCredential vmProxmoxCredential = JsonUtil.parseObject(proxmoxBaseRequest.getCredential(), VmProxmoxCredential.class);
        PveClient client = vmProxmoxCredential.getClient();
        List<DataStore> dataStores = listDataStore(proxmoxBaseRequest);
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
}
