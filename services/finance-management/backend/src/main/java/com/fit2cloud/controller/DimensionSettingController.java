package com.fit2cloud.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.AuthorizeResourcesRequest;
import com.fit2cloud.controller.request.NotAuthorizeResourcesRequest;
import com.fit2cloud.controller.response.AuthorizeResourcesResponse;
import com.fit2cloud.dao.entity.BillDimensionSetting;
import com.fit2cloud.dao.jentity.BillAuthorizeRule;
import com.fit2cloud.service.IBillDimensionSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/3  5:48 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@Tag(name = "分账设置相关接口")
@RequestMapping("/api/dimension_setting")
@Validated
public class DimensionSettingController {
    @Resource
    private IBillDimensionSettingService billDimensionSettingService;

    @GetMapping("/authorize_values")
    @Operation(summary = "获取指定授权字段的值", description = "获取指定授权字段的值")
    @PreAuthorize("@cepc.hasAnyCePermission('DIMENSION_SETTING:READ')")
    public ResultHolder<List<DefaultKeyValue<String, String>>> authorizeValues(@RequestParam("authorizeKey") String authorizeKey) {
        List<DefaultKeyValue<String, String>> authorizeValues = billDimensionSettingService.authorizeValues(authorizeKey);
        return ResultHolder.success(authorizeValues);
    }

    @GetMapping("/authorize_keys")
    @Operation(summary = "获取可授权的字段", description = "获取可授权的字段")
    @PreAuthorize("@cepc.hasAnyCePermission('DIMENSION_SETTING:READ')")
    public ResultHolder<List<DefaultKeyValue<String, String>>> authorizeKeys() {
        List<DefaultKeyValue<String, String>> authorizeKeys = billDimensionSettingService.authorizeKeys();
        return ResultHolder.success(authorizeKeys);
    }

    @GetMapping("/{authorize_id}/{type}")
    @Operation(summary = "获取账单授权设置", description = "获取账单授权设置")
    @PreAuthorize("@cepc.hasAnyCePermission('DIMENSION_SETTING:READ')")
    public ResultHolder<BillDimensionSetting> getBillDimensionSetting(@Parameter(description = "授权账号id") @PathVariable("authorize_id") String authorizeId,
                                                                      @Parameter(description = "授权账号类型 组织或者工作空间") @Pattern(regexp = "^(ORGANIZATION|WORKSPACE)$", message = "授权类型只支持WORKSPACE,ORGANIZATION") @PathVariable("type") String type) {
        BillDimensionSetting billDimensionSetting = billDimensionSettingService.getBillDimensionSetting(authorizeId, type);
        return ResultHolder.success(billDimensionSetting);
    }

    @PostMapping("/{authorize_id}/{type}")
    @Operation(summary = "插入或者修改账单授权设置", description = "插入或者修改账单授权设置")
    @PreAuthorize("@cepc.hasAnyCePermission('DIMENSION_SETTING:CREATE')")
    @OperatedLog(resourceType = ResourceTypeEnum.DIMENSION, operated = OperatedTypeEnum.MODIFY,
            content = "#type=='ORGANIZATION'?'账单授权组织['+#authorizeId+']':'账单授权工作空间['+#authorizeId+']'",
            param = "#authorizeRule")
    public ResultHolder<BillDimensionSetting> saveOrUpdate(@Parameter(description = "授权账号id") @PathVariable("authorize_id") String authorizeId,
                                                           @Parameter(description = "授权账号类型 组织或者工作空间") @Pattern(regexp = "ORGANIZATION|WORKSPACE", message = "授权类型只支持WORKSPACE,ORGANIZATION") @PathVariable("type") String type,
                                                           @RequestBody BillAuthorizeRule authorizeRule) {
        BillDimensionSetting billDimensionSetting = billDimensionSettingService.saveOrUpdate(authorizeId, type, authorizeRule);
        return ResultHolder.success(billDimensionSetting);
    }

    @GetMapping("/authorize_resources/{page}/{limit}")
    @Operation(summary = "获取已授权的资源列表", description = "获取已授权的资源列表")
    @PreAuthorize("@cepc.hasAnyCePermission('DIMENSION_SETTING:READ')")
    public ResultHolder<Page<AuthorizeResourcesResponse>> authorizeResources(@Parameter(description = "当前页") @NotNull(message = "当前页必填") @PathVariable("page") Integer page,
                                                                             @Parameter(description = "每页多少条") @NotNull(message = "每页显示多少条必填") @NotNull @PathVariable("limit") Integer limit,
                                                                             AuthorizeResourcesRequest request) {
        return ResultHolder.success(billDimensionSettingService.getAuthorizeResources(page, limit, request));
    }

    @GetMapping("/not_authorize_resources/{page}/{limit}")
    @Operation(summary = "获取未授权资源列表", description = "获取未授权资源列表")
    @PreAuthorize("@cepc.hasAnyCePermission('DIMENSION_SETTING:READ')")
    public ResultHolder<Page<AuthorizeResourcesResponse>> notAuthorizeResources(@Parameter(description = "当前页") @NotNull(message = "当前页必填") @PathVariable("page") Integer page,
                                                                                @Parameter(description = "每页多少条") @NotNull(message = "每页显示多少条必填") @NotNull @PathVariable("limit") Integer limit,
                                                                                NotAuthorizeResourcesRequest request) {
        return ResultHolder.success(billDimensionSettingService.getNotAuthorizeResources(page, limit, request));
    }

    @GetMapping("/authorize")
    @Operation(summary = "授权数据", description = "授权数据")
    @PreAuthorize("@cepc.hasAnyCePermission('DIMENSION_SETTING:EDIT')")
    public ResultHolder<Boolean> authorize(@RequestParam("bill_dimension_setting_id") String billDimensionSettingId,
                                           @RequestParam("month") String month,
                                           @RequestParam("cloudAccountId") String cloudAccountId) {
        billDimensionSettingService.authorize(billDimensionSettingId, month, cloudAccountId);
        return ResultHolder.success(true);
    }

    @GetMapping("/author_edit_setting")
    @Operation(summary = "授权已修改的规则", description = "授权已修改的规则")
    @PreAuthorize("@cepc.hasAnyCePermission('DIMENSION_SETTING:EDIT')")
    public ResultHolder<Boolean> authorEditSetting() {
        billDimensionSettingService.authorEditSetting();
        return ResultHolder.success(true);
    }
}
