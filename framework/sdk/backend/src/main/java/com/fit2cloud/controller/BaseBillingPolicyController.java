package com.fit2cloud.controller;

import com.fit2cloud.autoconfigure.ChargingConfig;
import com.fit2cloud.common.charging.entity.BillingFieldMeta;
import com.fit2cloud.common.charging.entity.InstanceBill;
import com.fit2cloud.common.charging.generation.impl.SimpleBillingGeneration;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dto.ListInstanceBillRequest;
import com.fit2cloud.dto.charging.BillingFieldMetaSetting;
import com.fit2cloud.dto.charging.ChargingModuleInfo;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/1  15:36}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RequestMapping("/api/base/billing")
@RestController
@Tag(name = "账单相关接口", description = "账单相关接口")
public class BaseBillingPolicyController {


    @GetMapping("/policy_config")
    @Operation(summary = "获取账单配置")
    @Hidden
    private ResultHolder<List<BillingFieldMetaSetting>> getChargingModuleConfigInfo() {
        ChargingModuleInfo billSettings = ChargingConfig.getBillSettings();
        List<BillingFieldMetaSetting> billingFieldMetaSettings = billSettings
                .getBillSettings()
                .stream()
                .map(billSetting -> {
                    BillingFieldMetaSetting billingFieldMetaSetting = new BillingFieldMetaSetting();
                    Map<String, BillingFieldMeta> billingFieldMeta = billSetting.getBillingFieldMeta();
                    billingFieldMetaSetting.setBillingFieldMeta(billingFieldMeta);
                    billingFieldMetaSetting.setResourceType(billSetting.getResourceInstanceType());
                    billingFieldMetaSetting.setResourceName(billSetting.getResourceInstanceName());
                    billingFieldMetaSetting.setModule(billSettings.getModule());
                    if (Objects.nonNull(billSetting.getGlobalConfigMeta())) {
                        billingFieldMetaSetting.setGlobalConfigMetaForms(billSetting.getGlobalConfigMetaForms());
                        billingFieldMetaSetting.setDefaultGlobalConfigMeta(billSetting.getGlobalConfigMeta());
                    }
                    return billingFieldMetaSetting;
                }).toList();
        return ResultHolder.success(billingFieldMetaSettings);
    }


    @GetMapping("/instance_bill")
    @Operation(summary = "获取实例账单")
    private ResultHolder<List<InstanceBill>> listInstanceBill(ListInstanceBillRequest request) {
        ChargingModuleInfo billSettings = ChargingConfig.getBillSettings();
        List<InstanceBill> instanceBills = billSettings.getBillSettings().parallelStream()
                .map(billSetting -> SimpleBillingGeneration.of(billSetting).generation(request.getCloudAccountId(), request.getMonth(), request.getGranularity(), false))
                .flatMap(List::stream)
                .toList();
        return ResultHolder.success(instanceBills);
    }

}
