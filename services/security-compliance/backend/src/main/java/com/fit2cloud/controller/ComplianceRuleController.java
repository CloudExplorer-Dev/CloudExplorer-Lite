package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.rule.ComplianceRuleRequest;
import com.fit2cloud.controller.request.rule.PageComplianceRuleRequest;
import com.fit2cloud.controller.response.compliance_scan.SupportPlatformResourceResponse;
import com.fit2cloud.controller.response.rule.ComplianceRuleResponse;
import com.fit2cloud.controller.response.rule.ComplianceRuleSearchFieldResponse;
import com.fit2cloud.dao.entity.ComplianceRule;
import com.fit2cloud.dao.mapper.ComplianceRuleMapper;
import com.fit2cloud.service.IComplianceRuleService;
import io.swagger.v3.oas.annotations.Operation;
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
 * {@code @Date: 2022/12/8  17:37}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@RequestMapping("/api/compliance_rule")
@Validated
@Tag(name = "合规规则相关接口", description = "合规规则相关接口")
public class ComplianceRuleController {
    @Resource
    private IComplianceRuleService complianceRuleService;

    @Operation(summary = "分页查询合规规则")
    @GetMapping("/{currentPage}/{limit}")
    @PreAuthorize("@cepc.hasAnyCePermission('RULE:READ')")
    public ResultHolder<IPage<ComplianceRuleResponse>> page(@NotNull(message = "当前页不能为空")
                                                            @Min(message = "当前页不能小于0", value = 1)
                                                            @PathVariable("currentPage")
                                                            Integer currentPage,
                                                            @NotNull(message = "每页大小不能为空")
                                                            @Min(message = "每页大小不能小于1", value = 1)
                                                            @PathVariable("limit")
                                                            Integer limit,
                                                            PageComplianceRuleRequest request) {
        IPage<ComplianceRuleResponse> complianceRuleResponsePage = complianceRuleService.page(currentPage, limit, request);
        return ResultHolder.success(complianceRuleResponsePage);
    }

    @Operation(summary = "根据规则id获取合规规则")
    @GetMapping("/{complianceRuleId}")
    @PreAuthorize("@cepc.hasAnyCePermission('RULE:READ','SCAN:READ')")
    public ResultHolder<ComplianceRuleResponse> one(@PathVariable("complianceRuleId") String complianceRuleId) {
        ComplianceRule complianceRule = complianceRuleService.getById(complianceRuleId);
        return ResultHolder.success(new ComplianceRuleResponse(complianceRule));
    }

    @Operation(summary = "获取可过滤的合规条件维度")
    @GetMapping("/instance_search_field")
    @PreAuthorize("@cepc.hasAnyCePermission('RULE:READ')")
    public ResultHolder<List<ComplianceRuleSearchFieldResponse>> listInstanceSearchField(@RequestParam("platform")
                                                                                         String platform,
                                                                                         @RequestParam("resourceType")
                                                                                         String resourceType) {
        return ResultHolder.success(complianceRuleService.listInstanceSearchField(platform, resourceType));
    }


    @Operation(summary = "插入合规规则")
    @PostMapping
    @PreAuthorize("@cepc.hasAnyCePermission('RULE:CREATE')")
    @OperatedLog(resourceType = ResourceTypeEnum.COMPLIANCE_RULE, operated = OperatedTypeEnum.ADD,
            resourceId = "#complianceRuleRequest.id",
            content = "'创建合规规则['+#complianceRuleRequest.name+']'",
            param = "#complianceRuleRequest")
    public ResultHolder<ComplianceRuleResponse> saveComplianceRule(@Validated(ValidationGroup.SAVE.class)
                                                                   @RequestBody
                                                                   ComplianceRuleRequest complianceRuleRequest) {
        ComplianceRuleResponse complianceRuleResponse = complianceRuleService.save(complianceRuleRequest);
        return ResultHolder.success(complianceRuleResponse);
    }

    @Operation(summary = "获取规则实例类型")
    @GetMapping("/resource_type")
    @PreAuthorize("@cepc.hasAnyCePermission('RULE:READ','SCAN:READ')")
    public ResultHolder<List<DefaultKeyValue<String, String>>> listResourceType() {
        List<DefaultKeyValue<String, String>> resourceTypes = complianceRuleService.listResourceType();
        return ResultHolder.success(resourceTypes);
    }

    @PutMapping
    @Operation(summary = "修改合规规则")
    @PreAuthorize("@cepc.hasAnyCePermission('RULE:EDIT')")
    @OperatedLog(resourceType = ResourceTypeEnum.COMPLIANCE_RULE, operated = OperatedTypeEnum.MODIFY,
            resourceId = "#complianceRuleRequest.id",
            content = "#complianceRuleRequest.name!=null?'修改合规规则['+#complianceRuleRequest.name+']':#complianceRuleRequest.enable?'修改合规规则为[启用]':'修改合规规则为[禁用]'",
            param = "#complianceRuleRequest")
    public ResultHolder<ComplianceRuleResponse> updateComplianceRule(@Validated(ValidationGroup.UPDATE.class)
                                                                     @RequestBody
                                                                     ComplianceRuleRequest complianceRuleRequest) {
        ComplianceRuleResponse complianceRuleResponse = complianceRuleService.update(complianceRuleRequest);
        return ResultHolder.success(complianceRuleResponse);
    }

    @DeleteMapping("/{compliance_rule_id}")
    @Operation(summary = "删除合规规则")
    @PreAuthorize("@cepc.hasAnyCePermission('RULE:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.COMPLIANCE_RULE, operated = OperatedTypeEnum.DELETE,
            resourceId = "#id",
            content = "'删除合规规则['+#id+']'",
            param = "#id")
    public ResultHolder<Boolean> deleteComplianceRule(@NotNull(message = "规则id不能为空")
                                                      @CustomValidated(mapper = ComplianceRuleMapper.class, handler = ExistHandler.class, field = "id", message = "规则id不存在", exist = false)
                                                      @PathVariable("compliance_rule_id")
                                                      String id) {
        complianceRuleService.remove(id);
        return ResultHolder.success(true);
    }

    @Operation(summary = "获取支持的云平台以及对应的资源")
    @GetMapping("/support_platform")
    @PreAuthorize("@cepc.hasAnyCePermission('RULE:READ')")
    public ResultHolder<List<SupportPlatformResourceResponse>> listSupportPlatformResource() {
        List<SupportPlatformResourceResponse> list = complianceRuleService.listSupportPlatformResource();
        return ResultHolder.success(list);
    }
}
