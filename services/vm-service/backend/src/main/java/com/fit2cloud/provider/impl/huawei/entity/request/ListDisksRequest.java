package com.fit2cloud.provider.impl.huawei.entity.request;

import com.aliyun.ecs20140526.models.DescribeDisksRequest;
import com.huaweicloud.sdk.evs.v2.model.ListVolumesRequest;
import lombok.Data;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  2:56 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class ListDisksRequest extends ListVolumesRequest {
    /**
     * 认证信息
     */
    private String credential;
    /**
     * 区域id
     */
    private String regionId;
}
