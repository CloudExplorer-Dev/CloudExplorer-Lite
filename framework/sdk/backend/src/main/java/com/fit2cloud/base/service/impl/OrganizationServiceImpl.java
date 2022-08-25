package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.base.mapper.OrganizationMapper;
import com.fit2cloud.base.service.IOrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.request.PageOrganizationRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {

    @Override
    public IPage<Organization> pageOrganization(PageOrganizationRequest request) {
        // 用户信息
        UserDto credentials = (UserDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        IPage<Organization> page = new Page<>(request.getCurrentPage(), request.getPageSize(), false);
        LambdaQueryWrapper<Organization> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(request.getName())) {
            wrapper.like(true,Organization::getName,request.getName());
        }
        IPage<Organization> organizationIPage = baseMapper.pageOrganization(page, wrapper);
        organizationIPage.setTotal(baseMapper.listRootOrganizationIds(wrapper).size());
        return organizationIPage;
    }
}
