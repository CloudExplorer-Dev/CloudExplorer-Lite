package com.fit2cloud.provider.impl.huawei.entity.request;

import com.fit2cloud.provider.entity.request.BaseDiskResizeRequest;
import com.huaweicloud.sdk.ecs.v2.model.DetachServerVolumeRequest;
import com.huaweicloud.sdk.evs.v2.model.OsExtend;
import com.huaweicloud.sdk.evs.v2.model.ResizeVolumeRequest;
import com.huaweicloud.sdk.evs.v2.model.ResizeVolumeRequestBody;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/25 6:23 PM
 */
@Data
public class HuaweiResizeDiskRequest extends BaseDiskResizeRequest {

    public ResizeVolumeRequest toResizeVolumeRequest() {
        ResizeVolumeRequest resizeVolumeRequest = new ResizeVolumeRequest();
        resizeVolumeRequest.withVolumeId(super.getDiskId());
        ResizeVolumeRequestBody body = new ResizeVolumeRequestBody();
        OsExtend osExtend = new OsExtend();
        osExtend.setNewSize((int) super.getNewDiskSize());
        body.setOsExtend(osExtend);
        resizeVolumeRequest.withBody(body);
        return resizeVolumeRequest;
    }
}
