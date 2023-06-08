package com.fit2cloud.common.charging.generation.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fit2cloud.base.entity.BillPolicyCloudAccountMapping;
import com.fit2cloud.base.entity.BillPolicyDetails;
import com.fit2cloud.base.entity.ResourceInstance;
import com.fit2cloud.base.entity.ResourceInstanceState;
import com.fit2cloud.base.entity.json_entity.BillingField;
import com.fit2cloud.base.entity.json_entity.PackagePriceBillingPolicy;
import com.fit2cloud.base.mapper.BaseResourceInstanceMapper;
import com.fit2cloud.base.mapper.BaseResourceInstanceStateMapper;
import com.fit2cloud.base.service.IBaseBillPolicyCloudAccountMappingService;
import com.fit2cloud.base.service.IBaseBillPolicyDetailsService;
import com.fit2cloud.common.charging.constants.BillModeConstants;
import com.fit2cloud.common.charging.constants.BillingGranularityConstants;
import com.fit2cloud.common.charging.constants.UnitPriceConstants;
import com.fit2cloud.common.charging.entity.InstanceBill;
import com.fit2cloud.common.charging.entity.InstanceState;
import com.fit2cloud.common.charging.entity.UnitPrice;
import com.fit2cloud.common.charging.generation.BillingGeneration;
import com.fit2cloud.common.charging.policy.BillingPolicy;
import com.fit2cloud.common.charging.setting.BillSetting;
import com.fit2cloud.common.utils.SpringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiPredicate;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/26  14:46}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class SimpleBillingGeneration implements BillingGeneration {
    private final BillSetting setting;


    private SimpleBillingGeneration(BillSetting billSetting) {
        this.setting = billSetting;

    }

    public static SimpleBillingGeneration of(BillSetting setting) {
        return new SimpleBillingGeneration(setting);
    }

    @Override
    public List<InstanceBill> generation(String cloudAccountId, String month, BillingGranularityConstants granularity) {
        BaseResourceInstanceMapper resourceInstanceMapper = SpringUtil.getBean(BaseResourceInstanceMapper.class);
        BaseResourceInstanceStateMapper resourceInstanceStateMapper = SpringUtil.getBean(BaseResourceInstanceStateMapper.class);
        IBaseBillPolicyDetailsService baseBillPolicyDetailsService = SpringUtil.getBean(IBaseBillPolicyDetailsService.class);
        IBaseBillPolicyCloudAccountMappingService baseBillPolicyCloudAccountMappingService = SpringUtil.getBean(IBaseBillPolicyCloudAccountMappingService.class);
        // 资源实例状态
        List<ResourceInstanceState> resourceInstanceStates = resourceInstanceStateMapper
                .selectList(new LambdaQueryWrapper<ResourceInstanceState>()
                        .eq(ResourceInstanceState::getResourceType, setting.getResourceInstanceType())
                        .eq(ResourceInstanceState::getMonth, month)
                        .eq(ResourceInstanceState::getCloudAccountId, cloudAccountId)
                );
        List<BillPolicyCloudAccountMapping> billPolicyCloudAccountMappings = baseBillPolicyCloudAccountMappingService.list(new LambdaQueryWrapper<BillPolicyCloudAccountMapping>()
                .eq(BillPolicyCloudAccountMapping::getCloudAccountId, cloudAccountId));
        if (CollectionUtils.isEmpty(billPolicyCloudAccountMappings)) {
            return List.of();
        }
        List<BillPolicyDetails> billPolicyDetailsList = baseBillPolicyDetailsService.list(new LambdaQueryWrapper<BillPolicyDetails>()
                .in(BillPolicyDetails::getBillPolicyId, billPolicyCloudAccountMappings.stream()
                        .map(BillPolicyCloudAccountMapping::getBillPolicyId).toList())
                .eq(BillPolicyDetails::getResourceType, setting.getResourceInstanceType()));
        if (CollectionUtils.isEmpty(billPolicyDetailsList)) {
            return List.of();
        }
        // 实例信息
        List<ResourceInstance> resourceInstances = resourceInstanceMapper.selectList(new LambdaQueryWrapper<ResourceInstance>()
                .eq(ResourceInstance::getResourceType, setting.getResourceInstanceType())
                .eq(ResourceInstance::getCloudAccountId, cloudAccountId));
        return generation(month,
                resourceInstanceStates,
                resourceInstances,
                billPolicyDetailsList,
                granularity);
    }


    /**
     * 生成费用账单
     *
     * @param month                  生成的月份
     * @param resourceInstanceStates 资源实例状态
     * @param resourceInstances      资源实例详情
     * @param billPolicyDetails      计费策略
     * @param granularity            生成粒度
     * @return 账单数据
     */
    private List<InstanceBill> generation(String month,
                                          List<ResourceInstanceState> resourceInstanceStates,
                                          List<ResourceInstance> resourceInstances,
                                          List<BillPolicyDetails> billPolicyDetails,
                                          BillingGranularityConstants granularity) {
        return resourceInstanceStates.parallelStream().flatMap(resourceInstanceState -> {
            String resourceId = resourceInstanceState.getResourceId();
            // 获取当前状态所有的实例信息
            List<ResourceInstance> resourceInstanceList =
                    resourceInstances
                            .stream()
                            .filter(resourceInstance -> StringUtils.equals(resourceInstance.getResourceId(), resourceId))
                            .toList();
            // 实例变化
            List<Region> region = getRegion(resourceInstanceList, billPolicyDetails);
            // 实例状态
            InstanceState instanceState = new InstanceState(resourceInstanceState.getInstanceState());
            return region.stream()
                    .flatMap(rInstance -> toInstanceBill(rInstance, instanceState, month, granularity).stream());
        }).toList();

    }

    /**
     * 转换为实例费用数据
     *
     * @param regionInstance 区域实例
     * @param instanceState  资源实例状态
     * @param month          月份
     * @param granularity    计费粒度
     * @return 实例费用
     */
    private List<InstanceBill> toInstanceBill(Region regionInstance,
                                              InstanceState instanceState,
                                              String month,
                                              BillingGranularityConstants granularity) {
        BillPolicyDetails policy = regionInstance.billPolicy;
        // 套餐策略
        Optional<PackagePriceBillingPolicy> firstPackagePolicy = getFirstPackagePolicy(regionInstance.resourceInstance, policy.getPackagePriceBillingPolicy());
        List<BillingField> billingFields = new ArrayList<>();
        if (firstPackagePolicy.isPresent()) {
            PackagePriceBillingPolicy usePackagePolicy = firstPackagePolicy.get();
            billingFields = packagePolicyToBillingField(usePackagePolicy, regionInstance.resourceInstance.getBillMode());
        } else {
            billingFields = regionInstance.resourceInstance.getBillMode().equals(BillModeConstants.MONTHLY) ? policy.getUnitPriceMonthlyBillingPolicy() : policy.getUnitPriceOnDemandBillingPolicy();
        }
        // 获取粒度时间账单
        List<DefaultKeyValue<String, BigDecimal>> billing = billing(instanceState,
                month,
                regionInstance.start,
                regionInstance.end,
                regionInstance.resourceInstance.getBillMode().equals(BillModeConstants.MONTHLY) ? setting.getMonthlyPolicy() : setting.getOnDemandPolicy(),
                regionInstance.billPolicy.getGlobalConfigMeta(),
                setting.stateBilling(),
                regionInstance.resourceInstance.getMeta(),
                billingFields,
                granularity);
        // 生成账单数据
        return billing
                .stream()
                .map(b -> toInstanceBill(b, regionInstance.resourceInstance))
                .toList();
    }


    /**
     * 转换为账单实例对象
     *
     * @param defaultKeyValue  key:时间粒度, v: 费用
     * @param resourceInstance 资源实例对象
     * @return 账单数据
     */
    private InstanceBill toInstanceBill(DefaultKeyValue<String, BigDecimal> defaultKeyValue, ResourceInstance resourceInstance) {
        InstanceBill instanceBill = new InstanceBill();
        BeanUtils.copyProperties(resourceInstance, instanceBill);
        instanceBill.setBillingCycle(defaultKeyValue.getKey().substring(0, 7));
        instanceBill.setTotalPrice(defaultKeyValue.getValue());
        instanceBill.setDeductionTime(defaultKeyValue.getKey());
        return instanceBill;
    }

    /***
     * 将套餐计费策略转换为billing Field类型
     * @param policy  套餐计费策略
     * @param billMode 计费模式
     * @return 转换后
     */
    private List<BillingField> packagePolicyToBillingField(PackagePriceBillingPolicy policy, BillModeConstants billMode) {
        return policy.getBillPolicyFields()
                .stream()
                .map(field -> BillingField.of(field.getField(),
                        billMode.equals(BillModeConstants.MONTHLY) ? policy.getMonthly() : policy.getOnDemand(), billMode.equals(BillModeConstants.MONTHLY) ? UnitPriceConstants.MONTH : UnitPriceConstants.HOUR)
                ).toList();

    }

    /**
     * 获取套餐计费车牌
     *
     * @param instance                      实例数据
     * @param packagePriceBillingPolicyList 套餐计费策略
     * @return 匹配的套餐计费策略
     */
    private Optional<PackagePriceBillingPolicy> getFirstPackagePolicy(ResourceInstance instance, List<PackagePriceBillingPolicy> packagePriceBillingPolicyList) {
        return packagePriceBillingPolicyList.stream().filter(packagePriceBillingPolicy -> packagePriceBillingPolicy
                .getBillPolicyFields()
                .stream()
                .anyMatch(packageField -> instance.getMeta().get(packageField.getField()).equals(packageField.getNumber())
                )).findFirst();
    }

    private static List<Region> getRegion(
            List<ResourceInstance> resourceInstances,
            List<BillPolicyDetails> billPolicies) {
        if (CollectionUtils.isEmpty(resourceInstances) || CollectionUtils.isEmpty(billPolicies)) {
            return List.of();
        }
        // 排序
        resourceInstances = resourceInstances
                .stream()
                .sorted(Comparator.comparing(ResourceInstance::getCreateTime))
                .toList();
        // 排序
        billPolicies = billPolicies
                .stream()
                .sorted(Comparator.comparing(BillPolicyDetails::getCreateTime))
                .toList();
        // 加载到时间线中
        List<TimeLine<Object>> timeLines = new ArrayList<>();
        timeLines.addAll(resourceInstances.stream().map(r -> new TimeLine<Object>(r, r.getCreateTime())).toList());
        timeLines.addAll(billPolicies.stream().map(policy -> new TimeLine<Object>(policy, policy.getCreateTime())).toList());
        // 排序
        timeLines = timeLines.stream().sorted(Comparator.comparing(TimeLine::getCreateTime)).toList();


        List<Region> result = new ArrayList<>();
        ResourceInstance currentResourceInstance = resourceInstances.get(0);
        BillPolicyDetails currentPolicy = billPolicies.get(0);
        for (int i = 0; i < timeLines.size(); i++) {
            TimeLine<Object> pre = timeLines.get(i);
            if (pre.instance instanceof ResourceInstance resourceInstance) {
                currentResourceInstance = resourceInstance;
            }
            if (pre.instance instanceof BillPolicyDetails billPolicyDetails) {
                currentPolicy = billPolicyDetails;
            }
            if (i + 1 < timeLines.size()) {
                TimeLine<Object> next = timeLines.get(i + 1);
                InstanceState.Time start = i == 0 ? InstanceState.Time.of(1, 0, 0) : InstanceState.Time.of(pre.getCreateTime().getDayOfMonth(), pre.getCreateTime().getHour(), pre.getCreateTime().getMinute());
                InstanceState.Time end = InstanceState.Time.of(next.getCreateTime().getDayOfMonth(), next.getCreateTime().getHour(), next.getCreateTime().getMinute());
                result.add(new Region(start, end, currentResourceInstance, currentPolicy));
            } else {
                InstanceState.Time start = InstanceState.Time.of(pre.getCreateTime().getDayOfMonth(), pre.getCreateTime().getHour(), pre.getCreateTime().getMinute());
                result.add(new Region(start, null, currentResourceInstance, currentPolicy));
            }
        }
        return result;
    }


    /**
     * 计费
     *
     * @param instanceState 实例状态
     * @param month         月份
     * @param billingPolicy 计费策略
     * @param predicate     判断状态是否计费
     * @param billingMate   账单meta元数据
     * @param fields        计费字段
     * @param granularity   账单生成粒度
     * @return 账单计费数据
     */
    private List<DefaultKeyValue<String, BigDecimal>> billing(InstanceState instanceState,
                                                              String month,
                                                              InstanceState.Time start,
                                                              InstanceState.Time end,
                                                              BillingPolicy billingPolicy,
                                                              Map<String, Object> globalConfigMeta,
                                                              BiPredicate<Map<String, Object>, InstanceState.State> predicate,
                                                              Map<String, Integer> billingMate,
                                                              List<BillingField> fields,
                                                              BillingGranularityConstants granularity) {
        List<DefaultKeyValue<String, List<InstanceState.State>>> stateListGroup = instanceState.getStateListGroup(start, end, month, granularity);
        return stateListGroup
                .stream()
                .map(stateItem -> new DefaultKeyValue<>(stateItem.getKey(), getTotal(instanceState.getStateList(), stateItem, billingPolicy, globalConfigMeta, predicate, fields, billingMate)))
                .filter(s -> s.getValue().compareTo(new BigDecimal(0)) > 0)
                .toList();
    }


    /**
     * 获取账单
     *
     * @param defaultKeyValue 实例状态
     * @param billingPolicy   计费策略
     * @param predicate       是否计费
     * @param fields          计费字段
     * @param billingMate     计费Meta数据
     * @return 费用
     */
    private BigDecimal getTotal(
            List<InstanceState.State> monthStat,
            DefaultKeyValue<String, List<InstanceState.State>> defaultKeyValue,
            BillingPolicy billingPolicy,
            Map<String, Object> globalConfigMata,
            BiPredicate<Map<String, Object>, InstanceState.State> predicate,
            List<BillingField> fields,
            Map<String, Integer> billingMate) {
        return fields
                .stream()
                .map(field -> {
                    UnitPrice unitPrice = new UnitPrice(field.getUnit(), field.getPrice());
                    BigDecimal totalPrice = billingPolicy.getTotalPrice(monthStat, defaultKeyValue, unitPrice, globalConfigMata, predicate);
                    return totalPrice.multiply(new BigDecimal(billingMate.get(field.getField())));
                })
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));

    }

    @AllArgsConstructor
    private static class Region {
        /**
         * 开始时间
         */
        InstanceState.Time start;
        /**
         * 结束时间
         */
        InstanceState.Time end;

        /**
         * 资源实例
         */
        ResourceInstance resourceInstance;

        /**
         * 计费策略
         */
        BillPolicyDetails billPolicy;
    }

    @Data
    private static class TimeLine<T> {
        /**
         * 创建时间
         */
        private LocalDateTime createTime;

        private T instance;

        public TimeLine(T instance, LocalDateTime createTime) {
            this.createTime = createTime;
            this.instance = instance;
        }
    }
}
