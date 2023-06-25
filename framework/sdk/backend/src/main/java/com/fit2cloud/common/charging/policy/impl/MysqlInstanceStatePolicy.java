package com.fit2cloud.common.charging.policy.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fit2cloud.base.entity.ResourceInstanceState;
import com.fit2cloud.base.mapper.BaseResourceInstanceStateMapper;
import com.fit2cloud.base.service.IBaseResourceInstanceStateService;
import com.fit2cloud.common.charging.policy.InstanceStatePolicy;
import com.fit2cloud.common.utils.SpringUtil;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/25  11:13}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class MysqlInstanceStatePolicy implements InstanceStatePolicy {
    @Override
    public List<ResourceInstanceState> list(String cloudAccountId, String resourceType, String month) {
        BaseResourceInstanceStateMapper resourceInstanceStateMapper = SpringUtil.getBean(BaseResourceInstanceStateMapper.class);

        // 资源实例状态
        return resourceInstanceStateMapper
                .selectList(new LambdaQueryWrapper<ResourceInstanceState>()
                        .eq(ResourceInstanceState::getResourceType, resourceType)
                        .eq(ResourceInstanceState::getMonth, month)
                        .eq(ResourceInstanceState::getCloudAccountId, cloudAccountId)
                );
    }

    @Override
    public List<ResourceInstanceState> list(String resourceType, String month) {
        BaseResourceInstanceStateMapper resourceInstanceStateMapper = SpringUtil.getBean(BaseResourceInstanceStateMapper.class);

        // 资源实例状态
        return resourceInstanceStateMapper
                .selectList(new LambdaQueryWrapper<ResourceInstanceState>()
                        .eq(ResourceInstanceState::getResourceType, resourceType)
                        .eq(ResourceInstanceState::getMonth, month)
                );
    }

    @Override
    public void saveBatch(String resourceType, String month, List<ResourceInstanceState> states) {
        IBaseResourceInstanceStateService resourceInstanceStateService = SpringUtil.getBean(IBaseResourceInstanceStateService.class);

    }


}
