package com.fit2cloud;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.constants.AuthorizeTypeConstants;
import com.fit2cloud.controller.request.AuthorizeResourcesRequest;
import com.fit2cloud.controller.request.BillExpensesRequest;
import com.fit2cloud.controller.request.HistoryTrendRequest;
import com.fit2cloud.controller.response.AuthorizeResourcesResponse;
import com.fit2cloud.controller.response.BillView;
import com.fit2cloud.controller.response.Trend;
import com.fit2cloud.dao.entity.BillDimensionSetting;
import com.fit2cloud.dao.entity.BillRule;
import com.fit2cloud.dao.jentity.Group;
import com.fit2cloud.service.BillViewService;
import com.fit2cloud.service.IBillDimensionSettingService;
import com.fit2cloud.service.IBillRuleService;
import com.fit2cloud.service.SyncService;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
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
@SpringBootTest(classes = FinanceManagementApplication.class)
@TestPropertySource(locations = {
        "classpath:commons.properties",
        "file:${ce.config.file}"
})
public class CloudSyncBillTest {
    @Resource
    private SyncService syncService;
    @Resource
    private IBillDimensionSettingService billDimensionSettingService;
    @Resource
    private BillViewService billViewService;
    @Resource
    private IBillRuleService billRuleService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    @Resource
    IBillDimensionSettingService iBillDimensionSettingService;

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
    public void getAuthorizeResources() {
        AuthorizeResourcesRequest authorizeResourcesRequest = new AuthorizeResourcesRequest();
        authorizeResourcesRequest.setType(AuthorizeTypeConstants.ORGANIZATION.name());
        authorizeResourcesRequest.setAuthorizeId("754f25293d20f0abb4a33c6aae585ec4");
        Page<AuthorizeResourcesResponse> authorizeResources = iBillDimensionSettingService.getAuthorizeResources(2, 10, authorizeResourcesRequest);
        System.out.println(authorizeResources);
    }

    @Test
    public void testas() {
        BillDimensionSetting byId = iBillDimensionSettingService.getById("4e2b32251a7249984855dd5d23c494dd");
        iBillDimensionSettingService.authorize(byId);
    }

    @Test
    public void authorizeValues() {
        List<DefaultKeyValue<String, String>> group = billDimensionSettingService.authorizeValues("tags.kubernetes.do.not.delete");
        System.out.println(group);
    }


    @Test
    public void authorizeKeys() {
        List<DefaultKeyValue<String, String>> group = billDimensionSettingService.authorizeKeys();
        System.out.println(group);
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
