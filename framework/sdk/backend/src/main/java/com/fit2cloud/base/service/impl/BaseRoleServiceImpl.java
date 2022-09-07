package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.Role;
import com.fit2cloud.base.mapper.BaseRoleMapper;
import com.fit2cloud.base.service.IBaseRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
@Service
public class BaseRoleServiceImpl extends ServiceImpl<BaseRoleMapper, Role> implements IBaseRoleService {

    @Override
    public List<Role> roles() {
        return null;
    }
}
