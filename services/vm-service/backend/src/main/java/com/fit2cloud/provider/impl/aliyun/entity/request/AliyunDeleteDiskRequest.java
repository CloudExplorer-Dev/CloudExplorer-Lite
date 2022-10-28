package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.DeleteDiskRequest;
import com.aliyun.ecs20140526.models.DescribeDisksRequest;
import com.fit2cloud.provider.entity.request.BaseDiskRequest;
import lombok.Data;
import org.eclipse.jetty.util.ajax.JSON;

/**
 * Author: LiuDi
 * Date: 2022/10/24 6:20 PM
 */
@Data
public class AliyunDeleteDiskRequest extends BaseDiskRequest {

    public DeleteDiskRequest toDeleteDiskRequest() {
        DeleteDiskRequest request = new DeleteDiskRequest();
        request.setDiskId(super.getDiskId());
        return request;
    }

    public DescribeDisksRequest toDescribeDisksRequest() {
        DescribeDisksRequest describeDisksRequest = new DescribeDisksRequest();
        describeDisksRequest.setRegionId(super.getRegionId());
        describeDisksRequest.setDiskIds(JSON.toString(new String[]{super.getDiskId()}));
        return describeDisksRequest;
    }
}
