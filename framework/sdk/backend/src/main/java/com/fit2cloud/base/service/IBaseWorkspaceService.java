package com.fit2cloud.base.service;

import com.fit2cloud.base.entity.Workspace;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.response.NodeTree;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IBaseWorkspaceService extends IService<Workspace> {
     List<NodeTree> workspaceTree();
}
