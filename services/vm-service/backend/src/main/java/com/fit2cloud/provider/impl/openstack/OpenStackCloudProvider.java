package com.fit2cloud.provider.impl.openstack;

import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.platform.credential.impl.OpenStackCredential;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.provider.impl.openstack.entity.request.OpenStackBaseRequest;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.*;
import com.fit2cloud.provider.entity.request.BaseDiskRequest;
import com.fit2cloud.provider.entity.request.GetMetricsRequest;
import com.fit2cloud.provider.entity.result.CheckCreateServerResult;
import com.fit2cloud.provider.impl.openstack.api.OpenStackCloudApi;
import com.fit2cloud.provider.impl.openstack.entity.OpenStackFlavor;
import com.fit2cloud.provider.impl.openstack.entity.VolumeType;
import com.fit2cloud.provider.impl.openstack.entity.request.*;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.SecurityGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenStackCloudProvider extends AbstractCloudProvider<OpenStackCredential> implements ICloudProvider {

    @Override
    public FormObject getCreateServerForm() {
        return FormUtil.toForm(OpenStackServerCreateRequest.class);
    }

    @Override
    public List<F2CVirtualMachine> listVirtualMachine(String req) {
        return OpenStackCloudApi.listVirtualMachine(JsonUtil.parseObject(req, OpenStackBaseRequest.class));
    }

    @Override
    public List<F2CImage> listImage(String req) {
        return OpenStackCloudApi.listImage(JsonUtil.parseObject(req, OpenStackBaseRequest.class));
    }

    @Override
    public List<F2CDisk> listDisk(String req) {
        return OpenStackCloudApi.listDisk(JsonUtil.parseObject(req, OpenStackBaseRequest.class));
    }

    @Override
    public boolean powerOff(String req) {
        return OpenStackCloudApi.powerOff(JsonUtil.parseObject(req, OpenStackInstanceActionRequest.class));
    }

    @Override
    public boolean powerOn(String req) {
        return OpenStackCloudApi.powerOn(JsonUtil.parseObject(req, OpenStackInstanceActionRequest.class));
    }

    @Override
    public boolean shutdownInstance(String req) {
        return OpenStackCloudApi.powerOff(JsonUtil.parseObject(req, OpenStackInstanceActionRequest.class));
    }

    @Override
    public boolean rebootInstance(String req) {
        return OpenStackCloudApi.rebootInstance(JsonUtil.parseObject(req, OpenStackInstanceActionRequest.class));
    }

    @Override
    public boolean hardRebootInstance(String req) {
        return OpenStackCloudApi.rebootInstance(JsonUtil.parseObject(req, OpenStackInstanceActionRequest.class).setForce(true));
    }

    @Override
    public boolean deleteInstance(String req) {
        return OpenStackCloudApi.deleteInstance(JsonUtil.parseObject(req, OpenStackInstanceActionRequest.class));
    }

    @Override
    public FormObject getCreateDiskForm() {
        return FormUtil.toForm(OpenStackDiskCreateRequest.class);
    }

    @Override
    public F2CDisk createDisk(String req) {
        return OpenStackCloudApi.createDisk(JsonUtil.parseObject(req, OpenStackDiskCreateRequest.class));
    }

    @Override
    public boolean enlargeDisk(String req) {
        return OpenStackCloudApi.enlargeDisk(JsonUtil.parseObject(req, OpenStackDiskEnlargeRequest.class));
    }

    @Override
    public F2CDisk attachDisk(String req) {
        return OpenStackCloudApi.attachDisk(JsonUtil.parseObject(req, OpenStackDiskActionRequest.class));
    }

    @Override
    public boolean detachDisk(String req) {
        return OpenStackCloudApi.detachDisk(JsonUtil.parseObject(req, OpenStackDiskActionRequest.class));
    }

    @Override
    public boolean deleteDisk(String req) {
        return OpenStackCloudApi.deleteDisk(JsonUtil.parseObject(req, OpenStackDiskActionRequest.class));
    }

    public List<VolumeType> listVolumeType(String req) {
        return OpenStackCloudApi.listVolumeType(JsonUtil.parseObject(req, OpenStackBaseRequest.class));
    }

    public List<Flavor> getFlavors(String req) {
        return OpenStackCloudApi.getFlavors(JsonUtil.parseObject(req, OpenStackServerCreateRequest.class));
    }

    public List<SecurityGroup> getSecurityGroups(String req) {
        return OpenStackCloudApi.getSecurityGroups(JsonUtil.parseObject(req, OpenStackServerCreateRequest.class));
    }

    public List<Network> getNetworks(String req) {
        return OpenStackCloudApi.getNetworks(JsonUtil.parseObject(req, OpenStackServerCreateRequest.class));
    }

    public List<Map<String, String>> getLoginModes(String req) {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> option = new HashMap<>();
        option.put("id", "password");
        option.put("name", "密码");
        list.add(option);
        return list;
    }

    @Override
    public F2CVirtualMachine getSimpleServerByCreateRequest(String req) {
        return OpenStackCloudApi.getSimpleServerByCreateRequest(JsonUtil.parseObject(req, OpenStackServerCreateRequest.class));
    }

    @Override
    public CheckCreateServerResult validateServerCreateRequest(String req) {
        return OpenStackCloudApi.validateServerCreateRequest(JsonUtil.parseObject(req, OpenStackServerCreateRequest.class));
    }

    @Override
    public F2CVirtualMachine createVirtualMachine(String req) {
        return OpenStackCloudApi.createVirtualMachine(JsonUtil.parseObject(req, OpenStackServerCreateRequest.class));
    }

    @Override
    public List<F2CHost> listHost(String req) {
        return OpenStackCloudApi.listHost(JsonUtil.parseObject(req, OpenStackBaseRequest.class));
    }

    @Override
    public List<F2CDatastore> listDataStore(String req) {
        return OpenStackCloudApi.listDataStore(JsonUtil.parseObject(req, OpenStackBaseRequest.class));
    }

    @Override
    public List<F2CPerfMetricMonitorData> getF2CPerfMetricMonitorData(String req) {
        return OpenStackCloudApi.getF2CPerfMetricMonitorData(JsonUtil.parseObject(req, GetMetricsRequest.class));
    }

    @Override
    public List<F2CPerfMetricMonitorData> getF2CHostPerfMetricMonitorData(String req) {
        return OpenStackCloudApi.getF2CHostPerfMetricList(req, JsonUtil.parseObject(req, GetMetricsRequest.class));
    }

    @Override
    public List<F2CPerfMetricMonitorData> getF2CDatastorePerfMetricMonitorData(String req) {
        return OpenStackCloudApi.getF2CDatastorePerfMetricList(req, JsonUtil.parseObject(req, GetMetricsRequest.class));
    }

    @Override
    public FormObject getConfigUpdateForm() {
        return FormUtil.toForm(OpenStackConfigUpdateForm.class);
    }


    public List<OpenStackFlavor> getInstanceTypesForConfigUpdate(String req) {
        return OpenStackCloudApi.getInstanceTypesForConfigUpdate(JsonUtil.parseObject(req, OpenStackConfigUpdateRequest.class));
    }

    @Override
    public F2CVirtualMachine changeVmConfig(String req) {
        return OpenStackCloudApi.changeVmConfig(JsonUtil.parseObject(req, OpenStackConfigUpdateRequest.class));
    }

    @Override
    public List<F2CDisk> getVmF2CDisks(String req) {
        return OpenStackCloudApi.getVmF2CDisks(JsonUtil.parseObject(req, BaseDiskRequest.class));
    }

}
