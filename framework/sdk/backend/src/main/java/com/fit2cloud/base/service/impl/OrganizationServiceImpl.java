package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.base.mapper.OrganizationMapper;
import com.fit2cloud.base.service.IOrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.request.PageOrganizationRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 用户信息
//        UserDto credentials = (UserDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        IPage<Organization> page = new Page<>(request.getCurrentPage(), request.getPageSize(), false);
       QueryWrapper<Organization> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(request.getName())) {
            wrapper.like(true,"name", request.getName());
        }
        if (CollectionUtils.isNotEmpty(request.getUpdateTime())) {
            wrapper.between("update_time", simpleDateFormat.format(request.getUpdateTime().get(0)), simpleDateFormat.format(request.getUpdateTime().get(1)));
        }
        if (CollectionUtils.isNotEmpty(request.getCreateTime())) {
            wrapper.between("create_time", simpleDateFormat.format(request.getCreateTime().get(0)), simpleDateFormat.format(request.getCreateTime().get(1)));
        }
        if (request.getOrder() != null && StringUtils.isNotEmpty(request.getOrder().getColumn())) {
            wrapper.orderBy(false,request.getOrder().isAsc(),request.getOrder().getColumn());
        }
        IPage<Organization> organizationIPage = baseMapper.pageOrganization(page, wrapper);
        organizationIPage.setTotal(baseMapper.listRootOrganizationIds(wrapper).size());
        return organizationIPage;
    }
}
