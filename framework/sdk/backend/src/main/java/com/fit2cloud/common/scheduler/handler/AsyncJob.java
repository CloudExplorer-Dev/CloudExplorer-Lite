package com.fit2cloud.common.scheduler.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fit2cloud.common.utils.JsonUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

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
        Map<String, Object> wrappedMap = context.getMergedJobDataMap().getWrappedMap();
        TypeReference<Map<String, Object>> typeReference = new TypeReference<>() {
        };
        try {
            Map<String, Object> params = JsonUtil.mapper.readValue(JsonUtil.toJSONString(wrappedMap), typeReference);
            CompletableFuture.runAsync(() -> run(params), jobThreadPool);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 异步同步执行函数
     *
     * @param map 执行任务所需要的参数
     */
    protected abstract void run(Map<String, Object> map);

    /**
     * 异步执行函数
     *
     * @param params 执行函数所需参数
     */
    public void exec(Map<String, Object> params) {
        CompletableFuture.runAsync(() -> run(params), jobThreadPool);
    }
}
