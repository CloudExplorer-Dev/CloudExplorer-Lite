package org.openstack4j.model.network;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.common.TimeEntity;
import org.openstack4j.model.network.builder.PortBuilder;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A Network (Neutron) Port
 *
 * @author Jeremy Unruh
 */
public interface Port extends Resource, TimeEntity, Buildable<PortBuilder> {

    /**
     * @return the current state of the port
     */
    State getState();

    /**
     * @return the administrative state of port. If false, port does not forward packets.
     */
    boolean isAdminStateUp();

    /**
     * @return the id of the network where this port is associated with
     */
    String getNetworkId();

    /**
     * @return the id of the device (e.g. server) using this port.
     */
    String getDeviceId();

    /**
     * @return the entity (e.g.: DHCP Agent) using this port.
     */
    String getDeviceOwner();

    /**
     * @return the set of fixed IPs this port has been assigned
     */
    Set<? extends IP> getFixedIps();

    Set<? extends AllowedAddressPair> getAllowedAddressPairs();

    /**
     * @return the MacAddress of this port
     */
    String getMacAddress();

    /**
     * @return the security group identifiers assigned to this port
     */
    List<String> getSecurityGroups();

    /**
     * @return the parent ID of this port
     */
    String getTrunkPortParentId();

    /**
     * @return the trunk port type of this port
     */
    String getTrunkPortType();

    /**
     * @return the Vlan of this port
     */
    String getTrunkPortVlanId();

    /**
     * @return The port security status. A valid value is enabled (true) or disabled (false).
     */
    Boolean isPortSecurityEnabled();

    String getHostId();

    String getVifType();

    Map<String, Object> getVifDetails();

    String getvNicType();

    Map<String, Object> getProfile();
}
