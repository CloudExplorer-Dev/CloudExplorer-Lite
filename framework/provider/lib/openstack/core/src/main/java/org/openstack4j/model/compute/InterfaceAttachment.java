package org.openstack4j.model.compute;

import org.openstack4j.model.ModelEntity;

import java.util.List;

/**
 * Interface attachment model entity
 *
 * @author Jeremy Unruh
 */
public interface InterfaceAttachment extends ModelEntity {

    /**
     * List of Fixed IpAddresses
     *
     * @return list of fix addresses
     */
    List<? extends FixedIp> getFixedIps();

    /**
     * The interface MacAddress
     *
     * @return the MacAddress for this Interface
     */
    String getMacAddr();

    /**
     * The network identifier
     *
     * @return the network identifier associated to this interface
     */
    String getNetId();

    /**
     * The port aka interface identifier
     *
     * @return the port/interface identifier
     */
    String getPortId();

    /**
     * The current port state
     *
     * @return the port state
     */
    PortState getPortState();

    public interface FixedIp extends ModelEntity {

        /**
         * @return the IpAddress
         */
        String getIpAddress();

        /**
         * @return the subnet unique identifier
         */
        String getSubnetId();
    }

}
