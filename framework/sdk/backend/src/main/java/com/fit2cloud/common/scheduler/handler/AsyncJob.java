package com.fit2cloud.common.scheduler.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fit2cloud.common.utils.JsonUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * @Author:张少虎
 * @Date: 2022/9/19  10:12 AM
 * @Version 1.0
 * @注释:
 */
public abstract class AsyncJob implements Job {
    private static ThreadPoolExecutor jobThreadPool;

    // 有很多项目不需要使用定时任务,所以这个线程池也不需要初始化,再使用的时候再进行初始化
    private static final Supplier<ThreadPoolExecutor> threadPool = () -> {
        synchronized (AsyncJob.class) {
            if (Objects.isNull(jobThreadPool)) {
                // 这里需要牺牲cpu线程频繁中断,换取多任务同步执行
                // 当前被配置的意思是,来了任务就启动一个线程去处理,如果超过150个就放在队列里面, 并且核心线程在没有被使用的情况下会被销毁.因为定时任务是一个特殊场景,来了一个任务就需要一个线程去处理,因为任务执行时间长,每个任务都有粒度锁,如果一下上来10个任务,有两个任务是重复的,这样会造成任务重复执行
                jobThreadPool = new ThreadPoolExecutor(100, 100, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
                // 设置keepAliveTime也适用于核心线程,也就是说,核心线程池在一段时间没有使用的时候也会被销毁
                jobThreadPool.allowCoreThreadTimeOut(true);
            }
            return jobThreadPool;
        }
    };

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Map<String, Object> wrappedMap = context.getMergedJobDataMap().getWrappedMap();
        TypeReference<Map<String, Object>> typeReference = new TypeReference<>() {
        };
        try {
            Map<String, Object> params = JsonUtil.mapper.readValue(JsonUtil.toJSONString(wrappedMap), typeReference);
            CompletableFuture.runAsync(() -> run(params), threadPool.get());
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
        CompletableFuture.runAsync(() -> run(params), threadPool.get());
    }

    public static CompletableFuture<Void> run(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, threadPool.get());
    }

}
