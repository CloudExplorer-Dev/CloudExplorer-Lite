package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.CreateDiskRequest;
import com.aliyun.ecs20140526.models.DescribeDisksRequest;
import com.aliyun.ecs20140526.models.DescribeInstancesRequest;
import com.fit2cloud.common.utils.JsonUtil;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/24 8:52 PM
 */
@Data
public class AliyunCreateDiskRequest extends AliyunBaseRequest {
    private String zone;
    private String diskName;
    private String description;
    private String diskType;
    private Integer size;
    private String diskId;
    private String instanceUuid;
    private String deleteWithInstance;
    // 是否挂载在机器上
    private Boolean isAttached;

    public CreateDiskRequest toCreateDiskRequest(String chargeType) {
        CreateDiskRequest createDiskRequest = new CreateDiskRequest();
        createDiskRequest.setRegionId(super.getRegionId());
        createDiskRequest.setDiskName(this.diskName);
        createDiskRequest.setSize(this.size);
        createDiskRequest.setDiskCategory(this.diskType);
        createDiskRequest.setZoneId(this.zone);
        return createDiskRequest;
    }

    public DescribeDisksRequest toDescribeDisksRequest(String diskId) {
        DescribeDisksRequest describeDisksRequest = new DescribeDisksRequest();
        describeDisksRequest.setRegionId(super.getRegionId());
        describeDisksRequest.setZoneId(this.zone);
        describeDisksRequest.setDiskIds(JsonUtil.toJSONString(new String[]{diskId}));
        return describeDisksRequest;
    }

    public DescribeInstancesRequest toDescribeInstancesRequest() {
        DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
        describeInstancesRequest.setRegionId(super.getRegionId());
        describeInstancesRequest.setInstanceIds(JsonUtil.toJSONString(new String[]{this.instanceUuid}));
        return describeInstancesRequest;
    }
}
