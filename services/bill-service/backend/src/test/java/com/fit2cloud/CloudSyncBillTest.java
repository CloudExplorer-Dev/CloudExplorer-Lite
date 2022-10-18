package com.fit2cloud;

import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.platform.bill.impl.AliBill;
import com.fit2cloud.common.platform.bill.impl.HuaweiBill;
import com.fit2cloud.common.platform.bill.impl.TencentBill;
import com.fit2cloud.common.platform.credential.impl.HuaweiCredential;
import com.fit2cloud.common.platform.credential.impl.TencentCredential;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.impl.aliyun.AliyunCloudProvider;
import com.fit2cloud.provider.impl.aliyun.api.AliyunBillApi;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliyunBillCredential;
import com.fit2cloud.provider.impl.aliyun.entity.request.SyncBillRequest;
import com.fit2cloud.provider.impl.huawei.api.HuaweiBillApi;
import com.fit2cloud.provider.impl.huawei.entity.credential.HuaweiBillCredential;
import com.fit2cloud.provider.impl.tencent.api.TencentBillApi;
import com.fit2cloud.provider.impl.tencent.entity.credential.TencentBillCredential;
import com.tencentcloudapi.billing.v20180709.models.BillDetail;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private IBaseCloudAccountService cloudAccountService;

    @Test
    public void test() {
        CloudAccount cloudAccount = cloudAccountService.getById("9473809a9cbf7b1074b5472ac039f96c");
        String credential = cloudAccount.getCredential();
        Map<String, Object> billSetting = cloudAccount.getBillSetting();
        SyncBillRequest syncBillRequest = new SyncBillRequest();
        syncBillRequest.setCredential(JsonUtil.parseObject(credential, AliyunBillCredential.class));
        syncBillRequest.setBill(JsonUtil.parseObject(JsonUtil.toJSONString(billSetting), AliBill.class));
        syncBillRequest.setMonth("2022-03");
        List<CloudBill> cloudBills = AliyunBillApi.listBill(syncBillRequest);
        DoubleSummaryStatistics collect = cloudBills.stream().collect(Collectors.summarizingDouble(s -> s.getRealTotalCost().doubleValue()));
        System.out.println(cloudBills);

    }

    @Test
    public void huawei() {
        CloudAccount cloudAccount = cloudAccountService.getById("1ee96fe1a53705d20913cde12defab31");
        String credential = cloudAccount.getCredential();
        Map<String, Object> billSetting = cloudAccount.getBillSetting();
        com.fit2cloud.provider.impl.huawei.entity.request.SyncBillRequest syncBillRequest = new com.fit2cloud.provider.impl.huawei.entity.request.SyncBillRequest();
        syncBillRequest.setCredential(JsonUtil.parseObject(credential, HuaweiBillCredential.class));
        syncBillRequest.setBill(JsonUtil.parseObject(JsonUtil.toJSONString(billSetting), HuaweiBill.class));
        syncBillRequest.setMonth("2022-03");
        List<CloudBill> cloudBills = HuaweiBillApi.listBill(syncBillRequest);
        DoubleSummaryStatistics collect = cloudBills.stream().collect(Collectors.summarizingDouble(s -> s.getRealTotalCost().doubleValue()));
        System.out.println(cloudBills);

    }

    @Test
    public void tencent() {
        CloudAccount cloudAccount = cloudAccountService.getById("e6867e8dfb2b19747117cdc698f58cbc");
        String credential = cloudAccount.getCredential();
        Map<String, Object> billSetting = cloudAccount.getBillSetting();
        com.fit2cloud.provider.impl.tencent.entity.request.SyncBillRequest syncBillRequest = new com.fit2cloud.provider.impl.tencent.entity.request.SyncBillRequest();
        syncBillRequest.setCredential(JsonUtil.parseObject(credential, TencentBillCredential.class));
        syncBillRequest.setBill(JsonUtil.parseObject(JsonUtil.toJSONString(billSetting), TencentBill.class));
        syncBillRequest.setMonth("2022-09");
        List<CloudBill> cloudBills = TencentBillApi.listBill(syncBillRequest);
        DoubleSummaryStatistics collect = cloudBills.stream().collect(Collectors.summarizingDouble(s -> s.getRealTotalCost().doubleValue()));
        System.out.println(cloudBills);

    }
}
