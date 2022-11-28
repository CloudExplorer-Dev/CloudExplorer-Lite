package com.fit2cloud.provider.impl.huawei.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jianneng
 * @date 2022/11/18 19:24
 **/
@Data
@Accessors(chain = true)
public class NetworkConfig {

    /**
     * 子网UUID
     */
    private String uuid;
    /**
     * 子网名称
     */
    private String name;
    /**
     * VPC uuid
     */
    private String vpcId;
    /**
     * VPC名称
     */
    private String vpcName;
}
