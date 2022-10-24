package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.mapper.BaseVmCloudDiskMapper;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.disk.AttachVmCloudDiskRequest;
import com.fit2cloud.controller.request.disk.EnlargeVmCloudDiskRequest;
import com.fit2cloud.controller.request.disk.PageVmCloudDiskRequest;
import com.fit2cloud.dto.VmCloudDiskDTO;
import com.fit2cloud.dto.VmCloudServerDTO;
import com.fit2cloud.service.IVmCloudDiskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author jianneng
 * @date 2022/9/27 14:31
 **/
@RestController
@RequestMapping("/api/disk")
@Validated
@Api("硬盘相关接口")
public class VmCloudDiskController {
    @Resource
    private IVmCloudDiskService diskService;

    @ApiOperation(value = "分页查询硬盘", notes = "分页查询硬盘")
    @GetMapping("/page")
    public ResultHolder<IPage<VmCloudDiskDTO>> list(@Validated PageVmCloudDiskRequest pageVmCloudDiskRequest) {
        return ResultHolder.success(diskService.pageVmCloudDisk(pageVmCloudDiskRequest));
    }

    @ApiOperation(value = "根据云账号查询虚拟机")
    @GetMapping("/listVmByAccountId/{accountId}")
    public ResultHolder<List<VmCloudServerDTO>> cloudServerlist(@PathVariable("accountId") String accountId) {
        return ResultHolder.success(diskService.cloudServerList(accountId));
    }

    @ApiOperation(value = "根据ID查询磁盘信息")
    @GetMapping("/showCloudDiskById/{id}")
    public ResultHolder<VmCloudDiskDTO> cloudDisk(@PathVariable("id") String id) {
        return ResultHolder.success(diskService.cloudDisk(id));
    }

    @ApiOperation(value = "扩容磁盘")
    @PutMapping("enlarge")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.ENLARGE_DISK, resourceId = "#{req.id}", param = "#{req}")
    public ResultHolder<Boolean> enlarge(@RequestBody EnlargeVmCloudDiskRequest req) {
        return ResultHolder.success(diskService.enlarge(req.getId(),req.getNewDiskSize()));
    }

    @ApiOperation(value = "挂载磁盘")
    @PutMapping("attach")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.ATTACH_DISK, resourceId = "#{req.id}", param = "#{req}")
    public ResultHolder<Boolean> attach(@RequestBody AttachVmCloudDiskRequest req) {
        return ResultHolder.success(diskService.attach(req.getId(),req.getInstanceUuid(),req.getDeleteWithInstance()));
    }

    @ApiOperation(value = "卸载磁盘")
    @PutMapping("detach/{id}")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.DETACH_DISK, resourceId = "#id", param = "#id")
    public ResultHolder<Boolean> detach(@ApiParam("主键 ID")
                                        @NotNull(message = "{i18n.primary.key.cannot.be.null}")
                                        @CustomValidated(mapper = BaseVmCloudDiskMapper.class, handler = ExistHandler.class, message = "{i18n.primary.key.not.exist}", exist = false)
                                        @PathVariable("id") String id) {
        return ResultHolder.success(diskService.detach(id));
    }

    @ApiOperation(value = "删除磁盘")
    @DeleteMapping("delete/{id}")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.DELETE_DISK, resourceId = "#id", param = "#id")
    public ResultHolder<Boolean> delete(@ApiParam("主键 ID")
                                        @NotNull(message = "{i18n.primary.key.cannot.be.null}")
                                        @CustomValidated(mapper = BaseVmCloudDiskMapper.class, handler = ExistHandler.class, message = "{i18n.primary.key.not.exist}", exist = false)
                                        @PathVariable("id") String id) {
        return ResultHolder.success(diskService.delete(id));
    }
}
