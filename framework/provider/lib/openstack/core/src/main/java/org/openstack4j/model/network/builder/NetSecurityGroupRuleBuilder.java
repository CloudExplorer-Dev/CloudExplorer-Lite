package org.openstack4j.model.network.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.SecurityGroupRule;

/**
 * Builder for a SecurityGroup model class
 *
 * @author Nathan Anderson
 */
public interface NetSecurityGroupRuleBuilder extends Builder<NetSecurityGroupRuleBuilder, SecurityGroupRule> {

    /**
     * @see SecurityGroupRule#getId()
     */
    NetSecurityGroupRuleBuilder id(String id);

    /**
     * @see SecurityGroupRule#getTenantId()
     */
    NetSecurityGroupRuleBuilder tenantId(String tenantId);

    /**
     * @see SecurityGroupRule#getSecurityGroupId()
     */
    NetSecurityGroupRuleBuilder securityGroupId(String groupId);

    /**
     * @see SecurityGroupRule#getDirection()
     */
    NetSecurityGroupRuleBuilder direction(String direction);

    /**
     * @see SecurityGroupRule#getEtherType()
     */
    NetSecurityGroupRuleBuilder ethertype(String ethertype);

    /**
     * @see SecurityGroupRule#getPortRangeMax()
     */
    NetSecurityGroupRuleBuilder portRangeMax(int max);

    /**
     * @see SecurityGroupRule#getPortRangeMin()
     */
    NetSecurityGroupRuleBuilder portRangeMin(int min);

    /**
     * @see SecurityGroupRule#getProtocol()
     */
    NetSecurityGroupRuleBuilder protocol(String protocol);

    /**
     * @see SecurityGroupRule#getRemoteGroupId()
     */
    NetSecurityGroupRuleBuilder remoteGroupId(String remoteGroupId);

    /**
     * @see SecurityGroupRule#getRemoteIpPrefix()
     */
    NetSecurityGroupRuleBuilder remoteIpPrefix(String prefix);

    /**
     * @see SecurityGroupRule#getDescription()
     */
    NetSecurityGroupRuleBuilder description(String description);

}
