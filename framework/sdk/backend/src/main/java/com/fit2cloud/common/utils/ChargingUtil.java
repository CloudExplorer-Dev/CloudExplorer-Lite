package com.fit2cloud.common.utils;

import com.fit2cloud.base.entity.BillPolicyDetails;
import com.fit2cloud.base.entity.json_entity.BillingField;
import com.fit2cloud.base.entity.json_entity.PackagePriceBillingPolicy;
import com.fit2cloud.common.constants.ChargeTypeConstants;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/16  20:09}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class ChargingUtil {
    public static BigDecimal getBigDecimal(String instanceChargeType, Map<String, Integer> meta, BillPolicyDetails billPolicyDetails) {
        Optional<PackagePriceBillingPolicy> first = billPolicyDetails
                .getPackagePriceBillingPolicy()
                .stream()
                .filter(packagePolicy -> packagePolicy.getBillPolicyFields()
                        .stream()
                        .allMatch(policyField -> meta.getOrDefault(policyField.getField(), -1).equals(policyField.getNumber())))
                .filter(packagePolicy -> ChargeTypeConstants.POSTPAID.getCode().equals(instanceChargeType) ? Objects.nonNull(packagePolicy.getOnDemand()) : Objects.nonNull(packagePolicy.getMonthly()))
                .findFirst();
        // 如果套餐计费存在那么就是用套餐计费
        if (first.isPresent()) {
            PackagePriceBillingPolicy packagePriceBillingPolicy = first.get();
            if (ChargeTypeConstants.POSTPAID.getCode().equals(instanceChargeType)) {
                return packagePriceBillingPolicy.getOnDemand();
            } else {
                return packagePriceBillingPolicy.getMonthly();
            }
        } else {
            List<BillingField> billingFields;
            if (ChargeTypeConstants.POSTPAID.getCode().equals(instanceChargeType)) {
                billingFields = billPolicyDetails.getUnitPriceOnDemandBillingPolicy();
            } else {
                billingFields = billPolicyDetails.getUnitPriceMonthlyBillingPolicy();
            }
            return billingFields
                    .stream()
                    .map(policy -> new BigDecimal(meta.get(policy.getField())).multiply(policy.getPrice()))
                    .reduce(BigDecimal::add)
                    .orElse(new BigDecimal(0));

        }
    }
}
