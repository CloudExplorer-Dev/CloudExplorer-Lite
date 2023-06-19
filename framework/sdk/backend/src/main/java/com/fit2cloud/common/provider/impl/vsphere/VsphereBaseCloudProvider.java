package com.fit2cloud.common.provider.impl.vsphere;

import com.fit2cloud.common.constants.ChargeTypeConstants;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.VsphereCredential;
import com.fit2cloud.common.provider.AbstractBaseCloudProvider;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.vsphere.api.VsphereBaseMethodApi;
import com.fit2cloud.common.provider.impl.vsphere.entity.request.GetRegionsRequest;
import com.fit2cloud.common.utils.JsonUtil;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VsphereBaseCloudProvider extends AbstractBaseCloudProvider<VsphereCredential> {


    public List<Credential.Region> getRegions(String req) {
        return VsphereBaseMethodApi.getRegions(JsonUtil.parseObject(req, GetRegionsRequest.class));
    }

    @Override
    public F2CBalance getAccountBalance(String getAccountBalanceRequest) {
        return null;
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

    public List<DefaultKeyValue<String, String>> getChargeType(String req) {
        return Arrays.stream(ChargeTypeConstants.values())
                .map(model -> new DefaultKeyValue<>(model.getMessage(), model.getCode())).toList();
    }
}
