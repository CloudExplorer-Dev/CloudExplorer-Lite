package org.openstack4j.openstack.networking.internal.ext;

import org.openstack4j.api.networking.ext.FirewallService;
import org.openstack4j.core.transport.ExecutionOptions;
import org.openstack4j.core.transport.propagation.PropagateOnStatus;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.Firewall;
import org.openstack4j.model.network.ext.FirewallUpdate;
import org.openstack4j.openstack.compute.functions.ToActionResponseFunction;
import org.openstack4j.openstack.networking.domain.ext.NeutronFirewall;
import org.openstack4j.openstack.networking.domain.ext.NeutronFirewall.Firewalls;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Networking (Neutron) FwaaS Firewall Extension API
 *
 * @author Vishvesh Deshmukh
 */
public class FirewallServiceImpl extends BaseNetworkingServices implements FirewallService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Firewall> list() {
        return get(Firewalls.class, uri("/fw/firewalls")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Firewall> list(Map<String, String> filteringParams) {
        Invocation<Firewalls> req = get(Firewalls.class, uri("/fw/firewalls"));
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
    public Firewall get(String firewallId) {
        checkNotNull(firewallId);
        return get(NeutronFirewall.class, uri("/fw/firewalls/%s", firewallId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String firewallId) {
        checkNotNull(firewallId);
        return ToActionResponseFunction.INSTANCE.apply(delete(Void.class,
                uri("/fw/firewalls/%s", firewallId)).executeWithResponse());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Firewall create(Firewall firewall) {
        return post(NeutronFirewall.class, uri("/fw/firewalls")).entity(firewall)
                .execute(ExecutionOptions.<NeutronFirewall>create(PropagateOnStatus.on(404)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Firewall update(String firewallId, FirewallUpdate firewallUpdate) {
        checkNotNull(firewallId);
        checkNotNull(firewallUpdate);
        return put(NeutronFirewall.class, uri("/fw/firewalls/%s", firewallId)).entity(firewallUpdate).execute();
    }

}
