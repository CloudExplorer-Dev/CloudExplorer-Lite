package com.fit2cloud.provider.impl.tencent.entity.request;

import com.tencentcloudapi.cbs.v20170312.models.TerminateDisksRequest;
import lombok.Data;
import com.fit2cloud.vm.entity.request.BaseDiskRequest;

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
