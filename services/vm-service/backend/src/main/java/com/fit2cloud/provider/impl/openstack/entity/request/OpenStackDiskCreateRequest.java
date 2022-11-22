package com.fit2cloud.provider.impl.openstack.entity.request;

import com.fit2cloud.common.provider.impl.openstack.entity.request.OpenStackBaseRequest;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class OpenStackDiskCreateRequest extends OpenStackBaseRequest {

    private String zone;
    private String diskName;
    private String diskType;
    private String description;

    private String instanceUuid;

    private int size;


}
