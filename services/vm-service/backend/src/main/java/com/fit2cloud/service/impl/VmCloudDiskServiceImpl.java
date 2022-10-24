package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseVmCloudDiskMapper;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.base.service.IBaseVmCloudServerService;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.controller.request.disk.PageVmCloudDiskRequest;
import com.fit2cloud.dao.mapper.VmCloudDiskMapper;
import com.fit2cloud.dao.mapper.VmCloudServerMapper;
import com.fit2cloud.dto.VmCloudDiskDTO;
import com.fit2cloud.dto.VmCloudServerDTO;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.constants.DeleteWithInstance;
import com.fit2cloud.provider.constants.F2CDiskStatus;
import com.fit2cloud.provider.impl.vsphere.util.ResourceConstants;
import com.fit2cloud.service.IVmCloudDiskService;
import com.fit2cloud.service.OrganizationCommonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class VmCloudDiskServiceImpl extends ServiceImpl<BaseVmCloudDiskMapper, VmCloudDisk> implements IVmCloudDiskService {
    @Resource
    private OrganizationCommonService organizationCommonService;
    @Resource
    private VmCloudDiskMapper diskMapper;
    @Resource
    private VmCloudServerMapper serverMapper;
    @Resource
    private IBaseCloudAccountService cloudAccountService;
    @Resource
    private IBaseVmCloudServerService vmCloudServerService;

    @Override
    public IPage<VmCloudDiskDTO> pageVmCloudDisk(PageVmCloudDiskRequest request) {
        // 普通用户
        if (CurrentUserUtils.isUser()) {
            request.setWorkspaceId(CurrentUserUtils.getWorkspaceId());
        }
        // 组织管理员
        if (CurrentUserUtils.isOrgAdmin()) {
            request.setOrganizationId(CurrentUserUtils.getOrganizationId());
            request.setOrganizationIds(organizationCommonService.getOrgIdsByParentId(CurrentUserUtils.getOrganizationId()));
        }
        // 构建查询参数
        QueryWrapper<VmCloudDiskDTO> wrapper = addQuery(request);
        Page<VmCloudDiskDTO> page = new Page<>(request.getCurrentPage(), request.getPageSize(), true);
        IPage<VmCloudDiskDTO> result = diskMapper.pageList(page, wrapper);
        return result;
    }

    private QueryWrapper<VmCloudDiskDTO> addQuery(PageVmCloudDiskRequest request) {
        QueryWrapper<VmCloudDiskDTO> wrapper = new QueryWrapper<>();
        //排序
        if (request.getOrder() != null && StringUtils.isNotEmpty(request.getOrder().getColumn())) {
            wrapper.orderBy(true, request.getOrder().isAsc(), ColumnNameUtil.getColumnName(request.getOrder().getColumn(), VmCloudDiskDTO.class));
        } else {
            wrapper.orderBy(true, false, "vm_cloud_disk.update_time");
        }
        wrapper.like(StringUtils.isNotBlank(request.getWorkspaceId()), "vm_cloud_disk.workspace_id", request.getWorkspaceId());
        //wrapper.in(CollectionUtils.isNotEmpty(request.getOrganizationIds()),"vm_cloud_disk.organization_id",request.getOrganizationIds());
        wrapper.like(StringUtils.isNotBlank(request.getDiskName()), "vm_cloud_disk.disk_name", request.getDiskName());
        wrapper.like(StringUtils.isNotBlank(request.getAccountName()), "cloud_account.name", request.getAccountName());
        wrapper.like(StringUtils.isNotBlank(request.getVmInstanceName()), "vm_cloud_server.instance_name", request.getVmInstanceName());
        return wrapper;
    }

    public List<VmCloudServerDTO> cloudServerList(String accountId) {
        return serverMapper.selectListByAccountId(accountId);
    }

    public VmCloudDiskDTO cloudDisk(String id) {
        return diskMapper.selectDiskDetailById(id);
    }

    public boolean enlarge(String id, long newDiskSize) {
        try {
            VmCloudDisk vmCloudDisk = baseMapper.selectById(id);
            CloudAccount cloudAccount = cloudAccountService.getById(vmCloudDisk.getAccountId());

            HashMap<String, Object> params = CommonUtil.getParams(cloudAccount.getCredential(), vmCloudDisk.getRegion());
            params.put("diskId", vmCloudDisk.getDiskId());
            params.put("newDiskSize", newDiskSize);
            params.put("instanceUuid", vmCloudDisk.getInstanceUuid());// For Vmware
            Boolean result = CommonUtil.exec(cloudAccount.getPlatform(), ICloudProvider::of, JsonUtil.toJSONString(params), ICloudProvider::enlargeDisk);

            if (result) {
                vmCloudDisk.setSize(newDiskSize);
                baseMapper.updateById(vmCloudDisk);
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to enlarge cloud disk: " + e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = {Exception.class})
    public boolean attach(String id, String instanceUuid, Boolean deleteWithInstance) {
        try {
            VmCloudDisk vmCloudDisk = baseMapper.selectById(id);
            CloudAccount cloudAccount = cloudAccountService.getById(vmCloudDisk.getAccountId());

            HashMap<String, Object> params = CommonUtil.getParams(cloudAccount.getCredential(), vmCloudDisk.getRegion());
            params.put("diskId", vmCloudDisk.getDiskId());
            params.put("instanceUuid", instanceUuid);
            params.put("deleteWithInstance", DeleteWithInstance.NO.name());
            if (deleteWithInstance) {
                params.put("deleteWithInstance", DeleteWithInstance.YES.name());
            }
            Boolean result = CommonUtil.exec(cloudAccount.getPlatform(), ICloudProvider::of, JsonUtil.toJSONString(params), ICloudProvider::attachDisk);

            if (result) {
                vmCloudDisk.setStatus(F2CDiskStatus.IN_USE);
                vmCloudDisk.setInstanceUuid(instanceUuid);
//            vmCloudDisk.setDevice(disk.getDevice());
                vmCloudDisk.setDeleteWithInstance(DeleteWithInstance.NO.name());
                if (deleteWithInstance) {
                    vmCloudDisk.setDeleteWithInstance(DeleteWithInstance.YES.name());
                }
                baseMapper.updateById(vmCloudDisk);
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to attach cloud disk: " + e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = {Exception.class})
    public boolean detach(String id) {
        VmCloudDisk vmCloudDisk = baseMapper.selectById(id);
        CloudAccount cloudAccount = cloudAccountService.getById(vmCloudDisk.getAccountId());

        QueryWrapper<VmCloudServer> wrapper = new QueryWrapper<VmCloudServer>()
                .eq(ColumnNameUtil.getColumnName(VmCloudServer::getInstanceUuid, true), vmCloudDisk.getInstanceUuid());
        VmCloudServer vmCloudServer = vmCloudServerService.getOne(wrapper);

        if (vmCloudServer == null) {
            throw new RuntimeException("Failed to detach cloud disk! Virtual Machine is not exist.");
        }

        HashMap<String, Object> params = CommonUtil.getParams(cloudAccount.getCredential(), vmCloudDisk.getRegion());
        params.put("diskId", vmCloudDisk.getDiskId());
        params.put("instanceUuid", vmCloudDisk.getInstanceUuid());

        try {
            Boolean result = CommonUtil.exec(cloudAccount.getPlatform(), ICloudProvider::of, JsonUtil.toJSONString(params), ICloudProvider::detachDisk);
            if (result) {
                vmCloudDisk.setStatus(F2CDiskStatus.AVAILABLE);
                vmCloudDisk.setInstanceUuid(ResourceConstants.NO_INSTANCE);
                vmCloudDisk.setDevice(null);
                vmCloudDisk.setDeleteWithInstance(null);
                baseMapper.updateById(vmCloudDisk);
                return true;
            } else {
                throw new RuntimeException("Result is Empty!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to detach cloud disk!" + e.getMessage(), e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = {Exception.class})
    public boolean delete(String id) {
        VmCloudDisk vmCloudDisk = baseMapper.selectById(id);
        CloudAccount cloudAccount = cloudAccountService.getById(vmCloudDisk.getAccountId());

        HashMap<String, Object> params = CommonUtil.getParams(cloudAccount.getCredential(), vmCloudDisk.getRegion());
        params.put("diskId", vmCloudDisk.getDiskId());
        try {
            Boolean result = CommonUtil.exec(cloudAccount.getPlatform(), ICloudProvider::of, JsonUtil.toJSONString(params), ICloudProvider::deleteDisk);
            if (result) {
                vmCloudDisk.setStatus(F2CDiskStatus.DELETED);
                baseMapper.updateById(vmCloudDisk);
                return true;
            } else {
                throw new RuntimeException("Result is empty!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete cloud disk!" + e.getMessage(), e);
        }
    }
}
