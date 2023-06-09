package com.fit2cloud.common.provider.impl.openstack;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.OpenStackCredential;
import com.fit2cloud.common.provider.AbstractBaseCloudProvider;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.openstack.api.OpenStackBaseMethodApi;
import com.fit2cloud.common.provider.impl.openstack.entity.Zone;
import com.fit2cloud.common.provider.impl.openstack.entity.request.OpenStackBaseRequest;
import com.fit2cloud.common.utils.JsonUtil;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.ArrayList;
import java.util.List;


public class OpenStackBaseCloudProvider extends AbstractBaseCloudProvider<OpenStackCredential> {

    @Override
    public F2CBalance getAccountBalance(String req) {
        return null;
    }

    public List<Credential.Region> getRegions(String req) {
        return OpenStackBaseMethodApi.getRegions(JsonUtil.parseObject(req, OpenStackBaseRequest.class));
    }

    public static List<Zone> getZones(String req) {
        return OpenStackBaseMethodApi.getZones(JsonUtil.parseObject(req, OpenStackBaseRequest.class));
    }

    /**
     * 获取账单同步方式
     *
     * @param req 请求字符串
     * @return 账单所有同步方式
     */
    public List<DefaultKeyValue<String, String>> getSyncModes(String req) {
        return new ArrayList<>() {{
            add(new DefaultKeyValue<>("从API获取", "api"));
        }};
    }

}
