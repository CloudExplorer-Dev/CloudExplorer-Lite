package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.rds20140815.models.DescribeDBInstanceNetInfoRequest;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliSecurityComplianceCredential;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/12  10:50}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class GetRdsNetInfoInstanceRequest extends DescribeDBInstanceNetInfoRequest {
    /**
     * 认证信息
     */
    private AliSecurityComplianceCredential credential;
}
