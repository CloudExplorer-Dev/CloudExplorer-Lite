package com.fit2cloud.provider.entity.request;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/13 9:39 PM
 */
@Data
public class BaseDiskRequest {

    /**
     * 认证信息
     */
    private String credential;

    /**
     * 区域
     */
    private String regionId;

    /**
     * 磁盘IDS
     */
    String[] diskIds;

    /**
     * 磁盘ID
     */
    String diskId;

    /**
     * 实例ID
     */
    String instanceUuid;
}
