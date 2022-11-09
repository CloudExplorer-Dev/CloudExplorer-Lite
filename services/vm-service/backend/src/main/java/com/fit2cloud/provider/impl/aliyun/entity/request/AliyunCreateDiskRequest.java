package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.CreateDiskRequest;
import com.aliyun.ecs20140526.models.DescribeDisksRequest;
import com.aliyun.ecs20140526.models.DescribeInstancesRequest;
import lombok.Data;
import org.eclipse.jetty.util.ajax.JSON;

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
        createDiskRequest.setSize( this.size);
        createDiskRequest.setDiskCategory(this.diskType);
        //自动挂载 (zoneId 和instanceId不可同时存在 且只有预付费的才能自动挂载)
        if (this.isAttached && "PrePaid".equals(chargeType)) {
            createDiskRequest.setInstanceId(this.instanceUuid);
        } else {
            createDiskRequest.setZoneId(this.zone);
        }
        return createDiskRequest;
    }

    public  DescribeDisksRequest toDescribeDisksRequest(String diskId) {
        DescribeDisksRequest describeDisksRequest = new DescribeDisksRequest();
        describeDisksRequest.setRegionId(super.getRegionId());
        describeDisksRequest.setZoneId(this.zone);
        describeDisksRequest.setDiskIds(JSON.toString(new String[]{diskId}));
        return describeDisksRequest;
    }

    public DescribeInstancesRequest toDescribeInstancesRequest() {
        DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
        describeInstancesRequest.setRegionId(super.getRegionId());
        describeInstancesRequest.setInstanceIds(JSON.toString(new String[]{this.instanceUuid}));
        return describeInstancesRequest;
    }
}
