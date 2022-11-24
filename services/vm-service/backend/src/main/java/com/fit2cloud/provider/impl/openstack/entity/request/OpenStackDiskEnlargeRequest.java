package com.fit2cloud.provider.impl.openstack.entity.request;

import com.fit2cloud.common.provider.impl.openstack.entity.request.OpenStackBaseRequest;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class OpenStackDiskEnlargeRequest extends OpenStackBaseRequest {

    private String diskId;

    private String instanceUuid;

    private int newDiskSize;


}
