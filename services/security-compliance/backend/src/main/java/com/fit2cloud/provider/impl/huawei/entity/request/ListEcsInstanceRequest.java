package com.fit2cloud.provider.impl.huawei.entity.request;

import com.fit2cloud.provider.impl.huawei.entity.credential.HuaweiSecurityComplianceCredential;
import com.huaweicloud.sdk.ecs.v2.model.ListServersDetailsRequest;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/6  13:42}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ListEcsInstanceRequest extends ListServersDetailsRequest {
    /**
     * 认证对象
     */
    private HuaweiSecurityComplianceCredential credential;

    /**
     * 区域id
     */
    private String regionId;
}
