package com.fit2cloud.common.provider.impl.openstack.entity.request;

import lombok.Data;


@Data
public class GetRegionsRequest extends OpenStackBaseRequest {
    /**
     * 认证信息
     */
    private String credential;
}
