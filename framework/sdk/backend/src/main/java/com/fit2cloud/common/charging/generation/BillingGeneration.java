package com.fit2cloud.common.charging.generation;

import com.fit2cloud.common.charging.constants.BillingGranularityConstants;
import com.fit2cloud.common.charging.entity.InstanceBill;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/26  14:36}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface BillingGeneration {
    /**
     * 生成账单
     *
     * @return 账单数据
     */
    List<InstanceBill> generation(String cloudAccountId, String month, BillingGranularityConstants granularity);
}
