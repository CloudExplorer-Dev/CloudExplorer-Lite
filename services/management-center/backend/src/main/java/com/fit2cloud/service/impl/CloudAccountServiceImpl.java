package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.controller.request.cloud_account.CloudAccountRequest;
import com.fit2cloud.dao.entity.CloudAccount;
import com.fit2cloud.dao.mapper.CloudAccountMapper;
import com.fit2cloud.service.ICloudAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class CloudAccountServiceImpl extends ServiceImpl<CloudAccountMapper, CloudAccount> implements ICloudAccountService {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public IPage<CloudAccount> page(CloudAccountRequest cloudAccountRequest) {
        Page<CloudAccount> page = Page.of(cloudAccountRequest.getCurrentPage(), cloudAccountRequest.getPageSize());
        if (cloudAccountRequest.getOrder() != null) {
            page.setOrders(new ArrayList<>() {{
                add(cloudAccountRequest.getOrder());
            }});
        }
        LambdaQueryWrapper<CloudAccount> wrapper = new LambdaQueryWrapper<>();
        if (CollectionUtils.isNotEmpty(cloudAccountRequest.getPlatform())) {
            wrapper.in(CloudAccount::getPlatform, cloudAccountRequest.getPlatform());
        }
        if (CollectionUtils.isNotEmpty(cloudAccountRequest.getState())) {
            wrapper.in(CloudAccount::getState, cloudAccountRequest.getState());
        }
        if (CollectionUtils.isNotEmpty(cloudAccountRequest.getStatus())) {
            wrapper.in(CloudAccount::getStatus, cloudAccountRequest.getStatus());
        }
        if (CollectionUtils.isNotEmpty(cloudAccountRequest.getUpdateTime())) {
            wrapper.between(CloudAccount::getUpdateTime, simpleDateFormat.format(cloudAccountRequest.getUpdateTime().get(0)), simpleDateFormat.format(cloudAccountRequest.getUpdateTime().get(1)));
        }
        if (CollectionUtils.isNotEmpty(cloudAccountRequest.getCreateTime())) {
            wrapper.between(CloudAccount::getCreateTime, simpleDateFormat.format(cloudAccountRequest.getCreateTime().get(0)), simpleDateFormat.format(cloudAccountRequest.getCreateTime().get(1)));
        }
        return page(page, wrapper);
    }
}
