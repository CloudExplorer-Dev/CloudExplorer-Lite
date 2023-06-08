package com.fit2cloud.base.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/31  15:40}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class BillPolicyDetailsAccount extends BillPolicyAccount {

    /**
     * 策略详情
     */
    private List<BillPolicyDetails> billPolicyDetailsList;
}
