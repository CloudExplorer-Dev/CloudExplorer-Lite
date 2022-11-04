package com.fit2cloud.provider.impl.vsphere;

import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.platform.credential.impl.VsphereCredential;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.*;
import com.fit2cloud.provider.impl.vsphere.api.VsphereSyncCloudApi;
import com.fit2cloud.provider.impl.vsphere.entity.*;
import com.fit2cloud.provider.impl.vsphere.entity.request.*;
import com.fit2cloud.provider.impl.vsphere.util.DiskType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<F2CHost> listHost(String req) {
        return VsphereSyncCloudApi.listHost(JsonUtil.parseObject(req, VsphereVmBaseRequest.class));
    }

    @Override
    public List<F2CDatastore> listDataStore(String req) {
        return VsphereSyncCloudApi.listDataStore(JsonUtil.parseObject(req, VsphereVmBaseRequest.class));
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

    public List<F2CVsphereCluster> getClusters(String req) {
        VsphereVmCreateRequest request = JsonUtil.parseObject(req, VsphereVmCreateRequest.class);
        request.setRegionId(request.getRegion());
        return VsphereSyncCloudApi.getClusters(request);
    }

    public List<F2CVsphereNetwork> getNetworks(String req) {
        return VsphereSyncCloudApi.getNetworks(JsonUtil.parseObject(req, VsphereVmCreateRequest.class));
    }

    public List<Map<String, String>> getLocations(String req) {
        VsphereVmCreateRequest request = JsonUtil.parseObject(req, VsphereVmCreateRequest.class);
        return VsphereSyncCloudApi.getLocations(request);
    }

    public List<VsphereHost> getHosts(String req) {
        VsphereVmCreateRequest request = JsonUtil.parseObject(req, VsphereVmCreateRequest.class);
        return VsphereSyncCloudApi.getHosts(request);
    }

    public List<VsphereResourcePool> geResourcePools(String req) {
        VsphereVmCreateRequest request = JsonUtil.parseObject(req, VsphereVmCreateRequest.class);
        return VsphereSyncCloudApi.geResourcePools(request);
    }

    public List<VsphereFolder> getFolders(String req) {
        VsphereVmCreateRequest request = JsonUtil.parseObject(req, VsphereVmCreateRequest.class);
        return VsphereSyncCloudApi.getFolders(request);
    }

    public List<Map<String, String>> getDiskTypes(String req) {
        List<Map<String, String>> diskTypes = new ArrayList<>();
        Map<String, String> defaultMap = new HashMap<>();
        defaultMap.put("info", "与源格式相同");
        defaultMap.put("value", DiskType.DEFAULT);
        diskTypes.add(defaultMap);
        Map<String, String> thinMap = new HashMap<>();
        thinMap.put("info", "精简置备");
        thinMap.put("value", DiskType.THIN);
        diskTypes.add(thinMap);
        Map<String, String> Map = new HashMap<>();
        Map.put("info", "厚置备置零");
        Map.put("value", DiskType.EAGER_ZEROED);
        diskTypes.add(Map);
        return diskTypes;
    }

    public List<VsphereDatastore> getDatastoreList(String req) {
        VsphereVmCreateRequest request = JsonUtil.parseObject(req, VsphereVmCreateRequest.class);
        return VsphereSyncCloudApi.getDatastoreList(request);
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
