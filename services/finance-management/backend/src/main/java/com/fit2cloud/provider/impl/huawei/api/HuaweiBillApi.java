package com.fit2cloud.provider.impl.huawei.api;

import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.impl.huawei.entity.request.SyncBillRequest;
import com.fit2cloud.provider.impl.huawei.util.HuaweiMappingUtil;
import com.huaweicloud.sdk.bss.v2.BssClient;
import com.huaweicloud.sdk.bss.v2.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/14  10:21 AM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class HuaweiBillApi {

    public static List<CloudBill> listBill(SyncBillRequest request) {
        return request.getBill() != null && request.getBill().getUseBucket() ? listBillByBucket(request) : listBillByApi(request);
    }

    /**
     * 根据华为云api获取账单数据
     *
     * @param request 请求参数
     * @return 账单数据
     */
    public static List<CloudBill> listBillByApi(SyncBillRequest request) {
        BssClient bssClient = request.getCredential().getBssClient();
        request.setOffset(0);
        // 华为云每次请求可以请求1000条数据
        request.setLimit(1000);
        List<ResFeeRecordV2> bills = PageUtil.page(request,
                (req) -> listCustomerBillsFeeRecords(bssClient, req),
                ListCustomerselfResourceRecordsResponse::getFeeRecords,
                (req, res) -> req.getLimit() <= res.getFeeRecords().size(),
                (req, res) -> req.setOffset(req.getLimit() + req.getOffset()));
        return bills.stream().map(HuaweiMappingUtil::toCloudBill).toList();
    }


    public static List<CloudBill> listBillByBucket(SyncBillRequest request) {
        return new ArrayList<>();
    }


    private static ListCustomerselfResourceRecordsResponse listCustomerBillsFeeRecords(BssClient client, ListCustomerselfResourceRecordsRequest request) {
        return client.listCustomerselfResourceRecords(request);
    }
}
