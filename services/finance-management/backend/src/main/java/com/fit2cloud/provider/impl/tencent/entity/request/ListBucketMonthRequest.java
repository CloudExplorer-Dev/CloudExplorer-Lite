package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.provider.impl.tencent.entity.credential.TencentBillCredential;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/29  11:39}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ListBucketMonthRequest {
    /**
     * 认证信息
     */
    private TencentBillCredential credential;
    /**
     * 账单信息
     */
    private TencentBill bill;
}
