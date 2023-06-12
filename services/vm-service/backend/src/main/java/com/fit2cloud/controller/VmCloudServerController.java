package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
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
@Tag(name = "云主机相关接口")
public class VmCloudServerController {
    @Resource
    private IVmCloudServerService iVmCloudServerService;

    @Operation(summary = "分页查询云主机", description = "分页查询云主机")
    @GetMapping("/page")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:READ')")
    public ResultHolder<IPage<VmCloudServerDTO>> page(@Validated PageVmCloudServerRequest pageVmCloudServerRequest) {
        return ResultHolder.success(iVmCloudServerService.pageVmCloudServer(pageVmCloudServerRequest));
    }

    @Operation(summary = "查询云主机", description = "查询云主机")
    @GetMapping("/list")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:READ')")
    public ResultHolder<List<VmCloudServerDTO>> list(PageVmCloudServerRequest pageVmCloudServerRequest) {
        return ResultHolder.success(iVmCloudServerService.listVmCloudServer(pageVmCloudServerRequest));
    }

    @Operation(summary = "查询云主机数量", description = "查询云主机数量")
    @GetMapping("/count")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:READ')")
    public ResultHolder<Long> count() {
        return ResultHolder.success(iVmCloudServerService.countVmCloudServer());
    }

    @Operation(summary = "查询云主机数量", description = "查询云主机数量")
    @GetMapping("status/count")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:READ')")
    public ResultHolder<List<Map<String, Object>>> countByStatus() {
        return ResultHolder.success(iVmCloudServerService.countByStatus());
    }

