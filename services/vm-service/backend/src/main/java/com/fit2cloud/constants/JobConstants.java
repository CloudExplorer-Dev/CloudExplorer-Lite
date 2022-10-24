package com.fit2cloud.constants;

import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.dto.job.JobInitSettingDto;
import com.fit2cloud.dto.job.JobSettingParent;
import com.fit2cloud.quartz.CloudAccountSyncJob;

import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/8  6:21 PM
 * @Version 1.0
 * @注释:
 */
public class JobConstants implements JobSettingConfig.JobConfig {

    /**
     * 同步磁盘
     */
    private static final String SYNC_DISK = "SYNC_DISK";

    /**
     * 同步云主机
     */
    private static final String SYNC_VIRTUAL_MACHINE = "SYNC_VIRTUAL_MACHINE";

    /**
     * 同步镜像
     */
    private static final String SYNC_IMAGE = "SYNC_IMAGE";


    @Override
    public List<JobSettingParent> listJobInitSetting() {
        // 同步虚拟机
        JobInitSettingDto syncVirtual = new JobInitSettingDto(CloudAccountSyncJob.SyncVirtualMachineJob.class, SYNC_VIRTUAL_MACHINE, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), "同步云主机", null, p -> true);
        // 同步磁盘
        JobInitSettingDto syncDisk = new JobInitSettingDto(CloudAccountSyncJob.SyncDiskJob.class, SYNC_DISK, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), "同步磁盘", null, p -> true);
        // 同步镜像
        JobInitSettingDto syncImage = new JobInitSettingDto(CloudAccountSyncJob.SyncImageJob.class, SYNC_IMAGE, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), "同步镜像", null, p -> true);
        return List.of(syncDisk, syncVirtual, syncImage);
    }
}
