package com.fit2cloud.provider.impl.proxmox;

import com.fit2cloud.common.constants.ChargeTypeConstants;
import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.Form;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.proxmox.ProxmoxBaseCloudProvider;
import com.fit2cloud.common.provider.impl.proxmox.client.PveClient;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.DataStore;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.Node;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.provider.impl.proxmox.api.ActionApi;
import com.fit2cloud.provider.impl.proxmox.api.SyncApi;
import com.fit2cloud.provider.impl.proxmox.entity.ProxmoxActionBaseRequest;
import com.fit2cloud.provider.impl.proxmox.entity.constants.DiskType;
import com.fit2cloud.provider.impl.proxmox.entity.constants.ProxmoxPeriodOption;
import com.fit2cloud.provider.impl.proxmox.entity.credential.VmProxmoxCredential;
import com.fit2cloud.provider.impl.proxmox.entity.request.*;
import com.fit2cloud.vm.AbstractCloudProvider;
import com.fit2cloud.vm.ICloudProvider;
import com.fit2cloud.vm.ICreateServerRequest;
import com.fit2cloud.vm.constants.ActionInfoConstants;
import com.fit2cloud.vm.entity.F2CDisk;
import com.fit2cloud.vm.entity.F2CHost;
import com.fit2cloud.vm.entity.F2CImage;
import com.fit2cloud.vm.entity.F2CVirtualMachine;
import com.fit2cloud.vm.entity.request.BaseDiskRequest;
import com.fit2cloud.vm.entity.request.BaseDiskResizeRequest;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.pf4j.Extension;

import java.util.*;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/7  10:01}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Extension
public class ProxmoxCloudProvider extends AbstractCloudProvider<VmProxmoxCredential> implements ICloudProvider {

    public static final ProxmoxBaseCloudProvider proxmoxBaseProvider = new ProxmoxBaseCloudProvider();

    public static final Info info = new Info("vm-service", List.of(
            ActionInfoConstants.SYNC_IMAGE,
            ActionInfoConstants.SYNC_DISK,
            ActionInfoConstants.SYNC_VIRTUAL_MACHINE), Map.of());


    @Override
    public List<F2CDisk> getVmF2CDisks(String req) {
        ProxmoxActionBaseRequest request = JsonUtil.parseObject(req, ProxmoxActionBaseRequest.class);
        return SyncApi.getVmF2CDisks(request);
    }

    @Override
    public F2CVirtualMachine getSimpleServerByCreateRequest(String req) {
        ProxmoxCreateServerRequest request = JsonUtil.parseObject(req, ProxmoxCreateServerRequest.class);
        F2CVirtualMachine virtualMachine = new F2CVirtualMachine();
        int index = request.getIndex();
        String instanceType = (request.getCpu() * request.getCpuSlot()) + "vCpu " + request.getMem() + "GB";
        virtualMachine
                .setId(request.getId())
                .setName(request.getHostObj().get(index).getName())
                .setCpu(request.getCpu())
                .setMemory(request.getMem())
                .setIpArray(new ArrayList<>())
                .setInstanceType(instanceType)
                .setInstanceTypeDescription(instanceType)
                .setRemark(request.getHostObj().get(index).getNotes())
                .setInstanceChargeType(request.getInstanceChargeType());
        if (StringUtils.equals(ChargeTypeConstants.PREPAID.getCode(), request.getInstanceChargeType())) {
            virtualMachine.setExpiredTime(CommonUtil.getExpiredTimeEpochMilli(request.getPeriodNum()));
        }
        return virtualMachine;
    }

    @Override
    public boolean powerOn(String req) {
        ProxmoxActionBaseRequest powerOnRequest = JsonUtil.parseObject(req, ProxmoxActionBaseRequest.class);
        return ActionApi.powerOnVm(powerOnRequest);
    }

    @Override
    public boolean powerOff(String req) {
        ProxmoxActionBaseRequest powerOffRequest = JsonUtil.parseObject(req, ProxmoxActionBaseRequest.class);
        return ActionApi.powerOffVm(powerOffRequest);
    }

    @Override
    public boolean shutdownInstance(String req) {
        ProxmoxActionBaseRequest request = JsonUtil.parseObject(req, ProxmoxActionBaseRequest.class);
        return ActionApi.shutdownInstance(request);
    }

    @Override
    public boolean hardShutdownInstance(String req) {
        ProxmoxActionBaseRequest powerOffRequest = JsonUtil.parseObject(req, ProxmoxActionBaseRequest.class);
        return ActionApi.powerOffVm(powerOffRequest);
    }

    @Override
    public boolean rebootInstance(String req) {
        ProxmoxActionBaseRequest request = JsonUtil.parseObject(req, ProxmoxActionBaseRequest.class);
        return ActionApi.reboot(request);
    }

    @Override
    public boolean deleteInstance(String req) {
        ProxmoxActionBaseRequest request = JsonUtil.parseObject(req, ProxmoxActionBaseRequest.class);
        return ActionApi.delete(request);
    }

