package com.fit2cloud.provider.impl.tencent.entity.request;

import lombok.Data;

/**
 * @author : LiuDi
 * @date : 2022/12/20 17:38
 */
@Data
public class TencentUpdateConfigRequest extends TencentBaseRequest {
    private String instanceChargeType;
    private String instanceUuid;
    private String currentInstanceType;
    private String newInstanceType;
}
