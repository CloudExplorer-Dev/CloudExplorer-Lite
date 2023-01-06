package com.fit2cloud.provider.impl.huawei.entity.request;

import com.fit2cloud.provider.impl.huawei.entity.credential.HuaweiSecurityComplianceCredential;
import com.huaweicloud.sdk.evs.v2.model.ListVolumesRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/6  10:12}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ListDiskInstanceRequest extends ListVolumesRequest {
    /**
     * 认证对象
     */
    private HuaweiSecurityComplianceCredential credential;

    /**
     * 区域id
     */
    private String regionId;
}
