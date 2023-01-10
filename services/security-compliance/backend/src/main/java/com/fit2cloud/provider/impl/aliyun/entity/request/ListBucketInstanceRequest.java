package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.fit2cloud.provider.impl.aliyun.entity.credential.AliSecurityComplianceCredential;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/9  18:52}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ListBucketInstanceRequest {
    /**
     * 认证信息
     */
    private AliSecurityComplianceCredential credential;
}
