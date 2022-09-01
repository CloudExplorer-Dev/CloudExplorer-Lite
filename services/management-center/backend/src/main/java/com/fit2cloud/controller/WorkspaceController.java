package com.fit2cloud.controller;

import com.fit2cloud.base.mapper.BaseOrganizationMapper;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dao.entity.Workspace;
import com.fit2cloud.dto.WorkspaceDTO;
import com.fit2cloud.request.PageWorkspaceRequest;
import com.fit2cloud.request.WorkspaceRequest;
import com.fit2cloud.service.IWorkspaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;

/**
 * @author jianneng
 * @date 2022/8/30 16:15
 **/
@Validated
@RestController
@RequestMapping("workspace")
@Api("工作空间接口")
public class WorkspaceController {

    @Autowired
    private IWorkspaceService workspaceService;

    @ApiOperation(value="分页查询工作空间",notes = "分页查询工作空间")
    @GetMapping("/list")
    public ResultHolder<Object> listByPage(
            @Validated PageWorkspaceRequest pageWorkspaceRequest) {
        return ResultHolder.success(workspaceService.pageWorkspace(pageWorkspaceRequest));
    }

    @ApiOperation(value = "创建工作空间", notes = "创建工作空间")
    @PostMapping("/create")
    public ResultHolder<Boolean> create(
            @RequestBody
            @Validated(ValidationGroup.SAVE.class) WorkspaceRequest workspaceRequest){
        Boolean result = workspaceService.create(workspaceRequest);
        return ResultHolder.success(result);
    }

    @ApiOperation(value = "修改工作空间",notes = "修改工作空间")
    @PutMapping("/update")
    public ResultHolder<Boolean> update(
            @RequestBody
            @Validated(ValidationGroup.UPDATE.class) WorkspaceRequest workspaceRequest){
        return ResultHolder.success(workspaceService.update(workspaceRequest));
    }

    @ApiOperation(value = "根据ID或者名称获取一个工作空间",notes = "根据ID或者名称获取一个工作空间")
    @GetMapping("/one")
    public ResultHolder<WorkspaceDTO> one(
            @ApiParam("工作空间ID")
            @RequestParam("id") String id,
            @ApiParam("工作空间名称")
            @RequestParam("name") String name){
        return ResultHolder.success(workspaceService.getOne(id,name));
    }

    @ApiOperation(value = "删除一个工作空间",notes = "删除一个工作空间")
    @DeleteMapping("/{workspaceId}")
    public ResultHolder<Boolean> delete(
            @ApiParam("工作空间ID")
            @NotNull(message = "工作空间ID不能为空")
            @CustomValidated(
                    mapper = BaseOrganizationMapper.class,
                    handler = ExistHandler.class,
                    message = "工作空间不存在",
                    exist = false)
            @PathVariable("workspaceId") String workspaceId){
            return ResultHolder.success(workspaceService.removeById(workspaceId));
    }

    @ApiOperation(value = "批量删除工作空间",notes = "批量删除工作空间")
    @DeleteMapping
    public ResultHolder<Boolean> batchDelete(
            @ApiParam("批量删除工作空间")
            @Size(min = 1, message = "必须传入一个工作空间")
            @RequestBody ArrayList<Workspace> workspaces){
        return ResultHolder.success(workspaceService.batchDelete(workspaces));
    }


}
