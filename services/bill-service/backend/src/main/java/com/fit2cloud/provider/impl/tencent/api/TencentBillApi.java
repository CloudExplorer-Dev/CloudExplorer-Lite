package com.fit2cloud.provider.impl.tencent.api;


import com.fit2cloud.common.provider.exception.ReTryException;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.impl.tencent.entity.request.SyncBillRequest;
import com.fit2cloud.provider.impl.tencent.util.TencentMappingUtil;
import com.tencentcloudapi.billing.v20180709.BillingClient;
import com.tencentcloudapi.billing.v20180709.models.BillDetail;
import com.tencentcloudapi.billing.v20180709.models.DescribeBillDetailRequest;
import com.tencentcloudapi.billing.v20180709.models.DescribeBillDetailResponse;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/14  10:21 AM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class TencentBillApi {

    /**
     * 获取账单数据
     *
     * @param request 请求参数
     * @return
     */
    public static List<CloudBill> listBill(SyncBillRequest request) {
        return request.getBill() != null && request.getBill().getUseBucket() ? listBillByBucket(request) : listBillByApi(request);
    }

    /**
     * 从桶中读取账单信息
     *
     * @param request 请求对象
     * @return 账单数据
     */
    private static List<CloudBill> listBillByBucket(SyncBillRequest request) {
        return new ArrayList<>();
    }

    /**
     * 调用api获取账单信息
     *
     * @param request 账单信息请求对象
     * @return 账单数据
     */
    private static List<CloudBill> listBillByApi(SyncBillRequest request) {
        BillingClient billingClient = request.getCredential().getBillingClient();
        request.setOffset(0L);
        request.setLimit(100L);
        List<BillDetail> billDetails = PageUtil.page(request, req -> describeBillDetail(billingClient, req),
                res -> Arrays.stream(res.getDetailSet()).toList(),
                (req, res) -> req.getLimit() <= res.getDetailSet().length,
                (req, res) -> req.setOffset(req.getLimit() + req.getOffset()));
        return billDetails.stream().map(TencentMappingUtil::toCloudBill).toList();
    }

    /**
     * 调用腾讯云接口获取账单信息
     *
     * @param client  腾讯云客户端
     * @param request 请求对象
     * @return 账单数据
     */
    private static DescribeBillDetailResponse describeBillDetail(BillingClient client, DescribeBillDetailRequest request) {
        try {
            DescribeBillDetailRequest billDetailRequest = new DescribeBillDetailRequest();
            BeanUtils.copyProperties(request, billDetailRequest);
            return client.DescribeBillDetail(billDetailRequest);
        } catch (TencentCloudSDKException e) {
            ReTryException.throwReTry(e);
        }
        return null;
    }

}
