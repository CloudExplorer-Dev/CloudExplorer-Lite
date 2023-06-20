package com.fit2cloud.common.charging.setting.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fit2cloud.common.charging.entity.BillingFieldMeta;
import com.fit2cloud.common.charging.entity.InstanceState;
import com.fit2cloud.common.charging.instance.InstanceRecordMappingHandler;
import com.fit2cloud.common.charging.policy.BillingPolicy;
import com.fit2cloud.common.charging.policy.impl.SimpleAccurateBillingPolicy;
import com.fit2cloud.common.charging.setting.BillSetting;
import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.Form;
import com.fit2cloud.common.utils.JsonUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/30  11:35}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class SimpleBillingSetting implements BillSetting {
    /**
     * 按量计费策略
     */
    private final BillingPolicy onDemandPolicy;
    /**
     * 包年包月计费策略
     */
    private final BillingPolicy monthlyPolicy;
    /**
     * 实例资源类型
     */
    private final String resourceInstanceType;

    /**
     * 实例资源名称
     */
    private final String resourceInstanceName;
    /**
     * 实例记录映射处理器
     */
    private final InstanceRecordMappingHandler instanceRecordMappingHandler;

    /**
     * 判断当前状态是否计费
     */
    private final BiPredicate<Map<String, Object>, InstanceState.State> statePredicate;

    private final Map<String, BillingFieldMeta> billingFieldUIMeta;

    /**
     * 全局设置
     */
    private final Map<String, Object> globalConfigMeta;

    private final List<? extends Form> globalConfigMetaForms;

    private SimpleBillingSetting(BillingPolicy onDemandPolicy,
                                 BillingPolicy monthlyPolicy,
                                 InstanceRecordMappingHandler instanceRecordMappingHandler,
                                 String resourceInstanceType,
                                 String resourceInstanceName,
                                 Map<String, BillingFieldMeta> billingFieldUIMeta,
                                 BiPredicate<Map<String, Object>, InstanceState.State> statePredicate,
                                 Object globalConfigMeta
    ) {
        this.onDemandPolicy = onDemandPolicy;
        this.monthlyPolicy = monthlyPolicy;
        this.instanceRecordMappingHandler = instanceRecordMappingHandler;
        this.resourceInstanceType = resourceInstanceType;
        this.statePredicate = statePredicate;
        this.billingFieldUIMeta = billingFieldUIMeta;
        this.resourceInstanceName = resourceInstanceName;
        this.globalConfigMeta = Objects.isNull(globalConfigMeta) ? Map.of() : JsonUtil.parseObject(JsonUtil.toJSONString(globalConfigMeta), new TypeReference<Map<String, Object>>() {
        });
        this.globalConfigMetaForms = Objects.isNull(globalConfigMeta) ? null : FormUtil.toForm(globalConfigMeta.getClass()).getForms();
    }

    public static SimpleBillingSetting of(String resourceInstanceType,
                                          String resourceInstanceName,
                                          InstanceRecordMappingHandler instanceRecordMappingHandler,
                                          Map<String, BillingFieldMeta> billingFieldUIMeta,
                                          BiPredicate<Map<String, Object>, InstanceState.State> statePredicate,
                                          Object globalConfigMeta) {
        return new SimpleBillingSetting(
                new SimpleAccurateBillingPolicy(),
                new SimpleAccurateBillingPolicy(),
                instanceRecordMappingHandler,
                resourceInstanceType,
                resourceInstanceName,
                billingFieldUIMeta,
                statePredicate,
                globalConfigMeta
        );
    }

    @Override
    public InstanceRecordMappingHandler getResourceInstanceHandler() {
        return instanceRecordMappingHandler;
    }

    @Override
    public BillingPolicy getOnDemandPolicy() {
        return onDemandPolicy;
    }

    @Override
    public BillingPolicy getMonthlyPolicy() {
        return monthlyPolicy;
    }

    @Override
    public String getResourceInstanceType() {
        return resourceInstanceType;
    }

    @Override
    public String getResourceInstanceName() {
        return resourceInstanceName;
    }

    @Override
    public Map<String, BillingFieldMeta> getBillingFieldMeta() {
        return billingFieldUIMeta;
    }


    @Override
    public BiPredicate<Map<String, Object>, InstanceState.State> stateBilling() {
        return statePredicate;
    }

    @Override
    public Map<String, Object> getGlobalConfigMeta() {
        return globalConfigMeta;
    }

    @Override
    public List<? extends Form> getGlobalConfigMetaForms() {
        return globalConfigMetaForms;
    }
}
