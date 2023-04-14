package com.fit2cloud.provider.impl.huawei.entity.request;

import com.fit2cloud.provider.entity.request.BaseDiskAttachRequest;
import com.huaweicloud.sdk.ecs.v2.model.AttachServerVolumeOption;
import com.huaweicloud.sdk.ecs.v2.model.AttachServerVolumeRequest;
import com.huaweicloud.sdk.ecs.v2.model.AttachServerVolumeRequestBody;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/25 5:40 PM
 */
@Data
public class HuaweiAttachDiskRequest extends BaseDiskAttachRequest {

    public AttachServerVolumeRequest toAttachServerVolumeRequest() {
        AttachServerVolumeRequest attachServerVolumeRequest = new AttachServerVolumeRequest();
        AttachServerVolumeRequestBody body = new AttachServerVolumeRequestBody();
        AttachServerVolumeOption volumeAttachment = new AttachServerVolumeOption();
        volumeAttachment.setVolumeId(super.getDiskId());
        body.setVolumeAttachment(volumeAttachment);
        attachServerVolumeRequest.setServerId(super.getInstanceUuid());
        attachServerVolumeRequest.setBody(body);
        return attachServerVolumeRequest;
    }
}
