package com.fit2cloud.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.rule_group.ComplianceRuleGroupRequest;
import com.fit2cloud.controller.request.rule_group.PageComplianceRuleGroupRequest;
import com.fit2cloud.controller.response.rule_group.ComplianceRuleGroupResponse;
import com.fit2cloud.dao.entity.ComplianceRuleGroup;
import com.fit2cloud.dao.mapper.ComplianceRuleGroupMapper;
import com.fit2cloud.service.IComplianceRuleGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/8  17:38}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@RequestMapping("/api/compliance_rule_group")
@Validated
@Api(value = "合规规则组相关接口", tags = "合规规则组相关接口")
public class ComplianceRuleGroupController {
    @Resource
    private IComplianceRuleGroupService complianceRuleGroupService;

    @GetMapping
    @ApiOperation("获取所有规则组")
    @PreAuthorize("hasAnyCePermission('RULE:READ')")
    public ResultHolder<List<ComplianceRuleGroupResponse>> list() {
        return ResultHolder.success(complianceRuleGroupService.list().stream().map(item -> {
            ComplianceRuleGroupResponse complianceRuleGroupResponse = new ComplianceRuleGroupResponse();
            BeanUtils.copyProperties(item, complianceRuleGroupResponse);
            return complianceRuleGroupResponse;
        }).toList());
    }

    @GetMapping("/{complianceRuleGroupId}")
    @ApiOperation("根据合规规则组id获取规则组信息")
    @PreAuthorize("hasAnyCePermission('RULE:READ')")
    public ResultHolder<ComplianceRuleGroupResponse> one(@PathVariable("complianceRuleGroupId") String complianceRuleGroupId) {
        ComplianceRuleGroup complianceRuleGroup = complianceRuleGroupService.getById(complianceRuleGroupId);
        ComplianceRuleGroupResponse complianceRuleGroupResponse = new ComplianceRuleGroupResponse();
        BeanUtils.copyProperties(complianceRuleGroup, complianceRuleGroupResponse);
        return ResultHolder.success(complianceRuleGroupResponse);
    }

    @GetMapping("/{currentPage}/{limit}")
    @ApiOperation(value = "分页查询合规规则组", notes = "分页查询规格规则组")
    @PreAuthorize("hasAnyCePermission('RULE:READ')")
    public ResultHolder<Page<ComplianceRuleGroupResponse>> page(@NotNull(message = "当前页不能为空")
                                                                @Min(message = "当前页不能小于0", value = 1)
                                                                @PathVariable("currentPage")
                                                                Integer currentPage,
                                                                @NotNull(message = "每页大小不能为空")
                                                                @Min(message = "每页大小不能小于1", value = 1)
                                                                @PathVariable("limit")
                                                                Integer limit,
                                                                PageComplianceRuleGroupRequest request) {
        return ResultHolder.success(complianceRuleGroupService.page(currentPage, limit, request));
    }

    @PostMapping
    @ApiOperation(value = "插入合规规则组", notes = "插入合规规则组")
    @PreAuthorize("hasAnyCePermission('RULE:CREATE')")
    @OperatedLog(resourceType = ResourceTypeEnum.COMPLIANCE_RULE_GROUP, operated = OperatedTypeEnum.ADD,
            resourceId = "#request.id",
            content = "'创建合规规则组['+#request.name+']'",
            param = "#request")
    public ResultHolder<ComplianceRuleGroupResponse> save(@Validated(ValidationGroup.SELECT.class)
                                                          @RequestBody ComplianceRuleGroupRequest request) {
        ComplianceRuleGroupResponse complianceRuleGroupResponse = complianceRuleGroupService.save(request);
        return ResultHolder.success(complianceRuleGroupResponse);
    }

    @PutMapping
    @ApiOperation(value = "修改合规规则组", notes = "修改合规规则组")
    @PreAuthorize("hasAnyCePermission('RULE:EDIT')")
    @OperatedLog(resourceType = ResourceTypeEnum.COMPLIANCE_RULE_GROUP, operated = OperatedTypeEnum.MODIFY,
            resourceId = "#request.id",
            content = "'修改合规规则组['+#request.name+']'",
            param = "#request")
    public ResultHolder<ComplianceRuleGroupResponse> update(@Validated(ValidationGroup.UPDATE.class)
                                                            @RequestBody ComplianceRuleGroupRequest request) {
        ComplianceRuleGroupResponse complianceRuleGroupResponse = complianceRuleGroupService.update(request);
        return ResultHolder.success(complianceRuleGroupResponse);
    }

    @ApiOperation(value = "删除规则组", notes = "删除规则组根据规则组id")
    @DeleteMapping("/{compliance_rule_group_id}")
    @PreAuthorize("hasAnyCePermission('RULE:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.COMPLIANCE_RULE_GROUP, operated = OperatedTypeEnum.DELETE,
            resourceId = "#complianceRuleGroupId",
            content = "'删除合规规则组['+#complianceRuleGroupId+']'",
            param = "#complianceRuleGroupId")
    public ResultHolder<Boolean> deleteById(@NotNull(message = "规则组id不能为空")
                                            @CustomValidated(mapper = ComplianceRuleGroupMapper.class, handler = ExistHandler.class, field = "id", message = "规则组id不存在", exist = false)
                                            @PathVariable("compliance_rule_group_id")
                                            String complianceRuleGroupId) {
        return ResultHolder.success(complianceRuleGroupService.deleteById(complianceRuleGroupId));
    }
}
