package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.ext.Firewall;

import java.util.List;

/**
 * A Builder to Create Firewall of FwaaS
 *
 * @author Vishvesh Deshmukh
 */
public interface FirewallBuilder extends Builder<FirewallBuilder, Firewall> {

    /**
     * @param tenantId : Owner of the Firewall. Only an administrative user can
     *                 specify a tenant ID other than its own.
     * @return FirewallBuilder
     */
    public FirewallBuilder tenantId(String tenantId);

    /**
     * @param name : Human readable name for the firewall (255 characters limit). Does not have to be unique.
     * @return FirewallBuilder
     */
    public FirewallBuilder name(String name);

    /**
     * @param description : Human readable description for the firewall (1024 characters limit).
     * @return FirewallBuilder
     */
    public FirewallBuilder description(String description);

    /**
     * @param adminstateup :  The administrative state of the firewall,
     *                     which is up (true) or down (false).
     * @return FirewallBuilder
     */
    public FirewallBuilder adminStateUp(Boolean adminStateUp);

    /**
     * shared :  When set to True makes this firewall rule visible to tenants other
     * than its owner, and can be used in firewall policies not owned by its tenant.
     *
     * @return FirewallBuilder
     */
    public FirewallBuilder shared(Boolean shared);

    /**
     * @param policyid : The firewall policy uuid that this firewall is associated with.
     *                 This firewall will implement the rules contained in the firewall policy represented by this uuid.
     * @return FirewallBuilder
     */
    public FirewallBuilder policy(String policyId);

    /**
     * @param routerIds : A list of UUIDs for routers that are associated with the firewall.
     * @return FirewallBuilder
     */
    public FirewallBuilder routerIds(List<String> routerIds);
}
