package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.DescribeImagesRequest;
import com.fit2cloud.provider.impl.aliyun.constants.AliyunOSType;

/**
 * @author : LiuDi
 * @date : 2022/12/14 10:47
 */
public class AliyunGetImageRequest extends AliyunVmCreateRequest {

    /**
     * 镜像来源：
     * system：阿里云提供的公共镜像。
     * self：您创建的自定义镜像。
     * others
     */
    private String imageSource;

    public DescribeImagesRequest toDescribeImagesRequest() {
        DescribeImagesRequest describeImagesRequest = new DescribeImagesRequest()
                .setRegionId(this.getRegionId())
                .setStatus("Available")
                .setActionType("CreateEcs");

        this.imageSource = this.imageSource == null ? "system" : this.imageSource;
        describeImagesRequest.setImageOwnerAlias(imageSource);

        if (getInstanceType() != null) {
            describeImagesRequest.setInstanceType(getInstanceType());
        }

        if (this.getOs() != null) {
            describeImagesRequest.setOSType(AliyunOSType.WindowsServer.getDisplayValue().equalsIgnoreCase(this.getOs()) ? "windows" : "linux");
        }

        return describeImagesRequest;
    }
}
