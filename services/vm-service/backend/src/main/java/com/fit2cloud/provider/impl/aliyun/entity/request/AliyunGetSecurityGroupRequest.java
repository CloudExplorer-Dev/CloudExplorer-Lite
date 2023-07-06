package com.fit2cloud.provider.impl.aliyun.entity.request;

import lombok.Data;
import com.fit2cloud.vm.entity.F2CNetwork;

/**
 * Author: LiuDi
 * Date: 2022/11/15 5:38 PM
 */
@Data
public class AliyunGetSecurityGroupRequest extends AliyunBaseRequest {

    private F2CNetwork f2CNetwork;

    private String networkId;
}
