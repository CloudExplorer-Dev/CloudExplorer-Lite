package com.fit2cloud.provider.impl.vsphere;

import com.fit2cloud.common.provider.impl.vsphere.entity.credential.VsphereCredential;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.impl.vsphere.api.VsphereSyncCloudApi;
import com.fit2cloud.provider.impl.vsphere.entity.request.VsphereVmBaseRequest;

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
}
