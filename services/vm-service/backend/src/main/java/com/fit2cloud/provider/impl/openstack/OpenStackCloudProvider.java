package com.fit2cloud.provider.impl.openstack;

import com.fit2cloud.common.platform.credential.impl.OpenStackCredential;
import com.fit2cloud.common.provider.impl.openstack.entity.request.OpenStackBaseRequest;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.impl.openstack.api.OpenStackCloudApi;
import com.fit2cloud.provider.impl.openstack.entity.request.OpenStackInstanceActionRequest;

import java.util.List;

public class OpenStackCloudProvider extends AbstractCloudProvider<OpenStackCredential> implements ICloudProvider {

    @Override
    public List<F2CVirtualMachine> listVirtualMachine(String req) {
        return OpenStackCloudApi.listVirtualMachine(JsonUtil.parseObject(req, OpenStackBaseRequest.class));
    }

    @Override
    public List<F2CImage> listImage(String req) {
        return OpenStackCloudApi.listImage(JsonUtil.parseObject(req, OpenStackBaseRequest.class));
    }

    @Override
    public List<F2CDisk> listDisk(String req) {
        return OpenStackCloudApi.listDisk(JsonUtil.parseObject(req, OpenStackBaseRequest.class));
    }

    @Override
    public boolean powerOff(String req) {
        return OpenStackCloudApi.powerOff(JsonUtil.parseObject(req, OpenStackInstanceActionRequest.class));
    }

    @Override
    public boolean powerOn(String req) {
        return OpenStackCloudApi.powerOn(JsonUtil.parseObject(req, OpenStackInstanceActionRequest.class));
    }

    @Override
    public boolean shutdownInstance(String req) {
        return OpenStackCloudApi.powerOff(JsonUtil.parseObject(req, OpenStackInstanceActionRequest.class));
    }

    @Override
    public boolean rebootInstance(String req) {
        return OpenStackCloudApi.rebootInstance(JsonUtil.parseObject(req, OpenStackInstanceActionRequest.class));
    }

    @Override
    public boolean hardRebootInstance(String req) {
        return OpenStackCloudApi.rebootInstance(JsonUtil.parseObject(req, OpenStackInstanceActionRequest.class).setForce(true));
    }

    @Override
    public boolean deleteInstance(String req) {
        return OpenStackCloudApi.deleteInstance(JsonUtil.parseObject(req, OpenStackInstanceActionRequest.class));
    }


}
