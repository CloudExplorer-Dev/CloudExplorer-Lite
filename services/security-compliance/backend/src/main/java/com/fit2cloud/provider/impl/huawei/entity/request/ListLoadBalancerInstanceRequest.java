package com.fit2cloud.provider.impl.huawei.entity.request;

import com.fit2cloud.provider.impl.huawei.entity.credential.HuaweiSecurityComplianceCredential;
import com.huaweicloud.sdk.elb.v3.model.*;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/6  11:12}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ListLoadBalancerInstanceRequest extends ListLoadBalancersRequest {
    /**
     * 认证对象
     */
    private HuaweiSecurityComplianceCredential credential;

    /**
     * 区域id
     */
    private String regionId;
}
