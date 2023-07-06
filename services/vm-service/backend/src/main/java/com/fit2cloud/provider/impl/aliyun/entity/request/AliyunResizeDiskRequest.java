package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.DescribeDisksRequest;
import com.aliyun.ecs20140526.models.DescribeInstancesRequest;
import com.aliyun.ecs20140526.models.ResizeDiskRequest;
import com.fit2cloud.common.utils.JsonUtil;
import lombok.Data;
import com.fit2cloud.vm.entity.request.BaseDiskResizeRequest;

/**
 * Author: LiuDi
 * Date: 2022/10/24 6:15 PM
 */
@Data
public class AliyunResizeDiskRequest extends BaseDiskResizeRequest {

    public ResizeDiskRequest toResizeDiskRequest() {
        ResizeDiskRequest request = new ResizeDiskRequest();
        request.setDiskId(super.getDiskId());
        request.setNewSize((int) super.getNewDiskSize());
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
