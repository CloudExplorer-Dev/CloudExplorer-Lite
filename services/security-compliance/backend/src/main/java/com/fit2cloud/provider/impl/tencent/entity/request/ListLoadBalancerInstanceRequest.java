package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.provider.impl.tencent.entity.credential.TencentSecurityComplianceCredential;
import com.tencentcloudapi.clb.v20180317.models.DescribeLoadBalancersRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/6  10:54}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Setter
@Getter
public class ListLoadBalancerInstanceRequest extends DescribeLoadBalancersRequest {
    /**
     * 认证对象
     */
    private TencentSecurityComplianceCredential credential;
    /**
     * 区域
     */
    private String regionId;
}
