package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.UserRole;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.controller.request.OrganizationBatchRequest;
import com.fit2cloud.controller.request.workspace.PageWorkspaceRequest;
import com.fit2cloud.controller.request.workspace.WorkspaceBatchCreateRequest;
import com.fit2cloud.controller.request.workspace.WorkspaceRequest;
import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.dao.entity.Workspace;
import com.fit2cloud.dao.mapper.OrganizationMapper;
import com.fit2cloud.dao.mapper.WorkspaceMapper;
import com.fit2cloud.dto.WorkspaceDTO;
import com.fit2cloud.service.IOrganizationService;
import com.fit2cloud.service.IWorkspaceService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Autowired
    private MessageSource messageSource;

    @Resource
    private IOrganizationService organizationService;
    @Override
    public Boolean create(WorkspaceRequest request) {
        List<Workspace> list = list(new LambdaQueryWrapper<Workspace>().eq(Workspace::getName,request.getName()));
        if(CollectionUtils.isNotEmpty(list)){
            throw new Fit2cloudException(ErrorCodeConstants.WORKSPACE_NAME_REPEAT.getCode(),
                    messageSource.getMessage(ErrorCodeConstants.WORKSPACE_NAME_REPEAT.getMessage(),null, LocaleContextHolder.getLocale()));
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
            throw new Fit2cloudException(ErrorCodeConstants.WORKSPACE_NAME_REPEAT.getCode(),
                    messageSource.getMessage(ErrorCodeConstants.WORKSPACE_NAME_REPEAT.getMessage(),null,LocaleContextHolder.getLocale()));
        }
        Workspace workspace = getById(request.getId());
        if(Optional.ofNullable(workspace).isEmpty()){
            throw new Fit2cloudException(ErrorCodeConstants.WORKSPACE_IS_NOT_EXIST.getCode(),
                    messageSource.getMessage(ErrorCodeConstants.WORKSPACE_IS_NOT_EXIST.getMessage(),null,LocaleContextHolder.getLocale()));
        }
        BeanUtils.copyProperties(request,workspace);
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
        //根据组织名称模糊查询
        if(StringUtils.isNotEmpty(request.getOrganizationName())){
            queryWrapper.like("organizationName",request.getOrganizationName());
        }
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
            throw new Fit2cloudException(ErrorCodeConstants.WORKSPACE_ID_AND_NAME_REQUIRED.getCode(),
                    messageSource.getMessage(ErrorCodeConstants.WORKSPACE_ID_AND_NAME_REQUIRED.getMessage(),null,LocaleContextHolder.getLocale()));
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

    @Override
    public Boolean batch(WorkspaceBatchCreateRequest request) {
        List<String> names = request.getWorkspaceDetails().stream().map(WorkspaceBatchCreateRequest.WorkspaceDetails::getName).toList();
        List<Workspace> list = list(new LambdaQueryWrapper<Workspace>().in(Workspace::getName, names));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new Fit2cloudException(ErrorCodeConstants.WORKSPACE_NAME_REPEAT.getCode(), messageSource.getMessage(ErrorCodeConstants.WORKSPACE_NAME_REPEAT.getMessage(),
                    new Object[]{JsonUtil.toJSONString(list.stream().map(Workspace::getName).collect(Collectors.toList()))}, LocaleContextHolder.getLocale()));
        }
        List<Workspace> workspaces = request.getWorkspaceDetails().stream().map(workspaceDetails -> {
            Workspace workspace = new Workspace();
            workspace.setName(workspaceDetails.getName());
            workspace.setDescription(workspaceDetails.getDescription());
            workspace.setOrganizationId(request.getOrganizationId());
            workspace.setCreateTime(LocalDateTime.now());
            workspace.setUpdateTime(LocalDateTime.now());
            return workspace;
        }).toList();
        return saveBatch(workspaces);
    }
}
