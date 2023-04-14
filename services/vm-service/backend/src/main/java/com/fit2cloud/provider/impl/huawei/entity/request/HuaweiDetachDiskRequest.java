package com.fit2cloud.provider.impl.huawei.entity.request;

import com.fit2cloud.provider.entity.request.BaseDiskRequest;
import com.huaweicloud.sdk.ecs.v2.model.DetachServerVolumeRequest;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/25 8:38 PM
 */
@Data
public class HuaweiDetachDiskRequest extends BaseDiskRequest {

    public DetachServerVolumeRequest toDetachServerVolumeRequest() {
        DetachServerVolumeRequest detachServerVolumeRequest = new DetachServerVolumeRequest();
        detachServerVolumeRequest.setServerId(super.getInstanceUuid());
        detachServerVolumeRequest.setVolumeId(super.getDiskId());
        return detachServerVolumeRequest;
    }
}
