package com.fit2cloud.provider.impl.huawei.entity;

import lombok.Data;

/**
 * @author jianneng
 * @date 2022/11/18 17:54
 **/
@Data
public class F2CHuaweiVpc {

    private String uuid;
    private String name;
    private String cidr;
    private String status;
    private String description;
}
