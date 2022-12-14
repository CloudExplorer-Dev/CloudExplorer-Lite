package com.fit2cloud.provider.entity;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/11/16 3:36 PM
 */
@Data
public class F2CNetwork {
    private String regionId;
    private String zoneId;
    private String vpcId;
    private String vpcName;
    private String networkId;
    private String networkName;
    private String ipSegment;
}
