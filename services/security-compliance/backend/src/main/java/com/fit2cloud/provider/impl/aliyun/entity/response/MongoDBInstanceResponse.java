package com.fit2cloud.provider.impl.aliyun.entity.response;

import com.aliyun.dds20151201.models.DescribeDBInstancesResponseBody;
import com.aliyun.dds20151201.models.DescribeShardingNetworkAddressResponseBody;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/15  15:59}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Setter
@Getter
@NoArgsConstructor
public class MongoDBInstanceResponse extends DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyDBInstancesDBInstance {

    /**
     * 网络信息
     */
    private DescribeShardingNetworkAddressResponseBody networkObj;

    public MongoDBInstanceResponse(DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyDBInstancesDBInstance mongodbInstance) {
        BeanUtils.copyProperties(mongodbInstance, this);
    }

}
