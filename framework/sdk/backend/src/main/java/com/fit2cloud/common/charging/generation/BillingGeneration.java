package com.fit2cloud.common.charging.generation;

import com.fit2cloud.common.charging.constants.BillingGranularityConstants;
import com.fit2cloud.common.charging.entity.InstanceBill;
import com.fit2cloud.common.charging.entity.InstanceState;

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
     * @param cloudAccountId 云账号id
     * @param month          月份
     * @param start          开始时间
     * @param end            结束时间
     * @param granularity    生成粒度
     * @param isNowBilling   是否用最新策略生成
     * @return 账单数据
     */
    List<InstanceBill> generation(String cloudAccountId,
                                  String month,
                                  InstanceState.Time start,
                                  InstanceState.Time end,
                                  BillingGranularityConstants granularity,
                                  boolean isNowBilling);


    /**
     * 账单生成
     *
     * @param cloudAccountId 云账号id
     * @param generationDate 账单生成时间 支持时间格式分别为 yyyy-MM yyyy-MM-dd yyyy-MM-dd HH
     * @param granularity    生成粒度
     * @param isNowBilling   是否用最新策略生成
     * @return 生成的账单数据
     */
    List<InstanceBill> generation(String cloudAccountId,
                                  String generationDate,
                                  BillingGranularityConstants granularity,
                                  boolean isNowBilling);
}
