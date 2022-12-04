package org.openstack4j.openstack.networking.internal.ext;

import org.openstack4j.api.networking.ext.FirewallRuleService;
import org.openstack4j.core.transport.ExecutionOptions;
import org.openstack4j.core.transport.propagation.PropagateOnStatus;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.FirewallRule;
import org.openstack4j.model.network.ext.FirewallRuleUpdate;
import org.openstack4j.openstack.compute.functions.ToActionResponseFunction;
import org.openstack4j.openstack.networking.domain.ext.NeutronFirewallRule;
import org.openstack4j.openstack.networking.domain.ext.NeutronFirewallRule.FirewallRules;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Networking (Neutron) FwaaS FirewallRule Rule Extension API
 *
 * @author Vishvesh Deshmukh
 */
public class FirewallRuleServiceImpl extends BaseNetworkingServices implements FirewallRuleService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends FirewallRule> list() {
        return get(FirewallRules.class, uri("/fw/firewall_rules")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends FirewallRule> list(Map<String, String> filteringParams) {
        Invocation<FirewallRules> req = get(FirewallRules.class, uri("/fw/firewall_rules"));
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                req = req.param(entry.getKey(), entry.getValue());
            }
        }
        return req.execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FirewallRule get(String firewallRuleId) {
        checkNotNull(firewallRuleId);
        return get(NeutronFirewallRule.class, uri("/fw/firewall_rules/%s", firewallRuleId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String firewallRuleId) {
        checkNotNull(firewallRuleId);
        return ToActionResponseFunction.INSTANCE.apply(delete(Void.class,
                uri("/fw/firewall_rules/%s", firewallRuleId)).executeWithResponse());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FirewallRule create(FirewallRule firewall) {
        return post(NeutronFirewallRule.class, uri("/fw/firewall_rules")).entity(firewall)
                .execute(ExecutionOptions.<NeutronFirewallRule>create(PropagateOnStatus.on(404)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FirewallRule update(String firewallRuleId, FirewallRuleUpdate firewallRuleUpdate) {
        checkNotNull(firewallRuleId);
        checkNotNull(firewallRuleUpdate);
        return put(NeutronFirewallRule.class, uri("/fw/firewall_rules/%s", firewallRuleId)).entity(firewallRuleUpdate).execute();
    }

}
