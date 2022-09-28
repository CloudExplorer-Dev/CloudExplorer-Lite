package com.fit2cloud.common.provider.impl.huawei;

import com.fit2cloud.common.platform.credential.impl.TencentCredential;
import com.fit2cloud.common.provider.AbstractBaseCloudProvider;
import com.fit2cloud.common.provider.IBaseCloudProvider;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.huawei.api.HuaweiBaseCloudApi;
import com.fit2cloud.common.provider.impl.huawei.entity.request.GetAccountBalanceRequest;
import com.fit2cloud.common.utils.JsonUtil;


public class HuaweiBaseCloudProvider extends AbstractBaseCloudProvider<TencentCredential> implements IBaseCloudProvider {

    public F2CBalance getAccountBalance(String req) {
        return HuaweiBaseCloudApi.getAccountBalance(JsonUtil.parseObject(req, GetAccountBalanceRequest.class));
    }
}
