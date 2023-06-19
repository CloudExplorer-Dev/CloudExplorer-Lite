package com.fit2cloud.provider.impl.openstack.entity.request;

import com.fit2cloud.common.provider.impl.openstack.entity.request.OpenStackBaseRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/19  10:45}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class CalculateConfigUpdatePriceRequest extends OpenStackBaseRequest {
    /**
     * 云账号id
     */
    private String cloudAccountId;

    /**
     * 变更后类型
     */
    private String newInstanceType;
    /**
     * 实例付费类型
     */
    private String instanceChargeType;
    /**
     * 进行id
     */
    private String imageId;
    /**
     * 区域id
     */
    private String regionId;
}
