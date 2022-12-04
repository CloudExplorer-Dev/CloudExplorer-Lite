package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * A Neutron Firewall (FwaaS) : Firewall Policy Entity.
 *
 * <p>This is the child class of {@link AbstractNeutronFirewallPolicy} - which requires JsonRootName <code>firewall_policy</code>.</p>
 *
 * @author Vishvesh Deshmukh
 * @see AbstractNeutronFirewallPolicy
 */
@JsonRootName("firewall_policy")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronFirewallPolicy extends AbstractNeutronFirewallPolicy {
    private static final long serialVersionUID = 1L;
}
