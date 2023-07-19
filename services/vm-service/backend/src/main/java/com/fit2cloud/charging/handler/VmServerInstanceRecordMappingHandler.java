package com.fit2cloud.charging.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.entity.Workspace;
import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.base.service.IBaseWorkspaceService;
import com.fit2cloud.charging.constants.VmServerStateConstants;
import com.fit2cloud.common.charging.constants.BillModeConstants;
import com.fit2cloud.common.charging.entity.InstanceRecord;
import com.fit2cloud.common.charging.entity.InstanceState;
import com.fit2cloud.common.charging.instance.InstanceRecordMappingHandler;
import com.fit2cloud.common.constants.ChargeTypeConstants;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.service.IVmCloudServerService;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/30  18:53}
 * {@code @Version 1.0}
 * {@code @注释: }
 */

public class VmServerInstanceRecordMappingHandler implements InstanceRecordMappingHandler {
    private InstanceState.State getState(String vmStatus) {
        return Arrays.stream(VmServerStateConstants.values()).
                filter(server -> server.name().equals(vmStatus))
                .findFirst().map(server -> new InstanceState.State(server.getCode())
                ).orElse(new InstanceState.State(VmServerStateConstants.unknown.getCode()));
    }

    @Override
    public List<InstanceRecord> mapping() {
        IBaseWorkspaceService workspaceService = SpringUtil.getBean(IBaseWorkspaceService.class);
        IBaseOrganizationService organizationService = SpringUtil.getBean(IBaseOrganizationService.class);
        IVmCloudServerService vmCloudServerService = SpringUtil.getBean(IVmCloudServerService.class);
        List<Workspace> workspaceList = workspaceService.list();
        List<Organization> organizationList = organizationService.list();
        return vmCloudServerService
                .list(new LambdaQueryWrapper<VmCloudServer>()
                        .notIn(VmCloudServer::getInstanceStatus, List.of("Deleted"))
                )
                .stream()
                .map(vmCloudServer -> {
                    BillModeConstants billMode = StringUtils.isEmpty(vmCloudServer.getInstanceChargeType()) ?
                            BillModeConstants.ON_DEMAND : vmCloudServer.getInstanceChargeType().equals(ChargeTypeConstants.POSTPAID.getCode())
                            ? BillModeConstants.ON_DEMAND : BillModeConstants.MONTHLY;
                    InstanceRecord instanceRecord = new InstanceRecord();
                    instanceRecord.setResourceId(vmCloudServer.getId());
                    instanceRecord.setResourceName(vmCloudServer.getInstanceName());
                    instanceRecord.setBillMode(billMode);
                    instanceRecord.setMeta(Map.of("cpu", vmCloudServer.getCpu(), "memory", vmCloudServer.getMemory()));
                    instanceRecord.setRegion(vmCloudServer.getRegion());
                    instanceRecord.setCloudAccountId(vmCloudServer.getAccountId());
                    instanceRecord.setResourceType("ECS");
                    instanceRecord.setResourceName(instanceRecord.getResourceName());
                    instanceRecord.setProductId("ECS");
                    instanceRecord.setProductName("云服务器");
                    instanceRecord.setProductDetail("云服务器 (" + vmCloudServer.getInstanceTypeDescription() + ")");
                    instanceRecord.setState(getState(vmCloudServer.getInstanceStatus()));
                    instanceRecord.setZone(vmCloudServer.getZone());
                    String sourceId = vmCloudServer.getSourceId();
                    setAuthData(workspaceList, organizationList, instanceRecord, sourceId);
                    return instanceRecord;
                }).toList();
    }
}
