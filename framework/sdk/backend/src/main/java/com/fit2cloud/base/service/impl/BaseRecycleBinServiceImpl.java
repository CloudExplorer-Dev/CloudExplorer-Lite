package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fit2cloud.base.entity.RecycleBin;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseRecycleBinMapper;
import com.fit2cloud.base.service.IBaseRecycleBinService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.service.IBaseVmCloudDiskService;
import com.fit2cloud.base.service.IBaseVmCloudServerService;
import com.fit2cloud.common.constants.RecycleBinStatusConstants;
import com.fit2cloud.common.constants.ResourceTypeConstants;
import com.fit2cloud.common.utils.CurrentUserUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class BaseRecycleBinServiceImpl extends ServiceImpl<BaseRecycleBinMapper, RecycleBin> implements IBaseRecycleBinService {
    @Resource
    private IBaseVmCloudServerService cloudServerService;
    @Resource
    private IBaseVmCloudDiskService cloudDiskService;

    @Transactional
    @Override
    public void insertRecycleRecord(String resourceId, ResourceTypeConstants resourceType) {
        String userId = CurrentUserUtils.getUser().getId();
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setResourceId(resourceId);
        recycleBin.setResourceType(resourceType);
        recycleBin.setStatus(RecycleBinStatusConstants.ToBeRecycled);
        recycleBin.setCreateTime(LocalDateTime.now());
        recycleBin.setUserId(userId);
        save(recycleBin);

        // 云主机回收，同时将挂载的且属性为随云主机删除的磁盘放入回收站
        if (ResourceTypeConstants.VM.equals(resourceType)) {
            List<VmCloudDisk> vmCloudDisks = this.getRelateDisks(resourceId);
            if (CollectionUtils.isNotEmpty(vmCloudDisks)) {
                List<RecycleBin> recycleBinList = vmCloudDisks.stream().map(vmCloudDisk -> {
                    RecycleBin recycleDisk = new RecycleBin();
                    recycleDisk.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                    recycleDisk.setResourceId(vmCloudDisk.getId());
                    recycleDisk.setResourceType(ResourceTypeConstants.DISK);
                    recycleDisk.setStatus(RecycleBinStatusConstants.ToBeRecycled);
                    recycleDisk.setCreateTime(LocalDateTime.now());
                    recycleDisk.setUserId(userId);
                    return recycleDisk;
                }).collect(Collectors.toList());

                saveBatch(recycleBinList);
            }
        }
    }

    @Override
    public boolean updateRecycleRecordOnRecover(String id) {
        RecycleBin recycleBin = getById(id);
        return updateRecycleRecordOnRecover(recycleBin);
    }

    @Override
    public boolean updateRecycleRecordOnRecover(RecycleBin recycleBin) {
        String userId = CurrentUserUtils.getUser().getId();
        recycleBin.setStatus(RecycleBinStatusConstants.Recovered);
        recycleBin.setRecoverTime(LocalDateTime.now());
        recycleBin.setUserId(userId);
        updateById(recycleBin);

        // 如果是虚拟机 则随实例删除的磁盘也从回收站恢复
        String resourceId = recycleBin.getResourceId();
        ResourceTypeConstants resourceType = recycleBin.getResourceType();
        if (ResourceTypeConstants.VM.equals(resourceType)) {
            List<RecycleBin> diskRecords = this.getRelateDiskRecycleRecords(resourceId, RecycleBinStatusConstants.ToBeRecycled);
            if (CollectionUtils.isNotEmpty(diskRecords)) {
                List<RecycleBin> recycleBinList = diskRecords.stream().map(diskRecycleBin -> {
                    diskRecycleBin.setStatus(RecycleBinStatusConstants.Recovered);
                    diskRecycleBin.setRecoverTime(LocalDateTime.now());
                    diskRecycleBin.setUserId(userId);
                    return diskRecycleBin;
                }).collect(Collectors.toList());
                updateBatchById(recycleBinList);
            }
        }
        return true;
    }

    @Override
    public boolean updateRecycleRecordOnDelete(String resourceId, ResourceTypeConstants resourceType) {
        String userId = CurrentUserUtils.getUser().getId();
        QueryWrapper<RecycleBin> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RecycleBin::getResourceId, resourceId)
                .eq(RecycleBin::getResourceType, resourceType)
                .eq(RecycleBin::getStatus, RecycleBinStatusConstants.ToBeRecycled);
        List<RecycleBin> records = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(records)) {
            // 云主机回收，同时将挂载的且属性为随云主机删除的磁盘在回收站中的状态设置为删除
            if (ResourceTypeConstants.VM.equals(resourceType)) {
                List<RecycleBin> diskRecords = this.getRelateDiskRecycleRecords(resourceId, RecycleBinStatusConstants.ToBeRecycled);
                if (CollectionUtils.isNotEmpty(diskRecords)) {
                    records.addAll(diskRecords);
                }
            }
            List<RecycleBin> recycleBinList = records.stream().map(recycleBin -> {
                recycleBin.setStatus(RecycleBinStatusConstants.Deleted);
                recycleBin.setDeleteTime(LocalDateTime.now());
                recycleBin.setUserId(userId);
                return recycleBin;
            }).collect(Collectors.toList());
            updateBatchById(recycleBinList);
        }
        return true;
    }


    private List<VmCloudDisk> getRelateDisks(String vmId) {
        VmCloudServer vmCloudServer = cloudServerService.getById(vmId);
        QueryWrapper<VmCloudDisk> queryDiskWrapper = new QueryWrapper<>();
        queryDiskWrapper.lambda().eq(VmCloudDisk::getInstanceUuid, vmCloudServer.getInstanceUuid())
                .eq(VmCloudDisk::getAccountId, vmCloudServer.getAccountId())
                .eq(VmCloudDisk::getDeleteWithInstance, "YES")
                .ne(VmCloudDisk::getStatus, "deleted");
        return cloudDiskService.list(queryDiskWrapper);
    }


    private List<RecycleBin> getRelateDiskRecycleRecords(String vmId, RecycleBinStatusConstants recycleStatus) {
        List<VmCloudDisk> disks = this.getRelateDisks(vmId);
        if (CollectionUtils.isNotEmpty(disks)) {
            List<String> diskResourceIds = disks.stream().map(VmCloudDisk::getId).collect(Collectors.toList());
            QueryWrapper<RecycleBin> queryDiskResourceWrapper = new QueryWrapper<>();
            queryDiskResourceWrapper.lambda().in(RecycleBin::getResourceId, diskResourceIds)
                    .eq(RecycleBin::getResourceType, ResourceTypeConstants.DISK)
                    .eq(RecycleBin::getStatus, recycleStatus);
            List<RecycleBin> diskRecords = list(queryDiskResourceWrapper);
            return diskRecords;
        } else {
            return null;
        }

    }
}
