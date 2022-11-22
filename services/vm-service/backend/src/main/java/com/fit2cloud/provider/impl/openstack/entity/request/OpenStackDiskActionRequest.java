package com.fit2cloud.provider.impl.openstack.entity.request;

import com.fit2cloud.common.provider.impl.openstack.entity.request.OpenStackBaseRequest;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class OpenStackDiskActionRequest extends OpenStackBaseRequest {

    private String diskId;

    private String instanceUuid;

    private boolean force;

    /**
     * 挂载点
     */
    private String device;

}
