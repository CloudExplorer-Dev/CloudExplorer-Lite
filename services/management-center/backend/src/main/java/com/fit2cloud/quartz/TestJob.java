package com.fit2cloud.quartz;

import com.fit2cloud.common.utils.JsonUtil;
import jdk.jfr.Name;
import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author:张少虎
 * @Date: 2022/9/8  1:58 PM
 * @Version 1.0
 * @注释:
 */
@Name("测试处理器")
public class TestJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        try {
            System.out.println(context.getScheduler().getSchedulerInstanceId() + "--" + new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date())+JsonUtil.toJSONString(context.getMergedJobDataMap().getWrappedMap()));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
