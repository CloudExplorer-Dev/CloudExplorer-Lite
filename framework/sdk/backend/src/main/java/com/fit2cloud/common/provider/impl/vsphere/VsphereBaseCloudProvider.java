package com.fit2cloud.common.provider.impl.vsphere;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.VsphereCredential;
import com.fit2cloud.common.provider.AbstractBaseCloudProvider;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.vsphere.entity.request.GetRegionsRequest;
import com.fit2cloud.common.provider.impl.vsphere.api.VsphereBaseMethodApi;
import com.fit2cloud.common.utils.JsonUtil;

import java.util.List;


public class VsphereBaseCloudProvider extends AbstractBaseCloudProvider<VsphereCredential> {


    public List<Credential.Region> getRegions(String req) {
        return VsphereBaseMethodApi.getRegions(JsonUtil.parseObject(req, GetRegionsRequest.class));
    }

    @Override
    public F2CBalance getAccountBalance(String getAccountBalanceRequest) {
        return null;
    }
}
