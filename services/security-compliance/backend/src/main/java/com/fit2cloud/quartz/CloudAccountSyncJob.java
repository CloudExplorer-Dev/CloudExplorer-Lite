package com.fit2cloud.quartz;

import com.fit2cloud.common.constants.JobConstants;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.scheduler.handler.AsyncJob;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.service.ISyncService;
import jdk.jfr.Name;
import org.quartz.Job;

import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/5  15:41}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class CloudAccountSyncJob {

    @Name("同步扫描合规资源")
    public static class SyncScanJob extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            LogUtil.info("开始扫描合规资源: ", map);
            ISyncService syncService = SpringUtil.getBean(ISyncService.class);
            syncService.syncInstance(map.get(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name()).toString());
            LogUtil.info("合规资源扫描完毕:", map);
        }
    }
}
