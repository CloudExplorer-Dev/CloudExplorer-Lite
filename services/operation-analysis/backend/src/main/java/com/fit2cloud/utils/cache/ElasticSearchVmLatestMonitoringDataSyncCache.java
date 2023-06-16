package com.fit2cloud.utils.cache;

import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.service.impl.CloudServerOptimizationServiceImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.ref.SoftReference;
import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/1  4:29 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Component
@EnableScheduling
public class ElasticSearchVmLatestMonitoringDataSyncCache {
    /**
     * 云主机最近一次监控数据同步时间 -> 最近一次监控数据同步的时间 使用软引用,在云主机内存不足的情况下会被清除
     */
    private static SoftReference<Long> esVmLastMonitoringDataSyncTime = new SoftReference<>(null);

    /**
     * 设置缓存
     *
     * @param syncTime 同步时间
     */
    private synchronized static void setCache(Long syncTime) {
        esVmLastMonitoringDataSyncTime = new SoftReference<>(syncTime);
    }

    /**
     * 更新缓存
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public static void updateCache() {
        LogUtil.debug("定时更新监控数据最新时间缓存");
        CloudServerOptimizationServiceImpl cloudServerOptimizationService = SpringUtil.getBean(CloudServerOptimizationServiceImpl.class);
        Long syncTime = cloudServerOptimizationService.getVmCloudServerCpuOrMemoryMonitoringDataLatestTime();
        setCache(syncTime);
    }


    /**
     * 获取缓存
     */
    public synchronized static Long getCacheOrUpdate() {
        Long syncTime = esVmLastMonitoringDataSyncTime.get();
        if (Objects.isNull(syncTime)) {
            CloudServerOptimizationServiceImpl cloudServerOptimizationService = SpringUtil.getBean(CloudServerOptimizationServiceImpl.class);
            Long updateSyncTime = cloudServerOptimizationService.getVmCloudServerCpuOrMemoryMonitoringDataLatestTime();
            updateCache();
            return updateSyncTime;
        }
        return syncTime;
    }

}
