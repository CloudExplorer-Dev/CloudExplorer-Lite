package com.fit2cloud.provider.impl.tencent.entity.request;

import com.tencentcloudapi.cbs.v20170312.models.DescribeDisksRequest;
import lombok.Data;

/**
 * @Author:张少虎
 * @Date: 2022/9/23  4:26 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class ListDiskRequest extends DescribeDisksRequest {
    /**
     * 认证信息
     */
    private String credential;

    /**
     * 区域
     */
    private String regionId;
}
