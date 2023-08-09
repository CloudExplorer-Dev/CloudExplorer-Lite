package com.fit2cloud.provider.constants;

import java.math.BigDecimal;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/8/7  17:57}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface CurrencyParent {
    /**
     * 符号
     *
     * @return 符号
     */
    String symbol();

    /**
     * 单位
     *
     * @return 单位
     */
    String unit();

    /**
     * 币种 code
     *
     * @return 币种标识
     */
    String code();

    /**
     * 币种描述
     *
     * @return 描述
     */
    String getMessage();

    /**
     * 汇率
     *
     * @return 汇率
     */
    BigDecimal getExchangeRate();
}
