package com.fit2cloud.common.charging.policy;

import com.fit2cloud.common.charging.entity.InstanceState;
import com.fit2cloud.common.charging.entity.UnitPrice;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/25  16:34}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface BillingPolicy {
    /**
     * 获取总价
     * state.key 有三个值 yyyy-MM -> 一个月实例的状态 yyyy-MM-dd -> 一天实例状态 yyyy-MM-dd HH -> 一个小时的实例状态
     *
     * @param monthState 当月所有实例状态
     * @param state      实例状态
     * @param unitPrice  单价
     * @param predicate  是否计费
     * @return 总价
     */
    BigDecimal getTotalPrice(
            List<InstanceState.State> monthState,
            DefaultKeyValue<String, List<InstanceState.State>> state,
            UnitPrice unitPrice,
            Map<String, Object> globalConfigMata,
            BiPredicate<Map<String, Object>, InstanceState.State> predicate);

}
