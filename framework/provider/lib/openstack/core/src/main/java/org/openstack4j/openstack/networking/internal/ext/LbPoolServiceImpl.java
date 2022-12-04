package org.openstack4j.openstack.networking.internal.ext;

import org.openstack4j.api.Builders;
import org.openstack4j.api.networking.ext.LbPoolService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.*;
import org.openstack4j.openstack.compute.functions.ToActionResponseFunction;
import org.openstack4j.openstack.networking.domain.ext.NeutronHealthMonitor;
import org.openstack4j.openstack.networking.domain.ext.NeutronLbPool;
import org.openstack4j.openstack.networking.domain.ext.NeutronLbPool.LbPools;
import org.openstack4j.openstack.networking.domain.ext.NeutronLbPoolStats;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * OpenStack (Neutron) Lbaas pool based Operations
 *
 * @author liujunpeng
 */
public class LbPoolServiceImpl extends BaseNetworkingServices implements
        LbPoolService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends LbPool> list() {
        return get(LbPools.class, uri("/lb/pools")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends LbPool> list(Map<String, String> filteringParams) {
        Invocation<LbPools> req = get(LbPools.class, uri("/lb/pools"));
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
    public LbPool get(String lbPoolId) {
        checkNotNull(lbPoolId);
        return get(NeutronLbPool.class, uri("/lb/pools/%s", lbPoolId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String lbPoolId) {
        checkNotNull(lbPoolId);
        return ToActionResponseFunction.INSTANCE.apply(delete(void.class,
                uri("/lb/pools/%s", lbPoolId)).executeWithResponse());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LbPool create(LbPool lbPool) {
        checkNotNull(lbPool);
        return post(NeutronLbPool.class, uri("/lb/pools")).entity(lbPool).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LbPool update(String lbPoolId, LbPoolUpdate lbPool) {
        checkNotNull(lbPoolId);
        checkNotNull(lbPool);
        return put(NeutronLbPool.class, uri("/lb/pools/%s", lbPoolId)).entity(
                lbPool).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LbPoolStats stats(String lbPoolId) {
        checkNotNull(lbPoolId);
        return get(NeutronLbPoolStats.class, uri("/lb/pools/%s/stats.json", lbPoolId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HealthMonitor associateHealthMonitor(String lbPoolId, String healthMonitorId) {
        checkNotNull(lbPoolId);
        checkNotNull(healthMonitorId);
        return associateHealthMonitor(lbPoolId, Builders.lbPoolAssociateHealthMonitor().id(healthMonitorId).build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HealthMonitor associateHealthMonitor(String lbPoolId, HealthMonitorAssociate associate) {
        checkNotNull(lbPoolId);
        checkNotNull(associate);
        return post(NeutronHealthMonitor.class, uri("/lb/pools/%s/health_monitors", lbPoolId)).entity(associate).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse disAssociateHealthMonitor(String lbPoolId, String healthMonitorId) {
        checkNotNull(lbPoolId);
        checkNotNull(healthMonitorId);
        return ToActionResponseFunction.INSTANCE.apply(delete(void.class,
                uri("/lb/pools/%s/health_monitors/%s", lbPoolId, healthMonitorId)).executeWithResponse());
    }
}
