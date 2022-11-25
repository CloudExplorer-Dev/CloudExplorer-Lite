package com.fit2cloud.provider.impl.aliyun.entity.request;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/11/15 5:38 PM
 */
@Data
public class AliyunGetSecurityGroupRequest extends AliyunBaseRequest {

    private String networkId;

    private String vpcId;

}
