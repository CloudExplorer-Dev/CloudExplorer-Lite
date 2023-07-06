package com.fit2cloud.provider.impl.huawei.entity.request;

import com.huaweicloud.sdk.evs.v2.model.DeleteVolumeRequest;
import lombok.Data;
import com.fit2cloud.vm.entity.request.BaseDiskRequest;

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
