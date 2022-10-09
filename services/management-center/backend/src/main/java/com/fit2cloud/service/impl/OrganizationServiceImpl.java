package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.OrganizationUtil;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.controller.request.OrganizationBatchRequest;
import com.fit2cloud.controller.request.OrganizationRequest;
import com.fit2cloud.controller.request.PageOrganizationRequest;
import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.dao.mapper.OrganizationMapper;
import com.fit2cloud.response.OrganizationTree;
import com.fit2cloud.service.IOrganizationService;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
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
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public IPage<Organization> pageOrganization(PageOrganizationRequest request) {
        // 用户信息
        //UserDto credentials = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Organization> page = new Page<>(request.getCurrentPage(), request.getPageSize(), false);
        // 构建查询参数
        QueryWrapper<Organization> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(request.getName()), "name", request.getName())
                .between(CollectionUtils.isNotEmpty(request.getUpdateTime()), "update_time", CollectionUtils.isNotEmpty(request.getUpdateTime()) ? simpleDateFormat.format(request.getUpdateTime().get(0)) : "", CollectionUtils.isNotEmpty(request.getUpdateTime()) ? simpleDateFormat.format(request.getUpdateTime().get(1)) : "")
                .between(CollectionUtils.isNotEmpty(request.getCreateTime()), "create_time", CollectionUtils.isNotEmpty(request.getUpdateTime()) ? simpleDateFormat.format(request.getCreateTime().get(0)) : "", CollectionUtils.isNotEmpty(request.getUpdateTime()) ? simpleDateFormat.format(request.getCreateTime().get(1)) : "");
        if (request.getOrder() != null && StringUtils.isNotEmpty(request.getOrder().getColumn())) {
            wrapper.orderBy(true, request.getOrder().isAsc(), ColumnNameUtil.getColumnName(request.getOrder().getColumn(), Organization.class));
        } else {
            wrapper.orderBy(true, false, "update_time");
        }
        QueryWrapper<Organization> pageWrapper = wrapper.clone();
        pageWrapper.last("limit " + ((request.getCurrentPage() - 1) * request.getPageSize()) + "," + request.getPageSize());
        List<Organization> organizations = baseMapper.pageOrganization(pageWrapper);
        if (request.getOrder() != null && StringUtils.isNotEmpty(request.getOrder().getColumn())) {
            if (request.getOrder().isAsc()) {
                organizations = organizations.stream().sorted(Comparator.comparing(organization -> {
                    return getValueByField(organization, request.getOrder().getColumn()).toString();
                })).toList();
            } else {
                organizations = organizations.stream().sorted(Comparator.comparing(organization -> {
                    return getValueByField(organization, request.getOrder().getColumn()).toString();
                }).reversed()).toList();
            }
        }
        int total = baseMapper.listRootOrganizationIds(wrapper).size();
        page.setRecords(organizations);
        page.setTotal(total);
        return page;
    }

    @SneakyThrows
    private Object getValueByField(Object obj, String fieldKey) {
        try {
            Field field = FieldUtils.getDeclaredField(obj.getClass(), fieldKey, true);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            return "";
        }

    }

    @Override
    public Boolean batch(OrganizationBatchRequest request) {
        List<String> names = request.getOrgDetails().stream().map(OrganizationBatchRequest.OriginDetails::getName).toList();
        List<Organization> list = list(new LambdaQueryWrapper<Organization>().in(Organization::getName, names));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new Fit2cloudException(ErrorCodeConstants.ORGANIZATION_NAME_REPEAT.getCode(), ErrorCodeConstants.ORGANIZATION_NAME_REPEAT.getMessage(new Object[]{JsonUtil.toJSONString(list.stream().map(Organization::getName).collect(Collectors.toList()))}));
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
            throw new Fit2cloudException(ErrorCodeConstants.ORGANIZATION_CANNOT_DELETE.getCode(), ErrorCodeConstants.ORGANIZATION_CANNOT_DELETE.getMessage());
        }
        return removeById(id);
    }

    @Override
    public Organization getOne(String id, String name) {
        if (StringUtils.isEmpty(id) && StringUtils.isEmpty(name)) {
            throw new Fit2cloudException(ErrorCodeConstants.ORGANIZATION_ID_AND_NAME_REQUIRED.getCode(), ErrorCodeConstants.ORGANIZATION_ID_AND_NAME_REQUIRED.getMessage());
        }
        LambdaQueryWrapper<Organization> wrapper = new LambdaQueryWrapper<Organization>()
                .eq(StringUtils.isNotEmpty(id), Organization::getId, id)
                .eq(StringUtils.isNotEmpty(name), Organization::getName, name);
        return getOne(wrapper);
    }

    @Override
    public boolean update(OrganizationRequest request) {
        Organization organization = getById(request.getId());
        List<Organization> bottomOrganization = getBottomOrganization(new ArrayList<>(), organization.getId());
        if (bottomOrganization.stream().anyMatch(item -> item.getId().equals(request.getPid())) || request.getId().equals(request.getPid())) {
            throw new Fit2cloudException(ErrorCodeConstants.ORGANIZATION_UPDATE_NOT_THIS_CHILD.getCode(), ErrorCodeConstants.ORGANIZATION_ID_AND_NAME_REQUIRED.getMessage());
        }
        BeanUtils.copyProperties(request, organization);
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
