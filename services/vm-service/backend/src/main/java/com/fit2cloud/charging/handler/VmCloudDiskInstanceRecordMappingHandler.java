package com.fit2cloud.charging.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.base.entity.Workspace;
import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.base.service.IBaseWorkspaceService;
import com.fit2cloud.charging.constants.VmDiskStateConstants;
import com.fit2cloud.common.charging.constants.BillModeConstants;
import com.fit2cloud.common.charging.entity.InstanceRecord;
import com.fit2cloud.common.charging.entity.InstanceState;
import com.fit2cloud.common.charging.instance.InstanceRecordMappingHandler;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.service.IVmCloudDiskService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/30  18:53}
 * {@code @Version 1.0}
 * {@code @注释: }
 */

public class VmCloudDiskInstanceRecordMappingHandler implements InstanceRecordMappingHandler {
    private InstanceState.State getState(String vmStatus) {
        return Arrays.stream(VmDiskStateConstants.values()).
                filter(disk -> disk.name().equals(vmStatus))
                .findFirst().map(disk -> new InstanceState.State(disk.getCode())
                ).orElse(new InstanceState.State(VmDiskStateConstants.unknown.getCode()));

    }

    @Override
    public List<InstanceRecord> mapping() {
        IBaseWorkspaceService workspaceService = SpringUtil.getBean(IBaseWorkspaceService.class);
        IBaseOrganizationService organizationService = SpringUtil.getBean(IBaseOrganizationService.class);
        IVmCloudDiskService vmCloudDiskService = SpringUtil.getBean(IVmCloudDiskService.class);
        List<Workspace> workspaceList = workspaceService.list();
        List<Organization> organizationList = organizationService.list();
        return vmCloudDiskService.list(new LambdaQueryWrapper<VmCloudDisk>()
                        .notIn(VmCloudDisk::getStatus, List.of("deleted")))
                .stream().map(vmCloudDisk -> {
                    InstanceRecord instanceRecord = new InstanceRecord();
                    instanceRecord.setResourceId(vmCloudDisk.getId());
                    instanceRecord.setResourceName(vmCloudDisk.getDiskName());
                    instanceRecord.setBillMode(BillModeConstants.ON_DEMAND);
                    instanceRecord.setMeta(Map.of("size", vmCloudDisk.getSize().intValue()));
                    instanceRecord.setRegion(vmCloudDisk.getRegion());
                    instanceRecord.setCloudAccountId(vmCloudDisk.getAccountId());
                    instanceRecord.setResourceType("DISK");
                    instanceRecord.setResourceName(instanceRecord.getResourceName());
                    instanceRecord.setProductId("DISK");
                    instanceRecord.setProductName("云磁盘");
                    instanceRecord.setProductDetail("云磁盘 (" + vmCloudDisk.getSize() + ")");
                    instanceRecord.setState(getState(vmCloudDisk.getStatus()));
                    instanceRecord.setZone(vmCloudDisk.getZone());
                    String sourceId = vmCloudDisk.getSourceId();
                    setAuthData(workspaceList, organizationList, instanceRecord, sourceId);
                    return instanceRecord;
                }).toList();
    }


}
