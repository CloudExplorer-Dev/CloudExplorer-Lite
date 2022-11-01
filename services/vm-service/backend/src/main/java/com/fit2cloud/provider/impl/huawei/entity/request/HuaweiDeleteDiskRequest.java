package com.fit2cloud.provider.impl.huawei.entity.request;

import com.fit2cloud.provider.entity.request.BaseDiskRequest;
import com.huaweicloud.sdk.evs.v2.model.DeleteVolumeRequest;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/25 3:10 PM
 */
@Data
public class HuaweiDeleteDiskRequest extends BaseDiskRequest {

    public DeleteVolumeRequest toDeleteVolumeRequest() {
        DeleteVolumeRequest deleteVolumeRequest = new DeleteVolumeRequest();
        deleteVolumeRequest.setVolumeId(super.getDiskId());
        return deleteVolumeRequest;
    }
}
