package com.fit2cloud.provider.impl.huawei.api;

import com.fit2cloud.common.platform.bill.impl.HuaweiBill;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.provider.impl.huawei.entity.request.SyncBillRequest;
import com.huaweicloud.sdk.bss.v2.BssClient;
import com.huaweicloud.sdk.bss.v2.model.ListCustomerBillsFeeRecordsRequest;
import com.huaweicloud.sdk.bss.v2.model.ListCustomerBillsFeeRecordsResponse;
import com.huaweicloud.sdk.bss.v2.model.MonthlyBillRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/14  10:21 AM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class HuaweiBillApi {

    public static Object listBill(SyncBillRequest request) {
        Boolean useBucket = request.getBill().getUseBucket();
        return useBucket ? listBillByBucket(request) : listBillByApi(request);
    }

    /**
     * 根据华为云api获取账单数据
     *
     * @param request 请求参数
     * @return 账单数据
     */
    public static List<MonthlyBillRecord> listBillByApi(SyncBillRequest request) {
        BssClient bssClient = request.getCredential().getBssClient();
        ListCustomerBillsFeeRecordsRequest recordsRequest = new ListCustomerBillsFeeRecordsRequest();
        recordsRequest.setLimit(1000);
        return PageUtil.page(recordsRequest,
                (req) -> listCustomerBillsFeeRecords(bssClient, req),
                ListCustomerBillsFeeRecordsResponse::getRecords,
                (req, res) -> req.getLimit() <= res.getRecords().size(),
                (req, res) -> req.setOffset(req.getLimit() + req.getOffset()));
    }

    public static List<MonthlyBillRecord> listBillByBucket(SyncBillRequest request) {
        return new ArrayList<>();
    }

    private static ListCustomerBillsFeeRecordsResponse listCustomerBillsFeeRecords(BssClient client, ListCustomerBillsFeeRecordsRequest request) {
        return client.listCustomerBillsFeeRecords(request);
    }
}
