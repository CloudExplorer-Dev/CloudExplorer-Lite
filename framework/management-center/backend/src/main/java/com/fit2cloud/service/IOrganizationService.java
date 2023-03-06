package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.request.OrganizationBatchRequest;
import com.fit2cloud.controller.request.OrganizationRequest;
import com.fit2cloud.controller.request.PageOrganizationRequest;
import com.fit2cloud.base.entity.Organization;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IOrganizationService extends IService<Organization> {
    /**
     * 分页查询到组织
     *
     * @param request 查询请求对象
     * @return 分页对象
     */
    IPage<Organization> pageOrganization(PageOrganizationRequest request);


    /**
     * 批量添加
     *
     * @param request 批量添加的请求参数
     * @return 是否添加成功
     */
    Boolean batch(OrganizationBatchRequest request);


    /**
     * 删除组织 ,会根据传入组织的树结构从下到上进行删除
     *
     * @param organizations 需要删除的组织
     * @return 是否删除成功
     */
    boolean removeBatchTreeByIds(List<Organization> organizations);

    /**
     * 删除一个组织根据id
     *
     * @param id 组织id
     * @return 是否删除成功
     */
    boolean removeTreeById(String id);

    /**
     * 根据名称或者id查询一条组织记录
     *
     * @param id   组织主键id
     * @param name 组织名称
     * @return 组织对象
     */
    Organization getOne(String id, String name);

    /**
     * 更新一条数据
     * @param request 请求对象
     * @return        是否更新成功
     */
     boolean update(OrganizationRequest request);
}
