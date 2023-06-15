package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fit2cloud.base.entity.BillPolicyCloudAccountMapping;
import com.fit2cloud.base.mapper.BaseBillPolicyCloudAccountMappingMapper;
import com.fit2cloud.base.service.IBaseBillPolicyCloudAccountMappingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class BaseBillPolicyCloudAccountMappingServiceImpl extends ServiceImpl<BaseBillPolicyCloudAccountMappingMapper, BillPolicyCloudAccountMapping> implements IBaseBillPolicyCloudAccountMappingService {

    @Override
    public List<BillPolicyCloudAccountMapping> listLast(Wrapper<BillPolicyCloudAccountMapping> wrapper) {
        return this.getBaseMapper().listLast(wrapper);
    }
}
