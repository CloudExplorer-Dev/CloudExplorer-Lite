package com.fit2cloud.quartz;

import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.scheduler.handler.AsyncJob;
import jdk.jfr.Name;
import org.quartz.Job;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DemoJob {

    @Name("Demo job")
    public static class MyDemoJob extends AsyncJob implements Job {
        @Override
        protected void run(Map<String, Object> map) {
            LogUtil.info("定时任务接收到参数: ", map);
        }
    }

}
