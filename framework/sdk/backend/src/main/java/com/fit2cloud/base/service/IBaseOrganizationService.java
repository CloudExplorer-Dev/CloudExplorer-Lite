package com.fit2cloud.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.Organization;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.request.OrganizationBatchRequest;
import com.fit2cloud.request.PageOrganizationRequest;
import com.fit2cloud.response.OrganizationTree;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
public interface IBaseOrganizationService extends IService<Organization> {
   /**
    * 分页查询到组织
    * @param request 查询请求对象
    * @return        分页对象
    */
   IPage<Organization> pageOrganization(PageOrganizationRequest request);

    /**
     * 获取组织树
     * @return
     */
    List<OrganizationTree> tree();

    Boolean batch(OrganizationBatchRequest request);


    boolean removeBatchTreeByIds(List<Organization> organizationIds);

    boolean removeTreeById(String id);

    /**
     * 根据名称或者id查询一条组织记录
     * @param id     组织主键id
     * @param name   组织名称
     * @return       组织对象
     */
    Organization getOne(String id, String name);
}
