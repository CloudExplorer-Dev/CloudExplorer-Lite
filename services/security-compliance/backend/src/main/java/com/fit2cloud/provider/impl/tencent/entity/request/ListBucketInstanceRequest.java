package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.provider.impl.tencent.entity.credential.TencentSecurityComplianceCredential;
import com.qcloud.cos.model.ListBucketsRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/9  10:03}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ListBucketInstanceRequest extends ListBucketsRequest {
    /**
     * 认证对象
     */
    private TencentSecurityComplianceCredential credential;

}