    @Override
    public F2CDisk createDisk(String req) {
        AddDiskFormRequest request = JsonUtil.parseObject(req, AddDiskFormRequest.class);
        return ActionApi.createDisk(request);
    }

    @Override
    public boolean hardRebootInstance(String req) {
        return super.hardRebootInstance(req);
    }

    @Override
    public List<F2CImage> listImage(String req) {
        ProxmoxBaseRequest baseRequest = JsonUtil.parseObject(req, ProxmoxBaseRequest.class);
        return SyncApi.listImage(baseRequest);
    }


    @Override
    public FormObject getCreateServerForm() {
        return FormUtil.toForm(ProxmoxCreateServerRequest.class);
    }

    @Override
    public Class<? extends ICreateServerRequest> getCreateServerRequestClass() {
        return ProxmoxCreateServerRequest.class;
    }

    @Override
    public F2CBalance getAccountBalance(String getAccountBalanceRequest) {
        return proxmoxBaseProvider.getAccountBalance(getAccountBalanceRequest);
    }

    @Override
    public CloudAccountMeta getCloudAccountMeta() {
        return proxmoxBaseProvider.getCloudAccountMeta();
    }

    @Override
    public Info getInfo() {
        return info;
    }


    public List<Node> getNodes(String request) {
        VmProxmoxCredential credentialByRequest = getCredentialByRequest(request);
        PveClient.PVENodes nodes = credentialByRequest.getClient().getNodes();
        String data = nodes.index().getResponse().getJSONArray("data").toString();
        return JsonUtil.parseArray(data, Node.class);
    }

    public List<Map<String, Object>> getPeriodOption(String req) {
        List<Map<String, Object>> periodList = new ArrayList<>();
        for (ProxmoxPeriodOption option : ProxmoxPeriodOption.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("period", option.getPeriod());
            map.put("periodDisplayName", option.getPeriodDisplayName());
            periodList.add(map);
        }
        return periodList;
    }

    public List<DefaultKeyValue<String, String>> getAuthTypeList(String req) {
        return List.of(new DefaultKeyValue<>("自定义密码", "custom"));
    }

    @Override
    public FormObject getConfigUpdateForm() {
        return FormUtil.toForm(ConfigUpdateForm.class);
    }

    @Override
    public String calculateConfigPrice(String req) {
        ProxmoxCalculateConfigPriceRequest proxmoxCalculateConfigPriceRequest =
                JsonUtil.parseObject(req, ProxmoxCalculateConfigPriceRequest.class);
        return ActionApi.calculateConfigPrice(proxmoxCalculateConfigPriceRequest);
    }

    @Override
    public String calculateConfigUpdatePrice(String req) {
        CalculateConfigUpdatePriceRequest calculateConfigUpdatePriceRequest =
                JsonUtil.parseObject(req, CalculateConfigUpdatePriceRequest.class);
        ProxmoxCalculateConfigPriceRequest request = new ProxmoxCalculateConfigPriceRequest();
        request.setMem(calculateConfigUpdatePriceRequest.getMem());
        request.setCpuSlot(calculateConfigUpdatePriceRequest.getCpuSlot());
        request.setCpu(calculateConfigUpdatePriceRequest.getCpu());
        request.setCount(1);
        request.setPeriodNum("1");
        request.setAccountId(calculateConfigUpdatePriceRequest.getCloudAccountId());
        request.setDisks(List.of());
        request.setInstanceChargeType(calculateConfigUpdatePriceRequest.getInstanceChargeType());
        return ActionApi.calculateConfigPrice(request);

    }

    @Override
    public F2CVirtualMachine changeVmConfig(String req) {
        ProxmoxUpdateConfigRequest request = JsonUtil.parseObject(req, ProxmoxUpdateConfigRequest.class);
        return ActionApi.changeVmConfig(request);
    }

    @Override
    public FormObject getCreateDiskForm() {
        return FormUtil.toForm(AddDiskForm.class);
    }

    @Override
    public List<F2CDisk> listDisk(String req) {
        ProxmoxCreateServerRequest proxmoxCreateServerRequest = JsonUtil.parseObject(req, ProxmoxCreateServerRequest.class);
        return SyncApi.listF2CDisk(proxmoxCreateServerRequest);
    }

    @Override
    public boolean deleteDisk(String req) {
        BaseDiskRequest request = JsonUtil.parseObject(req, BaseDiskRequest.class);
        return ActionApi.deleteDisk(request);
    }

    @Override
    public boolean detachDisk(String req) {
        BaseDiskRequest request = JsonUtil.parseObject(req, BaseDiskRequest.class);
        return ActionApi.detachDisk(request);
    }


    @Override
    public boolean enlargeDisk(String req) {
        BaseDiskResizeRequest request = JsonUtil.parseObject(req, BaseDiskResizeRequest.class);
        return ActionApi.enlargeDisk(request);
    }

    @Override
    public F2CDisk attachDisk(String req) {
        BaseDiskRequest request = JsonUtil.parseObject(req, BaseDiskRequest.class);
        return ActionApi.attachDisk(request);
    }

