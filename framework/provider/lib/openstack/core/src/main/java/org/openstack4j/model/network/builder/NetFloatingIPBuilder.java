package org.openstack4j.model.network.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.NetFloatingIP;

/**
 * Builder for a FloatingIP model class.
 *
 * @author Nathan Anderson
 */
public interface NetFloatingIPBuilder extends Builder<NetFloatingIPBuilder, NetFloatingIP> {

    /**
     * sets Id of floating network
     *
     * @param networkId the network id
     * @return the floating ip builder
     */
    NetFloatingIPBuilder floatingNetworkId(String networkId);

    /**
     * Port id.
     *
     * @param portId the port id
     * @return the floating ip builder
     */
    NetFloatingIPBuilder portId(String portId);

    /**
     * Value of the floating IP to use.
     */
    NetFloatingIPBuilder floatingIpAddress(String address);

    /**
     * Fixed IP to connect to.
     */
    NetFloatingIPBuilder fixedIpAddress(String address);

    /**
     * Textual description of the resource.
     *
     * @param description Maximum of 250 characters.
     */
    NetFloatingIPBuilder description(String description);
}
