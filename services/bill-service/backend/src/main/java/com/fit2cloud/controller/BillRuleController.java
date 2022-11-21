package com.fit2cloud.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.constants.BillFieldConstants;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.AddBillRuleRequest;
import com.fit2cloud.controller.request.BillRuleRequest;
import com.fit2cloud.controller.request.UpdateBillRuleRequest;
import com.fit2cloud.dao.entity.BillRule;
import com.fit2cloud.dao.mapper.BillRuleMapper;
import com.fit2cloud.service.IBillRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    public ResultHolder<List<BillRule>> list() {
        return ResultHolder.success(billRuleService.list());
    }

    @GetMapping("/group_keys")
    public ResultHolder<List<DefaultKeyValue<String, String>>> groupKeys() {
        return ResultHolder.success(BillFieldConstants.BILL_FIELD.entrySet().stream().filter(field -> field.getValue().group()).map(field -> new DefaultKeyValue<>(field.getValue().label(), field.getKey())).toList());
    }

    @GetMapping("/group_child_keys")
    public ResultHolder<List<DefaultKeyValue<String, String>>> groupChildKeys(@ApiParam("可分租的父级key") @RequestParam("parentKey") String parentKey) {
        return ResultHolder.success(BillFieldConstants.BILL_FIELD.entrySet().stream()
                .filter(field -> field.getValue().group())
                .filter(field -> field.getKey().equals(parentKey))
                .findFirst()
                .map(s -> {
                    try {
                        return s.getValue().childKey().getConstructor().newInstance().childKeys();
                    } catch (Exception e) {
                        throw new Fit2cloudException(1112, "获取子字段发生异常:" + e.getMessage());
                    }
                }).orElseThrow(() -> new Fit2cloudException(1111, "不支持的key")));

    }

    @GetMapping("/page/{currentPage}/{limit}")
    public ResultHolder<Page<BillRule>> page(@ApiParam(value = "当前页", required = true) @PathVariable("currentPage") Integer currentPage,
                                             @ApiParam(value = "每页显示多少条", required = true) @PathVariable("limit") Integer limit,
                                             BillRuleRequest request) {
        Page<BillRule> page = billRuleService.page(currentPage, limit, request);
        return ResultHolder.success(page);
    }

    @PostMapping
    public ResultHolder<BillRule> add(@Validated(ValidationGroup.SAVE.class) @RequestBody AddBillRuleRequest request) {
        return ResultHolder.success(billRuleService.add(request));
    }

    @PutMapping
    public ResultHolder<BillRule> update(@Validated @RequestBody UpdateBillRuleRequest request) {
        return ResultHolder.success(billRuleService.update(request));
    }

    @DeleteMapping("/{bill_rule_id}")
    public ResultHolder<Boolean> delete(@CustomValidated(mapper = BillRuleMapper.class, handler = ExistHandler.class, message = "账单规则id必须存在", exist = false) @PathVariable("bill_rule_id") String billRuleId) {
        return ResultHolder.success(billRuleService.removeById(billRuleId));
    }

}
