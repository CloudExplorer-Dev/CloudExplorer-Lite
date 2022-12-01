package org.openstack4j.model.network;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.common.TimeEntity;
import org.openstack4j.model.network.builder.SubnetBuilder;

import java.util.List;

/**
 * A Subnet is a network with Pools and network based settings
 *
 * @author Jeremy Unruh
 */
public interface Subnet extends Resource, TimeEntity, Buildable<SubnetBuilder> {

    /**
     * @return true if DHCP is enabled for this subnet, false if not.
     */
    boolean isDHCPEnabled();

    /**
     * @return the id of the network this subnet is associated with
     */
    String getNetworkId();

    /**
     * @return the DNS server names/IP
     */
    List<String> getDnsNames();

    /**
     * @return the sub-ranges of cidr available for dynamic allocation to ports
     */
    List<? extends Pool> getAllocationPools();


    /**
     * @return the set of routes that should be used by devices with IPs from this subnet
     */
    List<? extends HostRoute> getHostRoutes();

    /**
     * @return the ip version used by this subnet
     */
    IPVersionType getIpVersion();


    /**
     * @return the default gateway used by devices in this subnet
     */
    String getGateway();

    /**
     * @return the cidr representing the IP range for this subnet, based on IP version
     */
    String getCidr();

    /**
     * @return The IPv6 address modes specifies mechanisms for assigning IP addresses
     */
    Ipv6AddressMode getIpv6AddressMode();

    /**
     * @return the IPv6 router advertisement specifies whether the networking service should transmit ICMPv6 packets, for a subnet
     */
    Ipv6RaMode getIpv6RaMode();
}
