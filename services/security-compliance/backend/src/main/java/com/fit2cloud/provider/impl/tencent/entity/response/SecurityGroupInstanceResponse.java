package com.fit2cloud.provider.impl.tencent.entity.response;

import com.tencentcloudapi.vpc.v20170312.models.SecurityGroup;
import com.tencentcloudapi.vpc.v20170312.models.SecurityGroupPolicySet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/15  17:04}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
@NoArgsConstructor
public class SecurityGroupInstanceResponse extends SecurityGroup {
    /**
     * 安全组规则
     */
    private SecurityGroupPolicySet rule;

    public SecurityGroupInstanceResponse(SecurityGroup securityGroup) {
        BeanUtils.copyProperties(securityGroup, this);
    }
}
