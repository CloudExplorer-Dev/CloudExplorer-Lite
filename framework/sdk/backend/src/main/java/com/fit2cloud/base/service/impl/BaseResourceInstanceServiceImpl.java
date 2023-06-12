package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.ResourceInstance;
import com.fit2cloud.base.mapper.BaseResourceInstanceMapper;
import com.fit2cloud.base.service.IBaseResourceInstanceService;
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
public class BaseResourceInstanceServiceImpl extends ServiceImpl<BaseResourceInstanceMapper, ResourceInstance> implements IBaseResourceInstanceService {

    @Override
    public List<ResourceInstance> listLastResourceInstance(Wrapper<ResourceInstance> wrapper) {
        return this.baseMapper.listLastResourceInstance(wrapper);
    }
}
