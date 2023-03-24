package com.fit2cloud.common.provider.impl.tencent;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.TencentCredential;
import com.fit2cloud.common.provider.AbstractBaseCloudProvider;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.tencent.api.TencentBaseCloudApi;
import com.fit2cloud.common.provider.impl.tencent.api.TencentBaseMethodApi;
import com.fit2cloud.common.provider.impl.tencent.entity.request.GetAccountBalanceRequest;
import com.fit2cloud.common.provider.impl.tencent.entity.request.GetBucketsRequest;
import com.fit2cloud.common.provider.impl.tencent.entity.request.GetRegionsRequest;
import com.fit2cloud.common.utils.JsonUtil;
import com.qcloud.cos.model.Bucket;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.ArrayList;
import java.util.List;

public class TencentBaseCloudProvider extends AbstractBaseCloudProvider<TencentCredential> {
    @Override
    public F2CBalance getAccountBalance(String req) {
        return TencentBaseCloudApi.getAccountBalance(JsonUtil.parseObject(req, GetAccountBalanceRequest.class));
    }

    public List<Credential.Region> getRegions(String req) {
        return TencentBaseMethodApi.getRegions(JsonUtil.parseObject(req, GetRegionsRequest.class));
    }

    public List<Bucket> getBuckets(String req) {
        return TencentBaseMethodApi.getBuckets(JsonUtil.parseObject(req, GetBucketsRequest.class));
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
