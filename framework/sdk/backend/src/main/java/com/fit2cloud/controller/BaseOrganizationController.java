package com.fit2cloud.controller;

import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dto.UserRoleDto;
import com.fit2cloud.response.OrganizationTree;
import com.fit2cloud.response.SourceTreeObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/8/24  1:39 PM
 * @Version 1.0
 * @注释: 组织对应接口
 */
@RestController
@RequestMapping("/api/organization")
@Tag(name = "公共组织相关接口", description = "公共组织相关接口")
@Validated
public class BaseOrganizationController {
    @Resource
    private IBaseOrganizationService organizationService;

    @GetMapping("/tree/{type}")
    @Operation(summary = "获取组织树", description = "获取组织树")
    public ResultHolder<List<OrganizationTree>> tree(@Parameter(name = "组织类型", example = "ORGANIZATION,WORKSPACE,ORGANIZATION_AND_WORKSPACE") @PathVariable("type") String type) {
        return ResultHolder.success(organizationService.tree(type));
    }

    @GetMapping("/sourceTree")
    @Operation(summary = "获取组织架构树", description = "获取组织架构树")
    public ResultHolder<List<SourceTreeObject>> sourceTree() {
        Map<RoleConstants.ROLE, List<UserRoleDto>> roleListMap = CurrentUserUtils.getUser().getRoleMap();
        return ResultHolder.success(organizationService.sourceTree(roleListMap));
    }

    @GetMapping("/sourceIdNames")
    @Operation(summary = "获取组织工作空间id名称映射", description = "获取组织工作空间id名称映射")
    public ResultHolder<Map<String, String>> sourceIdNameMap() {
        return ResultHolder.success(organizationService.sourceIdNameMap());
    }

    @GetMapping("/idFullNames")
    @Operation(summary = "获取组织id名称映射", description = "获取组织id名称映射")
    public ResultHolder<Map<String, String>> idFullNameMap() {
        return ResultHolder.success(organizationService.idFullNameMap());
    }

}
