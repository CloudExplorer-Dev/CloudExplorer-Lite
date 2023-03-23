package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.GrantRequest;
import com.fit2cloud.controller.request.vm.BatchOperateVmRequest;
import com.fit2cloud.controller.request.vm.ChangeServerConfigRequest;
import com.fit2cloud.controller.request.vm.CreateServerRequest;
import com.fit2cloud.controller.request.vm.PageVmCloudServerRequest;
import com.fit2cloud.dto.VmCloudServerDTO;
import com.fit2cloud.response.JobRecordResourceResponse;
import com.fit2cloud.service.IVmCloudServerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jianneng
 * @date 2022/9/27 14:31
 **/
@RestController
@RequestMapping("/api/server")
@Validated
@Api("云主机相关接口")
public class VmCloudServerController {
    @Resource
    private IVmCloudServerService iVmCloudServerService;

    @ApiOperation(value = "分页查询云主机", notes = "分页查询云主机")
    @GetMapping("/page")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:READ')")
    public ResultHolder<IPage<VmCloudServerDTO>> page(@Validated PageVmCloudServerRequest pageVmCloudServerRequest) {
        return ResultHolder.success(iVmCloudServerService.pageVmCloudServer(pageVmCloudServerRequest));
    }

    @ApiOperation(value = "查询云主机", notes = "查询云主机")
    @GetMapping("/list")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:READ')")
    public ResultHolder<List<VmCloudServer>> list(PageVmCloudServerRequest pageVmCloudServerRequest) {
        return ResultHolder.success(iVmCloudServerService.listVmCloudServer(pageVmCloudServerRequest));
    }

    @ApiOperation(value = "查询云主机数量", notes = "查询云主机数量")
    @GetMapping("/count")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:READ')")
    public ResultHolder<Long> count() {
        return ResultHolder.success(iVmCloudServerService.countVmCloudServer());
    }

    @ApiOperation(value = "查询云主机数量", notes = "查询云主机数量")
    @GetMapping("status/count")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:READ')")
    public ResultHolder<List<Map<String, Object>>> countByStatus() {
        return ResultHolder.success(iVmCloudServerService.countByStatus());
    }

