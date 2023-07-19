package com.fit2cloud.constants;

import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.autoconfigure.PluginsContextHolder;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.scheduler.util.CronUtils;
import com.fit2cloud.dto.job.JobSetting;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.quartz.CloudAccountSyncJob;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * { @Author:张少虎 }
 * { @Date: 2022/9/8  6:21 PM }
 * { @Version 1.0 }
 * { @注释: }
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

    public static final String CLOUD_ACCOUNT_BILL_SYNC_GROUP = "CLOUD_ACCOUNT_BILL_SYNC_GROUP";

    /**
     * 同步账单
     */
    private static final String SYNC_BILL = "SYNC_BILL";
    /**
     * 账单授权定时任务
     */
    private static final String AUTH_BILL = "AUTH_BILL";

    @Override
    public List<JobSetting> listJobInitSetting() {
        // 使用全参构造器
        JobSetting syncBill = new JobSetting(CloudAccountSyncJob.SyncBill.class, SYNC_BILL,
                CLOUD_ACCOUNT_BILL_SYNC_GROUP,
                "同步账单",
                CronUtils.create(new Integer[]{0}, Calendar.HOUR),
                p -> PluginsContextHolder.getExtensions(ICloudProvider.class).stream()
                        .filter(cloudProvider -> StringUtils.equals(cloudProvider.getCloudAccountMeta().platform, p))
                        .map(ICloudProvider::getParamsClass)
                        .allMatch(Objects::nonNull), (p) -> false, (p) -> false,
                p -> PluginsContextHolder.getPlatformExtension(ICloudProvider.class, p).getParamsClass()
                , (cloudAccount) -> {
            Map<String, Object> result = new HashMap<>();
            result.put(com.fit2cloud.common.constants.JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name(), cloudAccount.getId());
            try {
                Map<String, Object> defaultParams = PluginsContextHolder.getPlatformExtension(ICloudProvider.class, cloudAccount.getPlatform()).getParamsClass().getConstructor().newInstance().getDefaultParams();
                result.putAll(defaultParams);
            } catch (Exception e) {
                LogUtil.error("获取账单默认参数失败", e);
            }
            return result;
        }
        );
        JobSetting authBill = new JobSetting(CloudAccountSyncJob.BillAuthJob.class, AUTH_BILL,
                com.fit2cloud.common.constants.JobConstants.DefaultGroup.SYSTEM_GROUP.name()
                , "账单授权", "0 0 0 * * ? *", s -> false, s -> false, s -> false);
        return List.of(syncBill, authBill);
    }
}
