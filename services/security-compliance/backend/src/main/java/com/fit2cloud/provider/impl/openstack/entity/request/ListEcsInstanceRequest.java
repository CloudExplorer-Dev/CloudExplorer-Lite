package com.fit2cloud.provider.impl.openstack.entity.request;

import com.fit2cloud.common.constants.Language;
import com.fit2cloud.provider.impl.openstack.entity.credential.OpenstackComplianceCredential;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/6  17:28}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ListEcsInstanceRequest {
    /**
     * 认证对象
     */
    private OpenstackComplianceCredential credential;
    /**
     * 区域
     */
    private String regionId;
    /**
     * 语言
     */
    private Language language;
}
