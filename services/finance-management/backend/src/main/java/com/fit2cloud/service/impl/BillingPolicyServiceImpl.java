package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fit2cloud.autoconfigure.ServerInfo;
import com.fit2cloud.base.entity.BillPolicy;
import com.fit2cloud.base.entity.BillPolicyCloudAccountMapping;
import com.fit2cloud.base.entity.BillPolicyDetails;
import com.fit2cloud.base.entity.json_entity.BillingField;
import com.fit2cloud.base.entity.json_entity.PackagePriceBillingPolicy;
import com.fit2cloud.base.mapper.BaseBillPolicyDetailsMapper;
import com.fit2cloud.base.mapper.BaseBillPolicyMapper;
import com.fit2cloud.base.service.IBaseBillPolicyCloudAccountMappingService;
import com.fit2cloud.base.service.IBaseBillPolicyDetailsService;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.charging.constants.UnitPriceConstants;
import com.fit2cloud.common.charging.entity.BillingFieldMeta;
import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.ServiceUtil;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.AddBillingPolicyRequest;
import com.fit2cloud.controller.request.BillingPolicyRequest;
import com.fit2cloud.controller.request.LinkCloudAccountRequest;
import com.fit2cloud.controller.response.BillingPolicyDetailsResponse;
import com.fit2cloud.controller.response.CloudAccountResponse;
import com.fit2cloud.dto.charging.BillingFieldMetaSetting;
import com.fit2cloud.service.IBillingPolicyService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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
    @Resource(name = "securityContextWorkThreadPool")
    private DelegatingSecurityContextExecutor securityContextWorkThreadPool;

    /**
     * 获取模块云账号任务
     */
    private final Function<String, ResultHolder<List<BillingFieldMetaSetting>>> getChargingModuleInfo = (String moduleName) -> {
        String httpUrl = ServiceUtil.getHttpUrl(moduleName, "/api/base/billing/policy_config");
        ResponseEntity<ResultHolder<List<BillingFieldMetaSetting>>> cloudAccountJob = restTemplate
                .exchange(httpUrl, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<ResultHolder<List<BillingFieldMetaSetting>>>() {
                });
        return cloudAccountJob.getBody();
    };


    @Override
    public List<BillPolicy> listLastPolicy() {
        return this.list();
    }

    @Override
    public List<CloudAccountResponse> listCloudAccountByPolicy(String billingPolicy) {
        List<BillPolicyCloudAccountMapping> list = billPolicyCloudAccountMappingService
                .list(new LambdaQueryWrapper<BillPolicyCloudAccountMapping>()
                        .eq(StringUtils.isNotEmpty(billingPolicy), BillPolicyCloudAccountMapping::getBillPolicyId, billingPolicy));
        return cloudAccountService.list().stream().map(cloudAccount -> {
            CloudAccountResponse cloudAccountResponse = new CloudAccountResponse();
            BeanUtils.copyProperties(cloudAccount, cloudAccountResponse);
            cloudAccountResponse.setPublicCloud(PlatformConstants.valueOf(cloudAccount.getPlatform()).getPublicCloud());
            list.stream().filter(cb -> StringUtils.equals(cb.getCloudAccountId(), cloudAccount.getId()))
                    .findFirst().ifPresent(cb -> {
                        cloudAccountResponse.setSelected(true);
                    });
            return cloudAccountResponse;
        }).toList();

    }

    @Override
    public void updateBillingPolicy(String billingPolicyId, BillingPolicyRequest request) {
        if (StringUtils.isNotEmpty(request.getName())) {
            BillPolicy billPolicy = new BillPolicy();
            billPolicy.setName(request.getName());
            billPolicy.setId(billingPolicyId);
            updateById(billPolicy);
        }
        LocalDateTime currentTime = LocalDateTime.now();
        if (CollectionUtils.isNotEmpty(request.getBillingPolicyDetailsList())) {
            // 最后一条信息
            List<BillPolicyDetails> lastList = baseBillPolicyDetailsMapper.listLast(new LambdaQueryWrapper<BillPolicyDetails>()
                    .eq(BillPolicyDetails::getBillPolicyId, billingPolicyId));
            request.getBillingPolicyDetailsList()
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


    @Override
    public BillingPolicyDetailsResponse detailsBillingPolicy(String billingPolicyId) {
        // 查询所有微服务的计费策略设置
        List<BillingFieldMetaSetting> billingFieldMetaSettings = ServiceUtil.getServices("gateway", ServerInfo.module)
                .stream()
                .map(modelName -> CompletableFuture.supplyAsync(() -> getChargingModuleInfo.apply(modelName), securityContextWorkThreadPool))
                .map(CompletableFuture::join)
                .map(ResultHolder::getData)
                .flatMap(List::stream)
                .toList();
        BillPolicy billPolicy = getById(billingPolicyId);
        // 查询最新的计费策略详情
        List<BillPolicyDetails> billPolicyDetailsList = baseBillPolicyDetailsMapper.listLast(new LambdaQueryWrapper<BillPolicyDetails>()
                .eq(BillPolicyDetails::getBillPolicyId, billingPolicyId));
        // 构建计费策略详情返回值
        BillingPolicyDetailsResponse billingPolicyDetailsResponse = new BillingPolicyDetailsResponse();
        billingPolicyDetailsResponse.setName(billPolicy.getName());
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
    public BillPolicy createBillingPolicy(AddBillingPolicyRequest request) {
        LocalDateTime currentTime = LocalDateTime.now();
        BillPolicy billPolicy = new BillPolicy();
        billPolicy.setName(request.getName());
        billPolicy.setCreateTime(currentTime);
        billPolicy.setUpdateTime(currentTime);
        this.save(billPolicy);

        // 获取其他模块 计费设置
        List<BillingFieldMetaSetting> billingFieldMetaSettings = ServiceUtil.getServices("gateway", ServerInfo.module)
                .stream()
                .map(modelName -> CompletableFuture.supplyAsync(() -> getChargingModuleInfo.apply(modelName), securityContextWorkThreadPool))
                .map(CompletableFuture::join)
                .map(ResultHolder::getData)
                .flatMap(List::stream)
                .toList();

        List<BillPolicyDetails> billPolicyDetailsList = billingFieldMetaSettings
                .stream()
                .map(item -> toBillPolicyDetails(currentTime, billPolicy.getId(), item))
                .toList();

        // 插入计费详情
        billPolicyDetailsService.saveBatch(billPolicyDetailsList);

        if (CollectionUtils.isNotEmpty(request.getLinkCloudAccountIds())) {
            // 转变策略与云账号映射数据
            List<BillPolicyCloudAccountMapping> billPolicyCloudAccountMappings = request
                    .getLinkCloudAccountIds()
                    .stream()
                    .map(cloudAccountId -> buildBillPolicyCloudAccountMapping(currentTime, billPolicy.getId(), cloudAccountId))
                    .toList();
            // 删除策略云账号关联关系
            billPolicyCloudAccountMappingService.remove(new LambdaQueryWrapper<BillPolicyCloudAccountMapping>()
                    .in(BillPolicyCloudAccountMapping::getCloudAccountId, request.getLinkCloudAccountIds()));
            // 插入计费策略云账号关联关系
            billPolicyCloudAccountMappingService.saveBatch(billPolicyCloudAccountMappings);
        }
        return billPolicy;
    }


    @Override
    @Transactional
    public List<BillPolicyCloudAccountMapping> linkCloudAccount(LinkCloudAccountRequest request) {
        LocalDateTime currentTime = LocalDateTime.now();
        String billingPolicyId = request.getBillingPolicyId();
        // 删除映射关系
        billPolicyCloudAccountMappingService.remove(new LambdaQueryWrapper<BillPolicyCloudAccountMapping>()
                .eq(BillPolicyCloudAccountMapping::getBillPolicyId, request.getBillingPolicyId())
                .or().in(BillPolicyCloudAccountMapping::getCloudAccountId, request.getCloudAccountIdList()));
        // 构建映射数据
        List<BillPolicyCloudAccountMapping> billPolicyCloudAccountMappings = request.getCloudAccountIdList()
                .stream()
                .map(cloudAccountId -> buildBillPolicyCloudAccountMapping(currentTime, billingPolicyId, cloudAccountId))
                .toList();
        // 插入映射数据
        billPolicyCloudAccountMappingService.saveBatch(billPolicyCloudAccountMappings);
        return billPolicyCloudAccountMappings;
    }

    @Override
    public boolean remove(String billingPolicyId) {
        // 删除映射关系
        billPolicyCloudAccountMappingService.remove(new LambdaQueryWrapper<BillPolicyCloudAccountMapping>()
                .eq(BillPolicyCloudAccountMapping::getBillPolicyId, billingPolicyId));
        // 删除策略详情
        billPolicyDetailsService.remove(new LambdaQueryWrapper<BillPolicyDetails>()
                .eq(BillPolicyDetails::getBillPolicyId, billingPolicyId));
        // 删除策略
        this.removeById(billingPolicyId);
        return true;
    }


    private List<BillingField> toBillingFieldList(Map<String, BillingFieldMeta> metaMap, Function<BillingFieldMeta, BigDecimal> getDefaultPrice, List<BillingField> oldBillingFieldList) {
        return metaMap
                .entrySet()
                .stream()
                .map(item -> oldBillingFieldList.stream().filter(f -> StringUtils.equals(item.getKey(), f.getField())).findFirst().orElse(BillingField.of(item.getKey(), getDefaultPrice.apply(item.getValue()), UnitPriceConstants.HOUR)))
                .toList();
    }

    @NotNull
    private BillingPolicyDetailsResponse.BillingPolicyDetails buildBillingPolicyDetails(BillingFieldMetaSetting billSetting,
                                                                                        List<PackagePriceBillingPolicy> packagePriceBillingPolicyList,
                                                                                        List<BillingField> unitPriceOnDemandBillingPolicy,
                                                                                        List<BillingField> unitPriceMonthlyBillingPolicy,
                                                                                        Map<String, Object> globalConfigMeta) {
        BillingPolicyDetailsResponse.BillingPolicyDetails billingPolicyDetails = new BillingPolicyDetailsResponse.BillingPolicyDetails();
        billingPolicyDetails.setPackagePriceBillingPolicy(packagePriceBillingPolicyList);
        billingPolicyDetails.setUnitPriceOnDemandBillingPolicy(toBillingFieldList(billSetting.getBillingFieldMeta(), BillingFieldMeta::getDefaultOnDemandMPrice, unitPriceOnDemandBillingPolicy));
        billingPolicyDetails.setUnitPriceMonthlyBillingPolicy(toBillingFieldList(billSetting.getBillingFieldMeta(), BillingFieldMeta::getDefaultMonthlyMPrice, unitPriceMonthlyBillingPolicy));
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
    @NotNull
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
    @NotNull
    private static BillPolicyCloudAccountMapping buildBillPolicyCloudAccountMapping(LocalDateTime currentTime, String billPolicyId, String cloudAccountId) {
        BillPolicyCloudAccountMapping billPolicyCloudAccountMapping = new BillPolicyCloudAccountMapping();
        billPolicyCloudAccountMapping.setBillPolicyId(billPolicyId);
        billPolicyCloudAccountMapping.setCloudAccountId(cloudAccountId);
        billPolicyCloudAccountMapping.setCreateTime(currentTime);
        billPolicyCloudAccountMapping.setUpdateTime(currentTime);
        billPolicyCloudAccountMapping.setId(billPolicyId + '-' + cloudAccountId);
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
    @NotNull
    private BillPolicyDetails toBillPolicyDetails(LocalDateTime currentTime, String billPolicyId, BillingFieldMetaSetting billingFieldMetaSetting) {
        BillPolicyDetails billPolicyDetails = new BillPolicyDetails();
        billPolicyDetails.setBillPolicyId(billPolicyId);
        billPolicyDetails.setPackagePriceBillingPolicy(List.of());
        billPolicyDetails.setUnitPriceMonthlyBillingPolicy(toBillingFieldList(billingFieldMetaSetting.getBillingFieldMeta(), BillingFieldMeta::getDefaultMonthlyMPrice, List.of()));
        billPolicyDetails.setUnitPriceOnDemandBillingPolicy(toBillingFieldList(billingFieldMetaSetting.getBillingFieldMeta(), BillingFieldMeta::getDefaultOnDemandMPrice, List.of()));
        billPolicyDetails.setResourceType(billingFieldMetaSetting.getResourceType());
        billPolicyDetails.setGlobalConfigMeta(billingFieldMetaSetting.getDefaultGlobalConfigMeta());
        billPolicyDetails.setCreateTime(currentTime);
        billPolicyDetails.setUpdateTime(currentTime);
        return billPolicyDetails;
    }


}
