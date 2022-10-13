package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.base.entity.VmCloudImage;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.service.IBaseVmCloudDiskService;
import com.fit2cloud.base.service.IBaseVmCloudImageService;
import com.fit2cloud.base.service.IBaseVmCloudServerService;
import com.fit2cloud.common.utils.LocaleUtil;
import com.fit2cloud.response.cloud_account.ResourceCountResponse;
import com.fit2cloud.service.IResourceCountService;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/10/9 2:49 PM
 */
@Service
public class ResourceCountImpl implements IResourceCountService {
    @Resource
    IBaseVmCloudServerService cloudServerService;
    @Resource
    IBaseVmCloudDiskService cloudDiskService;
    @Resource
    IBaseVmCloudImageService cloudImageService;

    @Override
    public List<ResourceCountResponse> count(String accountId) {
        List<ResourceCountResponse> list = new ArrayList<>();
        return list;
    }
}
