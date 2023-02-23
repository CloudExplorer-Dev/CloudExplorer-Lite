package com.fit2cloud.constants;

import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.scheduler.util.CronUtils;
import com.fit2cloud.dto.job.JobSetting;
import com.fit2cloud.quartz.CloudAccountSyncJob;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * @Author:张少虎
 * @Date: 2022/9/8  6:21 PM
 * @Version 1.0
 * @注释:
 */
public class JobConstants implements JobSettingConfig.JobConfig {
    public enum JobSyncResourceType {
        BILL("同步账单");
        private String message;

        JobSyncResourceType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public static JobSyncResourceType of(String message) {
            return Arrays.stream(JobSyncResourceType.values()).filter(item -> item.message.equals(message)).findFirst().orElse(null);
        }
    }

    /**
     * 同步账单
     */
    private static final String SYNC_BILL = "SYNC_BILL";

    @Override
    public List<JobSetting> listJobInitSetting() {
        // 使用全参构造器
        JobSetting syncBill = new JobSetting(CloudAccountSyncJob.SyncBill.class, SYNC_BILL,
                com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_BILL_SYNC_GROUP.name(),
                "同步账单", null, CronUtils.create(new Integer[]{0}, Calendar.HOUR),
                p -> Arrays.stream(PlatformConstants.values()).filter(platformConstants -> platformConstants.name().equals(p)).map(PlatformConstants::getBillClass).allMatch(Objects::nonNull), (p) -> false, (p) -> false);
        return List.of(syncBill);
    }
}
