package com.fit2cloud.provider.impl.huawei;

import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.constants.DeleteWithInstance;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.entity.request.GetMetricsRequest;
import com.fit2cloud.provider.impl.huawei.api.HuaweiSyncCloudApi;
import com.fit2cloud.provider.impl.huawei.entity.credential.HuaweiVmCredential;
import com.fit2cloud.provider.impl.huawei.entity.request.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  11:06 AM
 * @Version 1.0
 * @注释:
 */
public class HuaweiCloudProvider extends AbstractCloudProvider<HuaweiVmCredential> implements ICloudProvider {
    @Override
    public List<F2CVirtualMachine> listVirtualMachine(String req) {
        return HuaweiSyncCloudApi.listVirtualMachine(JsonUtil.parseObject(req, ListVirtualMachineRequest.class));
    }

    @Override
    public List<F2CImage> listImage(String req) {
        return HuaweiSyncCloudApi.lisImages(JsonUtil.parseObject(req, ListImageRequest.class));
    }

    @Override
    public List<F2CDisk> listDisk(String req) {
        return HuaweiSyncCloudApi.listDisk(JsonUtil.parseObject(req, ListDisksRequest.class));
    }

    @Override
    public boolean powerOff(String req) {
        HuaweiInstanceRequest request = JsonUtil.parseObject(req, HuaweiInstanceRequest.class);
        request.setForce(true);
        return HuaweiSyncCloudApi.powerOff(request);
    }

    @Override
    public boolean powerOn(String req) {
        return HuaweiSyncCloudApi.powerOn(JsonUtil.parseObject(req, HuaweiInstanceRequest.class));
    }

    @Override
    public boolean shutdownInstance(String req) {
        return HuaweiSyncCloudApi.powerOff(JsonUtil.parseObject(req, HuaweiInstanceRequest.class));
    }

    @Override
    public boolean rebootInstance(String req) {
        return HuaweiSyncCloudApi.rebootInstance(JsonUtil.parseObject(req, HuaweiInstanceRequest.class));
    }

    @Override
    public boolean deleteInstance(String req) {
        return HuaweiSyncCloudApi.deleteInstance(JsonUtil.parseObject(req, HuaweiInstanceRequest.class));
    }

    @Override
    public boolean hardShutdownInstance(String req) {
        HuaweiInstanceRequest request = JsonUtil.parseObject(req, HuaweiInstanceRequest.class);
        request.setForce(true);
        return HuaweiSyncCloudApi.powerOff(request);
    }

    @Override
    public boolean hardRebootInstance(String req) {
        HuaweiInstanceRequest request = JsonUtil.parseObject(req, HuaweiInstanceRequest.class);
        request.setForce(true);
        return HuaweiSyncCloudApi.rebootInstance(request);
    }

    @Override
    public FormObject getCreateDiskForm() {
        return FormUtil.toForm(HuaweiCreateDiskForm.class);
    }

    public List<Map<String, String>> getDiskTypes(String req) {
        return HuaweiSyncCloudApi.getDiskTypes(JsonUtil.parseObject(req, HuaweiGetDiskTypeRequest.class));
    }

    @Override
    public List<Map<String, String>> getDeleteWithInstance(String req) {
        List<Map<String, String>> deleteWithInstance = new ArrayList<>();
        Map<String, String> yes = new HashMap<>();
        yes.put("id", DeleteWithInstance.YES.name());
        yes.put("name", "是");
        deleteWithInstance.add(yes);

        Map<String, String> no = new HashMap<>();
        no.put("id", DeleteWithInstance.NO.name());
        no.put("name", "否");
        deleteWithInstance.add(no);
        return deleteWithInstance;
    }

    @Override
    public List<F2CDisk> createDisks(String req) {
        return HuaweiSyncCloudApi.createDisks(JsonUtil.parseObject(req, HuaweiCreateDisksRequest.class));
    }

    @Override
    public F2CDisk createDisk(String req) {
        return HuaweiSyncCloudApi.createDisk(JsonUtil.parseObject(req, HuaweiCreateDiskRequest.class));
    }

    @Override
    public boolean enlargeDisk(String req) {
        return HuaweiSyncCloudApi.enlargeDisk(JsonUtil.parseObject(req, HuaweiResizeDiskRequest.class));
    }

    @Override
    public boolean attachDisk(String req) {
        return HuaweiSyncCloudApi.attachDisk(JsonUtil.parseObject(req, HuaweiAttachDiskRequest.class));
    }

    @Override
    public boolean detachDisk(String req) {
        return HuaweiSyncCloudApi.detachDisk(JsonUtil.parseObject(req, HuaweiDetachDiskRequest.class));
    }

    @Override
    public boolean deleteDisk(String req) {
        return HuaweiSyncCloudApi.deleteDisk(JsonUtil.parseObject(req, HuaweiDeleteDiskRequest.class));
    }

    @Override
    public List<F2CPerfMetricMonitorData> getF2CPerfMetricMonitorData(String req) {
        return HuaweiSyncCloudApi.getF2CPerfMetricList(JsonUtil.parseObject(req, GetMetricsRequest.class));
    }
}