    @Operation(summary = "开机", description = "启动云主机操作系统")
    @PostMapping("powerOn/{serverId}")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:START')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.POWER_ON,
            resourceId = "#serverId",
            param = "#serverId")
    public ResultHolder<Boolean> powerOn(@PathVariable String serverId) {
        return ResultHolder.success(iVmCloudServerService.powerOn(serverId));
    }

    @Operation(summary = "重启", description = "重启云主机操作系统")
    @PostMapping("reboot/{serverId}")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:RESTART')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.REBOOT,
            resourceId = "#serverId",
            param = "#serverId")
    public ResultHolder<Boolean> reboot(@PathVariable String serverId) throws Exception {
        return ResultHolder.success(iVmCloudServerService.rebootInstance(serverId));
    }

    @Operation(summary = "关机", description = "关闭云主机操作系统")
    @PostMapping("shutdown/{serverId}")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:STOP')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.SHUTDOWN,
            resourceId = "#serverId",
            param = "#serverId")
    public ResultHolder<Boolean> shutdown(@PathVariable String serverId) throws Exception {
        return ResultHolder.success(iVmCloudServerService.shutdownInstance(serverId));
    }

    @Operation(summary = "关闭电源", description = "关闭云主机电源")
    @PostMapping("powerOff/{serverId}")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:STOP')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.POWER_OFF,
            resourceId = "#serverId",
            param = "#serverId")
    public ResultHolder<Boolean> powerOff(@PathVariable String serverId) throws Exception {
        return ResultHolder.success(iVmCloudServerService.powerOff(serverId));
    }

    @Operation(summary = "删除", description = "删除云主机")
    @PostMapping("delete/{serverId}")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.DELETE,
            resourceId = "#serverId",
            param = "#serverId")
    public ResultHolder<Boolean> deleteInstance(@PathVariable String serverId) {
        return ResultHolder.success(iVmCloudServerService.deleteInstance(serverId));
    }

    @Operation(summary = "删除创建失败的记录", description = "删除云主机创建失败的记录")
    @PostMapping("deleteFailedRecord/{serverId}")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.DELETE,
            resourceId = "#serverId",
            param = "#serverId")
    public ResultHolder<Boolean> deleteFailedRecord(@PathVariable String serverId) {
        return ResultHolder.success(iVmCloudServerService.deleteFailedRecord(serverId));
    }

    @Operation(summary = "放入回收站", description = "云主机放入回收站")
    @PostMapping("recycle/{serverId}")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.RECYCLE, resourceId = "#serverId", param = "#serverId")
    public ResultHolder<Boolean> recycleInstance(@PathVariable String serverId) {
        return ResultHolder.success(iVmCloudServerService.recycleInstance(serverId));
    }

    @Operation(summary = "恢复", description = "云主机恢复")
    @PostMapping("recover/{recycleBinId}")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:RECOVER')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.RECOVER, resourceId = "#serverId", param = "#serverId")
    public ResultHolder<Boolean> recoverInstance(@PathVariable String recycleBinId) {
        return ResultHolder.success(iVmCloudServerService.recoverInstance(recycleBinId));
    }

    @Operation(summary = "批量操作", description = "批量操作云主机")
    @PostMapping("batchOperate")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:START','CLOUD_SERVER:STOP','CLOUD_SERVER:RESTART','CLOUD_SERVER:DELETE', 'CLOUD_SERVER:AUTH')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.BATCH_OPERATE, resourceId = "#request.instanceIds", content = "#request.operate")
    public ResultHolder<Boolean> batchOperate(@RequestBody BatchOperateVmRequest request) {
        return ResultHolder.success(iVmCloudServerService.batchOperate(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:READ')")
    @Operation(summary = "根据id查询云主机", description = "根据id查询云主机")
    public ResultHolder<VmCloudServerDTO> findCloudServer(@Parameter(name = "云主机id", required = true)
                                                          @PathVariable("id") String id) {
        return ResultHolder.success(iVmCloudServerService.getById(id));
    }

    @GetMapping("/ids")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:READ')")
    @Operation(summary = "根据ids查询云主机", description = "根据ids查询云主机")
    public ResultHolder<List<VmCloudServerDTO>> findCloudServer(@Parameter(name = "需要查询的云主机ids") @RequestParam("cloudServerIds[]") List<String> cloudServerIds) {
        return ResultHolder.success(iVmCloudServerService.getByIds(cloudServerIds));
    }

    @GetMapping("/operate/job_record")
    @PreAuthorize("@cepc.hasAnyCePermission('JOBS:READ')")
    @Operation(summary = "查询云主机最新的操作记录", description = "查询云主机最新的操作记录")
    public ResultHolder<Map<String, List<JobRecordResourceResponse>>> findCloudServerOperateStatus(@Parameter(name = "需要查询的云主机id") @RequestParam("cloudServerIds[]") List<String> cloudServerIds) {
        return ResultHolder.success(iVmCloudServerService.findCloudServerOperateStatus(cloudServerIds).stream().collect(Collectors.groupingBy(JobRecordResourceResponse::getResourceId)));
    }

    @Operation(summary = "创建云主机", description = "创建云主机")
    @PostMapping("create")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:CREATE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.ADD, param = "#request")
    public ResultHolder<Boolean> createServer(@RequestBody CreateServerRequest request) {
        return ResultHolder.success(iVmCloudServerService.createServer(request));
    }

    @Operation(summary = "配置变更")
    @PutMapping("changeConfig")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:RESIZE')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.CHANGE_SERVER_CONFIG, resourceId = "#req.id", content = "'变更配置为['+#req.newInstanceType+']'", param = "#req")
    public ResultHolder<Boolean> changeConfig(@RequestBody ChangeServerConfigRequest req) {
        return ResultHolder.success(iVmCloudServerService.changeConfig(req));
    }

    @Operation(summary = "根据云平台查询配置变更的表单数据")
    @GetMapping("/configUpdateForm/{platform}")
    public ResultHolder<FormObject> findConfigUpdateForm(@PathVariable("platform") String platform) {
        return ResultHolder.success(iVmCloudServerService.getConfigUpdateForm(platform));
    }

    @Operation(summary = "查询配置变更价格")
    @PostMapping("/configUpdatePrice/{platform}")
    public ResultHolder<String> calculateConfigUpdatePrice(@PathVariable("platform") String platform, @RequestBody Map<String, Object> params) {
        return ResultHolder.success(iVmCloudServerService.calculateConfigUpdatePrice(platform, params));
    }

    @Operation(summary = "云主机授权")
    @PostMapping("/grant")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:AUTH')")
    @OperatedLog(resourceType = ResourceTypeEnum.CLOUD_SERVER, operated = OperatedTypeEnum.BATCH_AUTHORISATION,
            content = "#grantServerRequest.grant?'云主机批量授权':'云主机批量取消授权'",
            resourceId = "#grantServerRequest.ids")
    public ResultHolder<Boolean> grant(@RequestBody GrantRequest grantServerRequest) {
        return ResultHolder.success(iVmCloudServerService.grant(grantServerRequest));
    }
}
