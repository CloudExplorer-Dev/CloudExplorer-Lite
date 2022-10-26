package com.fit2cloud.provider.impl.huawei.entity.request;

import lombok.Data;

/**
 * @author jianneng
 * @date 2022/10/26 09:58
 **/
@Data
public class HuaweiInstanceRequest extends HuaweiBaseRequest {

    private String uuId;

    /**
     * 强制执行
     */
    private Boolean force = false;

}
