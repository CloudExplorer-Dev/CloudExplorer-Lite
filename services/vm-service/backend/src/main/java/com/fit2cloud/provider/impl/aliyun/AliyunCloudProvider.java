package com.fit2cloud.provider.impl.aliyun;

import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.impl.aliyun.api.AliyunSyncCloudApi;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliyunVmCredential;
import com.fit2cloud.provider.impl.aliyun.entity.request.ListDisksRequest;
import com.fit2cloud.provider.impl.aliyun.entity.request.ListImageRequest;
import com.fit2cloud.provider.impl.aliyun.entity.request.ListVirtualMachineRequest;

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
        return false;
    }

    @Override
    public boolean powerOn(String req) {
        return false;
    }

    @Override
    public boolean shutdownInstance(String req){
        return false;
    }

    @Override
    public boolean rebootInstance(String req){
        return false;
    }

    @Override
    public boolean deleteInstance(String req){
        return false;
    }

}
