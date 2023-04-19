package com.fit2cloud.provider.impl.huawei.entity.request;

import lombok.Data;

/**
 * @author : LiuDi
 * @date : 2022/12/21 14:21
 */
@Data
public class HuaweiUpdateConfigRequest extends HuaweiBaseRequest {
    private String instanceUuid;
    private String currentInstanceType;
    private String newInstanceType;
}
