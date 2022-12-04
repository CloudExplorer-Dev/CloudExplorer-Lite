package org.openstack4j.model.network;

import org.openstack4j.model.ModelEntity;

/**
 * An availability zone groups network nodes that run services like DHCP, L3, FW, and others.
 *
 * @author Taemin
 */
public interface AvailabilityZone extends ModelEntity {

    /**
     * @return State
     */
    String getState();

    /**
     * @return Resource
     */
    String getResource();

    /**
     * @return The Zone Name of Neutron
     */
    String getName();


}
