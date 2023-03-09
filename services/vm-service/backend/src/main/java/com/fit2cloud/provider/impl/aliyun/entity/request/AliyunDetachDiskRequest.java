package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.DescribeDisksRequest;
import com.aliyun.ecs20140526.models.DescribeInstancesRequest;
import com.aliyun.ecs20140526.models.DetachDiskRequest;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.entity.request.BaseDiskRequest;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/24 8:38 PM
 */
@Data
public class AliyunDetachDiskRequest extends BaseDiskRequest {

    public DetachDiskRequest toDetachDisksRequest() {
        DetachDiskRequest request = new DetachDiskRequest();
        request.setDiskId(super.getDiskId());
        request.setInstanceId(super.getInstanceUuid());
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
