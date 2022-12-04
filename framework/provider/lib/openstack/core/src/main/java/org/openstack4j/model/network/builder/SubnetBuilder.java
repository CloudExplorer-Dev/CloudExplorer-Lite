package org.openstack4j.model.network.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.identity.v3.Tenant;
import org.openstack4j.model.network.*;

/**
 * A Builder which creates a Subnet
 *
 * @author Jeremy Unruh
 */
public interface SubnetBuilder extends Builder<SubnetBuilder, Subnet> {

    /**
     * @see Subnet#getName()
     */
    SubnetBuilder name(String name);

    /**
     * @see Subnet#getNetworkId()
     */
    SubnetBuilder networkId(String networkId);

    /**
     * @see Subnet#getNetworkId()
     */
    SubnetBuilder network(Network network);

    /**
     * @see Subnet#getIpVersion()
     */
    SubnetBuilder ipVersion(IPVersionType ipVersion);

    /**
     * @see Subnet#getCidr()
     */
    SubnetBuilder cidr(String cidr);

    /**
     * Adds a allocation pool
     *
     * @param start the starting IP
     * @param end   the ending IP
     * @return the builder
     */
    SubnetBuilder addPool(String start, String end);

    /**
     * @see Subnet#getTenantId()
     */
    SubnetBuilder tenantId(String tenantId);

    /**
     * @see Subnet#getTenantId()
     */
    SubnetBuilder tenant(Tenant tenant);

    /**
     * @see Subnet#isDHCPEnabled()
     */
    SubnetBuilder enableDHCP(boolean enable);

    /**
     * @see Subnet#gateway()
     */
    SubnetBuilder gateway(String gateway);

    /**
     * @see Subnet#isNoGateway()
     */
    SubnetBuilder noGateway();

    /**
     * @see Subnet#getDnsNames()
     */
    SubnetBuilder addDNSNameServer(String host);

    /**
     * Adds a host route to this subnet
     *
     * @param destination the destination subnet (ex: 10.0.0.0/16)
     * @param nexthop     the next gateway ip adddress (ex: 192.168.0.1)
     * @returnSubnetBuilder
     */
    SubnetBuilder addHostRoute(String destination, String nexthop);

    /**
     * @see Subnet#getIpv6AddressMode()
     */
    SubnetBuilder ipv6AddressMode(Ipv6AddressMode ipv6AddressMode);

    /**
     * @see Subnet#getIpv6RaMode()
     */
    SubnetBuilder ipv6RaMode(Ipv6RaMode ipv6RaMode);
}
