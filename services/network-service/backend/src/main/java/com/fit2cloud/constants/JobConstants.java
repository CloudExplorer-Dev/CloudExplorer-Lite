package com.fit2cloud.constants;

import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.dto.job.JobInitSettingDto;
import com.fit2cloud.quartz.CloudAccountSyncJob;
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
     * 同步VPC
     */
    private static final String SYNC_VPC = "SYNC_VPC";

    /**
     * 同步虚拟机
     */
    private static final String SYNC_SUBNET = "SYNC_SUBNET";

    /**
     * 同步镜像
     */
    private static final String SYNC_SECURITY_GROUP = "SYNC_SECURITY_GROUP";


    @Override
    public List<JobInitSettingDto> listJobInitSetting() {
        // 使用全参构造器
        JobInitSettingDto syncVpc = new JobInitSettingDto(CloudAccountSyncJob.SyncVpc.class, SYNC_VPC,
                com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), null, null, -1, null, "同步VPC", 60, DateBuilder.IntervalUnit.MINUTE, null);
        // 使用build函数
        JobInitSettingDto syncSubNet = JobInitSettingDto.builder()
                .jobHandler(CloudAccountSyncJob.SyncSubNet.class)
                .jobName(SYNC_SUBNET)
                .jobGroup(com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name())
                .repeatCount(-1)
                .timeInterval(60)
                .unit(DateBuilder.IntervalUnit.MINUTE)
                .description("同步子网").build();
        JobInitSettingDto syncSecurityGroup = JobInitSettingDto.builder()
                .jobHandler(CloudAccountSyncJob.SyncSecurityGroup.class)
                .repeatCount(-1)
                .timeInterval(60)
                .unit(DateBuilder.IntervalUnit.MINUTE)
                .jobName(SYNC_SECURITY_GROUP).jobGroup(com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name())
                .description("同步安全组").build();
        return List.of(syncVpc, syncSubNet, syncSecurityGroup);
    }
}
