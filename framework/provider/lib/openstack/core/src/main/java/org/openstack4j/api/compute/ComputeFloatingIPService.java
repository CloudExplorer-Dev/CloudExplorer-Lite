package org.openstack4j.api.compute;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.FloatingIP;
import org.openstack4j.model.compute.Server;

import java.util.List;

/**
 * OpenStack Compute Floating-IP Operation API.
 *
 * @author Nathan Anderson
 * @see org.openstack4j.api.networking.NetFloatingIPService
 * @deprecated This API is a proxy call to the Network service. Nova has deprecated all the proxy APIs and users should use the native APIs instead. This API will fail with a 404 starting from microversion 2.36.
 */
@Deprecated
public interface ComputeFloatingIPService extends RestService {

    /**
     * List floating ips associated with current tenant.
     *
     * @return the list<? extends floating i p>
     */
    List<? extends FloatingIP> list();

    /**
     * Lists the current Floating IP Pool Names
     *
     * @return List of floating IP pool names
     */
    List<String> getPoolNames();

    /**
     * Allocate a floating ip address to tenant.
     *
     * @param pool the pool
     * @return the floating ip
     */
    FloatingIP allocateIP(String pool);

    /**
     * Deallocate ip address from tenant.
     *
     * @param id the id of floating ip address
     * @return the action response
     */
    ActionResponse deallocateIP(String id);


    /**
     * Adds floating-ip to server.
     *
     * @param server         the server
     * @param fixedIpAddress the fixed ip address
     * @param ipAddress      the ip address
     * @return the action response
     */
    ActionResponse addFloatingIP(Server server, String fixedIpAddress, String ipAddress);

    /**
     * Adds floating-ip to server.
     *
     * @param server    the server
     * @param ipAddress the ip address
     * @return the action response
     */
    ActionResponse addFloatingIP(Server server, String ipAddress);

    /**
     * Remove floating-ip from server
     *
     * @param server    the server
     * @param ipAddress the ip address
     */
    ActionResponse removeFloatingIP(Server server, String ipAddress);

    /**
     * Adds floating-ip to server.
     *
     * @param serverId       the id of the server
     * @param fixedIpAddress the fixed ip address
     * @param ipAddress      the ip address
     * @return the action response
     */
    ActionResponse addFloatingIP(String serverId, String fixedIpAddress, String ipAddress);

    /**
     * Adds floating-ip to server.
     *
     * @param serverId  the id of the server
     * @param ipAddress the ip address
     * @return the action response
     */
    ActionResponse addFloatingIP(String serverId, String ipAddress);

    /**
     * Remove floating-ip from server
     *
     * @param serverId  the id of the server
     * @param ipAddress the ip address
     */
    ActionResponse removeFloatingIP(String serverId, String ipAddress);

}
