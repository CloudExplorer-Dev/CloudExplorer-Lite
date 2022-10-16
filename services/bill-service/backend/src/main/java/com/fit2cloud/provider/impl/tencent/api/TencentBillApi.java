package com.fit2cloud.provider.impl.tencent.api;


import com.fit2cloud.common.provider.exception.ReTryException;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.provider.impl.tencent.entity.request.SyncBillRequest;
import com.tencentcloudapi.billing.v20180709.BillingClient;
import com.tencentcloudapi.billing.v20180709.models.DescribeBillDetailRequest;
import com.tencentcloudapi.billing.v20180709.models.DescribeBillDetailResponse;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import java.util.Arrays;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/14  10:21 AM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class TencentBillApi {

    public static Object listBill(SyncBillRequest request) {
        Boolean useBucket = request.getBill().getUseBucket();
        return useBucket ? listBillByBucket(request) : listBillByApi(request);
    }

    private static Object listBillByBucket(SyncBillRequest request) {
        return null;
    }

    private static Object listBillByApi(SyncBillRequest request) {
        BillingClient billingClient = request.getCredential().getBillingClient();
        DescribeBillDetailRequest billDetailRequest = new DescribeBillDetailRequest();
        PageUtil.page(billDetailRequest, req -> describeBillDetail(billingClient, req),
                res -> Arrays.stream(res.getDetailSet()).toList(),
                (req, res) -> req.getLimit() <= res.getDetailSet().length,
                (req, res) -> {

                });
        return null;
    }

    private static DescribeBillDetailResponse describeBillDetail(BillingClient client, DescribeBillDetailRequest request) {
        try {
            return client.DescribeBillDetail(request);
        } catch (TencentCloudSDKException e) {
            ReTryException.throwReTry(e);
        }
        return null;
    }

}
