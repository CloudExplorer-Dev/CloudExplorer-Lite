package com.fit2cloud.provider.impl.aliyun.entity.request;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/11/17 4:32 PM
 */
@Data
public class AliyunGetAvailableResourceRequest extends AliyunBaseRequest {
    /**
     * 可用区
     */
    private String zoneId;
    /**
     * 实例收费类型
     */
    private String instanceChargeType;
    /**
     * 要查询的资源类型(InstanceType、Zone等)
     */
    private String destinationResource;
}
