package com.fit2cloud;

import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.service.ISyncService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;
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
        syncService.syncInstance("0769f3f764f8eeada44b54014224180e", ResourceTypeConstants.ECS);
    }
}
