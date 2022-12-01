package org.openstack4j.openstack.gbp.internal;

import org.openstack4j.api.gbp.NatPoolService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.NatPool;
import org.openstack4j.openstack.gbp.domain.GbpNatPool;
import org.openstack4j.openstack.gbp.domain.GbpNatPool.NatPools;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Nat Pool API Implementation
 *
 * @author vinod borole
 */
public class NatPoolServiceImpl extends BaseNetworkingServices implements NatPoolService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends NatPool> list() {
        return get(NatPools.class, uri("/grouppolicy/nat_pools")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends NatPool> list(Map<String, String> filteringParams) {
        Invocation<NatPools> natpoolInvocation = buildInvocation(filteringParams);
        return natpoolInvocation.execute().getList();
    }

    private Invocation<NatPools> buildInvocation(Map<String, String> filteringParams) {
        Invocation<NatPools> natpoolInvocation = get(NatPools.class, "/grouppolicy/nat_pools");
        if (filteringParams == null) {
            return natpoolInvocation;
        }
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                natpoolInvocation = natpoolInvocation.param(entry.getKey(), entry.getValue());
            }
        }
        return natpoolInvocation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NatPool get(String id) {
        checkNotNull(id);
        return get(GbpNatPool.class, uri("/grouppolicy/nat_pools/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String id) {
        checkNotNull(id);
        return deleteWithResponse(uri("/grouppolicy/nat_pools/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NatPool create(NatPool natpool) {
        return post(GbpNatPool.class, uri("/grouppolicy/nat_pools")).entity(natpool).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NatPool update(String natpoolId, NatPool natpool) {
        checkNotNull(natpoolId);
        checkNotNull(natpool);
        return put(GbpNatPool.class, uri("/grouppolicy/nat_pools/%s", natpoolId)).entity(natpool).execute();
    }


}
