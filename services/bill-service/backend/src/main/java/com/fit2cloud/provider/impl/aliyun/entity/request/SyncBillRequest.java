package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.bssopenapi20171214.models.DescribeInstanceBillRequest;
import com.aliyun.bssopenapi20171214.models.QueryBillOverviewRequest;
import com.fit2cloud.common.platform.bill.impl.AliBill;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliyunBillCredential;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/13  4:37 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class SyncBillRequest extends DescribeInstanceBillRequest {
    /**
     * 认证信息
     */
    private AliyunBillCredential credential;
    /**
     * 账单信息
     */
    private AliBill bill;


    public DescribeInstanceBillRequest setMonth(String month) {
        return super.setBillingCycle(month);
    }
}
