package com.fit2cloud.quartz;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fit2cloud.base.entity.JobRecord;
import com.fit2cloud.base.service.IBaseJobRecordService;
import com.fit2cloud.common.constants.JobStatusConstants;
import com.fit2cloud.common.scheduler.handler.AsyncJob;
import jdk.jfr.Name;
import org.quartz.Job;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/3/7  16:08}
 * {@code @Version 1.0}
 * {@code @注释:  任务超时检测任务}
 */
@Component
@Name("检测任务超时时间定时任务")
public class JobTimeOutJob extends AsyncJob implements Job {
    @Resource
    private IBaseJobRecordService jobRecordService;

    @Override
    protected void run(Map<String, Object> map) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.HOUR, -3);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(instance.getTime());
        LambdaUpdateWrapper<JobRecord> updateTimeOut = new LambdaUpdateWrapper<JobRecord>()
                .eq(JobRecord::getStatus, JobStatusConstants.SYNCING)
                .le(JobRecord::getCreateTime, format)
                .set(JobRecord::getStatus, JobStatusConstants.TIME_OUT);
        jobRecordService.update(updateTimeOut);
    }

}
