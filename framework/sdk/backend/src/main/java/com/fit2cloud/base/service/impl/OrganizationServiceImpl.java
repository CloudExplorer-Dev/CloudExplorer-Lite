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
import com.fit2cloud.common.constants.GlobalErrorCodeConstants;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.request.OrganizationBatchRequest;
import com.fit2cloud.request.PageOrganizationRequest;
import com.fit2cloud.response.OrganizationTree;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
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
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {

    @Override
    public IPage<Organization> pageOrganization(PageOrganizationRequest request) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 用户信息
//        UserDto credentials = (UserDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Organization> page = new Page<>(request.getCurrentPage(), request.getPageSize(), false);
        QueryWrapper<Organization> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(request.getName())) {
            wrapper.like(true, "name", request.getName());
        }
        if (CollectionUtils.isNotEmpty(request.getUpdateTime())) {
            wrapper.between("update_time", simpleDateFormat.format(request.getUpdateTime().get(0)), simpleDateFormat.format(request.getUpdateTime().get(1)));
        }
        if (CollectionUtils.isNotEmpty(request.getCreateTime())) {
            wrapper.between("create_time", simpleDateFormat.format(request.getCreateTime().get(0)), simpleDateFormat.format(request.getCreateTime().get(1)));
        }
        if (request.getOrder() != null && StringUtils.isNotEmpty(request.getOrder().getColumn())) {
            wrapper.orderBy(false, request.getOrder().isAsc(), request.getOrder().getColumn());
        }
        QueryWrapper<Organization> pageWrapper = wrapper.clone();
        pageWrapper.last("limit " + ((request.getCurrentPage() - 1) * request.getPageSize()) + "," + request.getPageSize());
        List<Organization> organizations = baseMapper.pageOrganization(pageWrapper);
        int total = baseMapper.listRootOrganizationIds(wrapper).size();
        page.setRecords(organizations);
        page.setTotal(total);
        return page;
    }

    @Override
    public List<OrganizationTree> tree() {
        List<Organization> list = list();
        return toTree(list);
    }

    @Override
    public Boolean batch(OrganizationBatchRequest request) {
        List<Organization> organizations = request.getOrgDetails().stream().map(originDetails -> {
            Organization organization = new Organization();
            organization.setName(originDetails.getName());
            organization.setDescription(originDetails.getDescription());
            organization.setPid(request.getPid());
            return organization;
        }).toList();
        return saveBatch(organizations);

    }

    @Override
    public boolean removeBatchTreeByIds(List<Organization> organizationIds) {
        // 因为是树形数据,如果父级别组织存在则不能删除, 根据树形组织,从子级开始排序
        List<OrganizationTree> organizationTrees = toTree(organizationIds);
        return deleteTreeById(organizationTrees);
    }

    /***
     * 删除的时候需要先删除子组织
     * @param organizationTrees 组织树
     * @return 是否删除成功
     */
    public boolean deleteTreeById(List<OrganizationTree> organizationTrees) {
        for (OrganizationTree organizationTree : organizationTrees) {
            if (CollectionUtils.isNotEmpty(organizationTree.getChildren())) {
                deleteTreeById(organizationTree.getChildren());
            }
            removeTreeById(organizationTree.getId());
        }
        return true;
    }


    @Override
    public boolean removeTreeById(String id) {
        long count = count(new LambdaQueryWrapper<Organization>().eq(Organization::getPid, id));
        if (count > 0) {
            throw new Fit2cloudException(GlobalErrorCodeConstants.ORGANIZATION_CANNOT_DELETE.getCode(), GlobalErrorCodeConstants.ORGANIZATION_CANNOT_DELETE.getMessage());
        }
        return removeById(id);
    }

    @Override
    public Organization getOne(String id, String name) {
        if (StringUtils.isEmpty(id) && StringUtils.isEmpty(name)) {
            throw new Fit2cloudException(GlobalErrorCodeConstants.ORGANIZATION_ID_AND_NAME_REQUIRED.getCode(), GlobalErrorCodeConstants.ORGANIZATION_ID_AND_NAME_REQUIRED.getMessage());
        }
        LambdaQueryWrapper<Organization> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(id)) {
            wrapper.eq(Organization::getId, id);
        }
        if (StringUtils.isNotEmpty(name)) {
            wrapper.eq(Organization::getName, name);
        }
        return getOne(wrapper);
    }

    /**
     * 将数据转换为树状数据
     *
     * @param source 组织原始数据
     * @return 组织树状数据
     */
    private List<OrganizationTree> toTree(List<Organization> source) {
        List<OrganizationTree> organizationTrees = source.stream().map(organization -> {
            OrganizationTree organizationTree = new OrganizationTree();
            BeanUtils.copyProperties(organization, organizationTree);
            return organizationTree;
        }).toList();

        for (OrganizationTree organization : organizationTrees) {
            if (StringUtils.isNotEmpty(organization.getPid())) {
                Optional<OrganizationTree> first = organizationTrees.stream().filter(org -> {
                    return org.getId().equals(organization.getPid());
                }).findFirst();
                if (first.isPresent()) {
                    if (CollectionUtils.isNotEmpty(first.get().getChildren())) {
                        first.get().getChildren().add(organization);
                    } else {
                        first.get().setChildren(new ArrayList<>() {{
                            add(organization);
                        }});
                    }
                }
            }
        }
        return organizationTrees.stream().filter(organizationTree -> StringUtils.isEmpty(organizationTree.getPid())).toList();
    }
}
