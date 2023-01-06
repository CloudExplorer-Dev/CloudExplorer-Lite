package com.fit2cloud.provider.impl.openstack.entity.request;

import com.fit2cloud.common.provider.impl.openstack.entity.request.OpenStackBaseRequest;
import lombok.Data;


@Data
public class OpenStackConfigUpdateRequest extends OpenStackBaseRequest {

    private String instanceUuid;
    private String newInstanceType;

}
