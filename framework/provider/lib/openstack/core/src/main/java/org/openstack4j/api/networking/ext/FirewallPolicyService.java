package org.openstack4j.api.networking.ext;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.FirewallPolicy;
import org.openstack4j.model.network.ext.FirewallPolicyUpdate;
import org.openstack4j.openstack.networking.domain.ext.FirewallRuleStrategy.RuleInsertStrategyType;

import java.util.List;
import java.util.Map;

/**
 * <p>Networking (Neutron) FwaaS FirewallPolicy Policy Extension API</p>
 *
 * <p>Represents an ordered collection of FirewallPolicy rules. A FirewallPolicy policy can be shared across tenants.
 * Thus it can also be made part of an audit workflow wherein the firewall_policy can be audited by the
 * relevant entity that is authorized (and can be different from the tenants which create or use the FirewallPolicy policy).
 * </p>
 *
 * <p>
 * The FWaaS extension provides OpenStack users with the ability to deploy firewalls to protect their networks. The FWaaS extension enables you to:
 * <ul>
 * 		<li>Apply FirewallPolicy rules on traffic entering and leaving tenant networks.</li>
 * 		<li>Support for applying tcp, udp, icmp, or protocol agnostic rules.</li>
 * 		<li>Creation and sharing of FirewallPolicy policies which hold an ordered collection of the FirewallPolicy rules.</li>
 * 		<li>Audit FirewallPolicy rules and policies.</li>
 * </ul>
 * </p>
 *
 * @author Vishvesh Deshmukh
 */
public interface FirewallPolicyService extends RestService {
    /**
     * List all FirewallPolicy(s) that the current tenant has access to.
     *
     * @return list of all FirewallPolicy(s)
     */
    List<? extends FirewallPolicy> list();

    /**
     * Returns list of FirewallPolicy(s) filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     * @return filtered list of FirewallPolicy(s)
     */
    List<? extends FirewallPolicy> list(Map<String, String> filteringParams);

    /**
     * Get the specified FirewallPolicy by ID
     *
     * @param firewallPolicyId the FirewallPolicy identifier
     * @return the FirewallPolicy or null if not found
     */
    FirewallPolicy get(String firewallPolicyId);

    /**
     * Delete the specified FirewallPolicy by ID
     *
     * @param firewallPolicyId the FirewallPolicy identifier
     * @return the action response
     */
    ActionResponse delete(String firewallPolicyId);

    /**
     * Create a FirewallPolicy
     *
     * @return FirewallPolicy
     */
    FirewallPolicy create(FirewallPolicy firewallPolicy);

    /**
     * Update a FirewallPolicy
     *
     * @param firewallPolicyId     the FirewallPolicy identifier
     * @param firewallPolicyUpdate FirewallUpdate
     * @return FirewallPolicy
     */
    FirewallPolicy update(String firewallPolicyId, FirewallPolicyUpdate firewallPolicyUpdate);

    /**
     * Inserts a firewall rule in a firewall policy relative to the position of other rules.
     *
     * @param firewallPolicyId          rule inserted in FirewallPolicy
     * @param firewallRuleId            rule to be inserted
     * @param type                      {@link RuleInsertStrategyType}
     * @param insertAfterOrBeforeRuleId rule id where the new firewallRule will be inserted/switched from
     * @return FirewallPolicy
     */
    FirewallPolicy insertFirewallRuleInPolicy(String firewallPolicyId, String firewallRuleId,
                                              RuleInsertStrategyType type, String insertAfterOrBeforeRuleId);

    /**
     * Removes a firewall rule from a firewall policy.
     *
     * @param firewallRuleId rule to be deleted.
     * @return FirewallPolicy
     */
    FirewallPolicy removeFirewallRuleFromPolicy(String firewallPolicyId, String firewallRuleId);
}
