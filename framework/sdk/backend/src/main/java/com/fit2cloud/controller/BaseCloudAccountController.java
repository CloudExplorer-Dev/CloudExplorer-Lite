package com.fit2cloud.controller;

import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.mapper.BaseCloudAccountMapper;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.form.vo.Form;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.request.cloud_account.CloudAccountModuleJob;
import com.fit2cloud.request.cloud_account.SyncRequest;
import com.fit2cloud.response.cloud_account.ResourceCountResponse;
import com.fit2cloud.response.cloud_account.SyncResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/15  5:40 PM
 * @Version 1.0
 * @注释:
 */
@RequestMapping("/api/base/cloud_account")
@RestController
public class BaseCloudAccountController {
    @Resource
    private IBaseCloudAccountService cloudAccountService;

    @GetMapping("/list")
    public ResultHolder<List<CloudAccount>> list() {
        return ResultHolder.success(cloudAccountService.list());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据id查询云账号", description = "根据id查询云账号")
    public ResultHolder<CloudAccount> findCloudAccount(@Parameter(name = "云账号id", required = true)
                                                       @CustomValidated(mapper = BaseCloudAccountMapper.class, handler = ExistHandler.class, message = "{i18n.cloud_account_id_not_existent}", exist = false)
                                                       @PathVariable("id") String id) {
        return ResultHolder.success(cloudAccountService.getById(id));
    }

    @PostMapping("/job_init/{cloud_account_id}")
    @Operation(summary = "初始化云账号定时任务", description = "初始化云账号定时任务")
    public ResultHolder<Boolean> initCloudAccountJob(@Parameter(name = "云账号id") @NotNull @PathVariable("cloud_account_id") String cloudAccountId) {
        cloudAccountService.initCloudAccountJob(cloudAccountId);
        return ResultHolder.success(true);
    }

    @PutMapping("/job/{cloud_account_id}")
    @Operation(summary = "修改云账号定时任务", description = "修改云账号定时任务")
    public ResultHolder<CloudAccountModuleJob> updateJobs(@RequestBody CloudAccountModuleJob moduleJob, @PathVariable("cloud_account_id") String cloudAccountId) {
        return ResultHolder.success(cloudAccountService.updateJob(moduleJob, cloudAccountId));
    }

    @GetMapping("/job/{cloud_account_id}")
    @Operation(summary = "获取云账号的定时任务", description = "获取云账号的定时任务")
    public ResultHolder<CloudAccountModuleJob> getCloudAccountJob(@Parameter(name = "云账号id")
                                                                  @NotNull(message = "{i18n.cloud_account.id.is.not.empty}")
                                                                  @CustomValidated(mapper = BaseCloudAccountMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.cloud_account.id.is.not.existent}", exist = false)
                                                                  @PathVariable("cloud_account_id") String accountId) {
        return ResultHolder.success(cloudAccountService.getCloudAccountJob(accountId));
    }

    @GetMapping("/job/resource/{cloudAccountId}")
    @Operation(summary = "获取单个模块的定时任务", description = "获取单个模块的定时任务")
    public ResultHolder<List<SyncResource>> getModuleResourceJob(@PathVariable String cloudAccountId) {
        return ResultHolder.success(cloudAccountService.getModuleResourceJob(cloudAccountId));
    }

    @PostMapping("/sync")
    @Operation(summary = "同步当前模块", description = "同步当前模块云账号")
    public ResultHolder<Boolean> sync(@RequestBody SyncRequest syncRequest) {
        cloudAccountService.sync(syncRequest);
        return ResultHolder.success(true);
    }

    @PostMapping("/sync/{cloudAccountId}")
    @Operation(summary = "同步当前模块根据云账号id", description = "同步当前模块根据云账号id")
    public ResultHolder<Boolean> sync(@PathVariable String cloudAccountId) {
        cloudAccountService.sync(cloudAccountId);
        return ResultHolder.success(true);
    }

    @GetMapping("/count/resource/{cloud_account_id}")
    @Operation(summary = "根据云账号ID获取各个模块的云资源计数", description = "根据云账号ID获取各个模块的云资源计数")
    public ResultHolder<List<ResourceCountResponse>> getModuleResourceCount(@PathVariable("cloud_account_id") String accountId) {
        return ResultHolder.success(cloudAccountService.getModuleResourceCount(accountId));
    }

    @GetMapping("/bill/form")
    @Operation(summary = "获取账单设置form表单", description = "获取账单设置form表单")
    public ResultHolder<List<? extends Form>> getBillSettingFormByPlatform(@RequestParam("platform") String platform) {
        List<? extends Form> forms = cloudAccountService.getBillSettingFormByPlatform(platform);
        return ResultHolder.success(forms);
    }

    @GetMapping("/balance/{id}")
    @Operation(summary = "获取云账号余额")
    public ResultHolder<Object> getAccountBalance(@Parameter(name = "云账号id", required = true)
                                                  @CustomValidated(mapper = BaseCloudAccountMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.cloud_account.id.is.not.existent}", exist = false)
                                                  @PathVariable("id") String id) {
        return ResultHolder.success(cloudAccountService.getAccountBalance(id));
    }

}
