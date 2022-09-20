package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.DescribeInstanceTypesRequest;
import lombok.Data;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  3:31 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class ListInstanceTypesRequest extends DescribeInstanceTypesRequest {
    /**
     * 认证信息
     */
    private String credential;
}
