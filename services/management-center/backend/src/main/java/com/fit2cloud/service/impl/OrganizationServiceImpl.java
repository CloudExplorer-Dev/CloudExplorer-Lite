package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.OrganizationUtil;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.controller.request.OrganizationBatchRequest;
import com.fit2cloud.controller.request.OrganizationRequest;
import com.fit2cloud.controller.request.PageOrganizationRequest;
import com.fit2cloud.dao.entity.Organization;
import com.fit2cloud.dao.mapper.OrganizationMapper;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.response.OrganizationTree;
import com.fit2cloud.service.IOrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {

    @Resource
    private MessageSource messageSource;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public IPage<Organization> pageOrganization(PageOrganizationRequest request) {
        // 用户信息
       //UserDto credentials = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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
    public Boolean batch(OrganizationBatchRequest request) {
        List<String> names = request.getOrgDetails().stream().map(OrganizationBatchRequest.OriginDetails::getName).toList();
        List<Organization> list = list(new LambdaQueryWrapper<Organization>().in(Organization::getName, names));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new Fit2cloudException(ErrorCodeConstants.ORGANIZATION_NAME_REPEAT.getCode(), messageSource.getMessage(ErrorCodeConstants.ORGANIZATION_NAME_REPEAT.getMessage(),
                    new Object[]{JsonUtil.toJSONString(list.stream().map(Organization::getName).collect(Collectors.toList()))}, LocaleContextHolder.getLocale()));
        }
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
    public boolean removeBatchTreeByIds(List<Organization> organizations) {
        // 因为是树形数据,如果父级别组织存在则不能删除, 根据树形组织,从子级开始排序
        List<OrganizationTree> organizationTrees = OrganizationUtil.toTree(organizations.stream().map(item -> {
            com.fit2cloud.base.entity.Organization organization = new com.fit2cloud.base.entity.Organization();
            BeanUtils.copyProperties(item, organization);
            return organization;
        }).collect(Collectors.toList()));
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
            throw new Fit2cloudException(ErrorCodeConstants.ORGANIZATION_CANNOT_DELETE.getCode(),  messageSource.getMessage(ErrorCodeConstants.ORGANIZATION_CANNOT_DELETE.getMessage(),null,LocaleContextHolder.getLocale()));
        }
        return removeById(id);
    }

    @Override
    public Organization getOne(String id, String name) {
        if (StringUtils.isEmpty(id) && StringUtils.isEmpty(name)) {
            throw new Fit2cloudException(ErrorCodeConstants.ORGANIZATION_ID_AND_NAME_REQUIRED.getCode(), messageSource.getMessage(ErrorCodeConstants.ORGANIZATION_ID_AND_NAME_REQUIRED.getMessage(),null,LocaleContextHolder.getLocale()));
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

    @Override
    public boolean update(OrganizationRequest request) {
        Organization organization = getById(request.getId());
        List<Organization> bottomOrganization = getBottomOrganization(new ArrayList<>(), organization.getId());
        if (bottomOrganization.stream().filter(item -> {
            return item.getId().equals(request.getPid());
        }).findFirst().isPresent()||request.getId().equals(request.getPid())) {
            throw new Fit2cloudException(ErrorCodeConstants.ORGANIZATION_UPDATE_NOT_THIS_CHILD.getCode(), messageSource.getMessage(ErrorCodeConstants.ORGANIZATION_UPDATE_NOT_THIS_CHILD.getMessage(),null,LocaleContextHolder.getLocale()));
        }
        BeanUtils.copyProperties(request,organization);
        return updateById(organization);
    }

    private List<Organization> getBottomOrganization(List<Organization> result, String pid) {
        List<Organization> child = list(new LambdaQueryWrapper<Organization>().eq(Organization::getPid, pid));
        if (CollectionUtils.isNotEmpty(child)) {
            result.addAll(child);
            for (Organization organization : child) {
                getBottomOrganization(result, organization.getId());
            }
        }
        return result;
    }
}
