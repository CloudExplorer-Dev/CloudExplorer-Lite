package com.fit2cloud.provider.impl.aliyun.api;

import com.aliyun.bssopenapi20171214.Client;
import com.aliyun.bssopenapi20171214.models.DescribeInstanceBillRequest;
import com.aliyun.bssopenapi20171214.models.DescribeInstanceBillResponse;
import com.aliyun.bssopenapi20171214.models.DescribeInstanceBillResponseBody;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.exception.ReTryException;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.common.util.MonthUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.impl.aliyun.entity.request.SyncBillRequest;
import com.fit2cloud.provider.impl.aliyun.uitil.AliyunMappingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/13  4:36 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class AliyunBillApi {


    /**
     * 火气阿里云账单数据 粒度为日
     *
     * @param syncBillRequest 请求参数
     * @return 账单数据
     */
    public static List<CloudBill> listBill(SyncBillRequest syncBillRequest) {
        syncBillRequest.setGranularity("DAILY");
        String billingCycle = syncBillRequest.getBillingCycle();
        List<String> monthDays = MonthUtil.getMonthDays(billingCycle);
        return monthDays.stream().map(day -> listBill(syncBillRequest, day)).flatMap(List::stream).toList();
    }

    /**
     * 获取阿里云数据
     *
     * @param syncBillRequest 同步账单
     * @return 账单数据
     */
    private static List<CloudBill> listBill(SyncBillRequest syncBillRequest, String billingDate) {
        syncBillRequest.setBillingDate(billingDate);
        // 每次查询300条
        syncBillRequest.setMaxResults(300);
        Client client = syncBillRequest.getCredential().getClient();
        List<DescribeInstanceBillResponseBody.DescribeInstanceBillResponseBodyDataItems> list = PageUtil.page(syncBillRequest,
                req -> describeInstanceBillWithOptions(client, req), res -> Objects.nonNull(res.body) ? res.body.getData().getItems() : new ArrayList<>(),
                (req, res) -> req.getMaxResults() <= res.getBody().getData().getItems().size(),
                (req, res) -> req.setNextToken(res.getBody().getData().getNextToken()));
        List<Credential.Region> regions = syncBillRequest.getCredential().regions();
        return list.stream().map(dataItem -> AliyunMappingUtil.toCloudBill(dataItem, syncBillRequest, regions)).toList();
    }

    /**
     * @param client  客户端
     * @param request 请求对象
     * @return 查询到数据
     */
    private static DescribeInstanceBillResponse describeInstanceBillWithOptions(Client client, DescribeInstanceBillRequest request) {
        try {
            return client.describeInstanceBillWithOptions(request, new RuntimeOptions());
        } catch (Exception e) {
            ReTryException.throwReTry(e);
        }
        return null;
    }
}
