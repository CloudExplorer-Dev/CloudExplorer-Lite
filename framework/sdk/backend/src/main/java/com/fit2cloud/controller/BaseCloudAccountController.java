package com.fit2cloud.controller;

import com.fit2cloud.base.mapper.BaseCloudAccountMapper;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.request.cloud_account.CloudAccountModuleJob;
import com.fit2cloud.request.cloud_account.SyncRequest;
import com.fit2cloud.response.cloud_account.SyncResource;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
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

    @PostMapping("/job_init/{cloud_account_id}")
    @ApiOperation(value = "初始化云账号定时任务", notes = "初始化云账号定时任务")
    public ResultHolder<Boolean> initCloudAccountJob(@ApiParam("云账号id") @NotNull @PathVariable("cloud_account_id") String cloudAccountId) {
        cloudAccountService.initCloudAccountJob(cloudAccountId);
        return ResultHolder.success(true);
    }

    @PutMapping("/job/{cloud_account_id}")
    @ApiOperation(value = "修改云账号定时任务", notes = "修改云账号定时任务")
    public ResultHolder<CloudAccountModuleJob> updateJobs(@RequestBody CloudAccountModuleJob moduleJob, @PathVariable("cloud_account_id") String cloudAccountId) {
        return ResultHolder.success(cloudAccountService.updateJob(moduleJob, cloudAccountId));
    }

    @GetMapping("/job/{cloud_account_id}")
    @ApiOperation(value = "获取云账号的定时任务", notes = "获取云账号的定时任务")
    public ResultHolder<CloudAccountModuleJob> getCloudAccountJob(@ApiParam("云账号id")
                                                                  @NotNull(message = "{i18n.cloud_account.id.is.not.empty}")
                                                                  @CustomValidated(mapper = BaseCloudAccountMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.cloud_account.id.is.not.existent}", exist = false)
                                                                  @PathVariable("cloud_account_id") String accountId) {
        return ResultHolder.success(cloudAccountService.getCloudAccountJob(accountId));
    }

    @GetMapping("/job/resource")
    @ApiOperation(value = "获取所有模块的定时任务", notes = "获取所有模块的定时任务")
    public ResultHolder<List<SyncResource>> getModuleResourceJob() {
        return ResultHolder.success(cloudAccountService.getModuleResourceJob());
    }

    @PostMapping("/sync")
    @ApiOperation(value = "同步当前模块", notes = "同步当前模块云账号")
    public ResultHolder<Boolean> sync(@RequestBody SyncRequest syncRequest) {
        cloudAccountService.sync(syncRequest);
        return ResultHolder.success(true);
    }
}
