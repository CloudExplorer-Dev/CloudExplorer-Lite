package com.fit2cloud.common.provider.impl.huawei;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.TencentCredential;
import com.fit2cloud.common.provider.AbstractBaseCloudProvider;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.huawei.api.HuaweiBaseCloudApi;
import com.fit2cloud.common.provider.impl.huawei.api.HuaweiBaseMethodApi;
import com.fit2cloud.common.provider.impl.huawei.entity.request.GetAccountBalanceRequest;
import com.fit2cloud.common.provider.impl.huawei.entity.request.GetBucketsRequest;
import com.fit2cloud.common.provider.impl.huawei.entity.request.GetRegionsRequest;
import com.fit2cloud.common.utils.JsonUtil;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.ArrayList;
import java.util.List;


public class HuaweiBaseCloudProvider extends AbstractBaseCloudProvider<TencentCredential> {
    @Override
    public F2CBalance getAccountBalance(String req) {
        return HuaweiBaseCloudApi.getAccountBalance(JsonUtil.parseObject(req, GetAccountBalanceRequest.class));
    }

    public List<Credential.Region> getRegions(String req) {
        return HuaweiBaseMethodApi.getRegions(JsonUtil.parseObject(req, GetRegionsRequest.class));
    }

    public Object getBuckets(String req) {
        return HuaweiBaseMethodApi.getBuckets(JsonUtil.parseObject(req, GetBucketsRequest.class));
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
            add(new DefaultKeyValue<>("从存储桶获取", "bucket"));
        }};
    }
}
