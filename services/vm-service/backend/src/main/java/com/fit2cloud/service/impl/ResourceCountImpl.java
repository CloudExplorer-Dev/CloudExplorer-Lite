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
        // 云主机
        QueryWrapper<VmCloudServer> vmQueryWrapper = Wrappers.query();
        vmQueryWrapper.lambda().ne(VmCloudServer::getInstanceStatus, "deleted").eq(VmCloudServer::getAccountId, accountId);
        ResourceCountResponse vm = new ResourceCountResponse("xuniyunzhuji", LocaleUtil.getMessage("i18n.resource.vm","云主机"), cloudServerService.count(vmQueryWrapper));
        list.add(vm);

        // 磁盘
        QueryWrapper<VmCloudDisk> diskQueryWrapper = Wrappers.query();
        diskQueryWrapper.lambda().ne(VmCloudDisk::getStatus, "deleted").eq(VmCloudDisk::getAccountId, accountId);
        ResourceCountResponse disk = new ResourceCountResponse("yuncunchu", LocaleUtil.getMessage("i18n.resource.disk","磁盘"), cloudDiskService.count(diskQueryWrapper));
        list.add(disk);

        // 镜像
        QueryWrapper<VmCloudImage> imageQueryWrapper = Wrappers.query();
        imageQueryWrapper.lambda().ne(VmCloudImage::getStatus, "deleted").eq(VmCloudImage::getAccountId, accountId);
        ResourceCountResponse image = new ResourceCountResponse("jingxiang", LocaleUtil.getMessage("i18n.resource.image","镜像"), cloudImageService.count(imageQueryWrapper));
        list.add(image);

        return list;
    }
}
