package com.fit2cloud.provider.impl.vsphere;

import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.platform.credential.impl.VsphereCredential;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.impl.vsphere.api.VsphereSyncCloudApi;
import com.fit2cloud.provider.impl.vsphere.entity.F2CVsphereCluster;
import com.fit2cloud.provider.impl.vsphere.entity.F2CVsphereNetwork;
import com.fit2cloud.provider.impl.vsphere.entity.VsphereTemplate;
import com.fit2cloud.provider.impl.vsphere.entity.request.VsphereNetworkRequest;
import com.fit2cloud.provider.impl.vsphere.entity.request.VsphereVmBaseRequest;
import com.fit2cloud.provider.impl.vsphere.entity.request.VsphereVmCreateRequest;
import com.fit2cloud.provider.impl.vsphere.entity.request.VsphereVmPowerRequest;

import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/9/20 5:20 PM
 */
public class VsphereCloudProvider extends AbstractCloudProvider<VsphereCredential> implements ICloudProvider {

    @Override
    public FormObject getCreateServerForm() {
        return FormUtil.toForm(VsphereVmCreateRequest.class);
    }

    @Override
    public List<F2CVirtualMachine> listVirtualMachine(String req) {
        return VsphereSyncCloudApi.listVirtualMachine(JsonUtil.parseObject(req, VsphereVmBaseRequest.class));
    }

    @Override
    public List<F2CImage> listImage(String req) {
        return VsphereSyncCloudApi.listImage(JsonUtil.parseObject(req, VsphereVmBaseRequest.class));
    }

    @Override
    public List<F2CDisk> listDisk(String req) {
        return VsphereSyncCloudApi.listDisk(JsonUtil.parseObject(req, VsphereVmBaseRequest.class));
    }

    @Override
    public boolean powerOff(String req) {
        return VsphereSyncCloudApi.powerOff(JsonUtil.parseObject(req, VsphereVmPowerRequest.class));
    }

    @Override
    public boolean powerOn(String req) {
        return VsphereSyncCloudApi.powerOn(JsonUtil.parseObject(req, VsphereVmPowerRequest.class));
    }

    @Override
    public boolean shutdownInstance(String req) {
        return VsphereSyncCloudApi.shutdownInstance(JsonUtil.parseObject(req, VsphereVmPowerRequest.class));
    }

    @Override
    public boolean rebootInstance(String req) {
        return VsphereSyncCloudApi.reboot(JsonUtil.parseObject(req, VsphereVmPowerRequest.class));
    }

    @Override
    public boolean deleteInstance(String req) {
        return VsphereSyncCloudApi.deleteInstance(JsonUtil.parseObject(req, VsphereVmPowerRequest.class));
    }

    public List<F2CVsphereCluster> getClusters(String req) {
        VsphereVmCreateRequest request= JsonUtil.parseObject(req, VsphereVmCreateRequest.class);
        request.setRegionId(request.getRegion());
        return VsphereSyncCloudApi.getClusters(request);
    }

    public List<VsphereTemplate> getTemplates(String req) {
        VsphereVmCreateRequest request= JsonUtil.parseObject(req, VsphereVmCreateRequest.class);
        request.setRegionId(request.getRegion());
        return VsphereSyncCloudApi.getTemplates(request);
    }

    public List<F2CVsphereNetwork> getNetworks(String req) {
        return VsphereSyncCloudApi.getNetworks(JsonUtil.parseObject(req, VsphereNetworkRequest.class));
    }
}
