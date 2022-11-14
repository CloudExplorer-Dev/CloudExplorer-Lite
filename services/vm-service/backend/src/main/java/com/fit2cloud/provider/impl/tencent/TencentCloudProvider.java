package com.fit2cloud.provider.impl.tencent;

import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.platform.credential.impl.TencentCredential;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.constants.DeleteWithInstance;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.entity.request.GetMetricsRequest;
import com.fit2cloud.provider.impl.tencent.api.TencetSyncCloudApi;
import com.fit2cloud.provider.impl.tencent.constants.TencentDiskType;
import com.fit2cloud.provider.impl.tencent.entity.request.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  11:11 AM
 * @Version 1.0
 * @注释:
 */
public class TencentCloudProvider extends AbstractCloudProvider<TencentCredential> implements ICloudProvider {
    @Override
    public List<F2CVirtualMachine> listVirtualMachine(String req) {
        return TencetSyncCloudApi.listVirtualMachine(JsonUtil.parseObject(req, ListVirtualMachineRequest.class));
    }

    @Override
    public List<F2CImage> listImage(String req) {
        return TencetSyncCloudApi.listImage(JsonUtil.parseObject(req, ListImageRequest.class));
    }

    @Override
    public List<F2CDisk> listDisk(String req) {
        return TencetSyncCloudApi.listDisk(JsonUtil.parseObject(req, ListDiskRequest.class));
    }

    @Override
    public boolean powerOff(String req) {
        TencentInstanceRequest request = JsonUtil.parseObject(req, TencentInstanceRequest.class);
        request.setForce(true);
        return TencetSyncCloudApi.powerOff(request);
    }

    @Override
    public boolean powerOn(String req) {
        return TencetSyncCloudApi.powerOn(JsonUtil.parseObject(req, TencentInstanceRequest.class));
    }

    @Override
    public boolean shutdownInstance(String req) {
        return TencetSyncCloudApi.powerOff(JsonUtil.parseObject(req, TencentInstanceRequest.class));
    }

    @Override
    public boolean rebootInstance(String req) {
        return TencetSyncCloudApi.rebootInstance(JsonUtil.parseObject(req, TencentInstanceRequest.class));
    }

    @Override
    public boolean deleteInstance(String req) {
        return TencetSyncCloudApi.deleteInstance(JsonUtil.parseObject(req, TencentInstanceRequest.class));
    }

    @Override
    public boolean hardShutdownInstance(String req) {
        TencentInstanceRequest request = JsonUtil.parseObject(req, TencentInstanceRequest.class);
        request.setForce(true);
        return TencetSyncCloudApi.powerOff(request);
    }

    @Override
    public boolean hardRebootInstance(String req) {
        TencentInstanceRequest request = JsonUtil.parseObject(req, TencentInstanceRequest.class);
        request.setForce(true);
        return TencetSyncCloudApi.rebootInstance(request);
    }

    @Override
    public FormObject getCreateDiskForm() {
        return FormUtil.toForm(TencentCreateDiskForm.class);
    }

    public List<Map<String, String>> getFileSystemType(String req) {
        List<Map<String, String>> types = new ArrayList<>();
        Map<String, String> typeOne = new HashMap<>();
        typeOne.put("id", "EXT4");
        typeOne.put("name", "EXT4");
        types.add(typeOne);

        Map<String, String> typeTwo = new HashMap<>();
        typeTwo.put("id", "XFS");
        typeTwo.put("name", "XFS");
        types.add(typeTwo);
        return types;
    }

    public List<Map<String, String>> getDiskTypes(String req) {
        List<Map<String, String>> types = new ArrayList<>();
        for (TencentDiskType tencentDiskType : TencentDiskType.values()) {
            Map<String, String> item = new HashMap<>();
            item.put("id", tencentDiskType.getId());
            item.put("name", tencentDiskType.getName());
            types.add(item);
        }
        return types;
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
        return TencetSyncCloudApi.createDisks(JsonUtil.parseObject(req, TencentCreateDisksRequest.class));
    }

    @Override
    public F2CDisk createDisk(String req) {
        return TencetSyncCloudApi.createDisk(JsonUtil.parseObject(req, TencentCreateDiskRequest.class));
    }

    @Override
    public boolean enlargeDisk(String req) {
        return TencetSyncCloudApi.enlargeDisk(JsonUtil.parseObject(req, TencentResizeDiskRequest.class));
    }

    @Override
    public boolean attachDisk(String req) {
        return TencetSyncCloudApi.attachDisk(JsonUtil.parseObject(req, TencentAttachDiskRequest.class));
    }

    public boolean detachDisk(String req) {
        return TencetSyncCloudApi.detachDisk(JsonUtil.parseObject(req, TencentDetachDiskRequest.class));
    }

    @Override
    public boolean deleteDisk(String req) {
        return TencetSyncCloudApi.deleteDisk(JsonUtil.parseObject(req, TencentDeleteDiskRequest.class));
    }

    @Override
    public List<F2CPerfMetricMonitorData> getF2CPerfMetricMonitorData(String req) {
        return TencetSyncCloudApi.getF2CPerfMetricList(JsonUtil.parseObject(req, GetMetricsRequest.class));
    }
}
