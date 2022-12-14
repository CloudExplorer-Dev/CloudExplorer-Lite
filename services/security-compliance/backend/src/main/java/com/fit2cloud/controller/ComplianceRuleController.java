package com.fit2cloud.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.rule.ComplianceRuleRequest;
import com.fit2cloud.controller.request.rule.PageComplianceRuleRequest;
import com.fit2cloud.controller.response.rule.ComplianceRuleResponse;
import com.fit2cloud.controller.response.rule.ComplianceRuleSearchFieldResponse;
import com.fit2cloud.dao.mapper.ComplianceRuleMapper;
import com.fit2cloud.service.IComplianceRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/8  17:37}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@RequestMapping("/api/compliance_rule")
@Validated
@Api("合规规则相关接口")
public class ComplianceRuleController {
    @Resource
    private IComplianceRuleService complianceRuleService;

    @ApiOperation("分页查询合规规则")
    @GetMapping("/{currentPage}/{limit}")
    public ResultHolder<Page<ComplianceRuleResponse>> page(@NotNull(message = "当前页不能为空")
                                                           @Min(message = "当前页不能小于0", value = 1)
                                                           @PathVariable("currentPage")
                                                           Integer currentPage,
                                                           @NotNull(message = "每页大小不能为空")
                                                           @Min(message = "每页大小不能小于1", value = 1)
                                                           @PathVariable("limit")
                                                           Integer limit,
                                                           PageComplianceRuleRequest request) {
        Page<ComplianceRuleResponse> complianceRuleResponsePage = complianceRuleService.page(currentPage, limit, request);
        return ResultHolder.success(complianceRuleResponsePage);
    }


    @ApiOperation("获取可过滤的合规条件维度")
    @GetMapping("/instance_search_field")
    public ResultHolder<List<ComplianceRuleSearchFieldResponse>> listInstanceSearchField(@RequestParam("platform")
                                                                                         String platform,
                                                                                         @RequestParam("resourceType")
                                                                                         String resourceType) {
        return ResultHolder.success(complianceRuleService.listInstanceSearchField(platform, resourceType));
    }


    @ApiOperation("插入合规规则")
    @PostMapping
    public ResultHolder<ComplianceRuleResponse> saveComplianceRule(@Validated(ValidationGroup.SAVE.class)
                                                                   @RequestBody
                                                                   ComplianceRuleRequest complianceRuleRequest) {
        ComplianceRuleResponse complianceRuleResponse = complianceRuleService.save(complianceRuleRequest);
        return ResultHolder.success(complianceRuleResponse);
    }

    @ApiOperation("获取规则实例类型")
    @GetMapping("/resource_type")
    public ResultHolder<List<DefaultKeyValue<String, String>>> listResourceType() {
        List<DefaultKeyValue<String, String>> resourceTypes = complianceRuleService.listResourceType();
        return ResultHolder.success(resourceTypes);
    }

    @PutMapping
    @ApiOperation("修改合规规则")
    public ResultHolder<ComplianceRuleResponse> updateComplianceRule(@Validated(ValidationGroup.UPDATE.class)
                                                                     @RequestBody
                                                                     ComplianceRuleRequest complianceRuleRequest) {
        ComplianceRuleResponse complianceRuleResponse = complianceRuleService.update(complianceRuleRequest);
        return ResultHolder.success(complianceRuleResponse);
    }

    @DeleteMapping("/{compliance_rule_id}")
    @ApiModelProperty("删除合规规则")
    public ResultHolder<Boolean> deleteComplianceRule(@NotNull(message = "规则id不能为空")
                                                      @CustomValidated(mapper = ComplianceRuleMapper.class, handler = ExistHandler.class, field = "id", message = "规则id不存在", exist = false)
                                                      @PathVariable("compliance_rule_id")
                                                      String id) {
        return ResultHolder.success(complianceRuleService.removeById(id));
    }
}
