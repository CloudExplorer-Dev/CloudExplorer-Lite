package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fit2cloud.autoconfigure.PluginsContextHolder;
import com.fit2cloud.autoconfigure.ServerInfo;
import com.fit2cloud.base.entity.BillPolicy;
import com.fit2cloud.base.entity.BillPolicyCloudAccountMapping;
import com.fit2cloud.base.entity.BillPolicyDetails;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.json_entity.BillingField;
import com.fit2cloud.base.entity.json_entity.PackagePriceBillingPolicy;
import com.fit2cloud.base.mapper.BaseBillPolicyDetailsMapper;
import com.fit2cloud.base.mapper.BaseBillPolicyMapper;
import com.fit2cloud.base.service.IBaseBillPolicyCloudAccountMappingService;
import com.fit2cloud.base.service.IBaseBillPolicyDetailsService;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.charging.constants.UnitPriceConstants;
import com.fit2cloud.common.charging.entity.BillingFieldMeta;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.provider.IBaseCloudProvider;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.ServiceUtil;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.BillingPolicyRequest;
import com.fit2cloud.controller.request.LinkCloudAccountRequest;
import com.fit2cloud.controller.response.BillingPolicyDetailsResponse;
import com.fit2cloud.controller.response.CloudAccountResponse;
import com.fit2cloud.dto.charging.BillingFieldMetaSetting;
import com.fit2cloud.service.IBillingPolicyService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/31  18:55}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Service
public class BillingPolicyServiceImpl extends ServiceImpl<BaseBillPolicyMapper, BillPolicy> implements IBillingPolicyService {
    @Resource
    private BaseBillPolicyDetailsMapper baseBillPolicyDetailsMapper;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private IBaseBillPolicyDetailsService billPolicyDetailsService;
    @Resource
    private IBaseBillPolicyCloudAccountMappingService billPolicyCloudAccountMappingService;
    @Resource
    private IBaseCloudAccountService cloudAccountService;
    @Resource(name = "workThreadPool")
    private ThreadPoolExecutor workThreadPool;

