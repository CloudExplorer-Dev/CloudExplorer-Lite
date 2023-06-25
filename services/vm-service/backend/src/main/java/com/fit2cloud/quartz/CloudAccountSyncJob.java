package com.fit2cloud.quartz;

import com.fit2cloud.autoconfigure.ChargingConfig;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.charging.instance.impl.SimpleInstanceStateRecorder;
import com.fit2cloud.common.charging.setting.BillSetting;
import com.fit2cloud.common.constants.JobConstants;
import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.scheduler.handler.AsyncJob;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.service.ISyncProviderService;
import jdk.jfr.Name;
import org.quartz.Job;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @Author:张少虎
 * @Date: 2022/9/17  5:52 PM
 * @Version 1.0
 * @注释:
 */
@Component
public class CloudAccountSyncJob {

    @Name("实例状态记录")
    public static class recorderInstanceState extends AsyncJob implements Job {

        @Override
        protected void run(Map<String, Object> map) {
            List<BillSetting> billSettings = ChargingConfig.getBillSettings().getBillSettings();
             billSettings
                    .parallelStream()
                    .forEach(billSetting -> SimpleInstanceStateRecorder.of(billSetting).runRecordState());
        }
    }

    @Name("实例变更")
    public static class recorderInstanceChange extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            List<BillSetting> billSettings = ChargingConfig.getBillSettings().getBillSettings();
            billSettings
                    .parallelStream()
                    .forEach(billSetting -> SimpleInstanceStateRecorder.of(billSetting).runRecordInstanceChange());
        }
    }

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

    @Name("监控数据同步")
    public static class SyncMetricMonitor extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            String cloudAccountId = map.get(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name()).toString();
            //List<Map<String, Object>> execParams = getExecParams();
            CloudAccount cloudAccount = SpringUtil.getBean(IBaseCloudAccountService.class).getById(cloudAccountId);
            LogUtil.info("开始同步监控数据: " + cloudAccount.getName());
            // 云主机监控
            new SyncCloudServerPerfMetricMonitor().exec(cloudAccount);
            //  宿主机监控
            new SyncCloudHostPerfMetricMonitor().exec(cloudAccount);
            // 云磁盘监控
            new SyncCloudDiskPerfMetricMonitor().exec(cloudAccount);
            // 存储器监控
            new SyncCloudDatastorePerfMetricMonitor().exec(cloudAccount);
            LogUtil.info("结束同步监控数据: " + cloudAccount.getName());

        }


        /**
         * 获取执行参数
         *
         * @return 所有云账号的执行参数
         */
        private List<Map<String, Object>> getExecParams() {
            List<CloudAccount> cloudAccounts = SpringUtil.getBean(IBaseCloudAccountService.class).list();
            return cloudAccounts.stream().map(cloudAccount -> {
                try {
                    List<Credential.Region> regions = Credential.of(cloudAccount.getPlatform(), cloudAccount.getCredential()).regions();
                    return JobConstants.CloudAccount.getCloudAccountJobParams(cloudAccount.getId(), regions);
                } catch (Exception e) {
                    return null;
                }

            }).filter(Objects::nonNull).toList();
        }
    }

    @Name("云主机监控")
    public static class SyncCloudServerPerfMetricMonitor extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            LogUtil.info("开始同步云主机监控数据: ", map);
            SpringUtil.getBean(ISyncProviderService.class).syncCloudServerPerfMetricMonitor(map);
            LogUtil.info("同步云主机监控数据结束:", map);
        }

        public Predicate<String> supportPlatform() {
            return (platform) -> true;
        }

        public void exec(CloudAccount cloudAccount) {
            if (supportPlatform().test(cloudAccount.getPlatform())) {
                try {
                    List<Credential.Region> regions = Credential.of(cloudAccount.getPlatform(), cloudAccount.getCredential()).regions();
                    exec(JobConstants.CloudAccount.getCloudAccountJobParams(cloudAccount.getId(), regions));
                } catch (Exception e) {
                    LogUtil.error(cloudAccount.getName() + "-" + e.getMessage());
                }
            }
        }
    }

    @Name("宿主机监控")
    public static class SyncCloudHostPerfMetricMonitor extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            LogUtil.info("开始同步宿主机监控数据: ", map);
            SpringUtil.getBean(ISyncProviderService.class).syncCloudHostPerfMetricMonitor(map);
            LogUtil.info("同步宿主机监控数据结束:", map);
        }

        public Predicate<String> supportPlatform() {
            return (platform) -> PlatformConstants.fit2cloud_vsphere_platform.name().equals(platform) || PlatformConstants.fit2cloud_openstack_platform.name().equals(platform);
        }

        public void exec(CloudAccount cloudAccount) {
            if (supportPlatform().test(cloudAccount.getPlatform())) {
                try {
                    List<Credential.Region> regions = Credential.of(cloudAccount.getPlatform(), cloudAccount.getCredential()).regions();
                    exec(JobConstants.CloudAccount.getCloudAccountJobParams(cloudAccount.getId(), regions));
                } catch (Exception e) {
                    LogUtil.error(cloudAccount.getName() + "-" + e.getMessage());
                }
            }
        }
    }

    @Name("云磁盘监控")
    public static class SyncCloudDiskPerfMetricMonitor extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            LogUtil.info("开始同步云磁盘监控数据: ", map);
            SpringUtil.getBean(ISyncProviderService.class).syncCloudDiskPerfMetricMonitor(map);
            LogUtil.info("同步云磁盘监控数据结束:", map);
        }

        public Predicate<String> supportPlatform() {
            return (platform) -> true;
        }

        public void exec(CloudAccount cloudAccount) {
            if (supportPlatform().test(cloudAccount.getPlatform())) {
                try {
                    List<Credential.Region> regions = Credential.of(cloudAccount.getPlatform(), cloudAccount.getCredential()).regions();
                    exec(JobConstants.CloudAccount.getCloudAccountJobParams(cloudAccount.getId(), regions));
                } catch (Exception e) {
                    LogUtil.error(cloudAccount.getName() + "-" + e.getMessage());
                }
            }
        }
    }

    @Name("存储器监控")
    public static class SyncCloudDatastorePerfMetricMonitor extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            LogUtil.info("开始同步存储器监控数据: ", map);
            SpringUtil.getBean(ISyncProviderService.class).syncCloudDatastorePerfMetricMonitor(map);
            LogUtil.info("同步存储器监控数据结束:", map);
        }

        public Predicate<String> supportPlatform() {
            return (platform) -> PlatformConstants.fit2cloud_vsphere_platform.name().equals(platform);
        }

        public void exec(CloudAccount cloudAccount) {
            if (supportPlatform().test(cloudAccount.getPlatform())) {
                try {
                    List<Credential.Region> regions = Credential.of(cloudAccount.getPlatform(), cloudAccount.getCredential()).regions();
                    exec(JobConstants.CloudAccount.getCloudAccountJobParams(cloudAccount.getId(), regions));
                } catch (Exception e) {
                    LogUtil.error(cloudAccount.getName() + "-" + e.getMessage());
                }
            }
        }
    }

}
