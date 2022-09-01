package com.fit2cloud.base.service;

import com.fit2cloud.base.entity.Organization;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.response.OrganizationTree;

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

    /**
     * 根据类型获取树形结构
     * @param type
     * @return
     */
    List<OrganizationTree> tree(String type);
}
