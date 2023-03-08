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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/29  6:23 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@Api("账单规则相关接口")
@RequestMapping("/api/bill_rule")
@Validated
public class BillRuleController {
    @Resource
    private IBillRuleService billRuleService;

    @GetMapping("/list")
    @ApiOperation(value = "查询所有账单规则", notes = "查询所有账单规则")
    @PreAuthorize("hasAnyCePermission('CUSTOM_BILL:READ','BILL_ViEW:READ')")
    public ResultHolder<List<BillRule>> list() {
        return ResultHolder.success(billRuleService.list());
    }

    @GetMapping("/group_keys")
    @ApiOperation(value = "获取可分组字段", notes = "获取可分组字段")
    @Cacheable(value = "bill_rule", keyGenerator = "notAuthKeyGenerator")
    @PreAuthorize("hasAnyCePermission('CUSTOM_BILL:READ')")
    public ResultHolder<List<DefaultKeyValue<String, String>>> groupKeys() {
        return ResultHolder.success(BillFieldConstants.BILL_FIELD.entrySet().stream().filter(field -> field.getValue().group()).map(field -> new DefaultKeyValue<>(field.getValue().label(), field.getKey())).toList());
    }

    @GetMapping("/group_child_keys")
    @ApiOperation(value = "获取可分组的子分组", notes = "获取可分组的子分组")
    @Cacheable(value = "bill_rule", keyGenerator = "notAuthKeyGenerator")
    @PreAuthorize("hasAnyCePermission('CUSTOM_BILL:READ')")
    public ResultHolder<List<DefaultKeyValue<String, String>>> groupChildKeys(@ApiParam("可分租的父级key") @RequestParam("parentKey") String parentKey) {
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
    @ApiOperation(value = "分页查询账单规则", notes = "分页查询账单规则")
    @PreAuthorize("hasAnyCePermission('CUSTOM_BILL:READ')")
    public ResultHolder<Page<BillRule>> page(@NotNull(message = "当前页不能为空") @Min(value = 0, message = "当前页不能小于0") @ApiParam(value = "当前页", required = true) @PathVariable("currentPage") Integer currentPage,
                                             @NotNull(message = "每页大小不能为空") @Min(value = 1, message = "每页大小不能小于1") @ApiParam(value = "每页显示多少条", required = true) @PathVariable("limit") Integer limit,
                                             BillRuleRequest request) {
        Page<BillRule> page = billRuleService.page(currentPage, limit, request);
        return ResultHolder.success(page);
    }

    @PostMapping
    @ApiOperation(value = "添加账单规则", notes = "添加账单规则")
    @PreAuthorize("hasAnyCePermission('CUSTOM_BILL:CREATE')")
    @OperatedLog(resourceType = ResourceTypeEnum.BILL_RULE, operated = OperatedTypeEnum.ADD,
            content = "'添加账单规则['+#request.name+']'",
            param = "#request")
    public ResultHolder<BillRule> add(@Validated(ValidationGroup.SAVE.class) @RequestBody AddBillRuleRequest request) {
        return ResultHolder.success(billRuleService.add(request));
    }

    @PutMapping
    @CacheEvict(value = "bill_view", allEntries = true)
    @ApiOperation(value = "修改账单规则", notes = "修改账单规则")
    @PreAuthorize("hasAnyCePermission('CUSTOM_BILL:EDIT')")
    @OperatedLog(resourceType = ResourceTypeEnum.BILL_RULE, operated = OperatedTypeEnum.MODIFY,
            resourceId = "#request.id",
            content = "'修改账单规则['+#request.name+']'",
            param = "#request")
    public ResultHolder<BillRule> update(@Validated(ValidationGroup.UPDATE.class) @RequestBody UpdateBillRuleRequest request) {
        return ResultHolder.success(billRuleService.update(request));
    }

    @DeleteMapping("/{bill_rule_id}")
    @ApiOperation(value = "删除账单规则", notes = "删除账单规则")
    @PreAuthorize("hasAnyCePermission('CUSTOM_BILL:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.BILL_RULE, operated = OperatedTypeEnum.DELETE,
            resourceId = "#billRuleId",
            content = "'删除账单规则['+#billRuleId+']'",
            param = "#billRuleId")
    public ResultHolder<Boolean> delete(@CustomValidated(mapper = BillRuleMapper.class, handler = ExistHandler.class, field = "id", message = "账单规则id必须存在", exist = false) @PathVariable("bill_rule_id") String billRuleId) {
        return ResultHolder.success(billRuleService.removeById(billRuleId));
    }

}
