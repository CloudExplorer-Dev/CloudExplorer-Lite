package com.fit2cloud.provider.impl.huawei.entity.request;

import com.huaweicloud.sdk.evs.v2.model.BssParamForCreateVolume;
import com.huaweicloud.sdk.evs.v2.model.CreateVolumeOption;
import com.huaweicloud.sdk.evs.v2.model.CreateVolumeRequest;
import com.huaweicloud.sdk.evs.v2.model.CreateVolumeRequestBody;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/25 1:52 PM
 */
@Data
public class HuaweiCreateDiskRequest extends HuaweiBaseRequest {
    private String zone;
    private String diskName;
    private String diskType;
    private String diskChargeType;
    private long size;
    /**
     * 是否自动挂载至云主机
     */
    private Boolean isAttached;
    /**
     * 云主机 ID
     */
    private String instanceUuid;
    /**
     * 云盘是否随云主机删除
     */
    private String deleteWithInstance;

    public CreateVolumeRequest toCreateVolumeRequest() {
        CreateVolumeRequest createVolumeRequest = new CreateVolumeRequest();
        CreateVolumeRequestBody body = new CreateVolumeRequestBody();
        CreateVolumeOption volume = new CreateVolumeOption();
        volume.setAvailabilityZone(this.zone);
        volume.setCount(1);
        volume.setSize((int) this.size);
        volume.setName(this.diskName);
        volume.setVolumeType(CreateVolumeOption.VolumeTypeEnum.fromValue(this.diskType));
        body.setVolume(volume);
        body.setServerId(this.instanceUuid);

        BssParamForCreateVolume bssParamBody = new BssParamForCreateVolume();
        bssParamBody.withIsAutoPay(BssParamForCreateVolume.IsAutoPayEnum.TRUE)
                .withIsAutoRenew(BssParamForCreateVolume.IsAutoRenewEnum.TRUE);
        body.withBssParam(bssParamBody);

        createVolumeRequest.setBody(body);

        return createVolumeRequest;
    }
}
