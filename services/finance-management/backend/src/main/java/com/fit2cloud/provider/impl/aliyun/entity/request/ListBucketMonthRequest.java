package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.fit2cloud.common.platform.bill.impl.AliBill;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliyunBillCredential;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/29  11:11}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ListBucketMonthRequest {
    /**
     * 认证信息
     */
    private AliyunBillCredential credential;
    /**
     * 账单信息
     */
    private AliBill bill;
}
