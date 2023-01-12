package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.DescribeSecurityGroupAttributeRequest;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliSecurityComplianceCredential;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/10  11:46}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class GetSecurityGroupRuleInstanceRequest extends DescribeSecurityGroupAttributeRequest {
    /**
     * 认证信息
     */
    private AliSecurityComplianceCredential credential;
}
