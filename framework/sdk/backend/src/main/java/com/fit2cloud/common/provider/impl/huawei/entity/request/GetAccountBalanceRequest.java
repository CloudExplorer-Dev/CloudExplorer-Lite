package com.fit2cloud.common.provider.impl.huawei.entity.request;

import com.huaweicloud.sdk.bss.v2.model.ShowCustomerAccountBalancesRequest;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/9/27 6:31 PM
 */
@Data
public class GetAccountBalanceRequest extends ShowCustomerAccountBalancesRequest {
    /**
     * 认证信息
     */
    private String credential;

    private String regionId;
}
