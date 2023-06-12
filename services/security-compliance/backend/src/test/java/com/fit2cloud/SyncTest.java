package com.fit2cloud;

import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.job.actuator.JobActuator;
import com.fit2cloud.common.job.job.Job;
import com.fit2cloud.common.job.job.SimpleJob;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.service.IJobService;
import com.fit2cloud.service.IJobStepService;
import com.fit2cloud.service.ISyncService;
import com.fit2cloud.service.impl.SyncServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/29  10:50}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@SpringBootTest(classes = SecurityComplianceApplication.class)
@TestPropertySource(locations = {"classpath:commons.properties", "file:${ce.config.file}"})
public class SyncTest {
    @Resource
    private ISyncService syncService;

    @Resource
    private IJobService service;

    @Test
    public void test() {
        Job<IJobStepService.Context> job = service.of("测试扫描");
        JobActuator<IJobStepService.Context> contextJobActuator = service.ofJobActuator(job, JobTypeConstants.SECURITY_COMPLIANCE_CLOUD_ACCOUNT_SYNC_JOB, "9ed1ac42e90f2sss6adsss2c66d4bf4eeabbbd",
                ResourceTypeConstants.ECS,
                List.of(JobActuator.ExecuteStepData.of("VERIFICATION::CLOUD_ACCOUNT")));
        contextJobActuator.run();
    }

    @Test
    public void syncAll() {
        ResourceTypeConstants[] values = ResourceTypeConstants.values();
        for (ResourceTypeConstants value : values) {
            CompletableFuture<Void> a = CompletableFuture.runAsync(() -> {
                syncService.syncInstance("0d12669b26899b529cce02e796f42aad", value);
            });
            CompletableFuture<Void> b = CompletableFuture.runAsync(() -> {
                syncService.syncInstance("948b67adf47bc1701cf2842042c4b1fc", value);
            });

            CompletableFuture<Void> c = CompletableFuture.runAsync(() -> {
                syncService.syncInstance("0769f3f764f8eeada44b54014224180e", value);
            });
            CompletableFuture.allOf(a, b, c).join();
        }
    }

    @Test
    public void syncAny() {
        syncService.syncInstance("0769f3f764f8eeada44b54014224180e", ResourceTypeConstants.SECURITY_GROUP);
    }
}
