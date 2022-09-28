package com.fit2cloud.common.provider;


import com.fit2cloud.common.provider.entity.F2CBalance;


public interface IBaseCloudProvider {

    F2CBalance getAccountBalance(String getAccountBalanceRequest);

}
