package com.fit2cloud.quartz;

import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.scheduler.handler.AsyncJob;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.service.ISyncProviderService;
import jdk.jfr.Name;
import org.quartz.Job;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/17  5:52 PM
 * @Version 1.0
 * @注释:
 */
@Component
public class CloudAccountSyncJob {

    @Name("同步磁盘定时任务")
    public static class SyncDiskJob extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            LogUtil.info("开始同步磁盘: ", map);
            SpringUtil.getBean(ISyncProviderService.class).syncCloudDisk(map);
            LogUtil.info("同步磁盘结束:", map);
        }
    }

    @Name("同步云主机定时任务")
    public static class SyncVirtualMachineJob extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            LogUtil.info("开始同步云主机: ", map);
            SpringUtil.getBean(ISyncProviderService.class).syncCloudServer(map);
            LogUtil.info("同步云主机结束: ", map);
        }
    }

    @Name("同步镜像定时任务")
    public static class SyncImageJob extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            LogUtil.info("开始同步镜像: ", map);
            SpringUtil.getBean(ISyncProviderService.class).syncCloudImage(map);
            LogUtil.info("同步镜像结束: ", map);
        }
    }

    @Name("同步宿主机定时任务")
    public static class SyncHostJob extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            LogUtil.info("开始同步宿主机: ", map);
            SpringUtil.getBean(ISyncProviderService.class).syncCloudHost(map);
            LogUtil.info("同步宿主机结束: ", map);
        }
    }

    @Name("同步存储器定时任务")
    public static class SyncDatastoreJob extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            LogUtil.info("开始同步存储器: ", map);
            SpringUtil.getBean(ISyncProviderService.class).syncCloudDatastore(map);
            LogUtil.info("同步存储器结束: ", map);
        }
    }

    @Name("同步云主机监控数据任务")
    public static class SyncCloudServerPerfMetricMonitor extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            LogUtil.info("开始同步云主机监控数据: ", map);
            SpringUtil.getBean(ISyncProviderService.class).syncCloudServerPerfMetricMonitor(map);
            LogUtil.info("同步云主机监控数据结束:", map);
        }
    }
}
