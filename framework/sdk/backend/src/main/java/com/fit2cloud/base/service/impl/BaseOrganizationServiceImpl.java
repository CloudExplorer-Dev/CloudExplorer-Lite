package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.base.entity.Workspace;
import com.fit2cloud.base.mapper.BaseOrganizationMapper;
import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.base.service.IBaseWorkspaceService;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.OrganizationUtil;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.dto.UserRoleDto;
import com.fit2cloud.response.OrganizationTree;
import com.fit2cloud.response.SourceTreeObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
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
public class BaseOrganizationServiceImpl extends ServiceImpl<BaseOrganizationMapper, Organization> implements IBaseOrganizationService {
    private static final String TreeTypeOrganization = "ORGANIZATION";
    private static final String TreeTypeOrganizationAndWorkspace = "ORGANIZATION_AND_WORKSPACE";
    @Resource
    private IBaseWorkspaceService workspaceService;

    @Override
    public List<OrganizationTree> tree() {
        return OrganizationUtil.toTree(list());
    }

    @Override
    public List<SourceTreeObject> sourceTree(Map<RoleConstants.ROLE, List<UserRoleDto>> roleListMap) {

        //只返回有权限的工作空间
        List<String> workspaceIds = roleListMap.getOrDefault(RoleConstants.ROLE.USER, new ArrayList<>()).stream().map(UserRoleDto::getSource).toList();

        List<Workspace> workspaces = CollectionUtils.isEmpty(workspaceIds) ? new ArrayList<>() : workspaceService.list(new LambdaQueryWrapper<Workspace>().in(Workspace::getId, workspaceIds));

        //已知需要的组织id
        Set<String> orgIds = roleListMap.getOrDefault(RoleConstants.ROLE.ORGADMIN, new ArrayList<>()).stream().map(UserRoleDto::getSource).collect(Collectors.toSet());
        orgIds.addAll(workspaces.stream().map(Workspace::getOrganizationId).toList());

        List<Organization> orgList = list();
        //所有组织的Map
        Map<String, Organization> orgMap = orgList.stream().collect(Collectors.toMap(Organization::getId, organization -> organization));

        //接收最后需要的所有orgId
        Set<String> needOrgIds = new HashSet<>();
        for (String orgId : orgIds) {
            fillOrgIdSet(orgId, orgMap, needOrgIds);
        }

        List<SourceTreeObject> list = JsonUtil.parseArray(JsonUtil.toJSONString(
                OrganizationUtil.toTree(
                        orgList.stream().filter(organization -> needOrgIds.contains(organization.getId())).toList()
                )
        ), SourceTreeObject.class);

        appendSourceTree(list, workspaces);

        if (CollectionUtils.isNotEmpty(roleListMap.getOrDefault(RoleConstants.ROLE.ADMIN, new ArrayList<>()))) {
            List<SourceTreeObject> adminList = new ArrayList<>();
            adminList.add(new SourceTreeObject().setRoot(true).setLabel("云管理平台").setChildren(list));
            return adminList;
        }
        return list;
    }

    private void fillOrgIdSet(String orgId, Map<String, Organization> orgMap, Set<String> needOrgIds) {
        Organization organization = orgMap.get(orgId);
        if (organization != null) {
            needOrgIds.add(orgId);
        } else {
            return;
        }
        if (StringUtils.isNotBlank(organization.getPid())) {
            fillOrgIdSet(organization.getPid(), orgMap, needOrgIds);
        }
    }

    @Override
    public List<OrganizationTree> tree(String type) {
        UserDto credentials = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Organization> organizationList = list();
        List<OrganizationTree> organizationTree = new ArrayList<>();
        if (Objects.equals(credentials.getCurrentRole(), RoleConstants.ROLE.ORGADMIN) && StringUtils.isNotEmpty(credentials.getCurrentSource())) {
            organizationList = getDownOrganization(credentials.getCurrentSource(), new ArrayList<>(), organizationList);
            organizationTree = OrganizationUtil.toTree(organizationList, credentials.getCurrentSource());
        } else if (Objects.equals(credentials.getCurrentRole(), RoleConstants.ROLE.ADMIN)) {
            organizationTree = OrganizationUtil.toTree(organizationList);
        }

        if (StringUtils.isEmpty(type) || StringUtils.equals(type, TreeTypeOrganization)) {
            return organizationTree;
        }
        if (StringUtils.equals(type, TreeTypeOrganizationAndWorkspace)) {
            // 查询到所有的工作空间
            List<Workspace> workspaces = workspaceService.list();
            appendOrganizationTree(organizationTree, workspaces);
        }
        return organizationTree;
    }

