package com.fit2cloud.provider.impl.huawei.entity.request;

import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.request.BaseDiskCreateRequest;
import com.huaweicloud.sdk.evs.v2.model.CreateVolumeOption;
import com.huaweicloud.sdk.evs.v2.model.CreateVolumeRequest;
import com.huaweicloud.sdk.evs.v2.model.CreateVolumeRequestBody;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/25 1:52 PM
 */
@Data
public class HuaweiCreateDiskRequest extends BaseDiskCreateRequest {

    public CreateVolumeRequest toCreateVolumeRequest(F2CDisk disk) {
        CreateVolumeRequest createVolumeRequest = new CreateVolumeRequest();
        CreateVolumeRequestBody body = new CreateVolumeRequestBody();
        CreateVolumeOption volume = new CreateVolumeOption();
        volume.setAvailabilityZone(disk.getZone());
        volume.setCount(1);
        volume.setSize((int) disk.getSize());
        volume.setName(disk.getDiskName());
        volume.setVolumeType(CreateVolumeOption.VolumeTypeEnum.fromValue(disk.getDiskType()));
        body.setVolume(volume);
        createVolumeRequest.setBody(body);

        return createVolumeRequest;
    }
}
