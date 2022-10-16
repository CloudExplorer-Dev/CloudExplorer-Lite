package com.fit2cloud.common.provider.impl.huawei.api;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.huawei.entity.credential.HuaweiBaseCredential;
import com.fit2cloud.common.provider.impl.huawei.entity.request.GetAccountBalanceRequest;
import com.fit2cloud.common.provider.impl.huawei.entity.request.GetRegionsRequest;
import com.fit2cloud.common.utils.JsonUtil;
import com.huaweicloud.sdk.bss.v2.BssClient;
import com.huaweicloud.sdk.bss.v2.model.AccountBalanceV3;
import com.huaweicloud.sdk.bss.v2.model.ShowCustomerAccountBalancesResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class HuaweiBaseCloudApi {

    /**
     * 获取云账户余额
     *
     * @param request
     * @return
     */
    public static F2CBalance getAccountBalance(GetAccountBalanceRequest request) {
        if (StringUtils.isNotEmpty(request.getCredential())) {
            HuaweiBaseCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiBaseCredential.class);
            BssClient bssClient = credential.getBssClient(request.getRegionId() != null ? request.getRegionId() : "cn-north-1");
            ShowCustomerAccountBalancesResponse response = bssClient.showCustomerAccountBalances(request);
            AccountBalanceV3 customerAccount = response.getAccountBalances().stream().filter(accountBalance -> 1 == accountBalance.getAccountType()).findFirst().get();
            return new F2CBalance(customerAccount.getAmount(), customerAccount.getCurrency(), response.getDebtAmount());
        }
        return new F2CBalance();
    }
}