    @Override
    public int getOrgLevel(String orgId) {
        List<Organization> upOrganization = getUpOrganization(orgId, new ArrayList<>());
        return upOrganization.size();
    }

    @Override
    public int getOrgLevel() {
        return getOrgLevel(tree(), new ArrayList<>(), 0);
    }

    public int getOrgLevel(List<OrganizationTree> source, List<Integer> levels, int currentLevel) {
        currentLevel++;
        for (OrganizationTree organizationTree : source) {
            levels.add(currentLevel);
            if (CollectionUtils.isNotEmpty(organizationTree.getChildren())) {
                getOrgLevel(organizationTree.getChildren(), levels, currentLevel);
            }
        }
        return levels.stream().max(Integer::compareTo).orElse(0);
    }

    /**
     * 获取上级组织
     *
     * @param orgId 组织id
     * @param res   返回值,传空数组
     * @return 当前组织及上级组织
     */
    private List<Organization> getUpOrganization(String orgId, List<Organization> res) {
        return getUpOrganization(orgId, res, id -> getById(orgId));
    }

    /**
     * 获取上级组织
     *
     * @param orgId            组织id
     * @param res              返回值,传空数组
     * @param findOrganization 根据id获取组织函数
     * @return 当前组织及上级组织
     */
    private List<Organization> getUpOrganization(String orgId, List<Organization> res, Function<String, Organization> findOrganization) {
        Organization organization = findOrganization.apply(orgId);
        res.add(organization);
        if (StringUtils.isNotEmpty(organization.getPid())) {
            return getUpOrganization(organization.getPid(), res);
        }
        return res;
    }


    @Override
    public List<Organization> getDownOrganization(String orgId, List<Organization> allOrgTree) {
        return getDownOrganization(orgId, new ArrayList<>(), allOrgTree);
    }

    /**
     * 获取下级组织
     *
     * @param orgId      组织id
     * @param res        返回值,传空数组
     * @param allOrgTree 全量数据
     * @return 当前组织的下级组织 包含当前组织
     */
    private List<Organization> getDownOrganization(String orgId, List<Organization> res, List<Organization> allOrgTree) {
        allOrgTree.stream().filter(organization -> StringUtils.equals(organization.getId(), orgId)).findFirst().ifPresent(org -> {
            res.add(org);
        });
        return getDownOrganization(orgId, res, (pid) -> allOrgTree.stream().filter(organization -> StringUtils.equals(organization.getPid(), pid)).toList());
    }

    /**
     * 获取指定组织的下级组织
     *
     * @param orgId                组织id
     * @param res                  返回值,传数组
     * @param findDownOrganization 根据组织id 查询到组织到下一级到所有组织
     * @return 当前组织下面到所有组织 不包含当前组织
     */
    private List<Organization> getDownOrganization(String orgId, List<Organization> res, Function<String, List<Organization>> findDownOrganization) {
        List<Organization> organizationList = findDownOrganization.apply(orgId);
        res.addAll(organizationList);
        for (Organization organization : organizationList) {
            getDownOrganization(organization.getId(), res, findDownOrganization);
        }
        return res;
    }

    private void appendOrganizationTree(List<OrganizationTree> organizationTree, List<Workspace> workspaces) {
        for (OrganizationTree tree : organizationTree) {
            List<Workspace> childWorkspaces = workspaces.stream().filter(workspace -> StringUtils.equals(tree.getId(), workspace.getOrganizationId())).toList();
            tree.setWorkspaces(childWorkspaces);
            if (CollectionUtils.isNotEmpty(tree.getChildren())) {
                appendOrganizationTree(tree.getChildren(), workspaces);
            }
        }
    }

    private void appendSourceTree(List<SourceTreeObject> organizationTree, List<Workspace> workspaces) {
        for (SourceTreeObject object : organizationTree) {
            object.setLabel(object.getName());

            if (object.isWorkspace()) {
                continue;
            }

            for (Workspace workspace : workspaces) {
                if (!StringUtils.equals(object.getId(), workspace.getOrganizationId())) {
                    continue;
                }
                if (object.getChildren() == null) {
                    object.setChildren(new ArrayList<>());
                }
                object.getChildren().add(new SourceTreeObject()
                        .setId(workspace.getId())
                        .setPid(workspace.getOrganizationId())
                        .setName(workspace.getName())
                        .setLabel(workspace.getName())
                        .setWorkspace(true)
                );

            }

            if (CollectionUtils.isNotEmpty(object.getChildren())) {
                appendSourceTree(object.getChildren(), workspaces);
            }
        }
    }
}
