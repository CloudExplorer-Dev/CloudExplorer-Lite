package com.fit2cloud.service;


import com.fit2cloud.common.provider.entity.F2CBalance;

/**
 * Author: LiuDi
 * Date: 2022/9/28 4:18 PM
 */
public interface IProviderService {

    /**
     * 获取云账户余额
     * @param accountId
     */
    F2CBalance getAccountBalance(String accountId);

}
