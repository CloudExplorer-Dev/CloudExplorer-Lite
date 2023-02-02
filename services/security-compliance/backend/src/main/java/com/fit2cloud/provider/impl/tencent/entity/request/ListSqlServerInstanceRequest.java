package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.provider.impl.tencent.entity.credential.TencentSecurityComplianceCredential;
import com.tencentcloudapi.sqlserver.v20180328.models.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/5  15:08}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ListSqlServerInstanceRequest extends DescribeDBInstancesRequest {
    /**
     * 认证对象
     */
    private TencentSecurityComplianceCredential credential;
    /**
     * 区域
     */
    private String regionId;
}