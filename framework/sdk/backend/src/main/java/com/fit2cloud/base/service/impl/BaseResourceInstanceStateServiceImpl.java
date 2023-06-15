package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.ResourceInstanceState;
import com.fit2cloud.base.mapper.BaseCloudAccountMapper;
import com.fit2cloud.base.mapper.BaseResourceInstanceStateMapper;
import com.fit2cloud.base.service.IBaseResourceInstanceStateService;
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
public class BaseResourceInstanceStateServiceImpl extends ServiceImpl<BaseResourceInstanceStateMapper, ResourceInstanceState> implements IBaseResourceInstanceStateService {


    @Resource
    private BaseCloudAccountMapper baseCloudAccountMapper;

    @Override
    public void cleanDeletedCloudAccount() {
        List<CloudAccount> cloudAccounts = baseCloudAccountMapper.selectList(null);
        this.remove(new LambdaQueryWrapper<ResourceInstanceState>()
                .notIn(ResourceInstanceState::getCloudAccountId, cloudAccounts.stream().map(CloudAccount::getId).toList()));

    }
}
