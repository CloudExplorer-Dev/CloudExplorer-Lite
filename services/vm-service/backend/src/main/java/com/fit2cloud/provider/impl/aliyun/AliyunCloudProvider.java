package com.fit2cloud.provider.impl.aliyun;

import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.entity.request.GetMetricsRequest;
import com.fit2cloud.provider.impl.aliyun.api.AliyunSyncCloudApi;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliyunVmCredential;
import com.fit2cloud.provider.impl.aliyun.entity.request.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  10:55 AM
 * @Version 1.0
 * @注释:
 */
public class AliyunCloudProvider extends AbstractCloudProvider<AliyunVmCredential> implements ICloudProvider {


    @Override
    public List<F2CVirtualMachine> listVirtualMachine(String req) {
        return AliyunSyncCloudApi.listVirtualMachine(JsonUtil.parseObject(req, ListVirtualMachineRequest.class));
    }

    @Override
    public List<F2CImage> listImage(String req) {
        return AliyunSyncCloudApi.listImage(JsonUtil.parseObject(req, ListImageRequest.class));
    }

    @Override
    public List<F2CDisk> listDisk(String req) {
        return AliyunSyncCloudApi.listDisk(JsonUtil.parseObject(req, ListDisksRequest.class));
    }

    @Override
    public boolean powerOff(String req) {
        AliyunInstanceRequest request = JsonUtil.parseObject(req, AliyunInstanceRequest.class);
        request.setForce(true);
        return AliyunSyncCloudApi.powerOff(request);
    }

    @Override
    public boolean powerOn(String req) {
        return AliyunSyncCloudApi.powerOn(JsonUtil.parseObject(req, AliyunInstanceRequest.class));
    }

    @Override
    public boolean shutdownInstance(String req) {
        return AliyunSyncCloudApi.powerOff(JsonUtil.parseObject(req, AliyunInstanceRequest.class));
    }

    @Override
    public boolean rebootInstance(String req) {
        return AliyunSyncCloudApi.rebootInstance(JsonUtil.parseObject(req, AliyunInstanceRequest.class));
    }

    @Override
    public boolean deleteInstance(String req) {
        AliyunInstanceRequest request = JsonUtil.parseObject(req, AliyunInstanceRequest.class);
        request.setForce(true);
        return AliyunSyncCloudApi.deleteInstance(request);
    }

    @Override
    public boolean hardShutdownInstance(String req) {
        AliyunInstanceRequest request = JsonUtil.parseObject(req, AliyunInstanceRequest.class);
        request.setForce(true);
        return AliyunSyncCloudApi.powerOff(request);
    }

    @Override
    public boolean hardRebootInstance(String req) {
        AliyunInstanceRequest request = JsonUtil.parseObject(req, AliyunInstanceRequest.class);
        request.setForce(true);
        return AliyunSyncCloudApi.rebootInstance(request);
    }

    @Override
    public FormObject getCreateDiskForm() {
        return FormUtil.toForm(AliyunCreateDiskForm.class);
    }

    public List<Map<String, String>> getDiskTypes(String req) {
        return AliyunSyncCloudApi.getDiskTypes(JsonUtil.parseObject(req, AliyunGetDiskTypeRequest.class));
    }

    @Override
    public List<Map<String, String>> getDeleteWithInstance(String req) {
        List<Map<String, String>> deleteWithInstance = new ArrayList<>();
        Map<String, String> no = new HashMap<>();
        no.put("id", "NO");
        no.put("name", "NO");
        deleteWithInstance.add(no);

        Map<String, String> yes = new HashMap<>();
        yes.put("id", "YES");
        yes.put("name", "YES");
        deleteWithInstance.add(yes);
        return deleteWithInstance;
    }

    @Override
    public List<F2CDisk> createDisks(String req) {
        return AliyunSyncCloudApi.createDisks(JsonUtil.parseObject(req, AliyunCreateDisksRequest.class));
    }

    @Override
    public F2CDisk createDisk(String req) {
        return AliyunSyncCloudApi.createDisk(JsonUtil.parseObject(req, AliyunCreateDiskRequest.class));
    }

    @Override
    public boolean enlargeDisk(String req) {
        return AliyunSyncCloudApi.enlargeDisk(JsonUtil.parseObject(req, AliyunResizeDiskRequest.class));
    }

    @Override
    public boolean attachDisk(String req) {
        return AliyunSyncCloudApi.attachDisk(JsonUtil.parseObject(req, AliyunAttachDiskRequest.class));
    }

    @Override
    public boolean detachDisk(String req) {
        return AliyunSyncCloudApi.detachDisk(JsonUtil.parseObject(req, AliyunDetachDiskRequest.class));
    }

    @Override
    public boolean deleteDisk(String req) {
        return AliyunSyncCloudApi.deleteDisk(JsonUtil.parseObject(req, AliyunDeleteDiskRequest.class));
    }

    @Override
    public List<F2CPerfMetricMonitorData> getF2CPerfMetricMonitorData(String req) {
        return AliyunSyncCloudApi.getF2CPerfMetricList(JsonUtil.parseObject(req, GetMetricsRequest.class));
    }
}
