package com.fit2cloud;

import com.fit2cloud.service.SyncService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/17  3:25 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@SpringBootTest(classes = BillServiceApplication.class)
@TestPropertySource(locations = {
        "classpath:commons.properties",
        "file:${ce.config.file}"
})
public class CloudSyncBillTest {
    @Resource
    private SyncService syncService;

    @Test
    public void sync() {
        CompletableFuture<Void> jobhw = CompletableFuture.runAsync(() -> {
            syncService.syncBill("1ee96fe1a53705d20913cde12defab31", "2022-09", "2022-08", "2022-07", "2022-06", "2022-05", "2022-04", "2022-03");
        });

        CompletableFuture<Void> jobtx = CompletableFuture.runAsync(() -> {
            syncService.syncBill("9473809a9cbf7b1074b5472ac039f96c", "2022-09", "2022-08", "2022-07", "2022-06", "2022-05", "2022-04", "2022-03");
        });
        CompletableFuture<Void> jobal = CompletableFuture.runAsync(() -> {
            syncService.syncBill("e6867e8dfb2b19747117cdc698f58cbc", "2022-09", "2022-08", "2022-07", "2022-06", "2022-05", "2022-04", "2022-03");
        });
        CompletableFuture.allOf(jobal, jobhw, jobtx).join();
    }
}
