package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.DescribeDisksRequest;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliSecurityComplianceCredential;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/6  10:25}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ListDiskInstanceRequest extends DescribeDisksRequest {
    /**
     * 认证信息
     */
    private AliSecurityComplianceCredential credential;


}
