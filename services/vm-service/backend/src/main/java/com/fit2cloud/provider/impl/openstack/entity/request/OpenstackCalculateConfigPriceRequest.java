package com.fit2cloud.provider.impl.openstack.entity.request;

import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/16  20:10}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class OpenstackCalculateConfigPriceRequest extends OpenStackServerCreateRequest {
    /**
     * 云账号id
     */
    private String accountId;
}
