package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.vm.BatchRecycleRequest;
import com.fit2cloud.controller.request.vm.PageRecycleBinRequest;
import com.fit2cloud.dto.RecycleBinDTO;
import com.fit2cloud.service.IRecycleBinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : LiuDi
 * @date : 2023/1/12 14:32
 */
@RestController
@RequestMapping("/api/vm/recycleBin")
@Validated
@Api("云主机模块回收站相关接口")
public class RecycleBinController {
    @Resource
    private IRecycleBinService recycleBinService;

    @ApiOperation(value = "分页查询回收站信息", notes = "分页查询回收站信息")
    @GetMapping("/page")
    @PreAuthorize("hasAnyCePermission('RECYCLE_BIN:READ')")
    public ResultHolder<IPage<RecycleBinDTO>> list(@Validated PageRecycleBinRequest pageRecycleBinRequest) {
        return ResultHolder.success(recycleBinService.pageRecycleBin(pageRecycleBinRequest));
    }

    @ApiOperation(value = "批量删除资源", notes = "批量删除资源")
    @PostMapping("batchDeleteResource")
    @PreAuthorize("hasAnyCePermission('RECYCLE_BIN:DELETE')")
    public ResultHolder<Boolean> batchDeleteResource(@RequestBody BatchRecycleRequest request) {
        return ResultHolder.success(recycleBinService.batchDeleteResource(request));
    }

    @ApiOperation(value = "删除单个资源", notes = "删除单个资源")
    @PostMapping("deleteResource/{recycleId}")
    @PreAuthorize("hasAnyCePermission('RECYCLE_BIN:DELETE')")
    public ResultHolder<Boolean> deleteResource(@PathVariable String recycleId) {
        return ResultHolder.success(recycleBinService.deleteResource(recycleId));
    }

    @ApiOperation(value = "批量恢复资源", notes = "批量恢复资源")
    @PostMapping("batchRecoverResource")
    @PreAuthorize("hasAnyCePermission('RECYCLE_BIN:RECOVER')")
    public ResultHolder<Boolean> batchRecoverResource(@RequestBody BatchRecycleRequest request) {
        return ResultHolder.success(recycleBinService.batchRecoverResource(request));
    }

    @ApiOperation(value = "恢复单个资源", notes = "恢复单个资源")
    @PostMapping("recoverResource/{recycleId}")
    @PreAuthorize("hasAnyCePermission('RECYCLE_BIN:RECOVER')")
    public ResultHolder<Boolean> recoverResource(@PathVariable String recycleId) {
        return ResultHolder.success(recycleBinService.recoverResource(recycleId));
    }

    @ApiOperation(value = "获取当前回收站开启状态", notes = "获取当前回收站开启状态")
    @GetMapping("getRecycleEnableStatus")
    public ResultHolder<Boolean> getRecycleEnableStatus() {
        return ResultHolder.success(recycleBinService.getRecycleEnableStatus());
    }

}
