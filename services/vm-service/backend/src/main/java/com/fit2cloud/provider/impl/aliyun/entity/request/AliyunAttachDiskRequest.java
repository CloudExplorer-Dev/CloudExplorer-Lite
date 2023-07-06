package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.AttachDiskRequest;
import com.aliyun.ecs20140526.models.DescribeDisksRequest;
import com.aliyun.ecs20140526.models.DescribeInstancesRequest;
import com.fit2cloud.common.utils.JsonUtil;
import lombok.Data;
import com.fit2cloud.vm.constants.DeleteWithInstance;
import com.fit2cloud.vm.entity.request.BaseDiskAttachRequest;

/**
 * Author: LiuDi
 * Date: 2022/10/24 8:40 PM
 */
@Data
public class AliyunAttachDiskRequest extends BaseDiskAttachRequest {

    public AttachDiskRequest toAttachDiskRequest() {
        AttachDiskRequest request = new AttachDiskRequest();
        request.setDiskId(super.getDiskId());
        request.setInstanceId(super.getInstanceUuid());
        request.setDeleteWithInstance(DeleteWithInstance.YES.name().equals(super.getDeleteWithInstance()));
        return request;
    }

    public DescribeDisksRequest toDescribeDisksRequest() {
        DescribeDisksRequest describeDisksRequest = new DescribeDisksRequest();
        describeDisksRequest.setRegionId(super.getRegionId());
        describeDisksRequest.setDiskIds(JsonUtil.toJSONString(new String[]{super.getDiskId()}));
        return describeDisksRequest;
    }

    public DescribeInstancesRequest toDescribeInstancesRequest() {
        DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
        describeInstancesRequest.setRegionId(super.getRegionId());
        describeInstancesRequest.setInstanceIds(JsonUtil.toJSONString(new String[]{super.getInstanceUuid()}));
        return describeInstancesRequest;
    }
}
