package com.fit2cloud;

import com.fit2cloud.controller.request.BillExpensesRequest;
import com.fit2cloud.controller.request.HistoryTrendRequest;
import com.fit2cloud.controller.response.BillView;
import com.fit2cloud.controller.response.Trend;
import com.fit2cloud.dao.entity.BillRule;
import com.fit2cloud.dao.jentity.Group;
import com.fit2cloud.service.BillViewService;
import com.fit2cloud.service.IBillRuleService;
import com.fit2cloud.service.SyncService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @Resource
    private BillViewService billViewService;
    @Resource
    private IBillRuleService billRuleService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

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

    @Test
    public void test() {
        BigDecimal month = billViewService.getBillExpenses("MONTH", "2022-10", new BillExpensesRequest());
        System.out.println(month.doubleValue());
    }

    @Test
    public void test1() {
        List<Trend> month = billViewService.getTrend("YEAR", 10, new HistoryTrendRequest());
        System.out.println(month);
    }

    @Test
    public void saveRule() {
        BillRule billRule = new BillRule();
        ArrayList<Group> groups = new ArrayList<>();
        Group group = new Group();
        group.setField("provider");
        group.setName("云平台");
        groups.add(group);
        Group group1 = new Group();
        group1.setName("区域");
        group1.setField("regionId");
        groups.add(group1);
        Group group2 = new Group();
        group2.setName("资源");
        group2.setField("reousrceId");
        groups.add(group2);
        Group group3 = new Group();
        group3.setName("付款模型");
        group3.setField("billMode");
        groups.add(group3);
        billRule.setGroups(groups);
        billRule.setFilters(new ArrayList<>());
        billRule.setName("测试");
        billRuleService.save(billRule);
    }

    @Test
    public void testView() {
        Map<String, List<BillView>> dba01764416936c52866c7511977259e = billViewService.billViewByRuleId("95326810c6146ce6a75f09320b2b2699", "2022-10");
        System.out.println(dba01764416936c52866c7511977259e);
    }
}
