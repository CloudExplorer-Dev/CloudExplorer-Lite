package com.fit2cloud.provider.impl.aliyun.entity.request;


import com.fit2cloud.provider.impl.aliyun.entity.credential.AliSecurityComplianceCredential;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/4  16:45}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ListRedisInstanceRequest extends com.aliyun.r_kvstore20150101.models.DescribeInstancesRequest {

    /**
     * 认证信息
     */
    private AliSecurityComplianceCredential credential;


}
