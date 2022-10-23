package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.provider.entity.request.BaseDiskRequest;
import com.tencentcloudapi.cbs.v20170312.models.TerminateDisksRequest;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/13
 */
@Data
public class TencentDeleteDiskRequest extends BaseDiskRequest {

    public TerminateDisksRequest toTerminateDisksRequest() {
        TerminateDisksRequest request = new TerminateDisksRequest();
        request.setDiskIds(new String[]{super.getDiskId()});
        return request;
    }
}
