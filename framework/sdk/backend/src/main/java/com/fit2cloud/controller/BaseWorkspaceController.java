package com.fit2cloud.controller;

import com.fit2cloud.base.entity.Workspace;
import com.fit2cloud.base.service.IBaseWorkspaceService;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.response.NodeTree;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/workspace")
@Tag(name = "公共工作空间相关接口", description = "公共工作空间相关接口")
@Validated
public class BaseWorkspaceController {
    @Resource
    private IBaseWorkspaceService workspaceService;

    @GetMapping("/tree")
    @Operation(summary = "获取工作空间树,不包含没有工作空间的组织节点")
    public ResultHolder<List<NodeTree>> workspaceTree() {
        return ResultHolder.success(workspaceService.workspaceTree(false));
    }

    @GetMapping("/orgTree")
    @Operation(summary = "获取工作空间树,包含没有工作空间的组织节点")
    public ResultHolder<List<NodeTree>> workspaceOrgTree() {
        return ResultHolder.success(workspaceService.workspaceTree(true));
    }

    @GetMapping("/list")
    @Operation(summary = "获取工作空间列表")
    public ResultHolder<List<Workspace>> workspaces() {
        return ResultHolder.success(workspaceService.list());
    }
}
