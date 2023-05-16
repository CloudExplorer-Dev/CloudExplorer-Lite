package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.DescribeAvailableResourceRequest;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
                .setResourceType(StringUtils.equals(this.diskUsage, "SystemDisk") ? "instance" : "disk")
                .setDestinationResource(this.diskUsage);

        if (getInstanceType() != null) {
            describeAvailableResourceRequest.setInstanceType(getInstanceType());
        }
        return describeAvailableResourceRequest;
    }
}
