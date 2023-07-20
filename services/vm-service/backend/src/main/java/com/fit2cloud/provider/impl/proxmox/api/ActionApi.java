package com.fit2cloud.provider.impl.proxmox.api;

import com.fit2cloud.base.entity.BillPolicyDetails;
import com.fit2cloud.common.constants.ChargeTypeConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.provider.impl.proxmox.client.PveClient;
import com.fit2cloud.common.provider.impl.proxmox.client.Result;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.DataStore;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.Disk;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.InstanceId;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.Qemu;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.config.Config;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.config.NetworkInterface;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.config.UnUsed;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.ServiceUtil;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.provider.impl.proxmox.entity.DiskConfig;
import com.fit2cloud.provider.impl.proxmox.entity.ProxmoxActionBaseRequest;
import com.fit2cloud.provider.impl.proxmox.entity.credential.VmProxmoxCredential;
import com.fit2cloud.provider.impl.proxmox.entity.request.*;
import com.fit2cloud.utils.ChargingUtil;
import com.fit2cloud.vm.entity.F2CDisk;
import com.fit2cloud.vm.entity.F2CVirtualMachine;
import com.fit2cloud.vm.entity.request.BaseDiskRequest;
import com.fit2cloud.vm.entity.request.BaseDiskResizeRequest;
import jodd.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/10  16:36}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class ActionApi {
    private static final Pattern ipv4Pattern = Pattern.compile("^((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))$");

    public static F2CVirtualMachine createVirtualMachine(ProxmoxCreateServerRequest request) {
        VmProxmoxCredential credential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        PveClient client = credential.getClient();
        DefaultKeyValue<Integer, Result> kv = cloneVm(client, request);
        if (kv.getValue().getStatusCode() != 200) {
            throw new Fit2cloudException(500, "克隆虚拟机失败");
        }

        // 等待 clone 完毕
        client.waitForTaskToFinish(kv.getValue().getResponse().getString("data"), 0, Long.MAX_VALUE);

        PveClient.PVENodes.PVENodeItem.PVEQemu.PVEVmidItem.PVEConfig config = client.getNodes().get(request.getRegionId()).getQemu().get(kv.getKey()).getConfig();
        // 构造基本参数
        Map<String, Object> params = new HashMap<>(Map.of("memory", request.getMem() * 1024,
                "cores", request.getCpu(),
                "sockets", request.getCpuSlot(),
                "cipassword", URLEncoder.encode(request.getAuthObj().getPassword(), StandardCharsets.UTF_8),
                "ciuser", request.getAuthObj().getUserName()));

        String dnsDomain = request.getNetworkList().get(request.getIndex()).getDnsDomain();
        if (StringUtils.isNotEmpty(dnsDomain)) {
            params.put("searchdomain", URLEncoder.encode(dnsDomain, StandardCharsets.UTF_8));
        }
        String dnsServers = request.getNetworkList().get(request.getIndex()).getDnsServers();
        if (StringUtils.isNotEmpty(dnsServers)) {
            params.put("nameserver", URLEncoder.encode((dnsServers), StandardCharsets.UTF_8));
        }

        ProxmoxNetworkForm proxmoxNetworkForm = request.getNetworkList().get(request.getIndex());
        // 构建网络参数
        for (int i = 0; i < proxmoxNetworkForm.getAdapters().size(); i++) {
            params.put("ipconfig" + i, proxmoxNetworkForm.getAdapters().get(i).toIpConfigString());
            params.put("net" + i, proxmoxNetworkForm.getAdapters().get(i).toNetString());
        }
        // 构建磁盘参数 template 排除模板自带磁盘
        for (int i = 0; i < request.getDisks().size(); i++) {
            if (request.getDisks().get(i).isTemplate()) {
                continue;
            }
            DiskConfig diskConfig = request.getDisks().get(i);
            params.put("scsi" + i, diskConfig.toString(request.getDatastore()));
        }
        // 修改虚拟机配置
        Result resultR = config.updateVm(params);
        if (resultR.getStatusCode() != 200) {
            throw new Fit2cloudException(500, resultR.getError());
        }

        Result result = client.getNodes().get(request.getRegionId()).getQemu().get(kv.getKey()).getStatus().getStart().vmStart();
        if (result.getStatusCode() != 200) {
            throw new Fit2cloudException(500, "启动虚拟机失败" + result.getError());
        }
        client.waitForTaskToFinish(result.getResponse().getString("data"), 0, Long.MAX_VALUE);
        // 等待Agent启动
        waitAgentStarted(client, request.getRegionId(), kv.getKey() + "");
        // 等待Agent
        F2CVirtualMachine f2CVirtualMachine = SyncApi.getF2CVirtualMachineById(credential, request.getRegionId(), kv.getKey() + "");
        f2CVirtualMachine.setId(request.getId());
        return f2CVirtualMachine;
    }


    public static boolean waitAgentStarted(PveClient client, String node, String vmId) {
        return waitForTaskToFinish(() -> {
            List<NetworkInterface> network = SyncApi.getNetwork(client, node, vmId);
            return network.stream()
                    .anyMatch(networkInterface -> !StringUtils.equals(networkInterface.getName(), "lo"));
        }, 1000 * 20, 1000 * 60 * 30);
    }

    public static boolean waitForTaskToFinish(Supplier<Boolean> taskIsRunning, long wait, long timeOut) throws JSONException {
        boolean isRunning = false;
        long timeStart = System.currentTimeMillis();
        long waitTime = System.currentTimeMillis();
        while (!isRunning && (System.currentTimeMillis() - timeStart) < timeOut) {
            if ((System.currentTimeMillis() - waitTime) >= wait) {
                isRunning = taskIsRunning.get();
                waitTime = System.currentTimeMillis();
            }
            try {
                Thread.sleep(wait);
            } catch (InterruptedException ignored) {

            }
        }
        return System.currentTimeMillis() - timeStart < timeOut;
    }

    /**
     * 克隆虚拟机
     *
     * @param client  客户端
     * @param request 请求对象
     * @return clone结果
     */
    public static synchronized DefaultKeyValue<Integer, Result> cloneVm(PveClient client, ProxmoxCreateServerRequest request) {
        PveClient.PVENodes.PVENodeItem.PVEQemu.PVEVmidItem.PVEClone clone = client.getNodes().get(request.getRegionId()).getQemu().get(request.getTemplate()).getClone();
        int nextId = client.getCluster().getNextid().nextid().getResponse().getInt("data");
        String notes = request.getHostObj().get(request.getIndex()).getNotes();
        Result result = clone.cloneVm(nextId, null, StringUtils.isNotEmpty(notes) ? URLEncoder.encode(notes, StandardCharsets.UTF_8) : null, null, Boolean.TRUE, request.getHostObj().get(request.getIndex()).getName(), null, null, request.getDatastore(), request.getRegionId());
        return new DefaultKeyValue<>(nextId, result);
    }


    public static boolean powerOnVm(ProxmoxActionBaseRequest powerOnRequest) {
        VmProxmoxCredential vmProxmoxCredential = JsonUtil.parseObject(powerOnRequest.getCredential(), VmProxmoxCredential.class);
        PveClient client = vmProxmoxCredential.getClient();
        Qemu qemu = SyncApi.getQemuById(client, powerOnRequest.getRegionId(), powerOnRequest.getUuid().getUuid());
        if (StringUtil.equals(qemu.getStatus(), "stopped")) {
            Result result = vmProxmoxCredential.getClient().getNodes().get(powerOnRequest.getRegionId()).getQemu().get(powerOnRequest.getUuid().getUuid()).getStatus().getStart().vmStart();
            client.waitForTaskToFinish(result.getResponse().getString("data"), 0, Long.MAX_VALUE);
            waitAgentStarted(client, powerOnRequest.getRegionId(), powerOnRequest.getUuid().getUuid());
        }
        return true;
    }


    public static boolean powerOffVm(ProxmoxActionBaseRequest powerOffRequest) {
        VmProxmoxCredential vmProxmoxCredential = JsonUtil.parseObject(powerOffRequest.getCredential(), VmProxmoxCredential.class);
        PveClient client = vmProxmoxCredential.getClient();
        Qemu qemu = SyncApi.getQemuById(client, powerOffRequest.getRegionId(), powerOffRequest.getUuid().getUuid());
        if (StringUtil.equals(qemu.getStatus(), "running")) {
            Result result = client.getNodes().get(powerOffRequest.getRegionId()).getQemu().get(powerOffRequest.getUuid().getUuid()).getStatus().getStop().vmStop();
            // 等待 clone 完毕
            client.waitForTaskToFinish(result.getResponse().getString("data"), 0, Long.MAX_VALUE);
        }
        return true;
    }

    public static boolean shutdownInstance(ProxmoxActionBaseRequest request) {
        VmProxmoxCredential vmProxmoxCredential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        PveClient client = vmProxmoxCredential.getClient();
        Qemu qemu = SyncApi.getQemuById(client, request.getRegionId(), request.getUuid().getUuid());
        if (StringUtil.equals(qemu.getStatus(), "running")) {
            Result result = client.getNodes().get(request.getRegionId()).getQemu().get(request.getUuid().getUuid()).getStatus().getShutdown().vmShutdown();
            client.waitForTaskToFinish(result.getResponse().getString("data"), 0, Long.MAX_VALUE);
            return true;
        }
        return true;
    }

    public static boolean reboot(ProxmoxActionBaseRequest request) {
        VmProxmoxCredential vmProxmoxCredential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        PveClient client = vmProxmoxCredential.getClient();
        PveClient.PVENodes.PVENodeItem.PVEQemu.PVEVmidItem pve = client.getNodes().get(request.getRegionId()).getQemu().get(request.getUuid().getUuid());
        Qemu qemu = SyncApi.getQemuById(client, request.getRegionId(), request.getUuid().getUuid());
        Result result;
        if (StringUtil.equals(qemu.getStatus(), "running")) {
            result = client.getNodes().get(request.getRegionId()).getQemu().get(request.getUuid().getUuid()).getStatus().getReboot().vmReboot();
        } else {
            result = client.getNodes().get(request.getRegionId()).getQemu().get(request.getUuid().getUuid()).getStatus().getStart().vmStart();
        }
        client.waitForTaskToFinish(result.getResponse().getString("data"), 0, Long.MAX_VALUE);
        waitAgentStarted(client, request.getRegionId(), request.getUuid().getUuid());
        return true;
    }

    public static boolean delete(ProxmoxActionBaseRequest request) {
        VmProxmoxCredential vmProxmoxCredential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        PveClient client = vmProxmoxCredential.getClient();
        // 关机
        shutdownInstance(request);
        Result result = client.getNodes().get(request.getRegionId()).getQemu().get(request.getUuid().getUuid()).destroyVm();
        client.waitForTaskToFinish(result.getResponse().getString("data"), 0, Long.MAX_VALUE);
        return true;
    }

    public static boolean deleteDisk(BaseDiskRequest request) {
        VmProxmoxCredential vmProxmoxCredential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        PveClient client = vmProxmoxCredential.getClient();
        List<Disk> disks = SyncApi.listDisk(client, request.getRegionId());
        String diskId = JsonUtil.parseObject(request.getDiskId(), InstanceId.class).getUuid();
        Optional<Disk> diskF = disks.stream().filter(disk -> StringUtils.equals(disk.getVolid(), diskId)).findFirst();
        if (diskF.isPresent()) {
            Disk disk = diskF.get();
            Config qemuConfig = SyncApi.getQemuConfig(client, request.getRegionId(), disk.getVmId() + "");
            Optional<UnUsed> first = qemuConfig.getUnuseds().stream().filter(item -> StringUtils.equals(item.getValue(), diskId)).findFirst();
            if (first.isPresent()) {
                first.ifPresent(item -> {
                    client.getNodes().get(request.getRegionId()).getQemu().get(disk.getVmId()).getConfig().updateVm(Map.of("delete", item.getKey()));
                });
                return true;
            }
        }
        return false;
    }

    public static boolean detachDisk(BaseDiskRequest request) {
        String diskId = JsonUtil.parseObject(request.getDiskId(), InstanceId.class).getUuid();
        String instanceUuid = JsonUtil.parseObject(request.getInstanceUuid(), InstanceId.class).getUuid();
        VmProxmoxCredential vmProxmoxCredential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        PveClient client = vmProxmoxCredential.getClient();
        Config qemuConfig = SyncApi.getQemuConfig(client, request.getRegionId(), instanceUuid);
        Optional<com.fit2cloud.common.provider.impl.proxmox.client.entity.config.Disk> first = qemuConfig.getDisks().stream()
                .filter(disk -> StringUtils.equals(disk.getVolId(), diskId)).findFirst();
        if (first.isPresent()) {
            first.ifPresent(item -> {
                client.getNodes().get(request.getRegionId()).getQemu().get(JsonUtil.parseObject(request.getInstanceUuid(), InstanceId.class).getUuid()).getConfig().updateVm(Map.of("delete", item.getKey()));
            });
            return true;
        }
        return false;
    }

    public static boolean enlargeDisk(BaseDiskResizeRequest request) {
        String diskId = JsonUtil.parseObject(request.getDiskId(), InstanceId.class).getUuid();
        VmProxmoxCredential vmProxmoxCredential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        PveClient client = vmProxmoxCredential.getClient();
        List<Disk> disks = SyncApi.listDisk(client, request.getRegionId());
        Optional<Disk> diskF = disks.stream().filter(disk -> StringUtils.equals(disk.getVolid(), diskId)).findFirst();
        if (diskF.isPresent()) {

            Disk disk = diskF.get();
            Config qemuConfig = SyncApi.getQemuConfig(client, request.getRegionId(), diskF.get().getVmId() + "");
            String diskKey = getDiskKey(qemuConfig, request.getDiskId());
            if (StringUtils.isNotEmpty(diskKey)) {
                client.getNodes().get(request.getRegionId()).getQemu().get(disk.getVmId()).getResize().resizeVm(diskKey, request.getNewDiskSize() + "G");
                return true;
            }

        }
        return false;
    }

    private static String getDiskKey(Config config, String diskId) {
        return config.getDisks().stream().filter(disk -> StringUtils.equals(disk.getVolId(), diskId)).findFirst().map(com.fit2cloud.common.provider.impl.proxmox.client.entity.config.Disk::getKey).orElseGet(() -> config.getUnuseds().stream().filter(unUsed -> StringUtils.equals(unUsed.getValue(), diskId)).findFirst().map(UnUsed::getValue).orElse(null));
    }

    public static F2CVirtualMachine changeVmConfig(ProxmoxUpdateConfigRequest req) {
        VmProxmoxCredential vmProxmoxCredential = JsonUtil.parseObject(req.getCredential(), VmProxmoxCredential.class);
        PveClient client = vmProxmoxCredential.getClient();
        client.getNodes().get(req.getRegionId()).getQemu().get(req.getInstanceUuid().getUuid()).getConfig().updateVm(Map.of("memory", req.getMem() * 1024, "cores", req.getCpu(), "sockets", req.getCpuSlot(), "delete", "balloon,shares,vcpus,affinity,cpuunits,cpulimit,cpu"));
        ProxmoxActionBaseRequest request = new ProxmoxActionBaseRequest();
        request.setUuid(JsonUtil.toJSONString(req.getInstanceUuid()));
        request.setRegionId(req.getRegionId());
        request.setCredential(req.getCredential());
        Qemu qemu = SyncApi.getQemuById(client, request.getRegionId(), request.getUuid().getUuid());
        if (StringUtils.equals(qemu.getStatus(), "running")) {
            // 关机
            ActionApi.reboot(request);
        }
        return SyncApi.getF2CVirtualMachineById(client, req.getRegionId(), req.getInstanceUuid().getUuid());
    }

    public static F2CDisk attachDisk(BaseDiskRequest request) {
        String instanceId = JsonUtil.parseObject(request.getInstanceUuid(), InstanceId.class).getUuid();
        String diskId = JsonUtil.parseObject(request.getDiskId(), InstanceId.class).getUuid();
        VmProxmoxCredential vmProxmoxCredential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        PveClient client = vmProxmoxCredential.getClient();
        Disk disk = SyncApi.getDisk(client, request.getRegionId(), diskId);

        if (Objects.nonNull(disk)) {
            Integer vmId = disk.getVmId();
            Config newConfig = SyncApi.getQemuConfig(client, request.getRegionId(), instanceId);
            String notUsedKey = getNotUsedKey(newConfig.getDisks().stream().map(com.fit2cloud.common.provider.impl.proxmox.client.entity.config.Disk::getKey).filter(key -> key.startsWith("scsi")).toList(), "scsi");
            if (StringUtils.equals(vmId.toString(), instanceId)) {
                client.getNodes().get(request.getRegionId()).getQemu().get(instanceId).getConfig().updateVm(Map.of(notUsedKey, diskId));
                return SyncApi.getF2CDisk(client, request.getRegionId(), instanceId, notUsedKey);
            } else {
                Config qemuConfig = SyncApi.getQemuConfig(client, request.getRegionId(), vmId.toString());
                UnUsed unUsed = qemuConfig.getUnuseds().stream().filter(un -> StringUtils.equals(disk.getVolid(), un.getValue())).findFirst().orElseThrow(() -> new Fit2cloudException(400, "未找到磁盘"));
                String oldKey = unUsed.getKey();
                Result result = client.getNodes().get(request.getRegionId()).getQemu().get(vmId).getMoveDisk().moveVmDisk(Map.of("vmid", vmId, "target-vmid", Integer.parseInt(instanceId), "disk", oldKey, "target-disk", notUsedKey));
                if (result.getStatusCode() == 200) {
                    client.waitForTaskToFinish(result.getResponse().getString("data"), 0, Long.MAX_VALUE);
                    return SyncApi.getF2CDisk(client, request.getRegionId(), instanceId, notUsedKey);
                }
            }
        }

        throw new Fit2cloudException(500, "挂载失败");
    }

    public static String getNotUsedKey(List<String> keys, String device) {
        keys = keys.stream().sorted(Comparator.comparingInt(pre -> Integer.parseInt(pre.replace(device, "")))).toList();
        for (int i = 0; i < keys.size(); i++) {
            if (!keys.get(i).endsWith(i + "")) {
                return device + i;
            }
        }
        return device + keys.size();
    }

    public static F2CDisk createDisk(AddDiskFormRequest request) {
        //scsi4: local:1,format=raw
        //local:1,format=raw
        VmProxmoxCredential vmProxmoxCredential = JsonUtil.parseObject(request.getCredential(), VmProxmoxCredential.class);
        PveClient client = vmProxmoxCredential.getClient();
        Config qemuConfig = SyncApi.getQemuConfig(client, request.getRegionId(), request.getInstanceUuid().getUuid());
        List<String> diskKeyList = qemuConfig.getDisks().stream()
                .filter(disk -> StringUtils.equals(disk.getDevice(), request.getDevice()))
                .map(com.fit2cloud.common.provider.impl.proxmox.client.entity.config.Disk::getKey).toList();
        String notUsedKey = getNotUsedKey(diskKeyList, request.getDevice());
        DataStore dataStore = SyncApi.getDataStore(client, request.getRegionId(), request.getDatastore());

        client.getNodes().get(request.getRegionId())
                .getQemu().get(request.getInstanceUuid().getUuid())
                .getConfig()
                .updateVm(Map.of(notUsedKey, URLEncoder.encode(request.getDatastore() + ":" + request.getSize() + (CollectionUtils.isEmpty(dataStore.getFormats()) ? "" : ",format=" + request.getFormat()), StandardCharsets.UTF_8)));
        return SyncApi.getF2CDisk(client, request.getRegionId(), request.getInstanceUuid().getUuid(), notUsedKey);
    }

    public static String calculateConfigPrice(ProxmoxCalculateConfigPriceRequest request) {
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
                            return ChargingUtil.getBigDecimal(instanceChargeType, Map.of("cpu", (request.getCpu() * request.getCpuSlot()), "memory", request.getMem()), billPolicyDetails);
                        }
                        if ("DISK".equals(billPolicyDetails.getResourceType())) {
                            List<DiskConfig> disks = request.getDisks();
                            return (CollectionUtils.isNotEmpty(disks) ? disks : new ArrayList<DiskConfig>()).stream().map(disk -> ChargingUtil.getBigDecimal(instanceChargeType, Map.of("size", disk.getSize()), billPolicyDetails))
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
