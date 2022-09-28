package com.fit2cloud.common.provider.impl.aliyun.entity.request;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/9/27 6:31 PM
 */
@Data
public class GetAccountBalanceRequest{
    /**
     * 认证信息
     */
    private String credential;

    private String regionId;
}
