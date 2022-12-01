package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Neutron Firewall (FwaaS) : Firewall Policy Entity.
 *
 * <p>This is the child class of {@link AbstractNeutronFirewallPolicy} which is used by `rule_insert/rule_remove` calls
 * - which doesn't require JsonRootName <code>firewall_policy</code>.</p>
 *
 * @author Vishvesh Deshmukh
 * @see AbstractNeutronFirewallPolicy
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronFirewallPolicyRule extends AbstractNeutronFirewallPolicy {
    private static final long serialVersionUID = 1L;
}
