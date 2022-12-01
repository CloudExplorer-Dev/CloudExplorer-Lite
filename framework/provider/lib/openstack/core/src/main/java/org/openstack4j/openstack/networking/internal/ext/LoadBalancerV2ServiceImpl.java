package org.openstack4j.openstack.networking.internal.ext;

import org.openstack4j.api.networking.ext.LoadBalancerV2Service;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.LoadBalancerV2;
import org.openstack4j.model.network.ext.LoadBalancerV2Stats;
import org.openstack4j.model.network.ext.LoadBalancerV2StatusTree;
import org.openstack4j.model.network.ext.LoadBalancerV2Update;
import org.openstack4j.openstack.compute.functions.ToActionResponseFunction;
import org.openstack4j.openstack.networking.domain.ext.LoadBalancerV2StatusTree.NeutronLoadBalancerV2StatusTree;
import org.openstack4j.openstack.networking.domain.ext.NeutronLoadBalancerV2;
import org.openstack4j.openstack.networking.domain.ext.NeutronLoadBalancerV2Stats;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Openstack (Neutron) lbaas v2 load balancer operations
 *
 * @author emjburns
 */
public class LoadBalancerV2ServiceImpl extends BaseNetworkingServices implements LoadBalancerV2Service {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends LoadBalancerV2> list() {
        return get(NeutronLoadBalancerV2.LoadBalancersV2.class, uri("/lbaas/loadbalancers")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends LoadBalancerV2> list(Map<String, String> filteringParams) {
        Invocation<NeutronLoadBalancerV2.LoadBalancersV2> req = get(NeutronLoadBalancerV2.LoadBalancersV2.class, uri("/lbaas/loadbalancers"));
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
    public LoadBalancerV2 get(String loadbalancerId) {
        checkNotNull(loadbalancerId);
        return get(NeutronLoadBalancerV2.class, uri("/lbaas/loadbalancers/%s", loadbalancerId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoadBalancerV2 create(LoadBalancerV2 loadbalancer) {
        checkNotNull(loadbalancer);
        return post(NeutronLoadBalancerV2.class, uri("/lbaas/loadbalancers")).entity(loadbalancer).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoadBalancerV2 update(String loadbalancerId, LoadBalancerV2Update loadbalancer) {
        checkNotNull(loadbalancerId);
        checkNotNull(loadbalancer);
        return put(NeutronLoadBalancerV2.class, uri("/lbaas/loadbalancers/%s", loadbalancerId)).entity(loadbalancer).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String loadbalancerId) {
        checkNotNull(loadbalancerId);
        return ToActionResponseFunction.INSTANCE.apply(delete(Void.class, uri("/lbaas/loadbalancers/%s", loadbalancerId)).executeWithResponse());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoadBalancerV2Stats stats(String loadbalancerId) {
        checkNotNull(loadbalancerId);
        return get(NeutronLoadBalancerV2Stats.class, uri("/lbaas/loadbalancers/%s/stats", loadbalancerId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoadBalancerV2StatusTree statusTree(String loadbalancerId) {
        checkNotNull(loadbalancerId);
        return get(NeutronLoadBalancerV2StatusTree.class, uri("/lbaas/loadbalancers/%s/statuses", loadbalancerId)).execute();
    }
}
