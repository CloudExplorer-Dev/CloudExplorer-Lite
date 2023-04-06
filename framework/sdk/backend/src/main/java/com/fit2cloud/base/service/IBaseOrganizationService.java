package com.fit2cloud.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.dto.UserRoleDto;
import com.fit2cloud.response.OrganizationTree;
import com.fit2cloud.response.SourceTreeObject;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IBaseOrganizationService extends IService<Organization> {

    /**
     * 获取组织树
     *
     * @return
     */
    List<OrganizationTree> tree();

    List<SourceTreeObject> sourceTree(Map<RoleConstants.ROLE, List<UserRoleDto>> roleListMap);

    /**
     * 根据类型获取树形结构
     *
     * @param type
     * @return
     */
    List<OrganizationTree> tree(String type);

    /**
     * 获取组织级别 根据组织id
     *
     * @param orgId 根据组织id获取组织级别
     * @return 组织级别
     */
    int getOrgLevel(String orgId);

    /**
     * 获取组织的层级
     *
     * @return 组织层级
     */
    int getOrgLevel();

    List<Organization> getDownOrganization(String orgId, List<Organization> allOrgTree);
}
