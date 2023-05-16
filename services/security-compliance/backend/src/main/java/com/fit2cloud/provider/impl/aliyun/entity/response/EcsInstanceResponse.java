package com.fit2cloud.provider.impl.aliyun.entity.response;

import com.aliyun.ecs20140526.models.DescribeDisksResponseBody;
import com.aliyun.ecs20140526.models.DescribeInstanceAutoRenewAttributeResponseBody;
import com.aliyun.ecs20140526.models.DescribeInstancesResponseBody;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/15  15:22}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class EcsInstanceResponse extends DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance {
    /**
     * 规则组信息
     */
    private List<SecurityGroupsSecurityGroupInstanceResponse> securityGroupRules;
    /**
     * 磁盘信息
     */
    private List<DescribeDisksResponseBody.DescribeDisksResponseBodyDisksDisk> disks;
    /**
     * 自动续费信息
     */
    private DescribeInstanceAutoRenewAttributeResponseBody.DescribeInstanceAutoRenewAttributeResponseBodyInstanceRenewAttributesInstanceRenewAttribute autoRenew;
}
