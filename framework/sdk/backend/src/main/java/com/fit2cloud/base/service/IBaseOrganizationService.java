package com.fit2cloud.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.response.OrganizationTree;
import com.fit2cloud.response.SourceTreeObject;

import java.util.List;

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

    List<SourceTreeObject> sourceTree();

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
}
