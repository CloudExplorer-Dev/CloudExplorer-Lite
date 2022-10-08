package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseVmCloudServerMapper;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.OrganizationUtil;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.vm.PageVmCloudServerRequest;
import com.fit2cloud.dao.mapper.VmCloudServerMapper;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.dto.VmCloudServerDTO;
import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.service.CommonService;
import com.fit2cloud.service.IVmCloudServerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.service.OrganizationCommonService;
import com.sun.nio.sctp.HandlerResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class VmCloudServerServiceImpl extends ServiceImpl<BaseVmCloudServerMapper, VmCloudServer> implements IVmCloudServerService {

    @Resource
    private OrganizationCommonService organizationCommonService;
    @Resource
    private VmCloudServerMapper vmCloudServerMapper;

    @Override
    public IPage<VmCloudServerDTO> pageVmCloudServer(PageVmCloudServerRequest request) {
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
        QueryWrapper<VmCloudServerDTO> wrapper = addQuery(request);
        Page<VmCloudServerDTO> page = new Page<>(request.getCurrentPage(), request.getPageSize(), true);
        IPage<VmCloudServerDTO> result = vmCloudServerMapper.pageList(page,wrapper);
        return result;
    }

    private QueryWrapper<VmCloudServerDTO> addQuery(PageVmCloudServerRequest request){
        QueryWrapper<VmCloudServerDTO> wrapper = new QueryWrapper<>();
        //排序
        if (request.getOrder() != null && StringUtils.isNotEmpty(request.getOrder().getColumn())) {
            wrapper.orderBy(true, request.getOrder().isAsc(), ColumnNameUtil.getColumnName(request.getOrder().getColumn(), VmCloudServerDTO.class));
        } else {
            wrapper.orderBy(true, false, "vm_cloud_server.update_time");
        }
        wrapper.like(StringUtils.isNotBlank(request.getWorkspaceId()),"vm_cloud_disk.workspace_id",request.getWorkspaceId());
        //wrapper.in(CollectionUtils.isNotEmpty(request.getOrganizationIds()),"vm_cloud_disk.organization_id",request.getOrganizationIds());
        wrapper.like(StringUtils.isNotBlank(request.getInstanceName()),"vm_cloud_server.instance_name",request.getInstanceName());
        wrapper.like(StringUtils.isNotBlank(request.getAccountName()),"cloud_account.name",request.getAccountName());
        wrapper.like(StringUtils.isNotBlank(request.getIpArray()),"vm_cloud_server.ip_array",request.getIpArray());

        wrapper.in(CollectionUtils.isNotEmpty(request.getInstanceStatus()),"vm_cloud_server.instance_status",request.getInstanceStatus());
        return wrapper;
    }
}
