package com.fit2cloud.constants;

import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.dto.job.JobSetting;
import com.fit2cloud.quartz.CloudAccountSyncJob;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/5  15:41}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class JobConstants implements JobSettingConfig.JobConfig {
    /**
     * 同步扫描规则
     */
    private static final String SYNC_SCAN_RESOURCE = "SYNC_SCAN_RESOURCE";

    @Override
    public List<JobSetting> listJobInitSetting() {
        // 同步扫描规则
        JobSetting syncScanResource = new JobSetting(CloudAccountSyncJob.SyncScanJob.class,
                SYNC_SCAN_RESOURCE,
                "CLOUD_COMPLIANCE_RESOURCE_SYNC_GROUP",
                "扫描合规资源", "0 0 3 * * ? *", p -> true, (p) -> false, (p) -> false);
        return List.of(syncScanResource);
    }
}
