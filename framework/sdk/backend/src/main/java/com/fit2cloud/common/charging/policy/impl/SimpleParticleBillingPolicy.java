package com.fit2cloud.common.charging.policy.impl;

import com.fit2cloud.common.charging.constants.UnitPriceConstants;
import com.fit2cloud.common.charging.entity.InstanceState;
import com.fit2cloud.common.charging.entity.UnitPrice;
import com.fit2cloud.common.charging.policy.BillingPolicy;
import com.fit2cloud.common.provider.util.CommonUtil;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/25  17:17}
 * {@code @Version 1.0}
 * {@code @注释:  根据当前粒度, 只要有一个点状态 则计费当前粒度的帐 }
 */
public class SimpleParticleBillingPolicy implements BillingPolicy {


    /**
     * 获取总价
     *
     * @param state     实例状态
     * @param unitPrice 单价
     * @return 总价
     */
    @Override
    public BigDecimal getTotalPrice(List<InstanceState.State> monthState,
                                    DefaultKeyValue<String, List<InstanceState.State>> state,
                                    UnitPrice unitPrice,
                                    Map<String, Object> globalConfigMata,
                                    BiPredicate<Map<String, Object>, InstanceState.State> predicate) {
        BigDecimal totalPrice = getTotalPrice(state.getKey(), unitPrice);
        return monthState
                .stream()
                .filter(s -> predicate.test(globalConfigMata, s))
                .findFirst()
                .map(s -> totalPrice)
                .orElse(new BigDecimal(0));
    }

    /**
     * 获取这一条账单的价格
     *
     * @param key       计费时间 yyyy-MM yyyy-MM-dd yyyy-MM-dd HH
     * @param unitPrice 单价
     * @return 价格
     */
    private BigDecimal getTotalPrice(String key, UnitPrice unitPrice) {
        long utcTime = CommonUtil.getUTCTime(key, "yyyy-MM");
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(utcTime);
        // 当月最大天数
        int maxDay = instance.getMaximum(Calendar.DAY_OF_MONTH);
        if (key.length() == 7)
        // yyyy-MM
        {
            if (unitPrice.unit().equals(UnitPriceConstants.MONTH)) {
                return unitPrice.price();
            } else if (unitPrice.unit().equals(UnitPriceConstants.DAY)) {
                return unitPrice.price().multiply(new BigDecimal(maxDay));
            } else if (unitPrice.unit().equals(UnitPriceConstants.HOUR)) {
                return unitPrice.price().multiply(new BigDecimal(maxDay * 60));
            }

        } else if (key.length() == 10)
        // yyyy-MM-dd
        {
            if (unitPrice.unit().equals(UnitPriceConstants.MONTH)) {
                return unitPrice.price().divide(new BigDecimal(maxDay), MathContext.DECIMAL32);
            } else if (unitPrice.unit().equals(UnitPriceConstants.DAY)) {
                return unitPrice.price();
            } else if (unitPrice.unit().equals(UnitPriceConstants.HOUR)) {
                return unitPrice.price().multiply(new BigDecimal(60));
            }

        } else
        //  yyyy-MM-dd HH
        {
            if (unitPrice.unit().equals(UnitPriceConstants.MONTH)) {
                return unitPrice.price().divide(new BigDecimal(maxDay * 60), MathContext.DECIMAL32);
            } else if (unitPrice.unit().equals(UnitPriceConstants.DAY)) {
                return unitPrice.price().divide(new BigDecimal(maxDay), MathContext.DECIMAL32);
            } else if (unitPrice.unit().equals(UnitPriceConstants.HOUR)) {
                return unitPrice.price();
            }
        }
        return new BigDecimal(0);
    }
}
