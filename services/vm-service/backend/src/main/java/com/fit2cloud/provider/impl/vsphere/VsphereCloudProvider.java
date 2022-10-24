package com.fit2cloud.provider.impl.vsphere;

import com.fit2cloud.common.platform.credential.impl.VsphereCredential;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.impl.vsphere.api.VsphereSyncCloudApi;
import com.fit2cloud.provider.impl.vsphere.entity.F2CVsphereNetwork;
import com.fit2cloud.provider.impl.vsphere.entity.request.*;

import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/9/20 5:20 PM
 */
public class VsphereCloudProvider extends AbstractCloudProvider<VsphereCredential> implements ICloudProvider {
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

    @Override
    public boolean hardShutdownInstance(String req) {
        return VsphereSyncCloudApi.powerOff(JsonUtil.parseObject(req, VsphereVmPowerRequest.class));
    }

    @Override
    public boolean hardRebootInstance(String req) {
        return VsphereSyncCloudApi.hardReboot(JsonUtil.parseObject(req, VsphereVmPowerRequest.class));
    }

    public List<F2CVsphereNetwork> getNetworks(String req) {
        return VsphereSyncCloudApi.getNetworks(JsonUtil.parseObject(req, VsphereNetworkRequest.class));
    }

    @Override
    public boolean enlargeDisk(String req) {
        return VsphereSyncCloudApi.enlargeDisk(JsonUtil.parseObject(req, VsphereResizeDiskRequest.class));
    }

    @Override
    public List<F2CDisk> createDisks(String req) {
        return VsphereSyncCloudApi.createDisks(JsonUtil.parseObject(req, VsphereCreateDiskRequest.class));
    }
}
