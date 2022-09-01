package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.dao.entity.Organization;
import com.fit2cloud.dao.entity.Workspace;
import com.fit2cloud.dao.mapper.WorkspaceMapper;
import com.fit2cloud.dto.WorkspaceDTO;
import com.fit2cloud.request.PageWorkspaceRequest;
import com.fit2cloud.request.WorkspaceRequest;
import com.fit2cloud.service.IWorkspaceService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
@Service
public class WorkspaceServiceImpl extends ServiceImpl<WorkspaceMapper, Workspace> implements IWorkspaceService {

    @Override
    public Boolean create(WorkspaceRequest request) {
        List<Workspace> list = list(new LambdaQueryWrapper<Workspace>().eq(Workspace::getName,request.getName()));
        if(CollectionUtils.isNotEmpty(list)){
            throw new Fit2cloudException(ErrorCodeConstants.WORKSPACE_NAME_REPEAT.getCode(), ErrorCodeConstants.WORKSPACE_NAME_REPEAT.getMessage());
        }
        Workspace workspace = new Workspace();
        BeanUtils.copyProperties(request,workspace);
        workspace.setCreateTime(LocalDateTime.now());
        workspace.setUpdateTime(LocalDateTime.now());
        return save(workspace);
    }

    @Override
    public Boolean update(WorkspaceRequest request) {
        List<Workspace> list = list(new LambdaQueryWrapper<Workspace>()
                .eq(Workspace::getName,request.getName())
                .ne(Workspace::getId,request.getId()));
        if(CollectionUtils.isNotEmpty(list)){
            throw new Fit2cloudException(ErrorCodeConstants.WORKSPACE_NAME_REPEAT.getCode(), ErrorCodeConstants.WORKSPACE_NAME_REPEAT.getMessage());
        }
        Workspace workspace = getById(request.getId());
        if(Optional.ofNullable(workspace).isEmpty()){
            throw new Fit2cloudException(ErrorCodeConstants.WORKSPACE_IS_NOT_EXIST.getCode(),ErrorCodeConstants.WORKSPACE_IS_NOT_EXIST.getMessage());
        }
        BeanUtils.copyProperties(request,workspace);
        workspace.setCreateTime(LocalDateTime.now());
        workspace.setUpdateTime(LocalDateTime.now());
        return updateById(workspace);
    }

    /**
    * @description: TODO 查询还需要优化，根据时间、组织查询还没有弄
    * @author jianneng
    * @date: 2022/9/1 10:14
    */
    @Override
    public IPage<WorkspaceDTO> pageWorkspace(PageWorkspaceRequest request) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Page<Workspace> page = new Page<>(request.getCurrentPage(), request.getPageSize(), true);
        QueryWrapper<Workspace> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(request.getName())){
            queryWrapper.like("_name",request.getName());
        }
        if (CollectionUtils.isNotEmpty(request.getUpdateTime())) {
            queryWrapper.between("update_time", simpleDateFormat.format(request.getUpdateTime().get(0)), simpleDateFormat.format(request.getUpdateTime().get(1)));
        }
        if (CollectionUtils.isNotEmpty(request.getCreateTime())) {
            queryWrapper.between("create_time", simpleDateFormat.format(request.getCreateTime().get(0)), simpleDateFormat.format(request.getCreateTime().get(1)));
        }
        if (request.getOrder() != null && StringUtils.isNotEmpty(request.getOrder().getColumn())) {
            queryWrapper.orderBy(false, request.getOrder().isAsc(), request.getOrder().getColumn());
        }
        QueryWrapper<Workspace> pageWrapper = queryWrapper.clone();
        IPage<WorkspaceDTO> organizations = baseMapper.pageList(page, pageWrapper);
        return organizations;
    }

    @Override
    public WorkspaceDTO getOne(String id, String name) {
        if (StringUtils.isEmpty(id) && StringUtils.isEmpty(name)) {
            throw new Fit2cloudException(ErrorCodeConstants.WORKSPACE_ID_AND_NAME_REQUIRED.getCode(), ErrorCodeConstants.WORKSPACE_ID_AND_NAME_REQUIRED.getMessage());
        }
        LambdaQueryWrapper<Workspace> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(id)) {
            wrapper.eq(Workspace::getId, id);
        }
        if (StringUtils.isNotEmpty(name)) {
            wrapper.eq(Workspace::getName, name);
        }
        Workspace workspace = getOne(wrapper);
        WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        BeanUtils.copyProperties(workspace,workspaceDTO);
        return workspaceDTO;
    }

    @Override
    public Boolean batchDelete(List<Workspace> workspaces) {
        List<String> ids = Optional.ofNullable(workspaces).orElse(new ArrayList<>()).stream().map(Workspace::getId).collect(Collectors.toList());
        return removeBatchByIds(ids);
    }
}
