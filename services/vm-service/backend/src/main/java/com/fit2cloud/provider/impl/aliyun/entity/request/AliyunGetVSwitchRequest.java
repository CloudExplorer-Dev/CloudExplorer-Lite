package com.fit2cloud.provider.impl.aliyun.entity.request;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/11/16 3:40 PM
 */
@Data
public class AliyunGetVSwitchRequest extends AliyunBaseRequest{

    private String zoneId;

    private String vpcId;
}
