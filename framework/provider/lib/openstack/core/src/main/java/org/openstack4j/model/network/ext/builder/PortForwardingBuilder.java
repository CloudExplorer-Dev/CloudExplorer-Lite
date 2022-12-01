package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.ext.PortForwarding;

/**
 * A builder to create floating IP port forwarding.
 *
 * @author zjluo
 */
public interface PortForwardingBuilder extends Builder<PortForwardingBuilder, PortForwarding> {

    /**
     * @see PortForwarding#getProtocol()
     */
    PortForwardingBuilder protocol(String protocol);

    /**
     * @see PortForwarding#getInternalIpAddress()
     */
    PortForwardingBuilder internalIpAddress(String internalIpAddress);

    /**
     * @see PortForwarding#getInternalPort()
     */
    PortForwardingBuilder internalPort(int internalPort);

    /**
     * @see PortForwarding#getInternalPortId()
     */
    PortForwardingBuilder internalPortId(String internalPortId);

    /**
     * @see PortForwarding#getExternalPort()
     */
    PortForwardingBuilder externalPort(int externalPort);

    /**
     * @see PortForwarding#getDescription()
     */
    PortForwardingBuilder description(String description);
}
