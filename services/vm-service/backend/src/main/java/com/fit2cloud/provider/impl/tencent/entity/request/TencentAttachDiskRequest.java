package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.provider.constants.DeleteWithInstance;
import com.fit2cloud.provider.entity.request.BaseDiskAttachRequest;
import com.tencentcloudapi.cbs.v20170312.models.AttachDisksRequest;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/13 8:40 PM
 */
@Data
public class TencentAttachDiskRequest extends BaseDiskAttachRequest {

    public AttachDisksRequest toAttachDisksRequest(){
        AttachDisksRequest request = new AttachDisksRequest();
        request.setAttachMode(super.getAttachMode());
        request.setDiskIds(new String[]{super.getDiskId()});
        request.setInstanceId(super.getInstanceUuid());
        request.setDeleteWithInstance(DeleteWithInstance.YES.name().equals(super.getDeleteWithInstance()));
        return request;
    }
}
