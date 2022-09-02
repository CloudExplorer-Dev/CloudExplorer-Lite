package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.base.entity.Workspace;
import com.fit2cloud.base.mapper.BaseOrganizationMapper;
import com.fit2cloud.base.mapper.BaseWorkspaceMapper;
import com.fit2cloud.base.service.IBaseWorkspaceService;
import com.fit2cloud.response.NodeTree;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class BaseWorkspaceServiceImpl extends ServiceImpl<BaseWorkspaceMapper, Workspace> implements IBaseWorkspaceService {
    @Resource
    BaseOrganizationMapper baseOrganizationMapper;

    public List<NodeTree> workspaceTree() {
        // 查询到所有的组织
        List<Organization> organizations = baseOrganizationMapper.selectList(new QueryWrapper<>());

        // 查询到所有的工作空间
        List<Workspace> workspaces = list();

        return toTree(organizations,workspaces);
    }

    public static List<NodeTree> toTree(List<Organization> orgList, List<Workspace> workspaceList) {
        List<NodeTree> nodeTrees = orgList.stream().map(organization -> {
            NodeTree orgNodeTree = new NodeTree();
            BeanUtils.copyProperties(organization, orgNodeTree);

            //添加工作空间节点
            if(CollectionUtils.isNotEmpty(workspaceList)){
                for (Workspace workspace : workspaceList) {
                    if (workspace.getOrganizationId().equals(orgNodeTree.getId())) {
                        NodeTree workspaceNodeTree = new NodeTree();
                        BeanUtils.copyProperties(workspace, workspaceNodeTree);
                        if (CollectionUtils.isNotEmpty(orgNodeTree.getChildren())) {
                            orgNodeTree.getChildren().add(workspaceNodeTree);
                        } else {
                            orgNodeTree.setChildren(new ArrayList<>() {{
                                add(workspaceNodeTree);
                            }});
                        }
                    }
                }
            }
            return orgNodeTree;
        }).filter(nodeTree->CollectionUtils.isNotEmpty(nodeTree.getChildren())).toList();

        for (NodeTree nodeTree : nodeTrees) {
            if (StringUtils.isNotEmpty(nodeTree.getPid())) {
                Optional<NodeTree> first = nodeTrees.stream().filter(org -> {
                    return org.getId().equals(nodeTree.getPid());
                }).findFirst();
                if (first.isPresent()) {
                    if (CollectionUtils.isNotEmpty(first.get().getChildren())) {
                        first.get().getChildren().add(nodeTree);
                    } else {
                        first.get().setChildren(new ArrayList<>() {{
                            add(nodeTree);
                        }});
                    }
                }
            }
        }
        return nodeTrees.stream().filter(nodeTree -> StringUtils.isEmpty(nodeTree.getPid())).toList();
    }
}
