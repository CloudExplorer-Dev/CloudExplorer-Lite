package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.DescribeInstancesRequest;
import com.aliyun.tea.NameInMap;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:张少虎
 * @Date: 2022/9/19  5:09 PM
 * @Version 1.0
 * @注释:
 */
@NoArgsConstructor
@Data
public class ListVirtualMachineRequest extends DescribeInstancesRequest {
    /**
     * 认证信息
     */
    private String credential;

    /**
     * 如果云管参数区域是region 那么就重写set函数去赋值
     *
     * @param region 区域
     */
    public void setRegion(String region) {
        this.regionId = region;
    }
}
