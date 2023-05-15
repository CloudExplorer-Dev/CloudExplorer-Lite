package com.fit2cloud.provider.impl.huawei.entity.request;


import com.fit2cloud.provider.impl.huawei.entity.credential.HuaweiSecurityComplianceCredential;
import com.huaweicloud.sdk.cbr.v1.model.ListPoliciesRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/15  11:11}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ListPoliciesInstanceRequest extends ListPoliciesRequest {
    /**
     * 认证对象
     */
    private HuaweiSecurityComplianceCredential credential;

    /**
     * 区域id
     */
    private String regionId;
}
