package com.fit2cloud.provider.impl.tencent.entity.request;

import lombok.Data;

/**
 * @author jianneng
 * @date 2022/10/26 11:52
 **/
@Data
public class TencentInstanceRequest extends TencentBaseRequest {

    private String uuid;

    /**
     * 强制执行
     */
    private Boolean force = false;
}
