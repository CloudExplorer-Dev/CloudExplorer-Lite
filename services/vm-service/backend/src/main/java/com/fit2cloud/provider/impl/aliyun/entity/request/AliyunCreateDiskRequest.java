package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.CreateDiskRequest;
import com.fit2cloud.provider.entity.request.BaseDiskCreateRequest;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/24 8:52 PM
 */
@Data
public class AliyunCreateDiskRequest extends BaseDiskCreateRequest {

    public CreateDiskRequest toCreateDiskRequest() {
        CreateDiskRequest createDiskRequest = new CreateDiskRequest();
        createDiskRequest.setRegionId(getRegionId());
        return createDiskRequest;
    }
}
