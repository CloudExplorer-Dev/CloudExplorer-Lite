package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.controller.request.workspace.PageWorkspaceRequest;
import com.fit2cloud.controller.request.workspace.WorkspaceBatchCreateRequest;
import com.fit2cloud.controller.request.workspace.WorkspaceRequest;
import com.fit2cloud.dao.entity.Workspace;
import com.fit2cloud.dto.WorkspaceDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
public interface IWorkspaceService extends IService<Workspace> {

    Boolean create(WorkspaceRequest request);

    Boolean update(WorkspaceRequest request);

    IPage<WorkspaceDTO> pageWorkspace(PageWorkspaceRequest request);

    WorkspaceDTO getOne(String id, String name);

    Boolean batchDelete(List<Workspace> workspaces);

    Boolean batch(WorkspaceBatchCreateRequest request);

}
