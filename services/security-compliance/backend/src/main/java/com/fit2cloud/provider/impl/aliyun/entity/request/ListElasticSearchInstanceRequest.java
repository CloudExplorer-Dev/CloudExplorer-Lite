package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.elasticsearch20170613.models.ListInstanceRequest;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliSecurityComplianceCredential;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/5  17:38}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ListElasticSearchInstanceRequest extends ListInstanceRequest {
    /**
     * 认证信息
     */
    private AliSecurityComplianceCredential credential;
    /**
     * 区域信息
     */
    private String regionId;
}
