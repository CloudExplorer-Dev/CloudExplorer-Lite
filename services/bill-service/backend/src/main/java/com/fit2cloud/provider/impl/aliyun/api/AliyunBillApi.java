package com.fit2cloud.provider.impl.aliyun.api;

import com.aliyun.bssopenapi20171214.Client;
import com.aliyun.bssopenapi20171214.models.DescribeInstanceBillRequest;
import com.aliyun.bssopenapi20171214.models.DescribeInstanceBillResponse;
import com.aliyun.bssopenapi20171214.models.DescribeInstanceBillResponseBody;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fit2cloud.common.provider.exception.ReTryException;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.provider.impl.aliyun.entity.request.SyncBillRequest;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/13  4:36 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class AliyunBillApi {

    public static List<DescribeInstanceBillResponseBody.DescribeInstanceBillResponseBodyDataItems> listBill(SyncBillRequest syncBillRequest) {
        DescribeInstanceBillRequest describeInstanceBillRequest = new DescribeInstanceBillRequest();
        // 每次查询300条
        describeInstanceBillRequest.setMaxResults(300);
        Client client = syncBillRequest.getCredential().getClient();
        return PageUtil.page(describeInstanceBillRequest,
                req -> describeInstanceBillWithOptions(client, req),
                res -> res.body.getData().getItems(), (req, res) -> req.getMaxResults() <= res.getBody().getData().getItems().size(),
                (req, res) -> req.setNextToken(res.getBody().getData().getNextToken()));
    }

    /**
     * @param client  客户端
     * @param request 请求对象
     * @return
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
