package com.fit2cloud.provider.impl.huawei.entity.request;

import com.huaweicloud.sdk.ims.v2.model.ListImagesRequest;
import lombok.Data;

/**
 * @Author:张少虎
 * @Date: 2022/9/22  7:26 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class ListImageRequest extends ListImagesRequest {
    /**
     * 认证信息
     */
    private String credential;

    private String regionId;

}
