package com.fit2cloud.common.provider.impl.tencent;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.TencentCredential;
import com.fit2cloud.common.provider.AbstractBaseCloudProvider;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.tencent.api.TencentBaseCloudApi;
import com.fit2cloud.common.provider.impl.tencent.entity.credential.TencentBaseMethodApi;
import com.fit2cloud.common.provider.impl.tencent.entity.request.GetAccountBalanceRequest;
import com.fit2cloud.common.provider.impl.tencent.entity.request.GetBucketsRequest;
import com.fit2cloud.common.provider.impl.tencent.entity.request.GetRegionsRequest;
import com.fit2cloud.common.utils.JsonUtil;
import com.qcloud.cos.model.Bucket;

import java.util.List;


public class TencentBaseCloudProvider extends AbstractBaseCloudProvider<TencentCredential> {

    public F2CBalance getAccountBalance(String req) {
        return TencentBaseCloudApi.getAccountBalance(JsonUtil.parseObject(req, GetAccountBalanceRequest.class));
    }

    public List<Credential.Region> getRegions(String req) {
        return TencentBaseMethodApi.getRegions(JsonUtil.parseObject(req, GetRegionsRequest.class));
    }

    public List<Bucket> getBuckets(String req) {
        return TencentBaseMethodApi.getBuckets(JsonUtil.parseObject(req, GetBucketsRequest.class));
    }
}
