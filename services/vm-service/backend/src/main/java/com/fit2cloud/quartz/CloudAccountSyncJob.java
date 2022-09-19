package com.fit2cloud.quartz;

import com.fit2cloud.common.scheduler.handler.AsyncJob;
import com.fit2cloud.common.utils.JsonUtil;
import jdk.jfr.Name;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        public void run(JobExecutionContext context) {
            System.out.println("同步磁盘" + ": " + new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()) + "参数: " + JsonUtil.toJSONString(context.getMergedJobDataMap().getWrappedMap()));
        }
    }

    @Name("同步网络定时任务")
    public static class SyncNetworkJob extends AsyncJob implements Job {

        @Override
        public void run(JobExecutionContext context) {
            System.out.println("同步网络" + ": " + new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()) + "参数: " + JsonUtil.toJSONString(context.getMergedJobDataMap().getWrappedMap()));
        }
    }

    @Name("同步虚拟机定时任务")
    public static class SyncVirtualMachineJob extends AsyncJob implements Job {

        @Override
        public void run(JobExecutionContext context) {
            System.out.println("同步虚拟机" + ": " + new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()) + "参数: " + JsonUtil.toJSONString(context.getMergedJobDataMap().getWrappedMap()));
        }
    }

    @Name("同步镜像定时任务")
    public static class SyncImageJob extends AsyncJob implements Job {
        @Override
        public void run(JobExecutionContext context) {
            System.out.println("同步镜像" + ": " + new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()) + "参数: " + JsonUtil.toJSONString(context.getMergedJobDataMap().getWrappedMap()));
        }
    }
}
