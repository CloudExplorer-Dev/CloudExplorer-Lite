package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.common.advice.annnotaion.TokenRenewal;
import com.fit2cloud.common.event.annotaion.Emit;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.cloud_account.*;
import com.fit2cloud.controller.response.cloud_account.CloudAccountJobDetailsResponse;
import com.fit2cloud.controller.response.cloud_account.CloudAccountResponse;
import com.fit2cloud.controller.response.cloud_account.PlatformResponse;
import com.fit2cloud.dao.entity.CloudAccount;
import com.fit2cloud.dao.mapper.CloudAccountMapper;
import com.fit2cloud.request.cloud_account.SyncRequest;
import com.fit2cloud.response.JobRecordResourceResponse;
import com.fit2cloud.response.cloud_account.ResourceCountResponse;
import com.fit2cloud.response.cloud_account.SyncResource;
import com.fit2cloud.service.ICloudAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Tag(name = "云账号相关接口")
public class CloudAccountController {
    @Resource
    private ICloudAccountService cloudAccountService;


    @GetMapping("page")
    @Operation(summary = "分页查询云账号", description = "分页查询云账号")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_ACCOUNT:READ')")
    public ResultHolder<IPage<CloudAccountResponse>> page(@Validated CloudAccountRequest cloudAccountRequest) {
        return ResultHolder.success(cloudAccountService.page(cloudAccountRequest));
    }

    @GetMapping("count")
    @Operation(summary = "查询云账号个数", description = "查询云账号个数")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_ACCOUNT:READ')")
    public ResultHolder<Long> count() {
        return ResultHolder.success(cloudAccountService.count());
    }


    @GetMapping("/{id}")
    @Operation(summary = "根据id查询云账号", description = "根据id查询云账号")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_ACCOUNT:READ')")
    public ResultHolder<CloudAccount> findCloudAccount(@Parameter(description = "云账号id", required = true)
                                                       @CustomValidated(mapper = CloudAccountMapper.class, handler = ExistHandler.class, message = "{i18n.cloud_account_id_not_existent}", exist = false)
                                                       @PathVariable("id") String id) {
        return ResultHolder.success(cloudAccountService.getById(id));
    }

    @GetMapping("/platform")
    @Operation(summary = "获取当前云平台所有供应商", description = "获取当前云平台所有供应商")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_ACCOUNT:READ')")
    public ResultHolder<List<PlatformResponse>> getPlatform() {
        List<PlatformResponse> platformResponses = cloudAccountService.getPlatforms();
        return ResultHolder.success(platformResponses);
    }

