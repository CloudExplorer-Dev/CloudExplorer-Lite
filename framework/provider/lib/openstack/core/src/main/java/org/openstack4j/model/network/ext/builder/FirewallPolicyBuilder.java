package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.ext.FirewallPolicy;

import java.util.List;

/**
 * A Builder to Create FirewallPolicy of FwaaS.
 *
 * @author Vishvesh Deshmukh
 */
public interface FirewallPolicyBuilder extends Builder<FirewallPolicyBuilder, FirewallPolicy> {

    /**
     * @param name : Human readable name for the FirewallPolicy (255 characters limit). Does not have to be unique.
     * @return FirewallPolicyBuilder
     */
    public FirewallPolicyBuilder name(String name);

    /**
     * @param tenantId : Owner of the FirewallPolicy. Only an administrative user can
     *                 specify a tenant ID other than its own.
     * @return FirewallPolicyBuilder
     */
    public FirewallPolicyBuilder tenantId(String tenantId);

    /**
     * @param description : Human readable description for the FirewallPolicy (1024 characters limit).
     * @return FirewallPolicyBuilder
     */
    public FirewallPolicyBuilder description(String description);

    /**
     * @param shared : When set to True makes this FirewallPolicy visible to tenants other
     *               than its owner, and can be used in FirewallPolicy not owned by its tenant.
     * @return FirewallPolicyBuilder
     */
    public FirewallPolicyBuilder shared(Boolean shared);

    /**
     * @param audited : When set to True by the policy owner indicates that the firewall policy has been audited.
     *                This attribute is meant to aid in the firewall policy audit workflows.
     *                Each time the firewall policy or the associated firewall rules are changed,
     *                this attribute will be set to False and will have to be explicitly set
     *                to True through an update operation.
     * @return FirewallPolicyBuilder
     */
    public FirewallPolicyBuilder audited(Boolean audited);

    /**
     * @param firewallRules(UUID)List : This is an ordered list of firewall rule uuids.
     *                                The firewall applies the rules in the order in which they appear in this list.
     * @return FirewallPolicyBuilder
     */
    public FirewallPolicyBuilder firewallRules(List<String> ruleIdList);
}
