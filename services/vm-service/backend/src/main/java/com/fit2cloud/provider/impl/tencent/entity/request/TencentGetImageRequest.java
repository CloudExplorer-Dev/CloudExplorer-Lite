package com.fit2cloud.provider.impl.tencent.entity.request;

import com.tencentcloudapi.cvm.v20170312.models.DescribeImagesRequest;
import com.tencentcloudapi.cvm.v20170312.models.Filter;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author : LiuDi
 * @date : 2022/12/13 11:06
 */
@Data
public class TencentGetImageRequest extends TencentVmCreateRequest {
    /**
     * 镜像类型，目前只支持公共镜像
     */
    private String imageType;

    public DescribeImagesRequest toDescribeImagesRequest() {
        DescribeImagesRequest describeImagesRequest = new DescribeImagesRequest();
        List<Filter> filters = new ArrayList<>();

        // 默认只展示公共镜像
        Filter filterImageType = new Filter();
        filterImageType.setName("image-type");
        filterImageType.setValues(new String[]{Optional.ofNullable(imageType).orElse("PUBLIC_IMAGE")});
        filters.add(filterImageType);

        Optional.ofNullable(this.getOs()).ifPresent((os) -> {
            Filter filterPlatform = new Filter();
            filterPlatform.setName("platform");
            filterPlatform.setValues(new String[]{os});
            filters.add(filterPlatform);
        });

        if (CollectionUtils.isNotEmpty(filters)) {
            describeImagesRequest.setFilters(filters.toArray(new Filter[]{}));
        }
        if (this.getInstanceTypeDTO() != null && StringUtils.isNotEmpty(this.getInstanceTypeDTO().getInstanceType())) {
            describeImagesRequest.setInstanceType(this.getInstanceTypeDTO().getInstanceType());
        }
        return describeImagesRequest;
    }

}
