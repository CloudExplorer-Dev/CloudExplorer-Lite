package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ram20150501.models.ListUsersRequest;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliSecurityComplianceCredential;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/6  15:52}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ListRamInstanceRequest extends ListUsersRequest {
    /**
     * 认证信息
     */
    private AliSecurityComplianceCredential credential;

}
