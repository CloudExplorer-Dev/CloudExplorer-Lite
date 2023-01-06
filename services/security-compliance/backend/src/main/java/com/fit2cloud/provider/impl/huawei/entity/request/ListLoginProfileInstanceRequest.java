package com.fit2cloud.provider.impl.huawei.entity.request;

import com.fit2cloud.provider.impl.huawei.entity.credential.HuaweiSecurityComplianceCredential;
import com.huaweicloud.sdk.iam.v3.model.ListUserLoginProtectsRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/6  17:56}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ListLoginProfileInstanceRequest extends ListUserLoginProtectsRequest {
    /**
     * 认证对象
     */
    private HuaweiSecurityComplianceCredential credential;
}
