package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.provider.entity.request.BaseDiskRequest;
import com.tencentcloudapi.cbs.v20170312.models.DetachDisksRequest;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/13 8:38 PM
 */
@Data
public class TencentDetachDiskRequest extends BaseDiskRequest {

    public DetachDisksRequest toDetachDisksRequest(){
        DetachDisksRequest request = new DetachDisksRequest();
        request.setDiskIds(new String[]{super.getDiskId()});
        request.setInstanceId(super.getInstanceUuid());
        return request;
    }
}
