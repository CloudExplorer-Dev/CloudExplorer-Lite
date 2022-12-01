package org.openstack4j.model.compute.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.compute.IPProtocol;
import org.openstack4j.model.compute.SecGroupExtension.Rule;

/**
 * Creates a Security Group Extension {@link Rule} entity
 *
 * @author Jeremy Unruh
 */
@Deprecated
public interface SecurityGroupRuleBuilder extends Builder<SecurityGroupRuleBuilder, Rule> {

    /**
     * IP protocol, one of TCP, UDP or ICMP
     *
     * @param protocol the protocol
     * @return the security group rule builder
     */
    SecurityGroupRuleBuilder protocol(IPProtocol protocol);

    /**
     * Port range which consists of a starting and destination port
     *
     * @param fromPort the source port
     * @param toPort   the destination port
     * @return the security group rule builder
     */
    SecurityGroupRuleBuilder range(int fromPort, int toPort);

    /**
     * Destination IP address(es) in CIDR notation
     *
     * @param cidr the CIDR notation
     * @return the security group rule builder
     */
    SecurityGroupRuleBuilder cidr(String cidr);

    /**
     * Security group id
     *
     * @param groupId the group id
     * @return the security group rule builder
     */
    SecurityGroupRuleBuilder groupId(String groupId);

    /**
     * Parent security group id
     *
     * @param parentGroupId the parent group id
     * @return the security group rule builder
     */
    SecurityGroupRuleBuilder parentGroupId(String parentGroupId);

}
