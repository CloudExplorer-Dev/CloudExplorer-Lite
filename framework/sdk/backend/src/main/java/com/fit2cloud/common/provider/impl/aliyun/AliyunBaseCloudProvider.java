package com.fit2cloud.common.provider.impl.aliyun;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.AliCredential;
import com.fit2cloud.common.provider.AbstractBaseCloudProvider;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.aliyun.api.AliyunBaseCloudApi;
import com.fit2cloud.common.provider.impl.aliyun.api.AliyunBaseMethodApi;
import com.fit2cloud.common.provider.impl.aliyun.entity.request.GetAccountBalanceRequest;
import com.fit2cloud.common.provider.impl.aliyun.entity.request.GetBucketsRequest;
import com.fit2cloud.common.provider.impl.aliyun.entity.request.GetRegionsRequest;
import com.fit2cloud.common.utils.JsonUtil;

import java.util.List;


public class AliyunBaseCloudProvider extends AbstractBaseCloudProvider<AliCredential> {

    @Override
    public F2CBalance getAccountBalance(String getAccountBalanceRequest) {
        return AliyunBaseCloudApi.getAccountBalance(JsonUtil.parseObject(getAccountBalanceRequest, GetAccountBalanceRequest.class));
    }

    public List<Credential.Region> getRegions(String req) {
        return AliyunBaseMethodApi.getRegions(JsonUtil.parseObject(req, GetRegionsRequest.class));
    }

    public Object getBuckets(String req) {
        return AliyunBaseMethodApi.getBuckets(JsonUtil.parseObject(req, GetBucketsRequest.class));
    }
}
