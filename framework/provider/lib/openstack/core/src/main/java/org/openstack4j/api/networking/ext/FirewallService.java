package org.openstack4j.api.networking.ext;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.Firewall;
import org.openstack4j.model.network.ext.FirewallUpdate;

import java.util.List;
import java.util.Map;

/**
 * <p>Networking (Neutron) FwaaS Firewall Extension API.</p>
 *
 * <p>Represents a logical firewall resource that a tenant can instantiate and manage. A firewall is associated with one firewall policy.</p>
 *
 * <p>
 * The FWaaS extension provides OpenStack users with the ability to deploy firewalls to protect their networks. The FWaaS extension enables you to:
 * <ul>
 * 		<li>Apply firewall rules on traffic entering and leaving tenant networks.</li>
 * 		<li>Support for applying tcp, udp, icmp, or protocol agnostic rules.</li>
 * 		<li>Creation and sharing of firewall policies which hold an ordered collection of the firewall rules.</li>
 * 		<li>Audit firewall rules and policies.</li>
 * </ul>
 * </p>
 *
 * @author Vishvesh Deshmukh
 */
public interface FirewallService extends RestService {
    /**
     * List all Firewall(s) that the current tenant has access to.
     *
     * @return list of all Firewall(s)
     */
    List<? extends Firewall> list();

    /**
     * Returns list of Firewall(s) filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     * @return filtered list of Firewall(s)
     */
    List<? extends Firewall> list(Map<String, String> filteringParams);

    /**
     * Get the specified Firewall by ID
     *
     * @param firewallId the Firewall identifier
     * @return the Firewall or null if not found
     */
    Firewall get(String firewallId);

    /**
     * Delete the specified Firewall by ID
     *
     * @param firewallId the Firewall identifier
     * @return the action response
     */
    ActionResponse delete(String firewallId);

    /**
     * Create a Firewall
     *
     * @return Firewall
     */
    Firewall create(Firewall firewall);

    /**
     * Update a Firewall
     *
     * @param firewallId     the Firewall identifier
     * @param firewallUpdate FirewallUpdate
     * @return Firewall
     */
    Firewall update(String firewallId, FirewallUpdate firewallUpdate);
}
