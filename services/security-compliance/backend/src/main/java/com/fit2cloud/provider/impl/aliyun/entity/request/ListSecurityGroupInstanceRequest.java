package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.DescribeSecurityGroupsRequest;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliSecurityComplianceCredential;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/10  11:39}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ListSecurityGroupInstanceRequest extends DescribeSecurityGroupsRequest {
    /**
     * 认证信息
     */
    private AliSecurityComplianceCredential credential;

}
