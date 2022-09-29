package com.fit2cloud.common.provider.impl.tencent;

import com.fit2cloud.common.platform.credential.impl.TencentCredential;
import com.fit2cloud.common.provider.AbstractBaseCloudProvider;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.tencent.api.TencentBaseCloudApi;
import com.fit2cloud.common.provider.impl.tencent.entity.request.GetAccountBalanceRequest;
import com.fit2cloud.common.utils.JsonUtil;


public class TencentBaseCloudProvider extends AbstractBaseCloudProvider<TencentCredential> {

    public F2CBalance getAccountBalance(String req) {
        return TencentBaseCloudApi.getAccountBalance(JsonUtil.parseObject(req, GetAccountBalanceRequest.class));
    }
}
