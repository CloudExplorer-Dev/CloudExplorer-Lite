package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.CommonRequest;
import com.fit2cloud.dto.AnalysisCloudAccountDTO;
import com.fit2cloud.service.ICommonAnalysisService;
import com.fit2cloud.service.impl.CurrentUserResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 基础资源分析
 * @author jianneng
 * @date 2022/12/11 18:42
 **/
@RestController
@RequestMapping("/api/common")
@Validated
@Api("公共接口")
public class CommonAnalysisController {
    @Resource
    private ICommonAnalysisService iCommonAnalysisService;
    @Resource
    private CurrentUserResourceService currentUserResourceService;

    @ApiOperation(value = "查询云账号数量", notes = "查询云账号数量")
    @GetMapping("/cloud_account/count")
    @PreAuthorize("hasAnyCePermission('BASE_RESOURCE_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<Long> cloudAccountByCloudAccountId(@RequestParam("cloudAccountId") String cloudAccountId) {
        return ResultHolder.success(iCommonAnalysisService.countCloudAccount(cloudAccountId));
    }
    @ApiOperation(value = "查询云账号", notes = "查询云账号")
    @GetMapping("/cloud_account/list")
    @PreAuthorize("hasAnyCePermission('OVERVIEW:READ')")
    public ResultHolder<List<CloudAccount>> cloudAccountList() {
        return ResultHolder.success(currentUserResourceService.currentUserCloudAccountList());
    }

    @ApiOperation(value = "云账号资源统计", notes = "云账号资源统计")
    @GetMapping("/cloud_account/resource/count/{currentPage}/{limit}")
    @PreAuthorize("hasAnyCePermission('OVERVIEW:READ')")
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
