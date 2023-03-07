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
import com.fit2cloud.controller.request.GrantRequest;
import com.fit2cloud.controller.request.disk.*;
import com.fit2cloud.dto.VmCloudDiskDTO;
import com.fit2cloud.dto.VmCloudServerDTO;
import com.fit2cloud.service.IVmCloudDiskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyCePermission('CLOUD_DISK:READ')")
    public ResultHolder<IPage<VmCloudDiskDTO>> list(@Validated PageVmCloudDiskRequest pageVmCloudDiskRequest) {
        return ResultHolder.success(diskService.pageVmCloudDisk(pageVmCloudDiskRequest));
    }

    @ApiOperation(value = "查询硬盘数量", notes = "查询硬盘数量")
    @GetMapping("/count")
    @PreAuthorize("hasAnyCePermission('CLOUD_DISK:READ')")
    public ResultHolder<Long> count() {
        return ResultHolder.success(diskService.countDisk());
    }

    @ApiOperation(value = "查询可以挂载磁盘的虚拟机")
    @GetMapping("/listVm")
    @PreAuthorize("hasAnyCePermission('CLOUD_DISK:READ')")
    public ResultHolder<List<VmCloudServerDTO>> cloudServerList(ListVmRequest req) {
        return ResultHolder.success(diskService.cloudServerList(req));
    }

    @ApiOperation(value = "根据ID查询磁盘信息")
    @GetMapping("/showCloudDiskById/{id}")
    @PreAuthorize("hasAnyCePermission('CLOUD_DISK:READ')")
    public ResultHolder<VmCloudDiskDTO> cloudDisk(@PathVariable("id") String id) {
        return ResultHolder.success(diskService.cloudDisk(id));
    }

    @GetMapping("/createDiskForm/{platform}")
    @ApiOperation(value = "根据云平台查询创建磁盘的表单数据")
    @PreAuthorize("hasAnyCePermission('CLOUD_DISK:READ')")
    public ResultHolder<FormObject> findCreateDiskForm(@PathVariable("platform") String platform) {
        return ResultHolder.success(diskService.getCreateDiskForm(platform));
    }

    @ApiOperation(value = "创建磁盘")
    @PostMapping("createDisk")
    @PreAuthorize("hasAnyCePermission('CLOUD_DISK:CREATE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.ADD,
            content = "'创建磁盘['+#request.diskName+']'",
            joinResourceType = ResourceTypeEnum.CLOUD_SERVER,
            joinResourceId = "#request.instanceUuid",
            param = "#request")
    public ResultHolder<Boolean> createDisk(@RequestBody CreateVmCloudDiskRequest request) {
        return ResultHolder.success(diskService.createDisk(request));
    }

    @ApiOperation(value = "扩容磁盘")
    @PutMapping("enlarge")
    @PreAuthorize("hasAnyCePermission('CLOUD_DISK:RESIZE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.ENLARGE,
            resourceId = "#req.id",
            joinResourceType = ResourceTypeEnum.CLOUD_SERVER,
            joinResourceId = "#req.instanceUuid",
            content = "'磁盘大小扩容到['+#req.newDiskSize+']GB'",
            param = "#req")
    public ResultHolder<Boolean> enlarge(@RequestBody EnlargeVmCloudDiskRequest req) {
        return ResultHolder.success(diskService.enlarge(req.getId(), req.getNewDiskSize()));
    }

    @ApiOperation(value = "挂载磁盘")
    @PutMapping("attach")
    @PreAuthorize("hasAnyCePermission('CLOUD_DISK:ATTACH')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK,
            operated = OperatedTypeEnum.ATTACH,
            resourceId = "#req.id",
            joinResourceType = ResourceTypeEnum.CLOUD_SERVER,
            joinResourceId = "#req.instanceUuid",
            content = "'磁盘挂载'",
            param = "#req")
    public ResultHolder<Boolean> attach(@RequestBody AttachVmCloudDiskRequest req) {
        return ResultHolder.success(diskService.attach(req.getId(), req.getInstanceUuid(), req.getDeleteWithInstance()));
    }

    @ApiOperation(value = "卸载磁盘")
    @PutMapping("detach/{id}")
    @PreAuthorize("hasAnyCePermission('CLOUD_DISK:DETACH')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.DETACH,
            resourceId = "#id",
            content = "'磁盘卸载'",
            param = "#id")
    public ResultHolder<Boolean> detach(@ApiParam("主键 ID")
                                        @NotNull(message = "{i18n.primary.key.cannot.be.null}")
                                        @CustomValidated(mapper = BaseVmCloudDiskMapper.class, handler = ExistHandler.class, message = "{i18n.primary.key.not.exist}", exist = false)
                                        @PathVariable("id") String id) {
        return ResultHolder.success(diskService.detach(id));
    }

    @ApiOperation(value = "删除磁盘")
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAnyCePermission('CLOUD_DISK:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.DELETE, resourceId = "#id", content = "'删除磁盘'", param = "#id")
    public ResultHolder<Boolean> delete(@ApiParam("主键 ID")
                                        @NotNull(message = "{i18n.primary.key.cannot.be.null}")
                                        @CustomValidated(mapper = BaseVmCloudDiskMapper.class, handler = ExistHandler.class, message = "{i18n.primary.key.not.exist}", exist = false)
                                        @PathVariable("id") String id) {
        return ResultHolder.success(diskService.delete(id));
    }

    @ApiOperation(value = "批量挂载磁盘")
    @PutMapping("batchAttach")
    @PreAuthorize("hasAnyCePermission('CLOUD_DISK:ATTACH')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.BATCH_ATTACH, resourceId = "#req.ids",
            joinResourceType = ResourceTypeEnum.CLOUD_SERVER,
            joinResourceId = "#req.instanceUuid",
            content = "'批量挂载磁盘'", param = "#req")
    public ResultHolder<Boolean> batchAttach(@RequestBody BatchAttachVmCloudDiskRequest req) {
        return ResultHolder.success(diskService.batchAttach(req.getIds(), req.getInstanceUuid(), req.getDeleteWithInstance()));
    }

    @ApiOperation(value = "批量卸载磁盘")
    @PutMapping("batchDetach")
    @PreAuthorize("hasAnyCePermission('CLOUD_DISK:DETACH')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.BATCH_DETACH, resourceId = "#ids", content = "'批量卸载磁盘'", param = "#ids")
    public ResultHolder<Boolean> batchDetach(@RequestBody String[] ids) {
        return ResultHolder.success(diskService.batchDetach(ids));
    }

    @ApiOperation(value = "批量删除磁盘")
    @DeleteMapping("batchDelete")
    @PreAuthorize("hasAnyCePermission('CLOUD_DISK:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.BATCH_DELETE, content = "'批量删除磁盘'",  param = "#ids")
    public ResultHolder<Boolean> batchDelete(@RequestBody String[] ids) {
        return ResultHolder.success(diskService.batchDelete(ids));
    }

    @ApiOperation(value = "磁盘授权")
    @PostMapping("/grant")
    @PreAuthorize("hasAnyCePermission('CLOUD_DISK:AUTH')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.BATCH_AUTHORISATION,
            resourceId = "#grantDiskRequest.ids",
            content = "#grantDiskRequest.grant?'磁盘批量授权':'磁盘批量取消授权'",
            param = "#grantDiskRequest")
    public ResultHolder<Boolean> grant(@RequestBody GrantRequest grantDiskRequest) {
        return ResultHolder.success(diskService.grant(grantDiskRequest));
    }

    @ApiOperation(value = "批量放入回收站")
    @PutMapping("batchRecycleDisks")
    @PreAuthorize("hasAnyCePermission('CLOUD_DISK:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.BATCH_RECYCLE,
            resourceId = "#ids",
            content = "'磁盘放入回收站'",
            param = "#ids")
    public ResultHolder<Boolean> batchRecycleDisks(@RequestBody String[] ids) {
        return ResultHolder.success(diskService.batchRecycleDisks(ids));
    }

    @ApiOperation(value = "放入回收站")
    @PutMapping("recycleDisk/{id}")
    @PreAuthorize("hasAnyCePermission('CLOUD_DISK:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_DISK, operated = OperatedTypeEnum.RECYCLE, resourceId = "#id", param = "#id")
    public ResultHolder<Boolean> recycleDisk(@ApiParam("主键 ID")
                                             @NotNull(message = "{i18n.primary.key.cannot.be.null}")
                                             @CustomValidated(mapper = BaseVmCloudDiskMapper.class, handler = ExistHandler.class, message = "{i18n.primary.key.not.exist}", exist = false)
                                             @PathVariable("id") String id) {
        return ResultHolder.success(diskService.recycleDisk(id));
    }
}
