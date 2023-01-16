package com.fit2cloud.provider.impl.aliyun.entity.request;


import com.aliyun.rds20140815.models.DescribeDBInstanceNetInfoRequest;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliSecurityComplianceCredential;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/11  17:54}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class GetRedisNetInfoInstanceRequest extends DescribeDBInstanceNetInfoRequest {
    /**
     * 认证信息
     */
    private AliSecurityComplianceCredential credential;

}
