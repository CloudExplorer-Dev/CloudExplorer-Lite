package com.fit2cloud.constants;

import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.dto.job.JobInitSettingDto;
import com.fit2cloud.quartz.CloudAccountSyncJobs;
import org.quartz.DateBuilder;

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
     * 同步网络
     */
    private static final String SYNC_NETWORK = "SYNC_NETWORK";

    /**
     * 同步虚拟机
     */
    private static final String SYNC_VIRTUAL_MACHINE = "SYNC_VIRTUAL_MACHINE";

    /**
     * 同步镜像
     */
    private static final String SYNC_IMAGE = "SYNC_IMAGE";


    @Override
    public List<JobInitSettingDto> listJobInitSetting() {
        // 使用全参构造器
        JobInitSettingDto syncVirtual = new JobInitSettingDto(CloudAccountSyncJobs.SyncVirtualMachineJob.class, SYNC_VIRTUAL_MACHINE, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), null, null, -1, null, "同步网络", 60, DateBuilder.IntervalUnit.MINUTE, null);
        // 使用build函数
        JobInitSettingDto syncDisk = JobInitSettingDto.builder()
                .jobHandler(CloudAccountSyncJobs.SyncDiskJob.class)
                .jobName(SYNC_DISK)
                .jobGroup(com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name())
                .repeatCount(-1)
                .description("同步磁盘").build();
        JobInitSettingDto syncNetwork = JobInitSettingDto.builder()
                .jobHandler(CloudAccountSyncJobs.SyncNetworkJob.class)
                .jobName(SYNC_NETWORK)
                .repeatCount(-1)
                .jobGroup(com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name())
                .description("同步网络").build();
        JobInitSettingDto syncImage = JobInitSettingDto.builder()
                .jobHandler(CloudAccountSyncJobs.SyncImageJob.class)
                .repeatCount(-1)
                .jobName(SYNC_IMAGE).jobGroup(com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name())
                .description("同步镜像").build();
        return List.of(syncDisk, syncNetwork, syncVirtual, syncImage);
    }
}
