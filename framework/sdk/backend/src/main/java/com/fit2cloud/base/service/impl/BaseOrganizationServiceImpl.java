package com.fit2cloud.base.service.impl;

import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.base.mapper.BaseOrganizationMapper;
import com.fit2cloud.base.service.IBaseOrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.common.utils.OrganizationUtil;
import com.fit2cloud.response.OrganizationTree;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class BaseOrganizationServiceImpl extends ServiceImpl<BaseOrganizationMapper, Organization> implements IBaseOrganizationService {

    @Override
    public List<OrganizationTree> tree() {
        return OrganizationUtil.toTree(list());
    }
}
