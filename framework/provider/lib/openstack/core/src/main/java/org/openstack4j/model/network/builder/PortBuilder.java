package org.openstack4j.model.network.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.identity.v3.Tenant;
import org.openstack4j.model.network.ExtraDhcpOptCreate;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.State;

import java.util.Map;

/**
 * A Builder which creates a Neutron Port
 *
 * @author Jeremy Unruh
 */
public interface PortBuilder extends Builder<PortBuilder, Port> {

    /**
     * @see Port#getName()
     */
    PortBuilder name(String name);

    /**
     * @see Port#getTenantId()
     */
    PortBuilder tenantId(String tenantId);

    /**
     * @see Port#getTenantId()
     */
    PortBuilder tenant(Tenant tenant);

    /**
     * @see Port#getNetworkId()
     */
    PortBuilder networkId(String networkId);

    /**
     * @param parentPortId - ID of of the parent port
     * @return PortBuilder
     * @see Port#getTrunkPortParentId()
     */
    PortBuilder trunkPortParentId(String parentPortId);

    /**
     * @param portType - type of the port
     * @return PortBuilder
     * @see Port#getTrunkPortType()
     */
    PortBuilder trunkPortType(String portType);

    /**
     * @param vlanId - ID of the vlan connected to trunk port
     * @return PortBuilder
     * @see Port#getTrunkPortVlanId()
     */
    PortBuilder trunkPortVlanId(String vlanId);

    /**
     * @see Port#getDeviceId()
     */
    PortBuilder deviceId(String deviceId);

    /**
     * @see Port#getDeviceOwner()
     */
    PortBuilder deviceOwner(String deviceOwner);

    /**
     * @see Port#getMacAddress()
     */
    PortBuilder macAddress(String macAddress);

    /**
     * Adds a fixed IP to the current list of fixed IP Addresses
     *
     * @param address  the IP Address
     * @param subnetId the subnet identifier
     * @return PortBuilder
     * @see Port#getFixedIps()
     */
    PortBuilder fixedIp(String address, String subnetId);

    /**
     * Removes a fixed IP from the current list of fixed IP Addresses
     *
     * @param address  the IP Address
     * @param subnetId the subnet identifier
     * @return PortBuilder
     */
    PortBuilder removeFixedIp(String address, String subnetId);


    /**
     * Adds an allowed address pair to the current list of allowed addresses
     *
     * @param ipAddress  the Subnet Address (i.e. 192.168.1.0/24)
     * @param macAddress the MAC Address
     * @return PortBuilder
     * @see Port#getAllowedAddressPairs()
     */
    PortBuilder allowedAddressPair(String ipAddress, String macAddress);


    /**
     * Removes an allowed address pair from the current list of allowed addresses
     *
     * @param ipAddress  the Subnet Address (i.e. 192.168.1.0/24)
     * @param macAddress the MAC address
     * @return PortBuilder
     * @see Port#getAllowedAddressPairs()
     */
    PortBuilder removeAddressPair(String ipAddress, String macAddress);


    /**
     * @see Port#isAdminStateUp()
     */
    PortBuilder adminState(boolean adminStateUp);

    /**
     * @see Port#getState()
     */
    PortBuilder state(State state);

    PortBuilder extraDhcpOpt(ExtraDhcpOptCreate extraDhcpOptCreate);

    PortBuilder securityGroup(String groupName);

    PortBuilder portSecurityEnabled(Boolean portSecurityEnabled);

    PortBuilder hostId(String hostId);

    PortBuilder vifType(String vifType);

    PortBuilder vifDetails(Map<String, Object> vifDetails);

    PortBuilder vNicType(String vNicType);

    PortBuilder profile(Map<String, Object> profile);


}
