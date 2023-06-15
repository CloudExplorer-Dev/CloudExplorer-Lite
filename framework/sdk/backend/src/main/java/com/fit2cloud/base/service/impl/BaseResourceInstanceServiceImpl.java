package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.ResourceInstance;
import com.fit2cloud.base.mapper.BaseCloudAccountMapper;
import com.fit2cloud.base.mapper.BaseResourceInstanceMapper;
import com.fit2cloud.base.service.IBaseResourceInstanceService;
import jakarta.annotation.Resource;
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


    @Resource
    private BaseCloudAccountMapper baseCloudAccountMapper;

    @Override
    public List<ResourceInstance> listLastResourceInstance(Wrapper<ResourceInstance> wrapper) {
        return this.baseMapper.listLastResourceInstance(wrapper);
    }

    @Override
    public void cleanDeletedCloudAccount() {
        List<CloudAccount> cloudAccounts = baseCloudAccountMapper.selectList(null);
        this.remove(new LambdaQueryWrapper<ResourceInstance>()
                .notIn(ResourceInstance::getCloudAccountId, cloudAccounts.stream().map(CloudAccount::getId).toList()));
    }
}
