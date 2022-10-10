package com.fit2cloud.quartz;

import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.scheduler.handler.AsyncJob;
import com.fit2cloud.common.utils.SpringUtil;
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

    @Name("同步VPC")
    public static class SyncVpc extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            LogUtil.info("开始同步VPC: ", map);
            LogUtil.info("开始同步VPC结束:", map);
        }
    }

    @Name("同步子网")
    public static class SyncSubNet extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            LogUtil.info("开始同步子网: ", map);
            LogUtil.info("开始同步子网结束: ", map);
        }
    }

    @Name("同步安全组")
    public static class SyncSecurityGroup extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            LogUtil.info("开始同步安全组: ", map);
            LogUtil.info("同步安全组结束: ", map);
        }
    }
}
