package com.fit2cloud.common.constants;

import com.fit2cloud.common.platform.bill.Bill;
import com.fit2cloud.common.platform.credential.Credential;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @Author:张少虎
 * @Date: 2022/9/15  4:29 PM
 * @Version 1.0
 * @注释:
 */
public enum JobConstants {
    ;

    public enum JobType {
        /**
         * 表达式
         */
        CRON,
        /**
         * 间隔
         */
        INTERVAL
    }

    public enum Group {
        /**
         * 云账号资源同步定时任务
         */
        CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP((jobName, cloudAccountId) -> jobName + "_" + cloudAccountId,
                cloudAccount -> {
                    List<Credential.Region> regions = new ArrayList<>();
                    try {
                        regions = Credential.of(cloudAccount.getPlatform(), cloudAccount.getCredential()).regions();
                    } catch (Exception ignored) {
                    }
                    return Map.of(CloudAccount.CLOUD_ACCOUNT_ID.name(), cloudAccount.getId(),
                            CloudAccount.REGIONS.name(), regions);


                }),
        /**
         * 云账单同步分组
         */
        CLOUD_ACCOUNT_BILL_SYNC_GROUP((jobName, cloudAccountId) -> jobName + "_" + cloudAccountId,
                cloudAccount -> Map.of(CloudAccount.CLOUD_ACCOUNT_ID.name(), cloudAccount.getId(),
                        CloudAccount.BILL_SETTING.name(), Bill.getDefaultParams(cloudAccount.getPlatform()))),
        /**
         * 合规规则扫描分组
         */
        CLOUD_COMPLIANCE_RESOURCE_SYNC_GROUP((jobName, cloudAccountId) -> jobName + "_" + cloudAccountId,
                cloudAccount -> Map.of(CloudAccount.CLOUD_ACCOUNT_ID.name(), cloudAccount.getId())),
        /**
         * 监控分组
         */
        CLOUD_RESOURCE_METRIC_SYNC_GROUP((jobName, cloudAccountId) -> jobName + "_" + cloudAccountId,
                cloudAccount -> Map.of(CloudAccount.CLOUD_ACCOUNT_ID.name(), cloudAccount.getId())),
        /**
         * 系统定时任务
         */
        SYSTEM_GROUP((jobName, cloudAccountId) -> jobName, cloudAccount -> new HashMap<>());

        Group(BiFunction<String, String, String> getJobName, Function<com.fit2cloud.base.entity.CloudAccount, Map<String, Object>> getDefaultParams) {
            this.getJobName = getJobName;
            this.getDefaultParams = getDefaultParams;
        }

        /**
         * 获取任务名称
         */
        public final BiFunction<String, String, String> getJobName;
        /**
         * 获取默认的定时任务执行参数
         */
        public final Function<com.fit2cloud.base.entity.CloudAccount, Map<String, Object>> getDefaultParams;

        public static Group ofByName(String name) {
            return Arrays.stream(Group.values()).filter(g -> StringUtils.equals(g.name(), name)).findFirst().orElse(null);
        }


    }


    public enum CloudAccount {
        /**
         * 云账号id
         */
        CLOUD_ACCOUNT_ID,
        /**
         * 区域数据
         */
        REGIONS,
        /**
         * 账单设置
         */
        BILL_SETTING;

        /**
         * 获取云账号定时任务参数
         *
         * @param accountId  云账号id
         * @param regionList 同步区域
         * @return 任务所需参数
         */
        public static Map<String, Object> getCloudAccountJobParams(java.lang.String accountId, List<Credential.Region> regionList) {
            Map<java.lang.String, java.lang.Object> params = new HashMap<>();
            params.put(CLOUD_ACCOUNT_ID.name(), accountId);
            params.put(REGIONS.name(), regionList);
            return params;
        }

        public static Map<String, Object> getCloudAccountBillSettingarams(java.lang.String accountId, Map<String, Object> billSetting) {
            Map<java.lang.String, java.lang.Object> params = new HashMap<>();
            params.put(CLOUD_ACCOUNT_ID.name(), accountId);
            params.put(BILL_SETTING.name(), billSetting);
            return params;
        }

        public static Map<String, Object> getCloudAccountPerfMetricMonitorJobParams(java.lang.String accountId) {
            Map<java.lang.String, java.lang.Object> params = new HashMap<>();
            params.put(CLOUD_ACCOUNT_ID.name(), accountId);
            return params;
        }

        /**
         * 获取云账号定时任务名
         *
         * @param jobName        任务名称
         * @param cloudAccountId 云账号id
         * @return 定时任务名称
         */
        public static String getCloudAccountJobName(String jobName, String cloudAccountId) {
            return jobName + "_" + cloudAccountId;
        }
    }


}
