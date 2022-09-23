package com.fit2cloud.provider.impl.huawei.entity.request;

import com.huaweicloud.sdk.ecs.v2.model.ListServersDetailsRequest;
import lombok.Data;

/**
 * @Author:张少虎
 * @Date: 2022/9/22  2:44 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class ListVirtualMachineRequest extends ListServersDetailsRequest {
    /**
     * 认证信息
     */
    private String credential;

    private String regionId;

}
