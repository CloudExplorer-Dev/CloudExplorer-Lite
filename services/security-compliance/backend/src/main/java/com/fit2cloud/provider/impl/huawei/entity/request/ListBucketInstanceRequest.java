package com.fit2cloud.provider.impl.huawei.entity.request;

import com.fit2cloud.provider.impl.huawei.entity.credential.HuaweiSecurityComplianceCredential;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/9  18:20}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ListBucketInstanceRequest {
    /**
     * 认证对象
     */
    private HuaweiSecurityComplianceCredential credential;
}
