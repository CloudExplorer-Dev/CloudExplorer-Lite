package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.DescribeAvailableResourceRequest;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/11/4 2:05 PM
 */
@Data
public class AliyunGetDiskTypeRequest extends AliyunVmCreateRequest {
    private String diskUsage;

    public DescribeAvailableResourceRequest toDescribeAvailableResourceRequest() {
        DescribeAvailableResourceRequest describeAvailableResourceRequest = new DescribeAvailableResourceRequest()
                .setRegionId(this.getRegionId())
                .setZoneId(this.getZoneId())
                .setInstanceChargeType(this.getInstanceChargeType())
                .setResourceType("instance")
                .setDestinationResource(this.diskUsage);

        if (this.getInstanceTypeDTO() != null && this.getInstanceTypeDTO().getInstanceType() != null) {
            describeAvailableResourceRequest.setInstanceType(this.getInstanceTypeDTO().getInstanceType());
        }
        return describeAvailableResourceRequest;
    }
}
