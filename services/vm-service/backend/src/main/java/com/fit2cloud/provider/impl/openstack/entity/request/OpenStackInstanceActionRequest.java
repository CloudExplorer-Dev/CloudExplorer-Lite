package com.fit2cloud.provider.impl.openstack.entity.request;

import com.fit2cloud.common.provider.impl.openstack.entity.request.OpenStackBaseRequest;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class OpenStackInstanceActionRequest extends OpenStackBaseRequest {

    private String uuid;

    /**
     * 强制执行
     */
    private Boolean force = false;
}
