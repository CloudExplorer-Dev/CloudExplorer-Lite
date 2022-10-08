package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.VmCloudImage;
import com.fit2cloud.base.mapper.BaseVmCloudImageMapper;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.controller.request.disk.PageVmCloudDiskRequest;
import com.fit2cloud.controller.request.images.PageVmCloudImageRequest;
import com.fit2cloud.dao.mapper.VmCloudDiskMapper;
import com.fit2cloud.dao.mapper.VmCloudImageMapper;
import com.fit2cloud.dto.VmCloudDiskDTO;
import com.fit2cloud.dto.VmCloudImageDTO;
import com.fit2cloud.service.IVmCloudImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.service.OrganizationCommonService;
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
public class VmCloudImageServiceImpl extends ServiceImpl<BaseVmCloudImageMapper, VmCloudImage> implements IVmCloudImageService {

    @Resource
    private OrganizationCommonService organizationCommonService;
    @Resource
    private VmCloudImageMapper imageMapper;
    @Override
    public IPage<VmCloudImageDTO> pageVmCloudImage(PageVmCloudImageRequest request) {
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
        QueryWrapper<VmCloudImageDTO> wrapper = addQuery(request);
        Page<VmCloudImageDTO> page = new Page<>(request.getCurrentPage(), request.getPageSize(), true);
        IPage<VmCloudImageDTO> result = imageMapper.pageList(page,wrapper);
        return result;
    }

    private QueryWrapper<VmCloudImageDTO> addQuery(PageVmCloudImageRequest request){
        QueryWrapper<VmCloudImageDTO> wrapper = new QueryWrapper<>();
        //排序
        if (request.getOrder() != null && StringUtils.isNotEmpty(request.getOrder().getColumn())) {
            wrapper.orderBy(true, request.getOrder().isAsc(), ColumnNameUtil.getColumnName(request.getOrder().getColumn(), VmCloudImageDTO.class));
        } else {
            wrapper.orderBy(true, false, "vm_cloud_image.update_time");
        }
        wrapper.like(StringUtils.isNotBlank(request.getWorkspaceId()),"vm_cloud_image.workspace_id",request.getWorkspaceId());
        wrapper.like(StringUtils.isNotBlank(request.getImageName()),"vm_cloud_image.image_name",request.getImageName());
        wrapper.like(StringUtils.isNotBlank(request.getAccountName()),"cloud_account.name",request.getAccountName());
        return wrapper;
    }
}
