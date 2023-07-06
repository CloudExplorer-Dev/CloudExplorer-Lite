package com.fit2cloud.quartz;

import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.scheduler.handler.AsyncJob;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.dto.job.JobSetting;
import com.fit2cloud.service.IBillDimensionSettingService;
import com.fit2cloud.service.SyncService;
import jdk.jfr.Name;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobKey;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/17  5:52 PM
 * @Version 1.0
 * @注释:
 */
@Component
public class CloudAccountSyncJob {

    @Name("同步账单")
    public static class SyncBill extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            LogUtil.info("开始同步账单: ", map);
            SyncService syncService = SpringUtil.getBean(SyncService.class);
            syncService.syncBill(map);
            LogUtil.info("同步账单结束:", map);
        }
    }

    @Name("账单授权任务")
    public static class BillAuthJob extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            LogUtil.info("开始授权账单");
            IBillDimensionSettingService billDimensionSettingService = SpringUtil.getBean(IBillDimensionSettingService.class);
            billDimensionSettingService.authorEditSetting();
            LogUtil.info("账单授权结束");
        }
    }
}
