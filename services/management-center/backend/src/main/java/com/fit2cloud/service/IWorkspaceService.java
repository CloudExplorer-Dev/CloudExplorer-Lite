package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.dao.entity.Organization;
import com.fit2cloud.dao.entity.Workspace;
import com.fit2cloud.dto.WorkspaceDTO;
import com.fit2cloud.request.PageWorkspaceRequest;
import com.fit2cloud.request.WorkspaceRequest;

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

}