    @Override
    public F2CVirtualMachine createVirtualMachine(String req) {
        ProxmoxCreateServerRequest proxmoxCreateServerRequest = JsonUtil.parseObject(req, ProxmoxCreateServerRequest.class);
        return ActionApi.createVirtualMachine(proxmoxCreateServerRequest);
    }

    public List<DefaultKeyValue<String, String>> getChargeType(String req) {
        return Arrays.stream(ChargeTypeConstants.values())
                .map(model -> new DefaultKeyValue<>(model.getMessage(), model.getCode()))
                .toList();
    }

    @SneakyThrows
    public List<?> listVmCloudImage(String req) {
        Object vmCloudImageServiceImpl = SpringUtil.getBean("VmCloudImageServiceImpl");
        return (List<?>) MethodUtils.invokeMethod(vmCloudImageServiceImpl, true, "listVmCloudImage", new Object[]{req});
    }


    @Override
    public List<F2CVirtualMachine> listVirtualMachine(String req) {
        ProxmoxBaseRequest proxmoxBaseRequest = JsonUtil.parseObject(req, ProxmoxBaseRequest.class);
        return SyncApi.listVirtualMachine(proxmoxBaseRequest);
    }

    @Override
    public List<F2CHost> listHost(String req) {
        ProxmoxBaseRequest proxmoxBaseRequest = JsonUtil.parseObject(req, ProxmoxBaseRequest.class);
        return SyncApi.listF2CHost(proxmoxBaseRequest);
    }

    public List<DataStore> getDataStoreList(String req) {
        ProxmoxBaseRequest proxmoxBaseRequest = JsonUtil.parseObject(req, ProxmoxBaseRequest.class);
        return SyncApi.listDataStore(proxmoxBaseRequest);
    }

    public List<DefaultKeyValue<String, String>> getDhcpType(String req) {
        return List.of(new DefaultKeyValue<>(" DHCP自动分配 ", "dhcp"),
                new DefaultKeyValue<>(" 手动分配 ", "static"));

    }

    public List<Object> getNetworkList(String req) {
        ProxmoxBaseRequest proxmoxBaseRequest = JsonUtil.parseObject(req, ProxmoxBaseRequest.class);
        VmProxmoxCredential credential = JsonUtil.parseObject(proxmoxBaseRequest.getCredential(), VmProxmoxCredential.class);
        return credential
                .getClient()
                .getNodes()
                .get(proxmoxBaseRequest.getRegionId())
                .getNetwork()
                .index("any_bridge")
                .getResponse()
                .getJSONArray("data")
                .toList();
    }

    public List<DefaultKeyValue<String, String>> getModelList(String req) {
        return List.of(new DefaultKeyValue<>("Intel E1000", "e1000"),
                new DefaultKeyValue<>("VirtIO (paravirtualized)", "virtio"),
                new DefaultKeyValue<>("Realtek RTL8139", "rtl8139"),
                new DefaultKeyValue<>("VMware vmxnet3", "vmxnet3"));
    }

    public List<DefaultKeyValue<String, String>> getDeviceList(String req) {
        return List.of(new DefaultKeyValue<>("SCSI", "scsi"),
                new DefaultKeyValue<>("IDE", "ide"),
                new DefaultKeyValue<>("SATA", "sata"),
                new DefaultKeyValue<>("VirtIO Block", "virtio"));
    }

    public List<DefaultKeyValue<String, String>> getFormatList(String req) {
        AddDiskForm addDiskForm = JsonUtil.parseObject(req, AddDiskForm.class);
        VmProxmoxCredential vmProxmoxCredential = JsonUtil.parseObject(addDiskForm.getCredential(), VmProxmoxCredential.class);
        DataStore dataStore = SyncApi.getDataStore(vmProxmoxCredential, addDiskForm.getRegionId(), addDiskForm.getDatastore());
        if (CollectionUtils.isEmpty(dataStore.getFormats())) {
            return List.of(new DefaultKeyValue<>(DiskType.raw.getMessage(), DiskType.raw.name()));
        } else {
            return Arrays.stream(DiskType.values()).filter(item -> dataStore.getFormats().contains(item.name()))
                    .map(item -> new DefaultKeyValue<>(item.getMessage(), item.name()))
                    .toList();
        }
    }

    public List<? extends Form> getNetworkForm(String req) {
        return FormUtil.toForm(ProxmoxNetworkForm.class).getForms();
    }

    public List<? extends Form> getNetworkAdapterForm(String req) {
        return FormUtil.toForm(ProxmoxNetworkForm.NetworkAdapter.class).getForms();
    }

    public List<? extends Form> getAuthObjForm(String req) {
        return FormUtil.toForm(ProxmoxCreateServerRequest.AuthObj.class).getForms();
    }

    public List<? extends Form> getHostObjForm(String req) {
        return FormUtil.toForm(ProxmoxCreateServerRequest.HostObj.class).getForms();
    }
}
