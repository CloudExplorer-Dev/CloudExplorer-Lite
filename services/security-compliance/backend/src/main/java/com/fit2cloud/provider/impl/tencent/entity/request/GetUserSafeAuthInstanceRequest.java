package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.provider.impl.tencent.entity.credential.TencentSecurityComplianceCredential;
import com.tencentcloudapi.cam.v20190116.models.DescribeSafeAuthFlagCollRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/13  11:04}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class GetUserSafeAuthInstanceRequest extends DescribeSafeAuthFlagCollRequest {
    /**
     * 认证对象
     */
    private TencentSecurityComplianceCredential credential;
}
