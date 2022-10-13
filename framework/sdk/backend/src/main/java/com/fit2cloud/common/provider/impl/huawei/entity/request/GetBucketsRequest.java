package com.fit2cloud.common.provider.impl.huawei.entity.request;

import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/12  3:26 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class GetBucketsRequest {
    /**
     * 认证信息
     */
    private String credential;

    /**
     * 区域
     */
    private String regionId;
}
