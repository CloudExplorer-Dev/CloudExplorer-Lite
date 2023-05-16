package com.fit2cloud.provider.impl.aliyun.entity.response;

import com.aliyun.ecs20140526.models.DescribeSecurityGroupAttributeResponseBody;
import com.aliyun.ecs20140526.models.DescribeSecurityGroupsResponseBody;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/15  15:25}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
@NoArgsConstructor
public class SecurityGroupsSecurityGroupInstanceResponse extends DescribeSecurityGroupsResponseBody.DescribeSecurityGroupsResponseBodySecurityGroupsSecurityGroup {
    /**
     * 规则
     */
    private DescribeSecurityGroupAttributeResponseBody rule;

    public SecurityGroupsSecurityGroupInstanceResponse(DescribeSecurityGroupsResponseBody.DescribeSecurityGroupsResponseBodySecurityGroupsSecurityGroup securityGroup) {
        BeanUtils.copyProperties(securityGroup, this);
    }
}
