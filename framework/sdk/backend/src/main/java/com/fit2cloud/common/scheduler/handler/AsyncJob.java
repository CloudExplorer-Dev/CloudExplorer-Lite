package com.fit2cloud.common.scheduler.handler;

import com.fit2cloud.common.platform.credential.Credential;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author:张少虎
 * @Date: 2022/9/19  10:12 AM
 * @Version 1.0
 * @注释:
 */
public abstract class AsyncJob implements Job {
    private static final ThreadPoolExecutor jobThreadPool = new ThreadPoolExecutor(2, 8, 30, TimeUnit.SECONDS, new LinkedBlockingQueue(200), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        CompletableFuture.runAsync(() -> this.run(context), jobThreadPool);
    }

    /**
     * 异步同步执行函数
     *
     * @param context 异步同步上下文
     */
    protected abstract void run(JobExecutionContext context);
}
