package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.recycle_bin.BatchRecycleRequest;
import com.fit2cloud.controller.request.recycle_bin.PageRecycleBinRequest;
import com.fit2cloud.controller.request.recycle_bin.RecycleRequest;
import com.fit2cloud.dto.RecycleBinDTO;
import com.fit2cloud.service.IRecycleBinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
 * @author : LiuDi
 * @date : 2023/1/12 14:32
 */
@RestController
@RequestMapping("/api/vm/recycleBin")
@Validated
@Tag(name="云主机模块回收站相关接口")
public class RecycleBinController {
    @Resource
    private IRecycleBinService recycleBinService;

    @Operation(summary = "分页查询回收站信息", description = "分页查询回收站信息")
    @GetMapping("/page")
    @PreAuthorize("@cepc.hasAnyCePermission('RECYCLE_BIN:READ')")
    public ResultHolder<IPage<RecycleBinDTO>> list(@Validated PageRecycleBinRequest pageRecycleBinRequest) {
        return ResultHolder.success(recycleBinService.pageRecycleBin(pageRecycleBinRequest));
    }

    @Operation(summary = "批量删除资源", description = "批量删除资源")
    @PostMapping("batchDeleteResource")
    @PreAuthorize("@cepc.hasAnyCePermission('RECYCLE_BIN:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.RECYCLE, operated = OperatedTypeEnum.BATCH_DELETE,
            resourceId = "#request.recycleIds",
            content = "'批量彻底删除['+#request.recycleIds.size+']个资源'",
            param = "#request")
    public ResultHolder<Boolean> batchDeleteResource(@RequestBody BatchRecycleRequest request) {
        return ResultHolder.success(recycleBinService.batchDeleteResource(request));
    }

    @Operation(summary = "批量删除云主机", description = "批量删除云主机")
    @PostMapping("batchDeleteVm")
    @PreAuthorize("@cepc.hasAnyCePermission('RECYCLE_BIN:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.BATCH_DELETE,
            resourceId = "#request.resourceIds",
            content = "'批量彻底删除['+#request.resourceIds.size+']个云主机'",
            param = "#request")
    public ResultHolder<Boolean> batchDeleteVm(@RequestBody BatchRecycleRequest request) {
        return ResultHolder.success(recycleBinService.batchDeleteResource(request));
    }

    @Operation(summary = "批量删除磁盘", description = "批量删除磁盘")
    @PostMapping("batchDeleteDisk")
    @PreAuthorize("@cepc.hasAnyCePermission('RECYCLE_BIN:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.BATCH_DELETE,
            resourceId = "#request.resourceIds",
            content = "'批量彻底删除['+#request.resourceIds.size+']个磁盘'",
            param = "#request")
    public ResultHolder<Boolean> batchDeleteDisk(@RequestBody BatchRecycleRequest request) {
        return ResultHolder.success(recycleBinService.batchDeleteResource(request));
    }

    @Operation(summary = "批量恢复资源", description = "批量恢复资源")
    @PostMapping("batchRecoverResource")
    @PreAuthorize("@cepc.hasAnyCePermission('RECYCLE_BIN:RECOVER')")
    @OperatedLog(resourceType = ResourceTypeEnum.RECYCLE, operated = OperatedTypeEnum.BATCH_RECOVER,
            resourceId = "#request.recycleIds",
            content = "'批量恢复资源['+#request.recycleIds.size+']'",
            param = "#request")
    public ResultHolder<Boolean> batchRecoverResource(@RequestBody BatchRecycleRequest request) {
        return ResultHolder.success(recycleBinService.batchRecoverResource(request));
    }

    @Operation(summary = "批量恢复云主机", description = "批量恢复云主机")
    @PostMapping("batchRecoverVm")
    @PreAuthorize("@cepc.hasAnyCePermission('RECYCLE_BIN:RECOVER')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.BATCH_RECOVER,
            resourceId = "#request.resourceIds",
            content = "'批量恢复['+#request.resourceIds.size+']个云主机'",
            param = "#request")
    public ResultHolder<Boolean> batchRecoverVm(@RequestBody BatchRecycleRequest request) {
        return ResultHolder.success(recycleBinService.batchRecoverResource(request));
    }

