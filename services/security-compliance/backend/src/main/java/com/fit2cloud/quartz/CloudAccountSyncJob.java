package com.fit2cloud.quartz;

import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.scheduler.handler.AsyncJob;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.controller.response.compliance_scan.SupportCloudAccountResourceResponse;
import com.fit2cloud.service.IComplianceScanService;
import com.fit2cloud.service.ISyncService;
import jdk.jfr.Name;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.quartz.Job;

import java.util.List;
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
            IComplianceScanService complianceScanService = SpringUtil.getBean(IComplianceScanService.class);
            List<SupportCloudAccountResourceResponse> supportCloudAccountResourceResponses = complianceScanService.listSupportCloudAccountResource();
            for (SupportCloudAccountResourceResponse supportCloudAccountResource : supportCloudAccountResourceResponses) {
                syncService.syncInstance(supportCloudAccountResource.getCloudAccount().getId(), supportCloudAccountResource.getResourceTypes()
                        .stream().map(DefaultKeyValue::getValue).toList());
            }
            LogUtil.info("合规资源扫描完毕:", map);
        }
    }
}
