package org.openstack4j.model.network;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.builder.NetSecurityGroupRuleBuilder;

/**
 * The Interface SecurityGroupRule.
 *
 * @author Nathan Anderson
 */
public interface SecurityGroupRule extends ModelEntity, Buildable<NetSecurityGroupRuleBuilder> {

    /**
     * Gets the direction.
     *
     * @return the direction
     */
    String getDirection();

    /**
     * Gets the ether type.
     *
     * @return the ether type
     */
    String getEtherType();

    /**
     * Gets the id.
     *
     * @return the id
     */
    String getId();

    /**
     * Gets the port range max.
     *
     * @return the port range max
     */
    Integer getPortRangeMax();

    /**
     * Gets the port range min.
     *
     * @return the port range min
     */
    Integer getPortRangeMin();

    /**
     * Gets the protocol.
     *
     * @return the protocol
     */
    String getProtocol();

    /**
     * Gets the remote group id.
     *
     * @return the remote group id
     */
    String getRemoteGroupId();

    /**
     * Gets the remote ip prefix.
     *
     * @return the remote ip prefix
     */
    String getRemoteIpPrefix();

    /**
     * Gets the security group id.
     *
     * @return the security group id
     */
    String getSecurityGroupId();

    /**
     * Gets the tenant id.
     *
     * @return the tenant id
     */
    String getTenantId();

    /**
     * Gets A human-readable description.
     *
     * @return the description
     */
    String getDescription();


}
