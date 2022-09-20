package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.DescribeInstancesRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:张少虎
 * @Date: 2022/9/19  5:09 PM
 * @Version 1.0
 * @注释:
 */
@Data
@NoArgsConstructor
public class ListVirtualMachineRequest extends DescribeInstancesRequest {
    /**
     * 认证信息
     */
    private String credential;
}
