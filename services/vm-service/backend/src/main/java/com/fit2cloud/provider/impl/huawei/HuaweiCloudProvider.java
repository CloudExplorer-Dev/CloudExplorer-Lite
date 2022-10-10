package com.fit2cloud.provider.impl.huawei;

import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.impl.huawei.api.HuaweiSyncCloudApi;
import com.fit2cloud.provider.impl.huawei.entity.credential.HuaweiVmCredential;
import com.fit2cloud.provider.impl.huawei.entity.request.ListDisksRequest;
import com.fit2cloud.provider.impl.huawei.entity.request.ListImageRequest;
import com.fit2cloud.provider.impl.huawei.entity.request.ListVirtualMachineRequest;

import java.util.List;

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
