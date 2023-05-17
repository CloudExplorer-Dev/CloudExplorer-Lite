package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.base.service.IBaseUserRoleService;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.controller.request.workspace.PageWorkspaceRequest;
import com.fit2cloud.controller.request.workspace.WorkspaceBatchCreateRequest;
import com.fit2cloud.controller.request.workspace.WorkspaceRequest;
import com.fit2cloud.dao.entity.Workspace;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
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
    private IBaseUserRoleService userRoleService;

    @Resource
    private IBaseOrganizationService baseOrganizationService;

    @Override
    public Boolean create(WorkspaceRequest request) {
        List<Workspace> list = list(new LambdaQueryWrapper<Workspace>().eq(Workspace::getName, request.getName()));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new Fit2cloudException(ErrorCodeConstants.WORKSPACE_NAME_REPEAT.getCode(), ErrorCodeConstants.WORKSPACE_NAME_REPEAT.getMessage());
        }
        Workspace workspace = new Workspace();
        BeanUtils.copyProperties(request, workspace);
        workspace.setCreateTime(LocalDateTime.now());
        workspace.setUpdateTime(LocalDateTime.now());

        if (CurrentUserUtils.isOrgAdmin()) {
            List<String> orgIds = baseOrganizationService.getOrgAdminOrgIds();
            if (!orgIds.contains(request.getOrganizationId())) {
                throw new RuntimeException("没有权限在组织[" + baseOrganizationService.getById(request.getOrganizationId()).getName() + "]下创建工作空间");
            }
        }

        return save(workspace);
    }

    @Override
    public Boolean update(WorkspaceRequest request) {

        if (CurrentUserUtils.isOrgAdmin()) {
            List<String> orgIds = baseOrganizationService.getOrgAdminOrgIds();
            if (!orgIds.contains(request.getOrganizationId())) {
                throw new RuntimeException("没有权限在将工作空间移动到组织[" + baseOrganizationService.getById(request.getOrganizationId()).getName() + "]下");
            }
            if (CollectionUtils.isNotEmpty(orgIds)) {
                if (!list(new LambdaQueryWrapper<Workspace>().in(Workspace::getOrganizationId, orgIds))
                        .stream().map(Workspace::getId).toList()
                        .contains(request.getId())) {
                    throw new RuntimeException("没有权限修改该工作空间");
                }
            }
        }

        List<Workspace> list = list(new LambdaQueryWrapper<Workspace>()
                .eq(Workspace::getName, request.getName())
                .ne(Workspace::getId, request.getId()));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new Fit2cloudException(ErrorCodeConstants.WORKSPACE_NAME_REPEAT.getCode(), ErrorCodeConstants.WORKSPACE_NAME_REPEAT.getMessage());
        }
        Workspace workspace = getById(request.getId());
        if (Optional.ofNullable(workspace).isEmpty()) {
            throw new Fit2cloudException(ErrorCodeConstants.WORKSPACE_IS_NOT_EXIST.getCode(),
                    messageSource.getMessage(ErrorCodeConstants.WORKSPACE_IS_NOT_EXIST.getMessage(), null, LocaleContextHolder.getLocale()));
        }
        BeanUtils.copyProperties(request, workspace);
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
        Page<Workspace> page = PageUtil.of(request, Workspace.class, new OrderItem(ColumnNameUtil.getColumnName(Workspace::getCreateTime, true), false), true);
        QueryWrapper<Workspace> queryWrapper = addQuery(request);
        IPage<WorkspaceDTO> workspaceDTOs = baseMapper.pageList(page, queryWrapper);
        return workspaceDTOs;
    }

    private QueryWrapper<Workspace> addQuery(PageWorkspaceRequest request) {
        QueryWrapper<Workspace> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(request.getOrganizationName()), ColumnNameUtil.getColumnName(WorkspaceDTO::getOrganizationName, false), request.getOrganizationName());
        queryWrapper.like(StringUtils.isNotBlank(request.getName()), ColumnNameUtil.getColumnName(Workspace::getName, true), request.getName());
        queryWrapper.in(CollectionUtils.isNotEmpty(request.getOrganizationIds()), ColumnNameUtil.getColumnName(Workspace::getOrganizationId, true), request.getOrganizationIds());

        if (CurrentUserUtils.isOrgAdmin()) {
            QueryWrapper<Organization> wrapper = new QueryWrapper<>();
            wrapper.eq(StringUtils.isNotEmpty(CurrentUserUtils.getOrganizationId()), "id", CurrentUserUtils.getOrganizationId());
            List<String> orgIds = new ArrayList<>();
            orgIds.add(CurrentUserUtils.getOrganizationId());
            orgIds.addAll(baseOrganizationService.getDownOrganization(CurrentUserUtils.getOrganizationId(), baseOrganizationService.list()).stream().map(Organization::getId).toList());
            queryWrapper.in(ColumnNameUtil.getColumnName(Workspace::getOrganizationId, true), orgIds);
        }

        return queryWrapper;
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
        BeanUtils.copyProperties(workspace, workspaceDTO);
        return workspaceDTO;
    }

    @Override
    public Boolean delete(String id) {
        if (CurrentUserUtils.isOrgAdmin()) {
            List<String> orgIds = baseOrganizationService.getOrgAdminOrgIds();
            if (CollectionUtils.isNotEmpty(orgIds)) {
                if (!list(new LambdaQueryWrapper<Workspace>().in(Workspace::getOrganizationId, orgIds))
                        .stream().map(Workspace::getId).toList()
                        .contains(id)) {
                    throw new RuntimeException("没有权限删除该工作空间");
                }
            }
        }

        userRoleService.deleteUserRoleByWorkspaceId(id);
        return removeById(id);
    }

    @Override
    public Boolean batchDelete(List<Workspace> workspaces) {
        List<String> ids = Optional.ofNullable(workspaces).orElse(new ArrayList<>()).stream().map(Workspace::getId).collect(Collectors.toList());

        if (CurrentUserUtils.isOrgAdmin()) {
            List<String> orgIds = baseOrganizationService.getOrgAdminOrgIds();
            if (CollectionUtils.isNotEmpty(orgIds)) {
                for (String id : ids) {
                    if (!list(new LambdaQueryWrapper<Workspace>().in(Workspace::getOrganizationId, orgIds))
                            .stream().map(Workspace::getId).toList()
                            .contains(id)) {
                        throw new RuntimeException("没有权限删除ID为[" + id + "]的工作空间");
                    }
                }
            }
        }

        ids.forEach(id -> userRoleService.deleteUserRoleByWorkspaceId(id));
        return removeBatchByIds(ids);
    }

    @Override
    public List<Workspace> batch(WorkspaceBatchCreateRequest request) {

        if (CurrentUserUtils.isOrgAdmin()) {
            List<String> orgIds = baseOrganizationService.getOrgAdminOrgIds();
            if (!orgIds.contains(request.getOrganizationId())) {
                throw new RuntimeException("没有权限在组织[" + baseOrganizationService.getById(request.getOrganizationId()).getName() + "]下创建工作空间");
            }
        }

        List<String> names = request.getWorkspaceDetails().stream().map(WorkspaceBatchCreateRequest.WorkspaceDetails::getName).toList();
        List<Workspace> list = list(new LambdaQueryWrapper<Workspace>().in(Workspace::getName, names));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new Fit2cloudException(ErrorCodeConstants.WORKSPACE_NAME_REPEAT.getCode(), ErrorCodeConstants.WORKSPACE_NAME_REPEAT.getMessage(list.stream().map(Workspace::getName).toArray()));
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
        saveBatch(workspaces);
        return workspaces;
    }

    @Override
    public long countWorkspace() {
        QueryWrapper<Workspace> queryWrapper = addQuery(new PageWorkspaceRequest());
        return baseMapper.selectCount(queryWrapper);
    }
}
