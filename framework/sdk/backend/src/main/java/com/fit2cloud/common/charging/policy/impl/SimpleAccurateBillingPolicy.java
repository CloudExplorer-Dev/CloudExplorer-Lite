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
 * {@code @注释:  精准计费 有多少个分钟就根据单位计费计算}
 */

public class SimpleAccurateBillingPolicy implements BillingPolicy {

    /**
     * 获取这一条账单的价格
     *
     * @param state     实例状态
     * @param unitPrice 单价
     * @return 价格
     */
    @Override
    public BigDecimal getTotalPrice(List<InstanceState.State> monthState,
                                    DefaultKeyValue<String, List<InstanceState.State>> state,
                                    UnitPrice unitPrice,
                                    Map<String, Object> globalConfigMata,
                                    BiPredicate<Map<String, Object>, InstanceState.State> predicate) {
        List<InstanceState.State> stateList = state.getValue();
        // 需要收费的数据 size个分钟需要收费
        int size = stateList
                .stream()
                .filter(s -> predicate.test(globalConfigMata, s))
                .toList()
                .size();

        int unitSize = 1;
        if (unitPrice.unit().equals(UnitPriceConstants.HOUR)) {
            unitSize = 60;
        } else if (unitPrice.unit().equals(UnitPriceConstants.DAY)) {
            unitSize = 60 * 24;
        } else if (unitPrice.unit().equals(UnitPriceConstants.MONTH)) {
            long utcTime = CommonUtil.getUTCTime(state.getKey(), "yyyy-MM");
            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(utcTime);
            int maximum = instance.getMaximum(Calendar.DAY_OF_MONTH);
            unitSize = maximum * 60 * 24;
        }
        return getTotalPrice(size, unitPrice.price(), unitSize);
    }


    /**
     * 获取价格
     *
     * @param size     多少个分钟
     * @param price    价格
     * @param unitSize 单位长度
     * @return 价格
     */
    private BigDecimal getTotalPrice(int size, BigDecimal price, int unitSize) {
        return new BigDecimal(size).divide(new BigDecimal(unitSize), MathContext.DECIMAL32)
                .multiply(price, MathContext.DECIMAL32);
    }

}