    @Operation(summary = "批量恢复磁盘", description = "批量恢复磁盘")
    @PostMapping("batchRecoverDisk")
    @PreAuthorize("@cepc.hasAnyCePermission('RECYCLE_BIN:RECOVER')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.BATCH_RECOVER,
            resourceId = "#request.resourceIds",
            content = "'批量恢复['+#request.resourceIds.size+']个磁盘'",
            param = "#request")
    public ResultHolder<Boolean> batchRecoverDisk(@RequestBody BatchRecycleRequest request) {
        return ResultHolder.success(recycleBinService.batchRecoverResource(request));
    }

    @Operation(summary = "删除单个资源", description = "删除单个资源")
    @PostMapping("deleteResource/{recycleId}")
    @PreAuthorize("@cepc.hasAnyCePermission('RECYCLE_BIN:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.RECYCLE, operated = OperatedTypeEnum.DELETE,
            resourceId = "#recycleId",
            content = "'删除单个资源'",
            param = "#recycleId")
    public ResultHolder<Boolean> deleteResource(@PathVariable String recycleId) {
        return ResultHolder.success(recycleBinService.deleteResource(recycleId));
    }

    @Operation(summary = "删除云主机", description = "删除云主机")
    @PostMapping("deleteVm")
    @PreAuthorize("@cepc.hasAnyCePermission('RECYCLE_BIN:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.DELETE,
            resourceId = "#request.resourceId",
            param = "#request")
    public ResultHolder<Boolean> deleteVm(@RequestBody RecycleRequest request) {
        return ResultHolder.success(recycleBinService.deleteResource(request.getId()));
    }

    @Operation(summary = "删除磁盘", description = "删除磁盘")
    @PostMapping("deleteDisk")
    @PreAuthorize("@cepc.hasAnyCePermission('RECYCLE_BIN:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.DELETE,
            resourceId = "#request.resourceId",
            param = "#request")
    public ResultHolder<Boolean> deleteDisk(@RequestBody RecycleRequest request) {
        return ResultHolder.success(recycleBinService.deleteResource(request.getId()));
    }

    @Operation(summary = "恢复单个资源", description = "恢复单个资源")
    @PostMapping("recoverResource/{recycleId}")
    @PreAuthorize("@cepc.hasAnyCePermission('RECYCLE_BIN:RECOVER')")
    @OperatedLog(resourceType = ResourceTypeEnum.RECYCLE, operated = OperatedTypeEnum.RECOVER,
            resourceId = "#recycleId",
            content = "'恢复单个资源'",
            param = "#recycleId")
    public ResultHolder<Boolean> recoverResource(@PathVariable String recycleId) {
        return ResultHolder.success(recycleBinService.recoverResource(recycleId));
    }

    @Operation(summary = "恢复云主机", description = "恢复云主机")
    @PostMapping("recoverVm")
    @PreAuthorize("@cepc.hasAnyCePermission('RECYCLE_BIN:RECOVER')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.RECOVER,
            resourceId = "#request.resourceId",
            param = "#request")
    public ResultHolder<Boolean> recoverVm(@RequestBody RecycleRequest request) {
        return ResultHolder.success(recycleBinService.recoverResource(request.getId()));
    }

    @Operation(summary = "恢复磁盘", description = "恢复磁盘")
    @PostMapping("recoverDisk")
    @PreAuthorize("@cepc.hasAnyCePermission('RECYCLE_BIN:RECOVER')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.RECOVER,
            resourceId = "#request.resourceId",
            param = "#request")
    public ResultHolder<Boolean> recoverDisk(@RequestBody RecycleRequest request) {
        return ResultHolder.success(recycleBinService.recoverResource(request.getId()));
    }

    @Operation(summary = "获取当前回收站开启状态", description = "获取当前回收站开启状态")
    @GetMapping("getRecycleEnableStatus")
    public ResultHolder<Boolean> getRecycleEnableStatus() {
        return ResultHolder.success(recycleBinService.getRecycleEnableStatus());
    }

}
