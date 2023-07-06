package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.CreateDiskRequest;
import lombok.Data;
import com.fit2cloud.vm.entity.F2CDisk;

import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/10/24 8:52 PM
 */
@Data
public class AliyunCreateDisksRequest extends AliyunBaseRequest {
    private String zone;
    private String diskName;
    private String description;
    private String diskType;
    private Integer size;
    private String diskId;
    private String instanceUuid;
    private List<F2CDisk> disks;
    private String deleteWithInstance;
    // 是否挂载在机器上
    private Boolean isAttached;

    public CreateDiskRequest toCreateDiskRequest() {
        CreateDiskRequest createDiskRequest = new CreateDiskRequest();
        createDiskRequest.setRegionId(getRegionId());
        return createDiskRequest;
    }
}