    @ApiOperation(value = "开机", notes = "启动云主机操作系统")
    @PostMapping("powerOn/{serverId}")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:START')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.POWER_ON,
            resourceId = "#serverId",
            param = "#serverId")
    public ResultHolder<Boolean> powerOn(@PathVariable String serverId) {
        return ResultHolder.success(iVmCloudServerService.powerOn(serverId));
    }

    @ApiOperation(value = "重启", notes = "重启云主机操作系统")
    @PostMapping("reboot/{serverId}")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:RESTART')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.REBOOT,
            resourceId = "#serverId",
            param = "#serverId")
    public ResultHolder<Boolean> reboot(@PathVariable String serverId) throws Exception {
        return ResultHolder.success(iVmCloudServerService.rebootInstance(serverId));
    }

    @ApiOperation(value = "关机", notes = "关闭云主机操作系统")
    @PostMapping("shutdown/{serverId}")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:STOP')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.SHUTDOWN,
            resourceId = "#serverId",
            param = "#serverId")
    public ResultHolder<Boolean> shutdown(@PathVariable String serverId) throws Exception {
        return ResultHolder.success(iVmCloudServerService.shutdownInstance(serverId));
    }

    @ApiOperation(value = "关闭电源", notes = "关闭云主机电源")
    @PostMapping("powerOff/{serverId}")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:STOP')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.POWER_OFF,
            resourceId = "#serverId",
            param = "#serverId")
    public ResultHolder<Boolean> powerOff(@PathVariable String serverId) throws Exception {
        return ResultHolder.success(iVmCloudServerService.powerOff(serverId));
    }

    @ApiOperation(value = "删除", notes = "删除云主机")
    @PostMapping("delete/{serverId}")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.DELETE,
            resourceId = "#serverId",
            param = "#serverId")
    public ResultHolder<Boolean> deleteInstance(@PathVariable String serverId) {
        return ResultHolder.success(iVmCloudServerService.deleteInstance(serverId));
    }

    @ApiOperation(value = "删除创建失败的记录", notes = "删除云主机创建失败的记录")
    @PostMapping("deleteFailedRecord/{serverId}")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.DELETE,
            resourceId = "#serverId",
            param = "#serverId")
    public ResultHolder<Boolean> deleteFailedRecord(@PathVariable String serverId) {
        return ResultHolder.success(iVmCloudServerService.deleteFailedRecord(serverId));
    }

    @ApiOperation(value = "放入回收站", notes = "云主机放入回收站")
    @PostMapping("recycle/{serverId}")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.RECYCLE, resourceId = "#serverId", param = "#serverId")
    public ResultHolder<Boolean> recycleInstance(@PathVariable String serverId) {
        return ResultHolder.success(iVmCloudServerService.recycleInstance(serverId));
    }

    @ApiOperation(value = "恢复", notes = "云主机恢复")
    @PostMapping("recover/{recycleBinId}")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:RECOVER')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.RECOVER, resourceId = "#serverId", param = "#serverId")
    public ResultHolder<Boolean> recoverInstance(@PathVariable String recycleBinId) {
        return ResultHolder.success(iVmCloudServerService.recoverInstance(recycleBinId));
    }

    @ApiOperation(value = "批量操作", notes = "批量操作云主机")
    @PostMapping("batchOperate")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:START','CLOUD_SERVER:STOP','CLOUD_SERVER:RESTART','CLOUD_SERVER:DELETE', 'CLOUD_SERVER:AUTH')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.BATCH_OPERATE, resourceId = "#request.instanceIds", content = "#request.operate")
    public ResultHolder<Boolean> batchOperate(@RequestBody BatchOperateVmRequest request) {
        return ResultHolder.success(iVmCloudServerService.batchOperate(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:READ')")
    @ApiOperation(value = "根据id查询云主机", notes = "根据id查询云主机")
    public ResultHolder<VmCloudServerDTO> findCloudServer(@ApiParam(value = "云主机id", required = true)
                                                          @PathVariable("id") String id) {
        return ResultHolder.success(iVmCloudServerService.getById(id));
    }

    @GetMapping("/ids")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:READ')")
    @ApiOperation(value = "根据ids查询云主机", notes = "根据ids查询云主机")
    public ResultHolder<List<VmCloudServerDTO>> findCloudServer(@ApiParam("需要查询的云主机ids") @RequestParam("cloudServerIds[]") List<String> cloudServerIds) {
        return ResultHolder.success(iVmCloudServerService.getByIds(cloudServerIds));
    }

    @GetMapping("/operate/job_record")
    @PreAuthorize("hasAnyCePermission('JOBS:READ')")
    @ApiOperation(value = "查询云主机最新的操作记录", notes = "查询云主机最新的操作记录")
    public ResultHolder<Map<String, List<JobRecordResourceResponse>>> findCloudServerOperateStatus(@ApiParam("需要查询的云主机id") @RequestParam("cloudServerIds[]") List<String> cloudServerIds) {
        return ResultHolder.success(iVmCloudServerService.findCloudServerOperateStatus(cloudServerIds).stream().collect(Collectors.groupingBy(JobRecordResourceResponse::getResourceId)));
    }

    @ApiOperation(value = "创建云主机", notes = "创建云主机")
    @PostMapping("create")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:CREATE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.ADD, param = "#request")
    public ResultHolder<Boolean> createServer(@RequestBody CreateServerRequest request) {
        return ResultHolder.success(iVmCloudServerService.createServer(request));
    }

    @ApiOperation(value = "配置变更")
    @PutMapping("changeConfig")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:RESIZE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.CHANGE_SERVER_CONFIG, resourceId = "#req.id", content = "'变更配置为['+#req.newInstanceType+']'", param = "#req")
    public ResultHolder<Boolean> changeConfig(@RequestBody ChangeServerConfigRequest req) {
        return ResultHolder.success(iVmCloudServerService.changeConfig(req));
    }

    @ApiOperation(value = "根据云平台查询配置变更的表单数据")
    @GetMapping("/configUpdateForm/{platform}")
    public ResultHolder<FormObject> findConfigUpdateForm(@PathVariable("platform") String platform) {
        return ResultHolder.success(iVmCloudServerService.getConfigUpdateForm(platform));
    }

    @ApiOperation(value = "查询配置变更价格")
    @PostMapping("/configUpdatePrice/{platform}")
    public ResultHolder<String> calculateConfigUpdatePrice(@PathVariable("platform") String platform, @RequestBody Map<String, Object> params) {
        return ResultHolder.success(iVmCloudServerService.calculateConfigUpdatePrice(platform, params));
    }

    @ApiOperation(value = "云主机授权")
    @PostMapping("/grant")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:AUTH')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.BATCH_AUTHORISATION,
            content = "#grantServerRequest.grant?'云主机批量授权':'云主机批量取消授权'",
            resourceId = "#grantServerRequest.ids")
    public ResultHolder<Boolean> grant(@RequestBody GrantRequest grantServerRequest) {
        return ResultHolder.success(iVmCloudServerService.grant(grantServerRequest));
    }
}
