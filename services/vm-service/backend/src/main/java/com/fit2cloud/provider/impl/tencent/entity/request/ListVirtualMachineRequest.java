package com.fit2cloud.provider.impl.tencent.entity.request;

import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesRequest;
import lombok.Data;

/**
 * @Author:张少虎
 * @Date: 2022/9/23  3:53 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class ListVirtualMachineRequest extends DescribeInstancesRequest {
    /**
     * 认证信息
     */
    private String credential;

    /**
     * 区域
     */
    private String regionId;
}
