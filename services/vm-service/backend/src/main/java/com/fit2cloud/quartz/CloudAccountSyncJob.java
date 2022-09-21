package com.fit2cloud.quartz;

import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.constants.JobConstants;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.scheduler.handler.AsyncJob;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.constants.ProviderConstants;
import com.fit2cloud.service.ISyncProviderService;
import jdk.jfr.Name;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    @Resource
    private ISyncProviderService syncProviderService;

    @Name("同步磁盘定时任务")
    public class SyncDiskJob extends AsyncJob implements Job {
        @Override
        public void run(JobExecutionContext context) {
            JobDataMap clone = (JobDataMap) context.getMergedJobDataMap().clone();
            syncProviderService.syncCloudDisk(clone.getWrappedMap());
        }
    }

    @Name("同步网络定时任务")
    public class SyncNetworkJob extends AsyncJob implements Job {
        @Override
        public void run(JobExecutionContext context) {
            System.out.println("同步网络" + ": " + new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()) + "参数: " + JsonUtil.toJSONString(context.getMergedJobDataMap().getWrappedMap()));
        }
    }

    @Name("同步虚拟机定时任务")
    public class SyncVirtualMachineJob extends AsyncJob implements Job {
        @Override
        public void run(JobExecutionContext context) {
            JobDataMap clone = (JobDataMap) context.getMergedJobDataMap().clone();
            syncProviderService.syncCloudServer(clone.getWrappedMap());
        }
    }

    @Name("同步镜像定时任务")
    public class SyncImageJob extends AsyncJob implements Job {
        @Override
        public void run(JobExecutionContext context) {
            JobDataMap clone = (JobDataMap) context.getMergedJobDataMap().clone();
            syncProviderService.syncCloudImage(clone.getWrappedMap());
        }
    }
}
