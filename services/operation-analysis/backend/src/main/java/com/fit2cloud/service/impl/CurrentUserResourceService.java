package com.fit2cloud.service.impl;

import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.base.service.IBaseVmCloudDiskService;
import com.fit2cloud.base.service.IBaseVmCloudServerService;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.constants.SpecialAttributesConstants;
import com.fit2cloud.service.IPermissionService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jianneng
 * @date 2023/3/20 14:50
 **/

@Service
public class CurrentUserResourceService {
    @Resource
    private IPermissionService permissionService;
    @Resource
    private IBaseVmCloudServerService iBaseVmCloudServerService;
    @Resource
    private IBaseVmCloudDiskService iBaseVmCloudDiskService;
    @Resource
    private IBaseCloudAccountService iBaseCloudAccountService;

    /**
     * 根据当前用户拥有的云主机获取云账号
     *
     * @return 云账号集合
     */
    public List<CloudAccount> currentUserCloudAccountList() {
        List<CloudAccount> resultList = new ArrayList<>();
        List<String> cloudAccountIds = currentUserCloudServerList().stream().map(VmCloudServer::getAccountId).toList();
        if (CollectionUtils.isNotEmpty(cloudAccountIds)) {
            MPJLambdaWrapper<CloudAccount> accountWrapper = new MPJLambdaWrapper<>();
            accountWrapper.in(!CurrentUserUtils.isAdmin(), CloudAccount::getId, cloudAccountIds);
            resultList = iBaseCloudAccountService.list(accountWrapper);
        }
        return resultList;
    }

    public List<VmCloudServer> currentUserCloudServerList() {
        List<String> sourceIds = permissionService.getSourceIds();
        MPJLambdaWrapper<VmCloudServer> wrapper = new MPJLambdaWrapper<>();
        wrapper.in(!CurrentUserUtils.isAdmin() && CollectionUtils.isNotEmpty(sourceIds), VmCloudServer::getSourceId, sourceIds);
        wrapper.notIn(true, VmCloudServer::getInstanceStatus, List.of(SpecialAttributesConstants.StatusField.VM_DELETE, SpecialAttributesConstants.StatusField.FAILED));
        return iBaseVmCloudServerService.list(wrapper);
    }

    public List<VmCloudDisk> currentUserCloudDiskList() {
        List<String> sourceIds = permissionService.getSourceIds();
        MPJLambdaWrapper<VmCloudDisk> wrapper = new MPJLambdaWrapper<>();
        wrapper.in(!CurrentUserUtils.isAdmin() && CollectionUtils.isNotEmpty(sourceIds), VmCloudServer::getSourceId, sourceIds);
        wrapper.notIn(true, VmCloudDisk::getStatus, List.of(SpecialAttributesConstants.StatusField.DISK_DELETE, SpecialAttributesConstants.StatusField.FAILED));
        return iBaseVmCloudDiskService.list(wrapper);
    }


}
