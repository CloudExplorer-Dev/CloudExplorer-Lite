package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.request.cloud_account.CloudAccountRequest;
import com.fit2cloud.dao.entity.CloudAccount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
public interface ICloudAccountService extends IService<CloudAccount> {

    /**
     * 分特查询云账号
     * @param cloudAccountRequest 云账号请求对象
     * @return                    分页对象
     */
    IPage<CloudAccount> page(CloudAccountRequest cloudAccountRequest);
}
