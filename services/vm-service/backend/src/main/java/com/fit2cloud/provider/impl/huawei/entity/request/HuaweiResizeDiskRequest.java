package com.fit2cloud.provider.impl.huawei.entity.request;

import com.fit2cloud.provider.entity.request.BaseDiskResizeRequest;
import com.huaweicloud.sdk.evs.v2.model.BssParamForResizeVolume;
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
        //自动扣费参数，只对包周期有意义，对按需无影响
        BssParamForResizeVolume bssParamBody = new BssParamForResizeVolume();
        bssParamBody.withIsAutoPay(BssParamForResizeVolume.IsAutoPayEnum.TRUE);
        body.withBssParam(bssParamBody);
        body.setOsExtend(osExtend);
        resizeVolumeRequest.withBody(body);
        return resizeVolumeRequest;
    }
}
