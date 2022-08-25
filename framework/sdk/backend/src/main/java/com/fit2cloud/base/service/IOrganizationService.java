package com.fit2cloud.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.Organization;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.request.PageOrganizationRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
public interface IOrganizationService extends IService<Organization> {
   /**
    * 分页查询到组织
    * @param request 查询请求对象
    * @return        分页对象
    */
   IPage<Organization> pageOrganization(PageOrganizationRequest request);
}
