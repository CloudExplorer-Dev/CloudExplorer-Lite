package com.fit2cloud.provider.constants;

import java.math.BigDecimal;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/8/7  11:24}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum CurrencyConstants implements CurrencyParent {
    CNY("人民币", "¥", "元", BigDecimal.valueOf(1)),

    USD("美元", "$", "美元", BigDecimal.valueOf(0.1391)),

    JPY("日元", "Ұ", "日元", BigDecimal.valueOf(19.7976)),

    EUR("欧元", "€", "欧元", BigDecimal.valueOf(0.1261)),

    GBP("英镑", "£", "镑", BigDecimal.valueOf(0.1086)),

    KRW("韩元", "₩", "韩元", BigDecimal.valueOf(0.1086)),

    HKD("港元", "HK$", "港元", BigDecimal.valueOf(1.0825)),

    AUD("澳元", "A$", "澳元", BigDecimal.valueOf(0.2125)),

    CAD("加元", "CA$", "加元", BigDecimal.valueOf(0.1861)),

    RUB("卢布", "₽", "卢布", BigDecimal.valueOf(13.3241));

    /**
     * 描述
     */
    private final String message;
    /**
     * 符号
     */
    private final String symbol;
    /**
     * 单位
     */
    private final String unit;
    /**
     * 默认汇率
     */
    private final BigDecimal exchangeRate;

    CurrencyConstants(String message, String symbol, String unit, BigDecimal exchangeRate) {
        this.message = message;
        this.symbol = symbol;
        this.unit = unit;
        this.exchangeRate = exchangeRate;
    }

    @Override
    public String symbol() {
        return symbol;
    }

    @Override
    public String unit() {
        return unit;
    }

    @Override
    public String code() {
        return name();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
}
