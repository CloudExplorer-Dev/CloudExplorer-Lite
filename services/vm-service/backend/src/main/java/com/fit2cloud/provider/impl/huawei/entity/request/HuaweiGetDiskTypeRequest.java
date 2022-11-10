package com.fit2cloud.provider.impl.huawei.entity.request;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/11/4 2:05 PM
 */
@Data
public class HuaweiGetDiskTypeRequest extends HuaweiBaseRequest {
    /**
     * 可用区 ID
     */
    private String zone;

}
