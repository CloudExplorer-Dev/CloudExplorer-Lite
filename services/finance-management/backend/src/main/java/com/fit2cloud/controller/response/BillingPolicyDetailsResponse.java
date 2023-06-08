package com.fit2cloud.controller.response;

import com.fit2cloud.base.entity.json_entity.BillingField;
import com.fit2cloud.base.entity.json_entity.PackagePriceBillingPolicy;
import com.fit2cloud.common.charging.entity.BillingFieldMeta;
import com.fit2cloud.common.form.vo.Form;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/1  15:52}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class BillingPolicyDetailsResponse {
    private String name;


    private List<BillingPolicyDetails> billingPolicyDetailsList;

    @Data
    public static class BillingPolicyDetails {

        /**
         * 资源类型
         */
        private String resourceType;

        /**
         * 资源名称
         */
        private String resourceName;

        /**
         * 单价(按需)计费策略
         */
        private List<BillingField> unitPriceOnDemandBillingPolicy;

        /**
         * 单价(包年包月)计费策略
         */
        private List<BillingField> unitPriceMonthlyBillingPolicy;

        /**
         * 套餐计费策略
         */
        private List<PackagePriceBillingPolicy> packagePriceBillingPolicy;

        /**
         * 账单计费策略UI元数据
         */
        private Map<String, BillingFieldMeta> billingFieldMeta;

        private List<? extends Form> globalConfigMetaForms;

        private Object globalConfigMeta;
    }

}
