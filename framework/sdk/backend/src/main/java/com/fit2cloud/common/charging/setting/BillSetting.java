package com.fit2cloud.common.charging.setting;

import com.fit2cloud.common.charging.entity.BillingFieldMeta;
import com.fit2cloud.common.charging.entity.InstanceState;
import com.fit2cloud.common.charging.instance.InstanceRecordMappingHandler;
import com.fit2cloud.common.charging.policy.BillingPolicy;
import com.fit2cloud.common.form.vo.Form;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/25  15:45}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface BillSetting {
    /**
     * 账单资源
     *
     * @return 资源实例处理器
     */
    InstanceRecordMappingHandler getResourceInstanceHandler();


    /**
     * 按需的计费策略
     *
     * @return 按需的计费策略
     */
    BillingPolicy getOnDemandPolicy();

    /**
     * 包年包月的计费策略
     *
     * @return 包年包月的计费策略
     */
    BillingPolicy getMonthlyPolicy();

    /**
     * 获取资源实例类型
     *
     * @return 资源实例类型
     */
    String getResourceInstanceType();

    /**
     * 资源实例名称
     *
     * @return 资源实例名称
     */
    String getResourceInstanceName();

    /**
     * 页面绘制所需要的属性
     *
     * @return key: field value: 页面绘制所需要的属性
     */
    Map<String, BillingFieldMeta> getBillingFieldMeta();


    /**
     * 断言当前实例状态是否计费
     *
     * @return true 计费,false不计费
     */
    BiPredicate<Map<String, Object>, InstanceState.State> stateBilling();

    /**
     * 全局配置元数据
     *
     * @return 全局配置
     */
    Map<String, Object> getGlobalConfigMeta();

    List<? extends Form> getGlobalConfigMetaForms();

}
