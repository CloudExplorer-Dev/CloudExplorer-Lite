package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.DescribeImagesRequest;
import lombok.Data;

/**
 * @Author:张少虎
 * @Date: 2022/9/21  9:27 AM
 * @Version 1.0
 * @注释:
 */
@Data
public class ListImageRequest extends DescribeImagesRequest {
    /**
     * 认证信息
     */
    private String credential;
}
