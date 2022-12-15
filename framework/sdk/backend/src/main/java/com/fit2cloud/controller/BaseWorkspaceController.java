package com.fit2cloud.controller;

import com.fit2cloud.base.service.IBaseWorkspaceService;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.response.NodeTree;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/workspace")
@Api("公共工作空间相关接口")
@Validated
public class BaseWorkspaceController {
    @Resource
    private IBaseWorkspaceService workspaceService;

    @GetMapping("/tree")
    @ApiOperation(value = "获取工作空间树")
    public ResultHolder<List<NodeTree>> workspaceTree() {
        return ResultHolder.success(workspaceService.workspaceTree());
    }
}