    @PostMapping
    @Operation(summary = "插入云账号", description = "插入云账号")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_ACCOUNT:CREATE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_ACCOUNT, operated = OperatedTypeEnum.ADD,
            content = "'添加云账号['+#addCloudAccountRequest.name+']'",
            param = "#addCloudAccountRequest")
    @Emit(value = "CREATE::CLOUD_ACCOUNT", el = "#result.data.id")
    public ResultHolder<CloudAccount> save(@Validated(ValidationGroup.SAVE.class) @RequestBody AddCloudAccountRequest addCloudAccountRequest) {
        CloudAccount cloudAccount = cloudAccountService.save(addCloudAccountRequest);
        return ResultHolder.success(cloudAccount);
    }


    @PutMapping
    @Operation(summary = "更新云账号", description = "更新云账号")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_ACCOUNT:EDIT')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_ACCOUNT, operated = OperatedTypeEnum.MODIFY,
            resourceId = "#updateCloudAccountRequest.id",
            content = "'更新云账号['+#updateCloudAccountRequest.name+']'",
            param = "#updateCloudAccountRequest")
    @Emit(value = "UPDATE::CLOUD_ACCOUNT", el = "#updateCloudAccountRequest.id")
    public ResultHolder<CloudAccount> update(@Validated(ValidationGroup.UPDATE.class) @RequestBody UpdateCloudAccountRequest updateCloudAccountRequest) {
        CloudAccount cloudAccount = cloudAccountService.update(updateCloudAccountRequest);
        return ResultHolder.success(cloudAccount);
    }

    @DeleteMapping("/{cloud_account_id}")
    @Operation(summary = "删除云账号", description = "删除云账号")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_ACCOUNT:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_ACCOUNT, operated = OperatedTypeEnum.DELETE,
            resourceId = "#accountId",
            content = "'删除云账号'",
            param = "#accountId")
    @Emit(value = "DELETE::CLOUD_ACCOUNT")
    public ResultHolder<Boolean> delete(@Parameter(description = "云账号id")
                                        @PathVariable("cloud_account_id")
                                        @CustomValidated(mapper = CloudAccountMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.cloud_account.id.is.not.existent}", exist = false)
                                        String accountId) {
        return ResultHolder.success(cloudAccountService.delete(accountId));
    }

    @DeleteMapping
    @Operation(summary = "批量删除云账号", description = "批量删除云账号")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_ACCOUNT:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_ACCOUNT, operated = OperatedTypeEnum.BATCH_DELETE,
            resourceId = "#cloudAccountIds",
            content = "'批量删除云账号'",
            param = "#cloudAccountIds")
    @Emit("DELETE_BATCH::CLOUD_ACCOUNT")
    public ResultHolder<Boolean> deleteBatch(@Parameter(description = "云账号id")
                                             @Size(min = 1, message = "{i18n.i18n.cloud_account.id.is.not.empty}")
                                             @RequestBody ArrayList<String> cloudAccountIds) {
        boolean delete = cloudAccountService.delete(cloudAccountIds);
        return ResultHolder.success(delete);
    }

    @PutMapping("/sync")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_ACCOUNT:SYNC_RESOURCE')")
    @Operation(summary = "根据云账号全量同步", description = "根据云账号全量同步")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_ACCOUNT, operated = OperatedTypeEnum.SYNC,
            resourceId = "#cloudAccountIds",
            content = "'全量同步云账号'",
            param = "#cloudAccountIds")
    public ResultHolder<Boolean> sync(@Parameter(description = "云账号id") @Size(min = 1, message = "{i18n.i18n.cloud_account.id.is.not.empty}") @RequestBody ArrayList<String> cloudAccountIds) {
        cloudAccountService.sync(cloudAccountIds);
        return ResultHolder.success(true);
    }

    @GetMapping("/region/{cloud_account_id}")
    @Operation(summary = "获取当前云账号的区域信息", description = "获取当前云账号的区域信息")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_ACCOUNT:READ')")
    public ResultHolder<List<Credential.Region>> region(@Parameter(description = "云账号id")
                                                        @PathVariable("cloud_account_id")
                                                        @NotNull(message = "{i18n.cloud_account.id.is.not.empty}")
                                                        @CustomValidated(mapper = CloudAccountMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.cloud_account.id.is.not.existent}", exist = false)
                                                        String accountId) {
        List<Credential.Region> regions = cloudAccountService.listRegions(accountId);
        return ResultHolder.success(regions);
    }

    @GetMapping("/sync/job_record")
    @Operation(summary = "查询云账号最新的同步记录", description = "查询云账号最新的同步记录")
    @TokenRenewal
    public ResultHolder<Map<String, List<JobRecordResourceResponse>>> findCloudAccountSyncStatus(@Parameter(description = "需要查询的云账户id") @RequestParam("cloudAccountIds[]") List<String> cloudAccountIds) {
        return ResultHolder.success(cloudAccountService.findCloudAccountSyncStatus(cloudAccountIds).stream().collect(Collectors.groupingBy(JobRecordResourceResponse::getResourceId)));
    }

    @GetMapping("/verification/{cloud_account_id}")
    @Operation(summary = "校验云账号信息", description = "校验云账号信息")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_ACCOUNT:READ')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_ACCOUNT, operated = OperatedTypeEnum.CHECK,
            resourceId = "#accountId",
            content = "'验证账号信息'",
            param = "#accountId")
    public ResultHolder<CloudAccount> verification(@Parameter(description = "云账号id")
                                                   @NotNull(message = "{i18n.cloud_account.id.is.not.empty}")
                                                   @CustomValidated(mapper = CloudAccountMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.cloud_account.id.is.not.existent}", exist = false)
                                                   @PathVariable("cloud_account_id") String accountId) {
        CloudAccount cloudAccount = cloudAccountService.verification(accountId);
        return ResultHolder.success(cloudAccount);
    }

    @GetMapping("/jobs/{cloud_account_id}")
    @Operation(summary = "获取云账号的定时任务", description = "获取云账号的定时任务")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_ACCOUNT:READ')")
    public ResultHolder<CloudAccountJobDetailsResponse> jobs(@Parameter(description = "云账号id")
                                                             @NotNull(message = "{i18n.cloud_account.id.is.not.empty}")
                                                             @CustomValidated(mapper = CloudAccountMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.cloud_account.id.is.not.existent}", exist = false)
                                                             @PathVariable("cloud_account_id") String accountId) {
        return ResultHolder.success(cloudAccountService.jobs(accountId));
    }

    @PutMapping("/jobs")
    @Operation(summary = "编辑云账号定时任务", description = "编辑云账号定时任务")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_ACCOUNT:SYNC_SETTING')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_ACCOUNT, operated = OperatedTypeEnum.MODIFY,
            resourceId = "#updateJobsRequest.cloudAccountId",
            content = "'编辑云账号定时任务'",
            param = "#updateJobsRequest")
    public ResultHolder<CloudAccountJobDetailsResponse> updateJobs(@RequestBody UpdateJobsRequest updateJobsRequest) {
        return ResultHolder.success(cloudAccountService.updateJob(updateJobsRequest));
    }

    @GetMapping("/jobs/resource/{cloudAccountId}")
    @Operation(summary = "获取所有模块的同步资源", description = "获取所有模块的同步资源")
    public ResultHolder<List<SyncResource>> getResourceJobs(@PathVariable String cloudAccountId) {
        List<SyncResource> moduleResourceJob = cloudAccountService.getModuleResourceJob(cloudAccountId);
        return ResultHolder.success(moduleResourceJob);
    }

    @PostMapping("/sync")
    @Operation(summary = "同步", description = "同步")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_ACCOUNT:SYNC_RESOURCE', 'CLOUD_ACCOUNT:SYNC_BILL')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_ACCOUNT, operated = OperatedTypeEnum.SYNC,
            resourceId = "#request.cloudAccountId",
            content = "'执行同步'",
            param = "#request")
    public ResultHolder<Boolean> sync(@RequestBody SyncRequest request) {
        cloudAccountService.sync(request);
        return ResultHolder.success(true);
    }


    @PutMapping("/updateName")
    @Operation(summary = "编辑云账号名称")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_ACCOUNT:EDIT')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_ACCOUNT, operated = OperatedTypeEnum.MODIFY,
            resourceId = "#updateAccountNameRequest.id",
            content = "'编辑云账号名称为['+#updateAccountNameRequest.name+']'",
            param = "#updateAccountNameRequest")
    public ResultHolder<Boolean> updateAccountName(@RequestBody UpdateAccountNameRequest updateAccountNameRequest) {
        return ResultHolder.success(cloudAccountService.updateAccountName(updateAccountNameRequest));
    }

    @GetMapping("/resourceCount/{cloud_account_id}")
    @Operation(summary = "获取云账号资源计数")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_ACCOUNT:READ')")
    public ResultHolder<List<ResourceCountResponse>> resourceCount(@Parameter(description = "云账号id")
                                                                   @NotNull(message = "{i18n.cloud_account.id.is.not.empty}")
                                                                   @CustomValidated(mapper = CloudAccountMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.cloud_account.id.is.not.existent}", exist = false)
                                                                   @PathVariable("cloud_account_id") String accountId) {
        return ResultHolder.success(cloudAccountService.getModuleResourceCount(accountId));
    }

    @GetMapping("/pageSyncRecord")
    @Operation(summary = "获取同步记录")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_ACCOUNT:READ')")
    public ResultHolder<IPage<JobRecordResourceResponse>> pageSyncRecord(@Validated SyncRecordRequest syncRecordRequest) {
        return ResultHolder.success(cloudAccountService.pageSyncRecord(syncRecordRequest));
    }

    @GetMapping("/record/types")
    @Operation(summary = "获取同步记录类型")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_ACCOUNT:READ')")
    public ResultHolder<List<String>> listRecordTypes() {
        return ResultHolder.success(cloudAccountService.listTypes());
    }
}
