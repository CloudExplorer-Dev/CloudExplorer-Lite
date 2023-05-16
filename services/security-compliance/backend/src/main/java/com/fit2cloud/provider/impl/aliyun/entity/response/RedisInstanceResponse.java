package com.fit2cloud.provider.impl.aliyun.entity.response;

import com.aliyun.r_kvstore20150101.models.DescribeDBInstanceNetInfoResponseBody;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/15  15:53}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
@NoArgsConstructor
public class RedisInstanceResponse extends com.aliyun.r_kvstore20150101.models.DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesKVStoreInstance {

    /**
     * redis实例网络相关数据
     */
    private DescribeDBInstanceNetInfoResponseBody network;

    public RedisInstanceResponse(com.aliyun.r_kvstore20150101.models.DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesKVStoreInstance rInstance) {
        BeanUtils.copyProperties(rInstance, this);
    }
}
