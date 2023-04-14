package com.fit2cloud.provider.impl.vsphere.entity.request;

import com.fit2cloud.common.constants.Language;
import com.fit2cloud.provider.impl.vsphere.entity.credential.VsphereComplianceCredential;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/6  15:30}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ListResourcePoolRequest {
    /**
     * 数据中心ID
     */
    private String regionId;
    /**
     * 语言
     */
    private Language language;
    /**
     * 认证信息
     */
    private VsphereComplianceCredential credential;
}
