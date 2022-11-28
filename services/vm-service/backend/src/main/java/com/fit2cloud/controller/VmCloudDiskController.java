package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.mapper.BaseVmCloudDiskMapper;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.disk.*;
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

    @ApiOperation(value = "查询可以挂载磁盘的虚拟机")
    @GetMapping("/listVm")
    public ResultHolder<List<VmCloudServerDTO>> cloudServerList(ListVmRequest req) {
        return ResultHolder.success(diskService.cloudServerList(req));
    }

    @ApiOperation(value = "根据ID查询磁盘信息")
    @GetMapping("/showCloudDiskById/{id}")
    public ResultHolder<VmCloudDiskDTO> cloudDisk(@PathVariable("id") String id) {
        return ResultHolder.success(diskService.cloudDisk(id));
    }

    @GetMapping("/createDiskForm/{platform}")
    @ApiOperation(value = "根据云平台查询创建云磁盘的表单数据")
    public ResultHolder<FormObject> findCreateDiskForm(@PathVariable("platform") String platform) {
        return ResultHolder.success(diskService.getCreateDiskForm(platform));
    }

    @ApiOperation(value = "创建磁盘")
    @PostMapping("createDisk")
    public ResultHolder<Boolean> createDisk(@RequestBody CreateVmCloudDiskRequest request) {
        return ResultHolder.success(diskService.createDisk(request));
    }

    @ApiOperation(value = "扩容磁盘")
    @PutMapping("enlarge")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.ENLARGE_DISK, resourceId = "#{req.id}", param = "#{req}")
    public ResultHolder<Boolean> enlarge(@RequestBody EnlargeVmCloudDiskRequest req) {
        return ResultHolder.success(diskService.enlarge(req.getId(), req.getNewDiskSize()));
    }

    @ApiOperation(value = "挂载磁盘")
    @PutMapping("attach")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.ATTACH_DISK, resourceId = "#{req.id}", param = "#{req}")
    public ResultHolder<Boolean> attach(@RequestBody AttachVmCloudDiskRequest req) {
        return ResultHolder.success(diskService.attach(req.getId(), req.getInstanceUuid(), req.getDeleteWithInstance()));
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

    @ApiOperation(value = "批量挂载磁盘")
    @PutMapping("batchAttach")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.BATCH_ATTACH_DISK, param = "#{req}")
    public ResultHolder<Boolean> batchAttach(@RequestBody BatchAttachVmCloudDiskRequest req) {
        return ResultHolder.success(diskService.batchAttach(req.getIds(), req.getInstanceUuid(), req.getDeleteWithInstance()));
    }

    @ApiOperation(value = "批量卸载磁盘")
    @PutMapping("batchDetach")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.BATCH_DETACH_DISK, param = "#{ids}")
    public ResultHolder<Boolean> batchDetach(@RequestBody String[] ids) {
        return ResultHolder.success(diskService.batchDetach(ids));
    }

    @ApiOperation(value = "批量删除磁盘")
    @DeleteMapping("batchDelete")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.BATCH_DELETE_DISK, param = "#{ids}")
    public ResultHolder<Boolean> batchDelete(@RequestBody String[] ids) {
        return ResultHolder.success(diskService.batchDelete(ids));
    }
}