    /**
     * 获取模块云账号任务
     */
    private final Function<String, ResultHolder<List<BillingFieldMetaSetting>>> getChargingModuleInfo = (String moduleName) -> {
        String httpUrl = ServiceUtil.getHttpUrl(moduleName, "/api/base/billing/policy_config");
        ResponseEntity<ResultHolder<List<BillingFieldMetaSetting>>> cloudAccountJob = restTemplate
                .exchange(httpUrl, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
                });
        return cloudAccountJob.getBody();
    };


    @Override
    public List<BillPolicy> listLastPolicy() {
        return this.list();
    }

    @Override
    public List<CloudAccountResponse> listCloudAccountByPolicy(String billingPolicy) {
        List<BillPolicyCloudAccountMapping> billPolicyCloudAccountMappingList = billPolicyCloudAccountMappingService
                .listLast(new LambdaQueryWrapper<>());

        List<String> policyIdList = billPolicyCloudAccountMappingList.stream()
                .map(BillPolicyCloudAccountMapping::getBillPolicyId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<BillPolicy> policyList = this.list(new LambdaQueryWrapper<BillPolicy>()
                .in(CollectionUtils.isNotEmpty(policyIdList), BillPolicy::getId, policyIdList));

        return cloudAccountService.list().stream().map(cloudAccount -> {
            CloudAccountResponse cloudAccountResponse = new CloudAccountResponse();
            BeanUtils.copyProperties(cloudAccount, cloudAccountResponse);
            cloudAccountResponse.setPublicCloud(PluginsContextHolder.getPlatformExtension(IBaseCloudProvider.class, cloudAccount.getPlatform()).getCloudAccountMeta().publicCloud);
            billPolicyCloudAccountMappingList
                    .stream()
                    .peek(cb ->
                            policyList.stream().filter(p -> StringUtils.equals(cloudAccount.getId(), cb.getCloudAccountId()) &&
                                            StringUtils.equals(p.getId(), cb.getBillPolicyId()))
                                    .findFirst().ifPresent(cloudAccountResponse::setBillPolicy)
                    )
                    .filter(cb -> StringUtils.equals(cb.getCloudAccountId(), cloudAccount.getId()) &&
                            StringUtils.equals(billingPolicy, cb.getBillPolicyId()))
                    .findFirst()
                    .ifPresent(cb -> cloudAccountResponse.setSelected(StringUtils.isNotEmpty(cb.getBillPolicyId())));
            return cloudAccountResponse;
        }).toList();

    }

    @Override
    @Transactional
    public void updateBillingPolicy(String billingPolicyId, BillingPolicyRequest request) {
        if (StringUtils.isNotEmpty(request.getName())) {
            reName(billingPolicyId, request.getName());
        }
        LocalDateTime currentTime = LocalDateTime.now();
        updateBillingPolicyDetails(request.getBillingPolicyDetailsList(), billingPolicyId, currentTime);
        linkCloudAccount(billingPolicyId, request.getCloudAccountList());
    }


    @Override
    public BillingPolicyDetailsResponse detailsBillingPolicy(String billingPolicyId) {

        // 查询所有微服务的计费策略设置
        List<BillingFieldMetaSetting> billingFieldMetaSettings = ServiceUtil.getServices("gateway", ServerInfo.module)
                .stream()
                .map(modelName -> CompletableFuture.supplyAsync(() -> getChargingModuleInfo.apply(modelName), new DelegatingSecurityContextExecutor(workThreadPool)))
                .map(CompletableFuture::join)
                .map(ResultHolder::getData)
                .flatMap(List::stream)
                .toList();
        // 查询最新的计费策略详情
        List<BillPolicyDetails> billPolicyDetailsList = new ArrayList<>();
        // 构建计费策略详情返回值
        BillingPolicyDetailsResponse billingPolicyDetailsResponse = new BillingPolicyDetailsResponse();
        if (StringUtils.isNotEmpty(billingPolicyId)) {
            BillPolicy billPolicy = getById(billingPolicyId);
            billingPolicyDetailsResponse.setName(billPolicy.getName());
            billPolicyDetailsList.addAll(baseBillPolicyDetailsMapper.listLast(new LambdaQueryWrapper<BillPolicyDetails>()
                    .eq(BillPolicyDetails::getBillPolicyId, billingPolicyId)));
        }
        // 构建计费详情
        List<BillingPolicyDetailsResponse.BillingPolicyDetails> billingPolicyDetails = billingFieldMetaSettings
                .stream()
                .map(billSetting -> billPolicyDetailsList
                        .stream()
                        .filter(billPolicyDetails -> StringUtils.equals(billPolicyDetails.getResourceType(), billSetting.getResourceType()))
                        .findFirst().map(billPolicyDetails -> buildBillingPolicyDetails(billSetting,
                                billPolicyDetails.getPackagePriceBillingPolicy(),
                                billPolicyDetails.getUnitPriceOnDemandBillingPolicy(),
                                billPolicyDetails.getUnitPriceMonthlyBillingPolicy(),
                                billPolicyDetails.getGlobalConfigMeta()))
                        .orElseGet(() -> buildBillingPolicyDetails(billSetting,
                                List.of(),
                                List.of(),
                                List.of(),
                                billSetting.getDefaultGlobalConfigMeta())))
                .toList();
        billingPolicyDetailsResponse.setBillingPolicyDetailsList(billingPolicyDetails);
        return billingPolicyDetailsResponse;
    }


    @Override
    public BillPolicy createBillingPolicy(BillingPolicyRequest request) {
        LocalDateTime currentTime = LocalDateTime.now();
        BillPolicy billPolicy = new BillPolicy();
        billPolicy.setName(request.getName());
        billPolicy.setCreateTime(currentTime);
        billPolicy.setUpdateTime(currentTime);
        this.save(billPolicy);

        if (CollectionUtils.isNotEmpty(request.getCloudAccountList())) {
            LinkCloudAccountRequest linkCloudAccountRequest = new LinkCloudAccountRequest();
            linkCloudAccountRequest.setBillingPolicyId(billPolicy.getId());
            linkCloudAccountRequest.setCloudAccountIdList(request.getCloudAccountList());
            linkCloudAccount(linkCloudAccountRequest);
        }
        updateBillingPolicyDetails(request.getBillingPolicyDetailsList(), billPolicy.getId(), currentTime);
        return billPolicy;
    }


    @Override
    @Transactional
    public List<BillPolicyCloudAccountMapping> linkCloudAccount(LinkCloudAccountRequest request) {
        return linkCloudAccount(request.getBillingPolicyId(), request.getCloudAccountIdList());
    }


    @Override
    public boolean remove(String billingPolicyId) {
        LocalDateTime currentTime = LocalDateTime.now();
        // 获取最后
        List<BillPolicyCloudAccountMapping> billPolicyCloudAccountMappingList = billPolicyCloudAccountMappingService.listLast(new LambdaQueryWrapper<>());
        billPolicyCloudAccountMappingList = billPolicyCloudAccountMappingList
                .stream()
                .filter(billPolicyCloudAccountMapping -> StringUtils.equals(billPolicyCloudAccountMapping.getBillPolicyId(), billingPolicyId))
                .map(BillPolicyCloudAccountMapping::getCloudAccountId)
                .map(cloudAccountId -> {
                    BillPolicyCloudAccountMapping billPolicyCloudAccountMapping = new BillPolicyCloudAccountMapping();
                    billPolicyCloudAccountMapping.setCloudAccountId(cloudAccountId);
                    billPolicyCloudAccountMapping.setCreateTime(currentTime);
                    billPolicyCloudAccountMapping.setUpdateTime(currentTime);
                    return billPolicyCloudAccountMapping;
                }).toList();
        if (CollectionUtils.isNotEmpty(billPolicyCloudAccountMappingList)) {
            billPolicyCloudAccountMappingService.saveBatch(billPolicyCloudAccountMappingList);
        }
        // 删除策略
        this.removeById(billingPolicyId);
        return true;
    }

    @Override
    public List<BillPolicyDetails> calculateConfigPrice(String cloudAccountId) {
        List<BillPolicyCloudAccountMapping> billPolicyCloudAccountMappingList =
                billPolicyCloudAccountMappingService.listLast(new QueryWrapper<BillPolicyCloudAccountMapping>()
                        .eq(ColumnNameUtil.getColumnName(BillPolicyCloudAccountMapping::getCloudAccountId, "bill_policy_cloud_account_mapping"), cloudAccountId));
        if (CollectionUtils.isEmpty(billPolicyCloudAccountMappingList)) {
            return List.of();
        }
        return baseBillPolicyDetailsMapper.listLast(new QueryWrapper<BillPolicyDetails>()
                .in(ColumnNameUtil.getColumnName(BillPolicyDetails::getBillPolicyId, "bill_policy_details"),
                        billPolicyCloudAccountMappingList.stream()
                                .map(BillPolicyCloudAccountMapping::getBillPolicyId).distinct().toList()));
    }

    @Override
    public BillPolicy reName(String billingPolicyId, String name) {
        List<BillPolicy> billPolicyList = this.list(new LambdaQueryWrapper<BillPolicy>()
                .ne(BillPolicy::getId, billingPolicyId)
                .eq(BillPolicy::getName, name));
        if (CollectionUtils.isEmpty((billPolicyList))) {
            BillPolicy newBillPolicy = new BillPolicy();
            newBillPolicy.setName(name);
            newBillPolicy.setId(billingPolicyId);
            updateById(newBillPolicy);
            return getById(billingPolicyId);
        } else {
            throw new Fit2cloudException(222, "策略名称已存在");
        }
    }


    private List<BillingField> toBillingFieldList(Map<String, BillingFieldMeta> metaMap, Function<BillingFieldMeta, BigDecimal> getDefaultPrice, List<BillingField> oldBillingFieldList, UnitPriceConstants unitPriceConstants) {
        return metaMap
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(s -> s.getValue().getOrder()))
                .map(item -> oldBillingFieldList.stream().filter(f -> StringUtils.equals(item.getKey(), f.getField())).findFirst().orElse(BillingField.of(item.getKey(), getDefaultPrice.apply(item.getValue()), unitPriceConstants)))
                .toList();
    }

    private BillingPolicyDetailsResponse.BillingPolicyDetails buildBillingPolicyDetails(BillingFieldMetaSetting billSetting,
                                                                                        List<PackagePriceBillingPolicy> packagePriceBillingPolicyList,
                                                                                        List<BillingField> unitPriceOnDemandBillingPolicy,
                                                                                        List<BillingField> unitPriceMonthlyBillingPolicy,
                                                                                        Map<String, Object> globalConfigMeta) {
        BillingPolicyDetailsResponse.BillingPolicyDetails billingPolicyDetails = new BillingPolicyDetailsResponse.BillingPolicyDetails();
        billingPolicyDetails.setPackagePriceBillingPolicy(packagePriceBillingPolicyList);
        billingPolicyDetails.setUnitPriceOnDemandBillingPolicy(toBillingFieldList(billSetting.getBillingFieldMeta(), BillingFieldMeta::getDefaultOnDemandMPrice, unitPriceOnDemandBillingPolicy, UnitPriceConstants.HOUR));
        billingPolicyDetails.setUnitPriceMonthlyBillingPolicy(toBillingFieldList(billSetting.getBillingFieldMeta(), BillingFieldMeta::getDefaultMonthlyMPrice, unitPriceMonthlyBillingPolicy, UnitPriceConstants.MONTH));
        billingPolicyDetails.setBillingFieldMeta(billSetting.getBillingFieldMeta());
        billingPolicyDetails.setResourceType(billSetting.getResourceType());
        billingPolicyDetails.setResourceName(billSetting.getResourceName());
        billingPolicyDetails.setGlobalConfigMetaForms(billSetting.getGlobalConfigMetaForms());
        billingPolicyDetails.setGlobalConfigMeta(MapUtils.isNotEmpty(globalConfigMeta) ? globalConfigMeta : billSetting.getDefaultGlobalConfigMeta());
        return billingPolicyDetails;
    }


    /**
     * 将 BillingPolicyRequest.BillingPolicyDetails 转换为 BillPolicyDetails对象
     *
     * @param billingPolicyId      策略id
     * @param currentTime          创建时间
     * @param billingPolicyDetails 策略详情
     * @return 计费策略详情实例对象
     */
    private static BillPolicyDetails toBillPolicyDetails(String billingPolicyId,
                                                         LocalDateTime currentTime,
                                                         BillingPolicyRequest.BillingPolicyDetails billingPolicyDetails) {
        BillPolicyDetails billPolicyDetails = new BillPolicyDetails();
        billPolicyDetails.setBillPolicyId(billingPolicyId);
        billPolicyDetails.setPackagePriceBillingPolicy(billingPolicyDetails.getPackagePriceBillingPolicy());
        billPolicyDetails.setUnitPriceMonthlyBillingPolicy(billingPolicyDetails.getUnitPriceMonthlyBillingPolicy());
        billPolicyDetails.setUnitPriceOnDemandBillingPolicy(billingPolicyDetails.getUnitPriceOnDemandBillingPolicy());
        billPolicyDetails.setResourceType(billingPolicyDetails.getResourceType());
        billPolicyDetails.setGlobalConfigMeta(JsonUtil.parseObject(JsonUtil.toJSONString(billingPolicyDetails.getGlobalConfigMeta()), new TypeReference<Map<String, Object>>() {
        }));
        billPolicyDetails.setCreateTime(currentTime);
        billPolicyDetails.setUpdateTime(currentTime);
        return billPolicyDetails;
    }

    /**
     * 构建计费策略与云账号映射实例
     *
     * @param currentTime    当前创建时间
     * @param billPolicyId   计费策略id
     * @param cloudAccountId 云账号id
     * @return 计费策略与云账号映射实例对象
     */

    private static BillPolicyCloudAccountMapping buildBillPolicyCloudAccountMapping(LocalDateTime currentTime, String billPolicyId, String cloudAccountId) {
        BillPolicyCloudAccountMapping billPolicyCloudAccountMapping = new BillPolicyCloudAccountMapping();
        billPolicyCloudAccountMapping.setBillPolicyId(billPolicyId);
        billPolicyCloudAccountMapping.setCloudAccountId(cloudAccountId);
        billPolicyCloudAccountMapping.setCreateTime(currentTime);
        billPolicyCloudAccountMapping.setUpdateTime(currentTime);
        return billPolicyCloudAccountMapping;
    }


    /**
     * 将计费策略设置 转换为计费策略详情
     *
     * @param currentTime             创建时间
     * @param billPolicyId            计费策略Id
     * @param billingFieldMetaSetting 计费策略设置
     * @return 计费策略详情
     */

    private BillPolicyDetails toBillPolicyDetails(LocalDateTime currentTime, String billPolicyId, BillingFieldMetaSetting billingFieldMetaSetting) {
        BillPolicyDetails billPolicyDetails = new BillPolicyDetails();
        billPolicyDetails.setBillPolicyId(billPolicyId);
        billPolicyDetails.setPackagePriceBillingPolicy(List.of());
        billPolicyDetails.setUnitPriceMonthlyBillingPolicy(toBillingFieldList(billingFieldMetaSetting.getBillingFieldMeta(), BillingFieldMeta::getDefaultMonthlyMPrice, List.of(), UnitPriceConstants.MONTH));
        billPolicyDetails.setUnitPriceOnDemandBillingPolicy(toBillingFieldList(billingFieldMetaSetting.getBillingFieldMeta(), BillingFieldMeta::getDefaultOnDemandMPrice, List.of(), UnitPriceConstants.HOUR));
        billPolicyDetails.setResourceType(billingFieldMetaSetting.getResourceType());
        billPolicyDetails.setGlobalConfigMeta(billingFieldMetaSetting.getDefaultGlobalConfigMeta());
        billPolicyDetails.setCreateTime(currentTime);
        billPolicyDetails.setUpdateTime(currentTime);
        return billPolicyDetails;
    }


    /**
     * 修改计费策略详情
     *
     * @param billingPolicyDetailsList 详情列表
     * @param billingPolicyId          计费策略id
     * @param currentTime              当前时间
     */
    private void updateBillingPolicyDetails(List<BillingPolicyRequest.BillingPolicyDetails> billingPolicyDetailsList,
                                            String billingPolicyId,
                                            LocalDateTime currentTime) {
        if (CollectionUtils.isNotEmpty(billingPolicyDetailsList)) {
            // 最后一条信息
            List<BillPolicyDetails> lastList = baseBillPolicyDetailsMapper.listLast(new LambdaQueryWrapper<BillPolicyDetails>()
                    .eq(BillPolicyDetails::getBillPolicyId, billingPolicyId));
            billingPolicyDetailsList
                    .stream()
                    .map(billingPolicyDetails -> toBillPolicyDetails(billingPolicyId, currentTime, billingPolicyDetails))
                    .forEach(billPolicyDetails -> {
                        // 查询到资源类型相同的资源
                        Optional<BillPolicyDetails> first = lastList
                                .stream().
                                filter(last -> StringUtils.equals(last.getResourceType(), billPolicyDetails.getResourceType()))
                                .findFirst();

                        // 如果存在,对比是否发生了改变  改变了插入一条数据
                        if (first.isPresent()) {
                            if (!first.get().equals(billPolicyDetails)) {
                                billPolicyDetailsService.save(billPolicyDetails);
                            }
                        } else {
                            // 不存在直接插入
                            billPolicyDetailsService.save(billPolicyDetails);
                        }
                    });
        }
    }

    /**
     * 关联云账号
     *
     * @param policyId         策略id
     * @param cloudAccountList 云账号列表
     * @return 关联信息
     */
    private List<BillPolicyCloudAccountMapping> linkCloudAccount(String policyId, List<String> cloudAccountList) {

        LocalDateTime currentTime = LocalDateTime.now();

        List<String> platformList = PluginsContextHolder.getExtensions(IBaseCloudProvider.class).stream()
                .filter(provider -> !provider.getCloudAccountMeta().publicCloud)
                .map(provider -> provider.getCloudAccountMeta().platform).toList();

        List<CloudAccount> cloudAccounts = cloudAccountService.list(new LambdaQueryWrapper<CloudAccount>().in(CloudAccount::getPlatform, platformList));

        List<BillPolicyCloudAccountMapping> billPolicyCloudAccountMappingList = billPolicyCloudAccountMappingService
                .listLast(new LambdaQueryWrapper<>());

        List<BillPolicyCloudAccountMapping> billPolicyCloudAccountMappings = cloudAccounts.stream().map(cloudAccount -> {
            return (CollectionUtils.isEmpty(cloudAccountList) ? new ArrayList<String>() : cloudAccountList).stream()
                    .filter(cloudAccountId -> StringUtils.equals(cloudAccountId, cloudAccount.getId()))
                    .findFirst().map(cloudAccountId -> {
                        return buildBillPolicyCloudAccountMapping(currentTime, policyId, cloudAccount.getId());
                    }).orElseGet(() -> {
                        return billPolicyCloudAccountMappingList.stream()
                                .filter(b -> StringUtils.equals(b.getBillPolicyId(), policyId) && StringUtils.equals(b.getCloudAccountId(), cloudAccount.getId()))
                                .findFirst()
                                .map(b -> buildBillPolicyCloudAccountMapping(currentTime, null, cloudAccount.getId()))
                                .orElse(null);

                    });
        }).filter(Objects::nonNull).toList();

        // 插入映射数据
        billPolicyCloudAccountMappingService.saveBatch(billPolicyCloudAccountMappings);
        return billPolicyCloudAccountMappings;
    }


}
