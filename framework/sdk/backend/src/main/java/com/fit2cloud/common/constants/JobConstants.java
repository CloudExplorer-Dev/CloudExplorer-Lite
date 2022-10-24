package com.fit2cloud.common.constants;

import com.fit2cloud.common.platform.credential.Credential;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/15  4:29 PM
 * @Version 1.0
 * @注释:
 */
public enum JobConstants {
    ;

    public enum Group {

        /**
         * 云账号资源同步定时任务
         */
        CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP,
        /**
         * 云账单同步分组
         */
        CLOUD_ACCOUNT_BILL_SYNC_GROUP,
        /**
         * 系统定时任务
         */
        SYSTEM_GROUP;
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

        /**
         * 获取云账号定时任务名
         *
         * @param jobname        任务名称
         * @param cloudAccountId 云账号id
         * @return 定时任务名称
         */
        public static String getCloudAccountJobName(String jobname, String cloudAccountId) {
            return jobname + "_" + cloudAccountId;
        }
    }


}
