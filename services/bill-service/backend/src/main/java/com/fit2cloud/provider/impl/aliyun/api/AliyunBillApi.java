package com.fit2cloud.provider.impl.aliyun.api;

import com.aliyun.bssopenapi20171214.Client;
import com.aliyun.bssopenapi20171214.models.DescribeInstanceBillRequest;
import com.aliyun.bssopenapi20171214.models.DescribeInstanceBillResponse;
import com.aliyun.bssopenapi20171214.models.DescribeInstanceBillResponseBody;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fit2cloud.common.platform.bill.Bill;
import com.fit2cloud.common.provider.exception.ReTryException;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.impl.aliyun.entity.request.SyncBillRequest;
import com.fit2cloud.provider.impl.aliyun.uitil.AliyunMappingUtil;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/13  4:36 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class AliyunBillApi {

    /**
     * 获取阿里云数据
     *
     * @param syncBillRequest
     * @return
     */
    public static List<CloudBill> listBill(SyncBillRequest syncBillRequest) {
        // 每次查询300条
        syncBillRequest.setMaxResults(300);
        Client client = syncBillRequest.getCredential().getClient();
        List<DescribeInstanceBillResponseBody.DescribeInstanceBillResponseBodyDataItems> list = PageUtil.page(syncBillRequest,
                req -> describeInstanceBillWithOptions(client, req),
                res -> res.body.getData().getItems(), (req, res) -> req.getMaxResults() <= res.getBody().getData().getItems().size(),
                (req, res) -> req.setNextToken(res.getBody().getData().getNextToken()));
        return list.stream().map(dataItem -> AliyunMappingUtil.toCloudBill(dataItem, syncBillRequest)).toList();
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
