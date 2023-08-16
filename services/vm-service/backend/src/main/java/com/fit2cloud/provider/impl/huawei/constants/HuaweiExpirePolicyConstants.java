package com.fit2cloud.provider.impl.huawei.constants;

import org.apache.commons.collections4.KeyValue;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/8/15  16:59}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum HuaweiExpirePolicyConstants implements KeyValue<String, Integer> {
    reserve("进入宽限期/保留期", 0),
    to_on_demand("转按需", 1),
    automatic_unsubscription("自动退订", 2),
    auto_renew("自动续订", 3);


    private final String message;
    private final int value;

    HuaweiExpirePolicyConstants(String message, int value) {
        this.message = message;
        this.value = value;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public Integer getValue() {
        return null;
    }
    /**
     * 到期策略: 0:进入宽限期/保留期1:转按需2:自动退订3:自动续订
     */

}
