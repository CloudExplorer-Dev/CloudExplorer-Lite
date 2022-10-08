package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.base.mapper.BaseVmCloudDiskMapper;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.controller.request.disk.PageVmCloudDiskRequest;
import com.fit2cloud.dao.mapper.VmCloudDiskMapper;
import com.fit2cloud.dto.VmCloudDiskDTO;
import com.fit2cloud.dto.VmCloudServerDTO;
import com.fit2cloud.service.IVmCloudDiskService;
import com.fit2cloud.service.OrganizationCommonService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
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
        IPage<VmCloudDiskDTO> result = diskMapper.pageList(page,wrapper);
        return result;
    }

    private QueryWrapper<VmCloudDiskDTO> addQuery(PageVmCloudDiskRequest request){
        QueryWrapper<VmCloudDiskDTO> wrapper = new QueryWrapper<>();
        //排序
        if (request.getOrder() != null && StringUtils.isNotEmpty(request.getOrder().getColumn())) {
            wrapper.orderBy(true, request.getOrder().isAsc(), ColumnNameUtil.getColumnName(request.getOrder().getColumn(), VmCloudDiskDTO.class));
        }else {
            wrapper.orderBy(true, false, "vm_cloud_disk.update_time");
        }
        wrapper.like(StringUtils.isNotBlank(request.getWorkspaceId()),"vm_cloud_disk.workspace_id",request.getWorkspaceId());
        //wrapper.in(CollectionUtils.isNotEmpty(request.getOrganizationIds()),"vm_cloud_disk.organization_id",request.getOrganizationIds());
        wrapper.like(StringUtils.isNotBlank(request.getDiskName()),"vm_cloud_disk.disk_name",request.getDiskName());
        wrapper.like(StringUtils.isNotBlank(request.getAccountName()),"cloud_account.name",request.getAccountName());
        wrapper.like(StringUtils.isNotBlank(request.getVmInstanceName()),"vm_cloud_server.instance_name",request.getVmInstanceName());
        return wrapper;
    }
}
