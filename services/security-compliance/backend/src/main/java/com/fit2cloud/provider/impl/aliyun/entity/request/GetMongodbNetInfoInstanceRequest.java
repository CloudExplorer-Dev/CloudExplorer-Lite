package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.dds20151201.models.DescribeShardingNetworkAddressRequest;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliSecurityComplianceCredential;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/12  9:31}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class GetMongodbNetInfoInstanceRequest extends DescribeShardingNetworkAddressRequest {
    /**
     * 认证信息
     */
    private AliSecurityComplianceCredential credential;
}
