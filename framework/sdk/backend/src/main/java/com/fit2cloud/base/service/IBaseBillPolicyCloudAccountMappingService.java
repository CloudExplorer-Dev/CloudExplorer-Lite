package com.fit2cloud.base.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fit2cloud.base.entity.BillPolicyCloudAccountMapping;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IBaseBillPolicyCloudAccountMappingService extends IService<BillPolicyCloudAccountMapping> {

    List<BillPolicyCloudAccountMapping> listLast(Wrapper<BillPolicyCloudAccountMapping> wrapper);
}
