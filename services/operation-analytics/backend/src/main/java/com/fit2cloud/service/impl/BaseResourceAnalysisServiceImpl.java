package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.fit2cloud.base.entity.Role;
import com.fit2cloud.base.entity.VmCloudHost;
import com.fit2cloud.base.service.IBaseVmCloudHostService;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.controller.request.datastore.PageDatastoreRequest;
import com.fit2cloud.controller.request.host.PageHostRequest;
import com.fit2cloud.dao.mapper.VmCloudDatastoreMapper;
import com.fit2cloud.dao.mapper.VmCloudHostMapper;
import com.fit2cloud.dto.VmCloudDatastoreDTO;
import com.fit2cloud.dto.VmCloudHostDTO;
import com.fit2cloud.request.role.RolePageRequest;
import com.fit2cloud.request.role.RoleRequest;
import com.fit2cloud.service.IBaseResourceAnalysisService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author jianneng
 * @date 2022/12/11 18:43
 **/
@Service
public class BaseResourceAnalysisServiceImpl implements IBaseResourceAnalysisService {

    @Resource
    private VmCloudHostMapper vmCloudHostMapper;
    @Resource
    private VmCloudDatastoreMapper vmCloudDatastoreMapper;
    public IPage<VmCloudHostDTO>  pageHost(PageHostRequest request){
        // 构建查询参数
        QueryWrapper<VmCloudHostDTO> wrapper = addQuery(request);
        Page<VmCloudHostDTO> page = new Page<>(request.getCurrentPage(), request.getPageSize(), true);
        IPage<VmCloudHostDTO> result = vmCloudHostMapper.pageList(page, wrapper);
        return result;
    }
    private QueryWrapper<VmCloudHostDTO> addQuery(PageHostRequest request) {
        QueryWrapper<VmCloudHostDTO> wrapper = new QueryWrapper<>();
        //排序
        if (request.getOrder() != null && StringUtils.isNotEmpty(request.getOrder().getColumn())) {
            wrapper.orderBy(true, request.getOrder().isAsc(), ColumnNameUtil.getColumnName(request.getOrder().getColumn(), VmCloudHostDTO.class));
        } else {
            wrapper.orderBy(true, false, "vm_cloud_host.create_time");
        }
        wrapper.like(StringUtils.isNotBlank(request.getHostName()), "vm_cloud_host.host_name", request.getHostName());
        return wrapper;
    }
    @Override
    public IPage<VmCloudDatastoreDTO> pageDatastore(PageDatastoreRequest request) {
        // 构建查询参数
        QueryWrapper<VmCloudDatastoreDTO> wrapper = addQueryDatastore(request);
        Page<VmCloudDatastoreDTO> page = new Page<>(request.getCurrentPage(), request.getPageSize(), true);
        IPage<VmCloudDatastoreDTO> result = vmCloudDatastoreMapper.pageList(page, wrapper);
        return result;
    }
    private QueryWrapper<VmCloudDatastoreDTO> addQueryDatastore(PageDatastoreRequest request) {
        QueryWrapper<VmCloudDatastoreDTO> wrapper = new QueryWrapper<>();
        //排序
        if (request.getOrder() != null && StringUtils.isNotEmpty(request.getOrder().getColumn())) {
            wrapper.orderBy(true, request.getOrder().isAsc(), ColumnNameUtil.getColumnName(request.getOrder().getColumn(), VmCloudDatastoreDTO.class));
        } else {
            wrapper.orderBy(true, false, "vm_cloud_datastore.create_time");
        }
        wrapper.like(StringUtils.isNotBlank(request.getDatastoreName()), "vm_cloud_datastore.datastore_name", request.getDatastoreName());
        return wrapper;
    }

}
