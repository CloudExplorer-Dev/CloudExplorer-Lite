package com.fit2cloud.common.provider.impl.aliyun;

import com.fit2cloud.common.platform.credential.impl.AliCredential;
import com.fit2cloud.common.provider.AbstractBaseCloudProvider;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.aliyun.api.AliyunBaseCloudApi;
import com.fit2cloud.common.provider.impl.aliyun.entity.request.GetAccountBalanceRequest;
import com.fit2cloud.common.utils.JsonUtil;


public class AliyunBaseCloudProvider extends AbstractBaseCloudProvider<AliCredential> {

    @Override
    public F2CBalance getAccountBalance(String getAccountBalanceRequest) {
        return AliyunBaseCloudApi.getAccountBalance(JsonUtil.parseObject(getAccountBalanceRequest, GetAccountBalanceRequest.class));
    }
}
