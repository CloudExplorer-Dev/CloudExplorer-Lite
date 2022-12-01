package org.openstack4j.api.networking.ext;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.PortForwarding;

import java.util.List;
import java.util.Map;

/**
 * Provides floating IP port forwarding service.
 *
 * @author zjluo
 */
public interface PortForwardingService extends RestService {
    /**
     * Returns list of floating IP port forwarding.
     *
     * @param floatingIpId the floating ip identifier
     * @return List of port forwarding
     */
    List<? extends PortForwarding> list(String floatingIpId);

    /**
     * Returns list of floating IP port forwarding filtered by parameters.
     *
     * @param floatingIpId    the floating ip identifier
     * @param filteringParams map (name, value) of filtering parameters
     * @return list of floating IP port forwarding
     */
    List<? extends PortForwarding> list(String floatingIpId, Map<String, String> filteringParams);

    /**
     * Gets a floating IP port forwarding by id.
     *
     * @param floatingIpId the floating ip identifier
     * @param id           the port_forwarding identifier
     * @return a floating IP port forwarding
     */
    PortForwarding get(String floatingIpId, String id);

    /**
     * Deletes floating IP port forwarding by id.
     *
     * @param floatingIpId the floating ip identifier
     * @param id           id the port forwarding identifier
     * @return the action response
     */
    ActionResponse delete(String floatingIpId, String id);


    /**
     * Creates a floating IP port forwarding
     *
     * @param floatingIpId   the floating ip identifier
     * @param portForwarding the floating IP port forwarding
     * @return the floating IP port forwarding
     */
    PortForwarding create(String floatingIpId, PortForwarding portForwarding);
}
