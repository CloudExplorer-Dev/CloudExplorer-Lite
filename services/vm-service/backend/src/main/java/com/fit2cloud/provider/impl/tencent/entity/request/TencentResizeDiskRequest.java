package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.provider.entity.request.BaseDiskResizeRequest;
import com.tencentcloudapi.cbs.v20170312.models.ResizeDiskRequest;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/13
 */
@Data
public class TencentResizeDiskRequest extends BaseDiskResizeRequest {

    public ResizeDiskRequest toResizeDiskRequest(){
        ResizeDiskRequest request = new ResizeDiskRequest();
        request.setDiskId(super.getDiskId());
        request.setDiskSize(super.getNewDiskSize());
        return request;
    }
}
