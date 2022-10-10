package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.cloud_account.*;
import com.fit2cloud.controller.response.cloud_account.CloudAccountJobDetailsResponse;
import com.fit2cloud.controller.response.cloud_account.CloudAccountResponse;
import com.fit2cloud.controller.response.cloud_account.PlatformResponse;
import com.fit2cloud.dao.entity.CloudAccount;
import com.fit2cloud.dao.mapper.CloudAccountMapper;
import com.fit2cloud.request.cloud_account.SyncRequest;
import com.fit2cloud.response.cloud_account.AccountJobRecordResponse;
import com.fit2cloud.response.cloud_account.ResourceCountResponse;
import com.fit2cloud.response.cloud_account.SyncResource;
import com.fit2cloud.service.ICloudAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author:张少虎
 * @Date: 2022/8/31  10:49 AM
 * @Version 1.0
 * @注释: 云账号相关接口
 */
@RestController
@RequestMapping("/api/cloud_account")
@Validated
@Api("云账号相关接口")
public class CloudAccountController {
    @Resource
    private ICloudAccountService cloudAccountService;


    @GetMapping("page")
    @ApiOperation(value = "分页查询云账号", notes = "分页查询云账号")
    @PreAuthorize("hasAnyCePermission('CLOUD_ACCOUNT:READ')")
    public ResultHolder<IPage<CloudAccountResponse>> page(@Validated CloudAccountRequest cloudAccountRequest) {
        return ResultHolder.success(cloudAccountService.page(cloudAccountRequest));
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询云账号", notes = "根据id查询云账号")
    @PreAuthorize("hasAnyCePermission('CLOUD_ACCOUNT:READ')")
    public ResultHolder<CloudAccount> findCloudAccount(@ApiParam(value = "云账号id", required = true)
                                                       @CustomValidated(mapper = CloudAccountMapper.class, handler = ExistHandler.class, message = "{i18n.cloud_account_id_not_existent}", exist = false)
                                                       @PathVariable("id") String id) {
        return ResultHolder.success(cloudAccountService.getById(id));
    }

    @GetMapping("/platform")
    @ApiOperation(value = "获取当前云平台所有供应商", notes = "获取当前云平台所有供应商")
    @PreAuthorize("hasAnyCePermission('CLOUD_ACCOUNT:READ')")
    public ResultHolder<List<PlatformResponse>> getPlatform() {
        List<PlatformResponse> platformResponses = cloudAccountService.getPlatforms();
        return ResultHolder.success(platformResponses);
    }

    @PostMapping
    @ApiOperation(value = "插入云账号", notes = "插入云账号")
    @PreAuthorize("hasAnyCePermission('CLOUD_ACCOUNT:CREATE')")
    public ResultHolder<CloudAccount> save(@RequestBody AddCloudAccountRequest addCloudAccountRequest) {
        CloudAccount cloudAccount = cloudAccountService.save(addCloudAccountRequest);
        return ResultHolder.success(cloudAccount);
    }


    @PutMapping
    @ApiOperation(value = "更新云账号", notes = "更新云账号")
    @PreAuthorize("hasAnyCePermission('CLOUD_ACCOUNT:EDIT')")
    public ResultHolder<CloudAccount> update(@RequestBody UpdateCloudAccountRequest updateCloudAccountRequest) {
        CloudAccount cloudAccount = cloudAccountService.update(updateCloudAccountRequest);
        return ResultHolder.success(cloudAccount);
    }

    @DeleteMapping("/{cloud_account_id}")
    @ApiOperation(value = "删除云账号", notes = "删除云账号")
    @PreAuthorize("hasAnyCePermission('CLOUD_ACCOUNT:DELETE')")
    public ResultHolder<Boolean> delete(@ApiParam("云账号id")
                                        @PathVariable("cloud_account_id")
                                        @CustomValidated(mapper = CloudAccountMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.cloud_account.id.is.not.existent}", exist = false)
                                        String accountId) {
        return ResultHolder.success(cloudAccountService.delete(accountId));
    }

    @DeleteMapping
    @ApiOperation(value = "批量删除云账号", notes = "批量删除云账号")
    @PreAuthorize("hasAnyCePermission('CLOUD_ACCOUNT:DELETE')")
    public ResultHolder<Boolean> deleteBatch(@ApiParam("云账号id")
                                             @Size(min = 1, message = "{i18n.i18n.cloud_account.id.is.not.empty}")
                                             @RequestBody ArrayList<String> cloudAccountIds) {
        boolean delete = cloudAccountService.delete(cloudAccountIds);
        return ResultHolder.success(delete);
    }

    @PutMapping("/sync")
    @ApiOperation(value = "根据云账号全量同步", notes = "根据云账号全量同步")
    public ResultHolder<Boolean> sync(@ApiParam("云账号id") @Size(min = 1, message = "{i18n.i18n.cloud_account.id.is.not.empty}") @RequestBody ArrayList<String> cloudAccountIds) {
        cloudAccountService.sync(cloudAccountIds);
        return ResultHolder.success(true);
    }

    @GetMapping("/region/{cloud_account_id}")
    @ApiOperation(value = "获取当前云账号的区域信息", notes = "获取当前云账号的区域信息")
    @PreAuthorize("hasAnyCePermission('CLOUD_ACCOUNT:READ')")
    public ResultHolder<List<Credential.Region>> region(@ApiParam("云账号id")
                                                        @PathVariable("cloud_account_id")
                                                        @NotNull(message = "{i18n.cloud_account.id.is.not.empty}")
                                                        @CustomValidated(mapper = CloudAccountMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.cloud_account.id.is.not.existent}", exist = false)
                                                        String accountId) {
        List<Credential.Region> regions = cloudAccountService.listRegions(accountId);
        return ResultHolder.success(regions);
    }

    @GetMapping("/sync/job_record")
    @ApiOperation(value = "查询云账号最新的同步记录", notes = "查询云账号最新的同步记录")
    public ResultHolder<Map<String, List<AccountJobRecordResponse>>> findCloudAcoountSyncStatus(@ApiParam("需要查询的云账户id") @RequestParam("cloudAccountIds[]") List<String> cloudAccountIds) {
        return ResultHolder.success(cloudAccountService.findCloudAcoountSyncStatus(cloudAccountIds).stream().collect(Collectors.groupingBy(AccountJobRecordResponse::getAccountId)));
    }

    @GetMapping("/verification/{cloud_account_id}")
    @ApiOperation(value = "校验云账号信息", notes = "校验云账号信息")
    @PreAuthorize("hasAnyCePermission('CLOUD_ACCOUNT:READ')")
    public ResultHolder<CloudAccount> verification(@ApiParam("云账号id")
                                                   @NotNull(message = "{i18n.cloud_account.id.is.not.empty}")
                                                   @CustomValidated(mapper = CloudAccountMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.cloud_account.id.is.not.existent}", exist = false)
                                                   @PathVariable("cloud_account_id") String accountId) {
        CloudAccount cloudAccount = cloudAccountService.verification(accountId);
        return ResultHolder.success(cloudAccount);
    }

    @GetMapping("/jobs/{cloud_account_id}")
    @ApiOperation(value = "获取云账号的定时任务", notes = "获取云账号的定时任务")
    @PreAuthorize("hasAnyCePermission('CLOUD_ACCOUNT:READ')")
    public ResultHolder<CloudAccountJobDetailsResponse> jobs(@ApiParam("云账号id")
                                                             @NotNull(message = "{i18n.cloud_account.id.is.not.empty}")
                                                             @CustomValidated(mapper = CloudAccountMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.cloud_account.id.is.not.existent}", exist = false)
                                                             @PathVariable("cloud_account_id") String accountId) {
        return ResultHolder.success(cloudAccountService.jobs(accountId));
    }

    @PutMapping("/jobs")
    @ApiOperation(value = "修改云账号定时任务", notes = "修改云账号定时任务")
    @PreAuthorize("hasAnyCePermission('CLOUD_ACCOUNT:EDIT')")
    public ResultHolder<CloudAccountJobDetailsResponse> updateJobs(@RequestBody UpdateJobsRequest updateJobsRequest) {
        return ResultHolder.success(cloudAccountService.updateJob(updateJobsRequest));
    }

    @GetMapping("/jobs/resource")
    @ApiOperation(value = "获取所有模块的同步资源", notes = "获取所有模块的同步资源")
    public ResultHolder<List<SyncResource>> getResourceJobs() {
        List<SyncResource> moduleResourceJob = cloudAccountService.getModuleResourceJob();
        return ResultHolder.success(moduleResourceJob);
    }

    @PostMapping("/sync")
    @ApiOperation(value = "同步", notes = "同步")
    public ResultHolder<Boolean> sync(@RequestBody SyncRequest request) {
        cloudAccountService.sync(request);
        return ResultHolder.success(true);
    }

    @GetMapping("/balance/{id}")
    @ApiOperation(value = "获取云账号余额")
    @PreAuthorize("hasAnyCePermission('CLOUD_ACCOUNT:EDIT')")
    public ResultHolder<Object> getAccountBalance(@ApiParam(value = "云账号id", required = true)
                                                  @CustomValidated(mapper = CloudAccountMapper.class, handler = ExistHandler.class, message = "{i18n.cloud_account_id_not_existent}", exist = false)
                                                  @PathVariable("id") String id) {
        return ResultHolder.success(cloudAccountService.getAccountBalance(id));
    }


    @PutMapping("/updateName")
    @ApiOperation(value = "修改云账号名称")
    @PreAuthorize("hasAnyCePermission('CLOUD_ACCOUNT:EDIT')")
    public ResultHolder<Boolean> updateAccountName(@RequestBody UpdateAccountNameRequest updateAccountNameRequest) {
        return ResultHolder.success(cloudAccountService.updateAccountName(updateAccountNameRequest));
    }

    @GetMapping("/resourceCount/{cloud_account_id}")
    @ApiOperation(value = "获取云账号资源计数")
    @PreAuthorize("hasAnyCePermission('CLOUD_ACCOUNT:EDIT')")
    public ResultHolder<List<ResourceCountResponse>> resourceCount(@ApiParam("云账号id")
                                                                   @NotNull(message = "{i18n.cloud_account.id.is.not.empty}")
                                                                   @CustomValidated(mapper = CloudAccountMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.cloud_account.id.is.not.existent}", exist = false)
                                                                   @PathVariable("cloud_account_id") String accountId) {
        return ResultHolder.success(cloudAccountService.getModuleResourceCount(accountId));
    }
}
