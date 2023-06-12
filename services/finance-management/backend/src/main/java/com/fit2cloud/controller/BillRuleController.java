package com.fit2cloud.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.constants.BillFieldConstants;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.AddBillRuleRequest;
import com.fit2cloud.controller.request.BillRuleRequest;
import com.fit2cloud.controller.request.UpdateBillRuleRequest;
import com.fit2cloud.dao.entity.BillRule;
import com.fit2cloud.dao.mapper.BillRuleMapper;
import com.fit2cloud.service.IBillRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/29  6:23 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@Tag(name = "账单规则相关接口")
@RequestMapping("/api/bill_rule")
@Validated
public class BillRuleController {
    @Resource
    private IBillRuleService billRuleService;

    @GetMapping("/list")
    @Operation(summary = "查询所有账单规则", description = "查询所有账单规则")
    @PreAuthorize("@cepc.hasAnyCePermission('CUSTOM_BILL:READ','BILL_ViEW:READ')")
    public ResultHolder<List<BillRule>> list() {
        return ResultHolder.success(billRuleService.list());
    }

    @GetMapping("/group_keys")
    @Operation(summary = "获取可分组字段", description = "获取可分组字段")
    @PreAuthorize("@cepc.hasAnyCePermission('CUSTOM_BILL:READ')")
    public ResultHolder<List<DefaultKeyValue<String, String>>> groupKeys() {
        return ResultHolder.success(BillFieldConstants.BILL_FIELD.entrySet().stream().filter(field -> field.getValue().group()).map(field -> new DefaultKeyValue<>(field.getValue().label(), field.getKey())).toList());
    }

    @GetMapping("/group_child_keys")
    @Operation(summary = "获取可分组的子分组", description = "获取可分组的子分组")
    @PreAuthorize("@cepc.hasAnyCePermission('CUSTOM_BILL:READ')")
    public ResultHolder<List<DefaultKeyValue<String, String>>> groupChildKeys(@Parameter(name = "可分租的父级key") @RequestParam("parentKey") String parentKey) {
        return ResultHolder.success(BillFieldConstants.BILL_FIELD.entrySet().stream()
                .filter(field -> field.getValue().group())
                .filter(field -> field.getKey().equals(parentKey))
                .findFirst()
                .map(s -> {
                    try {
                        return s.getValue().childKey().getConstructor().newInstance().childKeys();
                    } catch (Exception e) {
                        throw new Fit2cloudException(ErrorCodeConstants.BILL_RULE_GRT_CHILD_GROUP_KEY.getCode(), ErrorCodeConstants.BILL_RULE_GRT_CHILD_GROUP_KEY.getMessage(new Object[]{e.getMessage()}));
                    }
                }).orElseThrow(() -> new Fit2cloudException(ErrorCodeConstants.BILL_RULE_UNSUPPORTED_GROUP_KEY.getCode(), ErrorCodeConstants.BILL_RULE_UNSUPPORTED_GROUP_KEY.getMessage(new Object[]{parentKey}))));

    }

    @GetMapping("/page/{currentPage}/{limit}")
    @Operation(summary = "分页查询账单规则", description = "分页查询账单规则")
    @PreAuthorize("@cepc.hasAnyCePermission('CUSTOM_BILL:READ')")
    public ResultHolder<Page<BillRule>> page(@NotNull(message = "当前页不能为空") @Min(value = 0, message = "当前页不能小于0") @Parameter(name = "当前页", required = true) @PathVariable("currentPage") Integer currentPage,
                                             @NotNull(message = "每页大小不能为空") @Min(value = 1, message = "每页大小不能小于1") @Parameter(name = "每页显示多少条", required = true) @PathVariable("limit") Integer limit,
                                             BillRuleRequest request) {
        Page<BillRule> page = billRuleService.page(currentPage, limit, request);
        return ResultHolder.success(page);
    }

    @PostMapping
    @Operation(summary = "添加账单规则", description = "添加账单规则")
    @PreAuthorize("@cepc.hasAnyCePermission('CUSTOM_BILL:CREATE')")
    @OperatedLog(resourceType = ResourceTypeEnum.BILL_RULE, operated = OperatedTypeEnum.ADD,
            content = "'添加账单规则['+#request.name+']'",
            param = "#request")
    public ResultHolder<BillRule> add(@Validated(ValidationGroup.SAVE.class) @RequestBody AddBillRuleRequest request) {
        return ResultHolder.success(billRuleService.add(request));
    }

    @PutMapping
    @Operation(summary = "修改账单规则", description = "修改账单规则")
    @PreAuthorize("@cepc.hasAnyCePermission('CUSTOM_BILL:EDIT')")
    @OperatedLog(resourceType = ResourceTypeEnum.BILL_RULE, operated = OperatedTypeEnum.MODIFY,
            resourceId = "#request.id",
            content = "'修改账单规则['+#request.name+']'",
            param = "#request")
    public ResultHolder<BillRule> update(@Validated(ValidationGroup.UPDATE.class) @RequestBody UpdateBillRuleRequest request) {
        return ResultHolder.success(billRuleService.update(request));
    }

    @DeleteMapping("/{bill_rule_id}")
    @Operation(summary = "删除账单规则", description = "删除账单规则")
    @PreAuthorize("@cepc.hasAnyCePermission('CUSTOM_BILL:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.BILL_RULE, operated = OperatedTypeEnum.DELETE,
            resourceId = "#billRuleId",
            content = "'删除账单规则['+#billRuleId+']'",
            param = "#billRuleId")
    public ResultHolder<Boolean> delete(@CustomValidated(mapper = BillRuleMapper.class, handler = ExistHandler.class, field = "id", message = "账单规则id必须存在", exist = false) @PathVariable("bill_rule_id") String billRuleId) {
        return ResultHolder.success(billRuleService.removeById(billRuleId));
    }

}
