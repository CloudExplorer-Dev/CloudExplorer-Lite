package org.openstack4j.api.networking;

import org.openstack4j.api.networking.ext.PortForwardingService;
import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.NetFloatingIP;

import java.util.List;
import java.util.Map;

/**
 * Provides Neutron-based FloatingIP services.
 *
 * @author Nathan Anderson
 */
public interface NetFloatingIPService extends RestService {

    /**
     * Returns list of floating IPs.
     *
     * @return List of NetFloatingIPs.
     */
    List<? extends NetFloatingIP> list();

    /**
     * Returns list of floating IPs filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends NetFloatingIP> list(Map<String, String> filteringParams);

    /**
     * Gets a NetFloatingIP by id.
     *
     * @param fipId the floating-ip identifier.
     * @return the NetFloatingIP
     */
    NetFloatingIP get(String fipId);

    /**
     * Deletes NetFloatingIP by id.
     *
     * @param fipId the floating-ip identifier.
     * @return the action response
     */
    ActionResponse delete(String fipId);

    /**
     * Creates a new Floating IP
     *
     * @param floatingIp the floating ip
     * @return the net floating ip
     */
    NetFloatingIP create(NetFloatingIP floatingIp);

    /**
     * Associates a Floating IP to a Port.
     *
     * @param fipId  the floating-ip identifier.
     * @param portId Id of the port to associate to.
     * @return the net floating ip
     */
    NetFloatingIP associateToPort(String fipId, String portId);

    /**
     * Disassociate from port.
     *
     * @param fipId the floating-ip identifier.
     * @return the net floating ip
     */
    NetFloatingIP disassociateFromPort(String fipId);

    /**
     * @return the port forwarding Service API
     */
    PortForwardingService portForwarding();
}
