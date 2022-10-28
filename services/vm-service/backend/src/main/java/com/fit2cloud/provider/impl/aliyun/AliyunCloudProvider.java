package com.fit2cloud.provider.impl.aliyun;

import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.impl.aliyun.api.AliyunSyncCloudApi;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliyunVmCredential;
import com.fit2cloud.provider.impl.aliyun.entity.request.*;

import java.util.List;

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
        return AliyunSyncCloudApi.powerOff(JsonUtil.parseObject(req, AliyunInstanceRequest.class));
    }

    @Override
    public boolean powerOn(String req) {
        return AliyunSyncCloudApi.powerOn(JsonUtil.parseObject(req, AliyunInstanceRequest.class));
    }

    @Override
    public boolean shutdownInstance(String req){
        return AliyunSyncCloudApi.powerOff(JsonUtil.parseObject(req, AliyunInstanceRequest.class));
    }

    @Override
    public boolean rebootInstance(String req){
        return AliyunSyncCloudApi.rebootInstance(JsonUtil.parseObject(req, AliyunInstanceRequest.class));
    }

    @Override
    public boolean deleteInstance(String req){
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
    public List<F2CDisk>  createDisks(String req) {
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
}
