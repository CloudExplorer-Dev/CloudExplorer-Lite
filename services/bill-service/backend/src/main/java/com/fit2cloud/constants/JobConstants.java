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
    private static final String SYNC_BILL = "SYNC_BILL";


    @Override
    public List<JobInitSettingDto> listJobInitSetting() {
        // 使用全参构造器
        JobInitSettingDto syncBill = new JobInitSettingDto(CloudAccountSyncJob.SyncBill.class, SYNC_BILL, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), null, null, -1, null, "同步账单", 60, DateBuilder.IntervalUnit.MINUTE, null);
        return List.of(syncBill);
    }
}
