package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.provider.impl.tencent.entity.credential.TencentSecurityComplianceCredential;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/9  14:52}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class GetBucketAclRequest {
    /**
     * 认证对象
     */
    private TencentSecurityComplianceCredential credential;
    /**
     * 区域
     */
    private String regionId;

    /**
     * 桶名称
     */
    private String bucketName;
}
