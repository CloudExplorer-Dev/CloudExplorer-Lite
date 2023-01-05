package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.rds20140815.models.*;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliSecurityComplianceCredential;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/5  14:09}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ListRdsInstanceRequest extends DescribeDBInstancesRequest {
    /**
     * 认证信息
     */
    private AliSecurityComplianceCredential credential;

    /**
     * 如果云管参数区域是region 那么就重写set函数去赋值
     *
     * @param region 区域
     */
    public void setRegion(String region) {
        this.regionId = region;
    }
}
