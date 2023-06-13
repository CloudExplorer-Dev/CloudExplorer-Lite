package com.fit2cloud.controller;

import com.fit2cloud.base.entity.BillPolicy;
import com.fit2cloud.base.entity.BillPolicyCloudAccountMapping;
import com.fit2cloud.base.mapper.BaseBillPolicyMapper;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.AddBillingPolicyRequest;
import com.fit2cloud.controller.request.BillingPolicyRequest;
import com.fit2cloud.controller.request.LinkCloudAccountRequest;
import com.fit2cloud.controller.request.ListCloudAccountByPolicyRequest;
import com.fit2cloud.controller.response.BillingPolicyDetailsResponse;
import com.fit2cloud.controller.response.CloudAccountResponse;
import com.fit2cloud.service.IBillingPolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/31  18:57}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@Tag(name = "计费相关接口")
@RequestMapping("/api/billing_policy")
@Validated
public class BillingPolicyController {
    @Resource
    private IBillingPolicyService billingPolicyService;

    @GetMapping()
    @Operation(summary = "查询所有计费策略 ")
    @PreAuthorize("@cepc.hasAnyCePermission('BILLING_POLICY:READ')")
    public ResultHolder<List<BillPolicy>> list() {
        return ResultHolder.success(billingPolicyService.list());
    }

    @Operation(summary = "策略详情")
    @GetMapping("/details/{billing_policy_id}")
    public ResultHolder<BillingPolicyDetailsResponse> detailsBillingPolicy(@CustomValidated(mapper = BaseBillPolicyMapper.class, handler = ExistHandler.class, field = "id", message = "计费策略id不存在", exist = false)
                                                                           @PathVariable("billing_policy_id") String billingPolicyId) {
        BillingPolicyDetailsResponse billingPolicyDetailsResponse = billingPolicyService.detailsBillingPolicy(billingPolicyId);
        return ResultHolder.success(billingPolicyDetailsResponse);
    }

    @GetMapping("/list_cloud_account")
    @Operation(summary = "查询当前策略可关联的云账号")
    @PreAuthorize("@cepc.hasAnyCePermission('BILLING_POLICY:READ')")
    public ResultHolder<List<CloudAccountResponse>> listCloudAccountByPolicy(ListCloudAccountByPolicyRequest request) {
        List<CloudAccountResponse> cloudAccounts = billingPolicyService.listCloudAccountByPolicy(request.getBillingPolicyId());
        return ResultHolder.success(cloudAccounts);
    }

    @DeleteMapping("/{billing_policy_id}")
    @Operation(summary = "删除一个计费策略")
    @PreAuthorize("@cepc.hasAnyCePermission('BILLING_POLICY:DELETE')")
    public ResultHolder<Boolean> removeBillingPolicy(@Parameter(description = "计费策略Id")
                                                     @CustomValidated(mapper = BaseBillPolicyMapper.class, handler = ExistHandler.class, field = "id", message = "计费策略id不存在", exist = false)
                                                     @PathVariable("billing_policy_id")
                                                     String billingPolicyId) {
        boolean remove = billingPolicyService.remove(billingPolicyId);
        return ResultHolder.success(remove);
    }

    @Operation(summary = "修改策略")
    @PutMapping("/{billing_policy_id}")
    public ResultHolder<BillPolicy> updateBillingPolicy(@Parameter(description = "计费策略id") @CustomValidated(mapper = BaseBillPolicyMapper.class, handler = ExistHandler.class, field = "id", message = "计费策略id不存在", exist = false)
                                                        @PathVariable("billing_policy_id") String billingPolicyId,
                                                        @Validated @RequestBody BillingPolicyRequest request) {
        billingPolicyService.updateBillingPolicy(billingPolicyId, request);
        return ResultHolder.success(null);
    }

    @Operation(summary = "创建策略")
    @PostMapping()
    public ResultHolder<BillPolicy> createBillingPolicy(@Validated @RequestBody AddBillingPolicyRequest request) {
        BillPolicy billingPolicy = billingPolicyService.createBillingPolicy(request);
        return ResultHolder.success(billingPolicy);
    }

    @Operation(summary = "关联云账号")
    @PostMapping("/link")
    public ResultHolder<List<BillPolicyCloudAccountMapping>> linkCloudAccount(@Validated @RequestBody LinkCloudAccountRequest request) {
        List<BillPolicyCloudAccountMapping> billPolicyCloudAccountMappings = billingPolicyService.linkCloudAccount(request);
        return ResultHolder.success(billPolicyCloudAccountMappings);
    }

}
