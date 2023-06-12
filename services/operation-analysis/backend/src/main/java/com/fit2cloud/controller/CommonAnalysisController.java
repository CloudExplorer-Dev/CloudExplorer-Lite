package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.CommonRequest;
import com.fit2cloud.dto.AnalysisCloudAccountDTO;
import com.fit2cloud.service.ICommonAnalysisService;
import com.fit2cloud.service.impl.CurrentUserResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 基础资源分析
 *
 * @author jianneng
 * @date 2022/12/11 18:42
 **/
@RestController
@RequestMapping("/api/common")
@Validated
@Tag(name = "公共接口")
public class CommonAnalysisController {
    @Resource
    private ICommonAnalysisService iCommonAnalysisService;
    @Resource
    private CurrentUserResourceService currentUserResourceService;

    @Operation(summary = "查询云账号数量", description = "查询云账号数量")
    @GetMapping("/cloud_account/count")
    @PreAuthorize("@cepc.hasAnyCePermission('BASE_RESOURCE_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<Long> cloudAccountByCloudAccountId(@RequestParam("cloudAccountId") String cloudAccountId) {
        return ResultHolder.success(iCommonAnalysisService.countCloudAccount(cloudAccountId));
    }

    @Operation(summary = "查询云账号", description = "查询云账号")
    @GetMapping("/cloud_account/list")
    @PreAuthorize("@cepc.hasAnyCePermission('OVERVIEW:READ','DISK_ANALYSIS:READ','SERVER_ANALYSIS:READ','SERVER_OPTIMIZATION:READ')")
    public ResultHolder<List<CloudAccount>> cloudAccountList() {
        return ResultHolder.success(currentUserResourceService.currentUserCloudAccountList());
    }

    @Operation(summary = "云账号资源统计", description = "云账号资源统计")
    @GetMapping("/cloud_account/resource/count/{currentPage}/{limit}")
    @PreAuthorize("@cepc.hasAnyCePermission('OVERVIEW:READ')")
    public ResultHolder<IPage<AnalysisCloudAccountDTO>> cloudAccountDetailed(@NotNull(message = "当前页不能为空")
                                                                             @Min(message = "当前页不能小于0", value = 1)
                                                                             @PathVariable("currentPage")
                                                                             Integer currentPage,
                                                                             @NotNull(message = "每页大小不能为空")
                                                                             @Min(message = "每页大小不能小于1", value = 1)
                                                                             @PathVariable("limit")
                                                                             Integer limit,
                                                                             CommonRequest commonRequest) {
        return ResultHolder.success(iCommonAnalysisService.cloudAccountDetailed(currentPage, limit, commonRequest.getCloudAccountId()));
    }


}
