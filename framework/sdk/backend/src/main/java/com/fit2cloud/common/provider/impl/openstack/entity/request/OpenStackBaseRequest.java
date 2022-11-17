package com.fit2cloud.common.provider.impl.openstack.entity.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fit2cloud.common.platform.credential.impl.OpenStackCredential;
import com.fit2cloud.common.utils.JsonUtil;
import lombok.Data;
import org.openstack4j.api.OSClient;

@Data
public class OpenStackBaseRequest extends BaseRequest {

    private OpenStackCredential openStackCredential;

    @JsonIgnore
    public OpenStackCredential getOpenStackCredential() {
        if (openStackCredential == null) {
            openStackCredential = JsonUtil.parseObject(getCredential(), OpenStackCredential.class);
        }
        return openStackCredential;
    }

    @JsonIgnore
    public OSClient.OSClientV3 getOSClient() {
        return getOpenStackCredential().getOSClient();
    }

}
