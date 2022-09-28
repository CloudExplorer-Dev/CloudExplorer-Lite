package com.fit2cloud.common.provider.impl.tencent.entity.request;

import com.tencentcloudapi.billing.v20180709.models.DescribeAccountBalanceRequest;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/9/27 6:31 PM
 */
@Data
public class GetAccountBalanceRequest extends DescribeAccountBalanceRequest {
    /**
     * 认证信息
     */
    private String credential;

    private String regionId;
}
