package com.fit2cloud.provider.impl.tencent.constants;

import org.apache.commons.collections4.KeyValue;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/8/14  14:16}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum RenewFlagConstants implements KeyValue<String, String> {
    /**
     * 自动续费标识。取值范围：
     * <p>
     * NOTIFY_AND_AUTO_RENEW：通知过期且自动续费
     * NOTIFY_AND_MANUAL_RENEW：通知过期不自动续费
     * DISABLE_NOTIFY_AND_MANUAL_RENEW：不通知过期不自动续费
     * <p>
     * 默认取值：NOTIFY_AND_MANUAL_RENEW。若该参数指定为NOTIFY_AND_AUTO_RENEW，在账户余额充足的情况下，实例到期后将按月自动续费。
     * 示例值
     */

    NOTIFY_AND_AUTO_RENEW("通知过期且自动续费"),

    NOTIFY_AND_MANUAL_RENEW("通知过期不自动续费"),

    DISABLE_NOTIFY_AND_MANUAL_RENEW("不通知过期不自动续费");
    private final String message;

    RenewFlagConstants(String message) {
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
