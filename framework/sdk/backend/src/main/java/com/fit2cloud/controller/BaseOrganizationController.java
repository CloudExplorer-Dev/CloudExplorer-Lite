package com.fit2cloud.controller;

import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.response.OrganizationTree;
import com.fit2cloud.response.SourceTreeObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/8/24  1:39 PM
 * @Version 1.0
 * @注释: 组织对应接口
 */
@RestController
@RequestMapping("/api/base/organization")
@Api("公共组织相关接口")
@Validated
public class BaseOrganizationController {
    @Resource
    private IBaseOrganizationService organizationService;

    @GetMapping("/tree/{type}")
    @ApiOperation(value = "获取组织树", notes = "获取组织树")
    public ResultHolder<List<OrganizationTree>> tree(@ApiParam(value = "组织类型", example = "ORGANIZATION,WORKSPACE,ORGANIZATION_AND_WORKSPACE") @PathVariable("type") String type) {
        return ResultHolder.success(organizationService.tree(type));
    }

    @GetMapping("/sourceTree")
    @ApiOperation(value = "获取组织架构树", notes = "获取组织架构树")
    public ResultHolder<List<SourceTreeObject>> sourceTree() {
        return ResultHolder.success(organizationService.sourceTree());
    }

}
