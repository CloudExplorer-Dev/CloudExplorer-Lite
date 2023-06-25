package com.fit2cloud.common.charging.generation.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fit2cloud.base.entity.*;
import com.fit2cloud.base.entity.json_entity.BillingField;
import com.fit2cloud.base.entity.json_entity.PackagePriceBillingPolicy;
import com.fit2cloud.base.mapper.BaseBillPolicyDetailsMapper;
import com.fit2cloud.base.mapper.BaseResourceInstanceMapper;
import com.fit2cloud.base.mapper.BaseResourceInstanceStateMapper;
import com.fit2cloud.base.service.IBaseBillPolicyCloudAccountMappingService;
import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.base.service.IBaseWorkspaceService;
import com.fit2cloud.common.charging.constants.BillModeConstants;
import com.fit2cloud.common.charging.constants.BillingGranularityConstants;
import com.fit2cloud.common.charging.constants.UnitPriceConstants;
import com.fit2cloud.common.charging.entity.InstanceBill;
import com.fit2cloud.common.charging.entity.InstanceState;
import com.fit2cloud.common.charging.entity.UnitPrice;
import com.fit2cloud.common.charging.generation.BillingGeneration;
import com.fit2cloud.common.charging.policy.BillingPolicy;
import com.fit2cloud.common.charging.setting.BillSetting;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.SpringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/26  14:46}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class SimpleBillingGeneration implements BillingGeneration {
    private final String packageFieldName = "package";
    private final BillSetting setting;

    private final List<Workspace> workspaces;

    private final List<Organization> organizations;

    private SimpleBillingGeneration(BillSetting billSetting) {
        this.setting = billSetting;
        IBaseOrganizationService organizationService = SpringUtil.getBean(IBaseOrganizationService.class);
        IBaseWorkspaceService workspaceService = SpringUtil.getBean(IBaseWorkspaceService.class);
        organizations = organizationService.list();
        workspaces = workspaceService.list();
    }

    public static SimpleBillingGeneration of(BillSetting setting) {
        return new SimpleBillingGeneration(setting);
    }

    @Override
    public List<InstanceBill> generation(String cloudAccountId,
                                         String month,
                                         InstanceState.Time start,
                                         InstanceState.Time end,
                                         BillingGranularityConstants granularity,
                                         boolean isNowBilling) {
        BaseResourceInstanceMapper resourceInstanceMapper = SpringUtil.getBean(BaseResourceInstanceMapper.class);

        List<ResourceInstanceState> resourceInstanceStates = setting.getInstanceStatePolicy().list(cloudAccountId, setting.getResourceInstanceType(), month);

        // 策略与云账号映射
        List<BillPolicyCloudAccountMapping> billPolicyCloudAccountMappings = listBillPolicyCloudAccountMapping(isNowBilling, cloudAccountId);
        if (CollectionUtils.isEmpty(billPolicyCloudAccountMappings)) {
            return List.of();
        }
        // 策略详情
        List<BillPolicyDetails> billPolicyDetailsList = listBillPolicyDetails(billPolicyCloudAccountMappings
                .stream()
                .map(BillPolicyCloudAccountMapping::getBillPolicyId).distinct().toList(), isNowBilling);

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
                billPolicyCloudAccountMappings,
                start,
                end,
                granularity
        );
    }

    private List<BillPolicyDetails> listBillPolicyDetails(List<String> billPolicyIdList, boolean isNowBilling) {
        BaseBillPolicyDetailsMapper baseBillPolicyDetailsMapper = SpringUtil.getBean(BaseBillPolicyDetailsMapper.class);
        if (isNowBilling) {
            return baseBillPolicyDetailsMapper.listLast(new QueryWrapper<BillPolicyDetails>()
                    .in(ColumnNameUtil.getColumnName(BillPolicyDetails::getBillPolicyId, "bill_policy_details"), billPolicyIdList)
                    .eq(ColumnNameUtil.getColumnName(BillPolicyDetails::getResourceType, "bill_policy_details"), setting.getResourceInstanceType()));
        } else {
            return baseBillPolicyDetailsMapper.selectList(new LambdaQueryWrapper<BillPolicyDetails>()
                    .in(BillPolicyDetails::getBillPolicyId, billPolicyIdList)
                    .eq(BillPolicyDetails::getResourceType, setting.getResourceInstanceType()));

        }
    }

    private List<BillPolicyCloudAccountMapping> listBillPolicyCloudAccountMapping(boolean isNowBilling, String cloudAccountId) {

        IBaseBillPolicyCloudAccountMappingService baseBillPolicyCloudAccountMappingService = SpringUtil.getBean(IBaseBillPolicyCloudAccountMappingService.class);
        if (isNowBilling) {

            return baseBillPolicyCloudAccountMappingService.listLast(new QueryWrapper<BillPolicyCloudAccountMapping>()
                    .eq(ColumnNameUtil.getColumnName(BillPolicyCloudAccountMapping::getCloudAccountId, true), cloudAccountId));
        } else {
            return baseBillPolicyCloudAccountMappingService.list(new LambdaQueryWrapper<BillPolicyCloudAccountMapping>()
                    .eq(BillPolicyCloudAccountMapping::getCloudAccountId, cloudAccountId));
        }

    }

    @Override
    public List<InstanceBill> generation(String cloudAccountId,
                                         String generationDate,
                                         BillingGranularityConstants granularity,
                                         boolean isNowBilling) {
        GenerationDate generationDateObj = new GenerationDate(generationDate);
        Region region = generationDateObj.getRegion();
        return generation(cloudAccountId, generationDateObj.getMonth(), region.start, region.end, granularity, isNowBilling);
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
                                          List<BillPolicyCloudAccountMapping> billPolicyCloudAccountMappings,
                                          InstanceState.Time start,
                                          InstanceState.Time end,
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
            List<Region> region = getRegion(resourceInstanceList, billPolicyDetails, billPolicyCloudAccountMappings, month, start, end);
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
        List<BillingField> billingFields;
        HashMap<String, Integer> billMeta = new HashMap<>();
        if (firstPackagePolicy.isPresent()) {
            PackagePriceBillingPolicy usePackagePolicy = firstPackagePolicy.get();
            billingFields = packagePolicyToBillingField(usePackagePolicy, regionInstance.resourceInstance.getBillMode());
            // 套餐价数量为1
            billMeta.put(packageFieldName, 1);
        } else {
            billingFields = regionInstance.resourceInstance.getBillMode().equals(BillModeConstants.MONTHLY) ?
                    policy.getUnitPriceMonthlyBillingPolicy() :
                    policy.getUnitPriceOnDemandBillingPolicy();
            // 数量为自己的数量
            billMeta.putAll(regionInstance.resourceInstance.getMeta());
        }
        // 获取粒度时间账单
        List<DefaultKeyValue<String, BigDecimal>> billing = billing(instanceState,
                month,
                regionInstance.start,
                regionInstance.end,
                regionInstance.resourceInstance.getBillMode(),
                regionInstance.billPolicy.getGlobalConfigMeta(),
                setting.stateBilling(),
                billMeta,
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
        // 付款账号就是当前云账号
        instanceBill.setPayAccountId(resourceInstance.getCloudAccountId());
        instanceBill.setBillingCycle(defaultKeyValue.getKey().substring(0, 7));
        instanceBill.setTotalPrice(defaultKeyValue.getValue());
        instanceBill.setDeductionTime(defaultKeyValue.getKey());
        updateWorkspaceOrOrganization(instanceBill);
        return instanceBill;
    }

    /**
     * 更新组织或者工作空间
     *
     * @param instanceBill 实例账单
     */
    private void updateWorkspaceOrOrganization(InstanceBill instanceBill) {
        String workspaceId = instanceBill.getWorkspaceId();
        if (StringUtils.isNotEmpty(workspaceId)) {
            workspaces.stream().filter(workspace -> StringUtils.equals(workspace.getId(), workspaceId))
                    .findFirst().ifPresent(workspace -> {
                        instanceBill.setWorkspaceName(workspace.getName());
                    });
        }
        if (StringUtils.isNotEmpty(instanceBill.getOrganizationId())) {
            organizations.stream().filter(organization -> StringUtils.equals(organization.getId(), instanceBill.getOrganizationId()))
                    .findFirst().ifPresent(organization -> instanceBill.setOrganizationName(instanceBill.getOrganizationName()));
        }
    }

    /***
     * 将套餐计费策略转换为billing Field类型
     * @param policy  套餐计费策略
     * @param billMode 计费模式
     * @return 转换后
     */
    private List<BillingField> packagePolicyToBillingField(PackagePriceBillingPolicy policy, BillModeConstants billMode) {
        return List.of(BillingField.of(packageFieldName,
                billMode.equals(BillModeConstants.MONTHLY) ? policy.getMonthly() : policy.getOnDemand(),
                billMode.equals(BillModeConstants.MONTHLY) ? UnitPriceConstants.MONTH : UnitPriceConstants.HOUR));
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
                .allMatch(packageField -> instance.getMeta().get(packageField.getField()).equals(packageField.getNumber())
                )).filter(policy -> instance.getBillMode().equals(BillModeConstants.MONTHLY) ? Objects.nonNull(policy.getMonthly()) : Objects.nonNull(policy.getOnDemand())
        ).findFirst();
    }

    /**
     * 资源实例和历史策略生产区域对应的 实例和策略
     *
     * @param resourceInstances 资源实例列表
     * @param billPolicies      策略列表
     * @return 区域
     */
    private static List<Region> getRegion(
            List<ResourceInstance> resourceInstances,
            List<BillPolicyDetails> billPolicies,
            List<BillPolicyCloudAccountMapping> billPolicyCloudAccountMappings,
            String month,
            InstanceState.Time startTime,
            InstanceState.Time endTime) {

        if (CollectionUtils.isEmpty(resourceInstances) || CollectionUtils.isEmpty(billPolicies)) {
            return List.of();
        }

        // 加载数据到时间线 开始
        List<TimeLine<Object>> sourceTimeLines = new ArrayList<>();
        sourceTimeLines.addAll(resourceInstances.stream().map(r -> new TimeLine<Object>(r, r.getCreateTime(), Status.NODE)).toList());
        sourceTimeLines.addAll(billPolicies.stream().map(policy -> new TimeLine<Object>(policy, policy.getCreateTime(), Status.NODE)).toList());
        LocalDateTime regionStart = getLocalDateTime(month, startTime, true);
        LocalDateTime regionEnd = getLocalDateTime(month, endTime, false);
        sourceTimeLines.add(new TimeLine<>("start", regionStart, Status.NODE));
        sourceTimeLines.add(new TimeLine<>("end", regionEnd, Status.NODE));
        // billPolicyCloudAccountMappings 云账号策略变更的时间线 如果 策略id为null则表示当前云账号未挂到任何一个策略上,顾不计费
        sourceTimeLines.addAll(billPolicyCloudAccountMappings.stream().map(billPolicyCloudAccountMapping -> {
            return new TimeLine<Object>(billPolicyCloudAccountMapping, billPolicyCloudAccountMapping.getCreateTime(), StringUtils.isNotEmpty(billPolicyCloudAccountMapping.getBillPolicyId()) ? Status.START : Status.END);
        }).toList());


        List<TimeLine<Object>> timeLines = sourceTimeLines.stream().filter(time -> time.createTime.compareTo(regionStart) >= 0 && time.createTime.compareTo(regionEnd) <= 0).toList();

        // 排序时间线
        timeLines = timeLines.stream().sorted(Comparator.comparing(TimeLine::getCreateTime)).toList();
        // 处理时间线数据
        List<Region> result = new ArrayList<>();
        ResourceInstance currentResourceInstance = resourceInstances.stream().min(Comparator.comparing(ResourceInstance::getCreateTime)).get();

        BillPolicyCloudAccountMapping currentPolicyCloudAccountMapping = billPolicyCloudAccountMappings.stream().min(Comparator.comparing(BillPolicyCloudAccountMapping::getCreateTime)).get();

        Status currentStatus = sourceTimeLines.stream().filter(time -> time.createTime.isAfter(regionStart))
                .findFirst()
                .map(s -> Status.NODE)
                .orElse(Status.END);

        Map<String, BillPolicyDetails> currentPolicyMap = new HashMap<>();

        Map<String, List<BillPolicyDetails>> collect = billPolicies.stream().collect(Collectors.groupingBy(BillPolicyDetails::getBillPolicyId));

        for (Map.Entry<String, List<BillPolicyDetails>> stringListEntry : collect.entrySet()) {
            currentPolicyMap.put(stringListEntry.getKey(), stringListEntry.getValue().stream().min(Comparator.comparing(BillPolicyDetails::getCreateTime)).get());
        }

        for (int i = 0; i < timeLines.size(); i++) {
            TimeLine<Object> pre = timeLines.get(i);
            if (pre.instance instanceof ResourceInstance resourceInstance) {
                currentResourceInstance = resourceInstance;
            }
            if (pre.instance instanceof BillPolicyDetails billPolicyDetails) {
                currentPolicyMap.put(billPolicyDetails.getBillPolicyId(), billPolicyDetails);
            }
            if (pre.instance instanceof BillPolicyCloudAccountMapping billPolicyCloudAccountMapping) {
                currentPolicyCloudAccountMapping = billPolicyCloudAccountMapping;
            }
            if (pre.status.equals(Status.START)) {
                currentStatus = pre.status;
            }
            if (i + 1 < timeLines.size()) {
                TimeLine<Object> next = timeLines.get(i + 1);
                if (currentStatus.equals(Status.END)) {
                    continue;
                }
                InstanceState.Time start = InstanceState.Time.of(pre.getCreateTime().getDayOfMonth(), pre.getCreateTime().getHour(), pre.getCreateTime().getMinute());
                InstanceState.Time end = next.getCreateTime().equals(regionEnd) ? null : InstanceState.Time.of(next.getCreateTime().getDayOfMonth(), next.getCreateTime().getHour(), next.getCreateTime().getMinute());
                result.add(new Region(start, end, currentResourceInstance, currentPolicyMap.get(currentPolicyCloudAccountMapping.getBillPolicyId())));
                if (next.status.equals(Status.END)) {
                    currentStatus = Status.END;
                }
            }

        }
        return filterRegion(result);
    }

    private static List<Region> filterRegion(List<Region> regions) {
        // 如果存在开始时间一样的Region 则使用后面的Region
        return regions.stream().filter(region -> Objects.isNull(region.end) || !region.end.toString().equals(region.start.toString())).toList();
    }

    private static LocalDateTime getLocalDateTime(String month,
                                                  InstanceState.Time time,
                                                  boolean min) {
        if (Objects.isNull(time)) {
            LocalDateTime localDateTime = CommonUtil.getLocalDateTime(month, "yyyy-MM");
            if (min) {
                return localDateTime.with(TemporalAdjusters.firstDayOfMonth());
            } else {
                return LocalDateTime.of(localDateTime.with(TemporalAdjusters.lastDayOfMonth()).toLocalDate(), LocalTime.MAX);
            }
        }
        String format = "%s-%s %s:%s";
        String formatted = format.formatted(month, time.getDay(), time.getHour(), time.getMinute());
        return CommonUtil.getLocalDateTime(formatted, "yyyy-MM-dd HH:mm");
    }

    /**
     * 计费
     *
     * @param instanceState 实例状态
     * @param month         月份
     * @param billMode      计费策略
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
                                                              BillModeConstants billMode,
                                                              Map<String, Object> globalConfigMeta,
                                                              BiPredicate<Map<String, Object>, InstanceState.State> predicate,
                                                              Map<String, Integer> billingMate,
                                                              List<BillingField> fields,
                                                              BillingGranularityConstants granularity) {
        BillingPolicy billingPolicy = billMode.equals(BillModeConstants.MONTHLY) ? setting.getMonthlyPolicy() : setting.getOnDemandPolicy();
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

        private Status status;

        private T instance;

        public TimeLine(T instance, LocalDateTime createTime, Status status) {
            this.createTime = createTime;
            this.instance = instance;
            this.status = status;
        }
    }

    private static enum Status {
        /**
         * 开始标志
         */
        START,
        /**
         * 节点标志
         */
        NODE,
        /**
         * 结束标志
         */
        END
    }

    private static class GenerationDate {

        private final String generationDate;
        private final LocalDateTime localDateTime;

        private final GenerationDateType type;

        public GenerationDate(String generationDate) {
            this.generationDate = generationDate;
            if (generationDate.length() == 7) {
                this.localDateTime = CommonUtil.getLocalDateTime(generationDate, "yyyy-MM");
                this.type = GenerationDateType.MONTH;
            } else if (generationDate.length() == 10) {
                this.localDateTime = CommonUtil.getLocalDateTime(generationDate, "yyyy-MM-dd");
                this.type = GenerationDateType.DAY;
            } else if (generationDate.length() == 13) {
                this.localDateTime = CommonUtil.getLocalDateTime(generationDate, "yyyy-MM-dd HH");
                this.type = GenerationDateType.HOUR;

            } else {
                throw new RuntimeException("参数错误");
            }
        }

        private String getMonth() {
            return generationDate.substring(0, 7);
        }

        private Region getRegion() {
            if (type.equals(GenerationDateType.MONTH)) {
                return new Region(null, null, null, null);
            } else if (type.equals(GenerationDateType.DAY)) {
                InstanceState.Time start = InstanceState.Time.of(localDateTime.getDayOfMonth(), localDateTime.getHour(), localDateTime.getMinute());
                LocalDateTime endLocalDateTime = localDateTime.plusDays(1);
                InstanceState.Time end = InstanceState.Time.of(endLocalDateTime.getDayOfMonth(), endLocalDateTime.getHour(), endLocalDateTime.getMinute());
                return new Region(start, end, null, null);
            } else {
                InstanceState.Time start = InstanceState.Time.of(localDateTime.getDayOfMonth(), localDateTime.getHour(), localDateTime.getMinute());
                LocalDateTime endLocalDateTime = localDateTime.plusHours(1);
                InstanceState.Time end = InstanceState.Time.of(endLocalDateTime.getDayOfMonth(), endLocalDateTime.getHour(), endLocalDateTime.getMinute());
                return new Region(start, end, null, null);
            }
        }

    }

    private static enum GenerationDateType {
        MONTH,
        DAY,
        HOUR
    }
}
