package com.fit2cloud.provider.impl.aliyun.entity.request;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/11/15 5:38 PM
 */
@Data
public class AliyunGetRegionRequest extends AliyunBaseRequest {
    /**
     * 实例计费方式
     */
    private String instanceChargeType;
}
