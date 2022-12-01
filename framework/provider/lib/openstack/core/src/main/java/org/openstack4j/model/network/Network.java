package org.openstack4j.model.network;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.common.TimeEntity;
import org.openstack4j.model.network.builder.NetworkBuilder;

import java.util.List;

/**
 * An OpenStack (Neutron) network
 *
 * @author Jeremy Unruh
 */
public interface Network extends Resource, TimeEntity, Buildable<NetworkBuilder> {

    /**
     * @return the status of the network
     */
    State getStatus();

    /**
     * @return list of subnet identifiers associated with the network
     */
    List<String> getSubnets();

    /**
     * @return the physical network provider or null
     */
    String getProviderPhyNet();

    /**
     * @return true if the administrative state is up
     */
    boolean isAdminStateUp();

    /**
     * @return the network type
     */
    NetworkType getNetworkType();

    /**
     * @return true if the router is external
     */
    boolean isRouterExternal();

    /**
     * @return true if the network is shared
     */
    boolean isShared();

    /**
     * @return the provider segmentation identifier
     */
    String getProviderSegID();

    /**
     * @return the list of Subnets
     */
    List<? extends Subnet> getNeutronSubnets();

    /**
     * @return The maximum transmission unit (MTU) value to address fragmentation. Minimum value is 68 for IPv4, and 1280 for IPv6.
     */
    Integer getMTU();

    /**
     * @return the list of the availability zone candidate for the network.
     */
    List<String> getAvailabilityZoneHints();

    /**
     * @return the list of the availability zone for the network.
     */
    List<String> getAvailabilityZones();

    /**
     * @return true if the port security enabled is shared
     */
    Boolean isPortSecurityEnabled();
}
