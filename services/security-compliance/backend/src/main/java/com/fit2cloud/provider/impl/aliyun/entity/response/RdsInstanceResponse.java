package com.fit2cloud.provider.impl.aliyun.entity.response;

import com.aliyun.ecs20140526.models.DescribeInstancesResponseBody;
import com.aliyun.rds20140815.models.DescribeDBInstanceNetInfoResponseBody;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/15  16:02}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
@NoArgsConstructor
public class RdsInstanceResponse extends com.aliyun.rds20140815.models.DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance {

    /**
     * 网络信息
     */
    private DescribeDBInstanceNetInfoResponseBody networkObj;

    /**
     * 标签数据
     */
    private List<DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstanceTagsTag> tags;

    public RdsInstanceResponse(com.aliyun.rds20140815.models.DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance rdsInstance) {
        BeanUtils.copyProperties(rdsInstance, this);

    }
}
