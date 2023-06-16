package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.common.event.annotaion.Emit;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.workspace.PageWorkspaceRequest;
import com.fit2cloud.controller.request.workspace.WorkspaceBatchCreateRequest;
import com.fit2cloud.controller.request.workspace.WorkspaceRequest;
import com.fit2cloud.dao.entity.Workspace;
import com.fit2cloud.dto.WorkspaceDTO;
import com.fit2cloud.service.IWorkspaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jianneng
 * @date 2022/8/30 16:15
 **/
@Validated
@RestController
@RequestMapping("api/workspace")
@Tag(name = "工作空间接口")
public class WorkspaceController {

    @Resource
    private IWorkspaceService workspaceService;

    @Operation(summary = "分页查询工作空间", description = "分页查询工作空间")
    @GetMapping("/page")
    @PreAuthorize("@cepc.hasAnyCePermission('WORKSPACE:READ')")
    public ResultHolder<IPage<WorkspaceDTO>> listByPage(@Validated PageWorkspaceRequest pageWorkspaceRequest) {
        return ResultHolder.success(workspaceService.pageWorkspace(pageWorkspaceRequest));
    }

    @Operation(summary = "查询工作空间数量", description = "查询工作空间数量")
    @GetMapping("/count")
    @PreAuthorize("@cepc.hasAnyCePermission('WORKSPACE:READ')")
    public ResultHolder<Long> count() {
        return ResultHolder.success(workspaceService.countWorkspace());
    }

    @Operation(summary = "创建工作空间", description = "创建工作空间")
    @PostMapping("/create")
    @PreAuthorize("@cepc.hasAnyCePermission('WORKSPACE:CREATE')")
    @OperatedLog(resourceType = ResourceTypeEnum.WORKSPACE, operated = OperatedTypeEnum.ADD,
            content = "'创建了名为'+#workspaceRequest.name+'的工作空间'",
            param = "#workspaceRequest")
    @Emit(value = "CREATE::WORKSPACE", el = "#workspaceRequest.id")
    public ResultHolder<Boolean> create(
            @RequestBody
            @Validated(ValidationGroup.SAVE.class) WorkspaceRequest workspaceRequest) {
        Boolean result = workspaceService.create(workspaceRequest);
        return ResultHolder.success(result);
    }

    @Operation(summary = "编辑工作空间", description = "编辑工作空间")
    @PutMapping("/update")
    @PreAuthorize("@cepc.hasAnyCePermission('WORKSPACE:EDIT')")
    @OperatedLog(resourceType = ResourceTypeEnum.WORKSPACE, operated = OperatedTypeEnum.MODIFY,
            resourceId = "#workspaceRequest.id",
            content = "'更新了['+#workspaceRequest.name+']'",
            param = "#workspaceRequest")
    @Emit(value = "UPDATE::WORKSPACE", el = "#workspaceRequest.id")
    public ResultHolder<Boolean> update(
            @RequestBody
            @Validated(ValidationGroup.UPDATE.class) WorkspaceRequest workspaceRequest) {
        return ResultHolder.success(workspaceService.update(workspaceRequest));
    }

    @Operation(summary = "根据ID或者名称获取一个工作空间", description = "根据ID或者名称获取一个工作空间")
    @GetMapping("/one")
    public ResultHolder<WorkspaceDTO> one(
            @Parameter(description = "工作空间ID")
            @RequestParam("id") String id,
            @Parameter(description = "工作空间名称")
            @RequestParam("name") String name) {
        return ResultHolder.success(workspaceService.getOne(id, name));
    }

    @Operation(summary = "删除一个工作空间", description = "删除一个工作空间")
    @DeleteMapping("/{workspaceId}")
    @OperatedLog(resourceType = ResourceTypeEnum.WORKSPACE, operated = OperatedTypeEnum.DELETE,
            resourceId = "#workspaceId",
            content = "'删除工作空间'",
            param = "#workspaceId")
    @PreAuthorize("@cepc.hasAnyCePermission('WORKSPACE:DELETE')")
    @Emit(value = "DELETE::WORKSPACE")
    public ResultHolder<Boolean> delete(
            @Parameter(description = "工作空间ID")
            @NotNull(message = "{i18n.workspace.id.is.not.empty}")
            @PathVariable("workspaceId") String workspaceId) {
        return ResultHolder.success(workspaceService.delete(workspaceId));
    }

    @Operation(summary = "批量删除工作空间", description = "批量删除工作空间")
    @DeleteMapping
    @PreAuthorize("@cepc.hasAnyCePermission('WORKSPACE:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.WORKSPACE, operated = OperatedTypeEnum.BATCH_DELETE,
            resourceId = "#workspaces.![id]",
            content = "'批量删除了'+#workspaces.size+'个工作空间'",
            param = "#workspaces")
    @Emit(value = "DELETE_BATCH::WORKSPACE", el = "#arrayOf(#workspaces).map(\"#root.id\")")
    public ResultHolder<Boolean> batchDelete(
            @Parameter(description = "批量删除工作空间")
            @Size(min = 1, message = "{i18n.workspace.is.required}")
            @RequestBody ArrayList<Workspace> workspaces) {
        return ResultHolder.success(workspaceService.batchDelete(workspaces));
    }

    @Operation(summary = "批量添加工作空间", description = "批量添加工作空间")
    @PostMapping("/batch")
    @OperatedLog(resourceType = ResourceTypeEnum.WORKSPACE, operated = OperatedTypeEnum.BATCH_ADD,
            content = "'批量创建了'+#request.workspaceDetails.size+'个工作空间['+#request.workspaceDetails.![name]+']'",
            param = "#request")
    @PreAuthorize("@cepc.hasAnyCePermission('WORKSPACE:CREATE')")
    @Emit(value = "CREATE_BATCH::WORKSPACE", el = "#arrayOf(#result.data).map(\"#root.id\")")
    public ResultHolder<List<Workspace>> batch(@RequestBody @Validated(ValidationGroup.SAVE.class) WorkspaceBatchCreateRequest request) {
        List<Workspace> batch = workspaceService.batch(request);
        return ResultHolder.success(batch);
    }


}
