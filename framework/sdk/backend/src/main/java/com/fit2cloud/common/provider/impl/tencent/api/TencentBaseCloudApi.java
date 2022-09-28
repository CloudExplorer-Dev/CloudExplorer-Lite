package com.fit2cloud.common.provider.impl.tencent.api;

import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.tencent.entity.credential.TencentBaseCredential;
import com.fit2cloud.common.provider.impl.tencent.entity.request.GetAccountBalanceRequest;
import com.fit2cloud.common.utils.JsonUtil;

import com.tencentcloudapi.billing.v20180709.BillingClient;
import com.tencentcloudapi.billing.v20180709.models.DescribeAccountBalanceResponse;
import org.apache.commons.lang3.StringUtils;

public class TencentBaseCloudApi {

    /**
     * 获取云账户余额
     *
     * @param request
     * @return
     */
    public static F2CBalance getAccountBalance(GetAccountBalanceRequest request) {
        if (StringUtils.isNotEmpty(request.getCredential())) {
            try {
                TencentBaseCredential credential = JsonUtil.parseObject(request.getCredential(), TencentBaseCredential.class);
                BillingClient billingClient = credential.getBillingClient();
                DescribeAccountBalanceResponse response = billingClient.DescribeAccountBalance(request);
                return new F2CBalance(response.getCashAccountBalance().doubleValue(), "", response.getOweAmount().doubleValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return new F2CBalance();
    }
}
