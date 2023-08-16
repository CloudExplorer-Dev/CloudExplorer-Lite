package com.fit2cloud.provider.impl.aliyun.constants;

import org.apache.commons.collections4.KeyValue;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/8/11  15:52}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum AliRenewalStatus implements KeyValue<String, String> {
    AutoRenewal("自动续费"),

    Normal("取消自动续费");

    private final String message;

    AliRenewalStatus(String message) {
        this.message = message;
    }


    @Override
    public String getKey() {
        return message;
    }

    @Override
    public String getValue() {
        return name();
    }
}
