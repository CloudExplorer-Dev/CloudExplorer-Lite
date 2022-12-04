package org.openstack4j.openstack.gbp.internal;

import org.openstack4j.api.gbp.L2policyService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.L2Policy;
import org.openstack4j.openstack.gbp.domain.GbpL2Policy;
import org.openstack4j.openstack.gbp.domain.GbpL2Policy.L2Policies;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * L2 Policy API Implementation
 *
 * @author vinod borole
 */
public class L2policyServiceImpl extends BaseNetworkingServices implements L2policyService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends L2Policy> list() {
        return get(L2Policies.class, uri("/grouppolicy/l2_policies")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends L2Policy> list(Map<String, String> filteringParams) {
        Invocation<L2Policies> l2PoliciesInvocation = buildInvocation(filteringParams);
        return l2PoliciesInvocation.execute().getList();
    }

    private Invocation<L2Policies> buildInvocation(Map<String, String> filteringParams) {
        Invocation<L2Policies> l2PoliciesInvocation = get(L2Policies.class, "/grouppolicy/l2_policies");
        if (filteringParams == null) {
            return l2PoliciesInvocation;
        }
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                l2PoliciesInvocation = l2PoliciesInvocation.param(entry.getKey(), entry.getValue());
            }
        }
        return l2PoliciesInvocation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public L2Policy get(String id) {
        checkNotNull(id);
        return get(GbpL2Policy.class, uri("/grouppolicy/l2_policies/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String id) {
        checkNotNull(id);
        return deleteWithResponse(uri("/grouppolicy/l2_policies/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public L2Policy create(L2Policy l2Policy) {
        return post(GbpL2Policy.class, uri("/grouppolicy/l2_policies")).entity(l2Policy).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public L2Policy update(String l2PolicyId, L2Policy l2Policy) {
        checkNotNull(l2PolicyId);
        checkNotNull(l2Policy);
        return put(GbpL2Policy.class, uri("/grouppolicy/l2_policies/%s", l2PolicyId)).entity(l2Policy).execute();
    }


}
