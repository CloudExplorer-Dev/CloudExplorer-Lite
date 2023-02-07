package com.fit2cloud.constants;

import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.dto.job.JobSetting;
import com.fit2cloud.quartz.CloudAccountSyncJob;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
    public List<JobSetting> listJobInitSetting() {
        // 使用全参构造器
        JobSetting syncBill = new JobSetting(CloudAccountSyncJob.SyncBill.class, SYNC_BILL, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_BILL_SYNC_GROUP.name(), "同步账单", null, p -> Arrays.stream(PlatformConstants.values()).filter(platformConstants -> platformConstants.name().equals(p)).map(PlatformConstants::getBillClass).allMatch(Objects::nonNull));
        return List.of(syncBill);
    }
}
