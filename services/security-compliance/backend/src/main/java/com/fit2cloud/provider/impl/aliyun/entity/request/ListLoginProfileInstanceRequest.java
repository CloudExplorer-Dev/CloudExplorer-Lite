package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.fit2cloud.provider.impl.aliyun.entity.credential.AliSecurityComplianceCredential;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/6  16:56}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ListLoginProfileInstanceRequest {
    /**
     * 认证信息
     */
    private AliSecurityComplianceCredential credential;

    /**
     * 用户名称
     */
    private List<String> usernameList;
}
