package org.openstack4j.model.network.options;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Provides server-side filtering options for listing ports
 *
 * @author Jeremy Unruh
 */
public class PortListOptions {

    private Map<String, String> queryParams = Maps.newHashMap();

    private PortListOptions() {
    }

    public static PortListOptions create() {
        return new PortListOptions();
    }

    /**
     * The ID of the entity that uses this port. For example, a DHCP agent.
     *
     * @param deviceOwner the device owner
     * @return options
     */
    public PortListOptions deviceOwner(String deviceOwner) {
        return add("device_owner", deviceOwner);
    }

    /**
     * The ID of the device that uses this port. For example, a virtual server.
     *
     * @param deviceId the device id
     * @return options
     */
    public PortListOptions deviceId(String deviceId) {
        return add("device_id", deviceId);
    }

    /**
     * The ID of the attached network
     *
     * @param networkId the network id
     * @return options
     */
    public PortListOptions networkId(String networkId) {
        return add("network_id", networkId);
    }

    /**
     * The administrative state of the router, which is up (true) or down (false)
     *
     * @param adminState the admin state
     * @return options
     */
    public PortListOptions adminState(boolean adminState) {
        return add("admin_state", String.valueOf(adminState));
    }

    /**
     * DEPRECATED: This option is not valid anymore since
     * neutron port does not have attribute named "display_name".
     * Use {@link #name(String)} to filter ports by name
     *
     * @param displayName the port display name
     * @return options
     */
    @Deprecated
    public PortListOptions displayName(String displayName) {
        return add("display_name", displayName);
    }

    /**
     * The port name
     *
     * @param name the port name
     * @return options
     */
    public PortListOptions name(String name) {
        return add("name", name);
    }

    /**
     * The ID of the tenant who owns the network
     *
     * @param tenantId the tenant id
     * @return options
     */
    public PortListOptions tenantId(String tenantId) {
        return add("tenant_id", tenantId);
    }

    /**
     * The MAC address of the port
     *
     * @param macAddress the mac address
     * @return options
     */
    public PortListOptions macAddress(String macAddress) {
        return add("mac_address", macAddress);
    }

    private PortListOptions add(String param, String value) {
        if (value != null)
            this.queryParams.put(param, value);
        return this;
    }

    public Map<String, String> getOptions() {
        return queryParams;
    }
}
