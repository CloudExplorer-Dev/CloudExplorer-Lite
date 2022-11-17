package com.fit2cloud.common.provider.impl.openstack;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.OpenStackCredential;
import com.fit2cloud.common.provider.AbstractBaseCloudProvider;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.openstack.api.OpenStackBaseMethodApi;
import com.fit2cloud.common.provider.impl.openstack.entity.request.OpenStackBaseRequest;
import com.fit2cloud.common.utils.JsonUtil;

import java.util.List;


public class OpenStackBaseCloudProvider extends AbstractBaseCloudProvider<OpenStackCredential> {

    public F2CBalance getAccountBalance(String req) {
        return null;
    }

    public List<Credential.Region> getRegions(String req) {
        return OpenStackBaseMethodApi.getRegions(JsonUtil.parseObject(req, OpenStackBaseRequest.class));
    }

}
