package com.fit2cloud.provider.impl.huawei.entity;

import lombok.Data;

/**
 * @author jianneng
 * @date 2022/11/18 17:53
 **/
@Data
public class F2CHuaweiSubnet {

    private String uuid;
    private String name;
    private String cidr;
    private String cidrV6;
    private String vpcId;
    private String vpcName;
    private String availabilityZone;
    private String description;
}
