package org.openstack4j.openstack.gbp.internal;

import org.openstack4j.api.gbp.L3policyService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.L3Policy;
import org.openstack4j.openstack.gbp.domain.GbpL3Policy;
import org.openstack4j.openstack.gbp.domain.GbpL3Policy.L3Policies;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * L3 Policy API Implementation
 *
 * @author vinod borole
 */
public class L3policyServiceImpl extends BaseNetworkingServices implements L3policyService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends L3Policy> list() {
        return get(L3Policies.class, uri("/grouppolicy/l3_policies")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends L3Policy> list(Map<String, String> filteringParams) {
        Invocation<L3Policies> l3PoliciesInvocation = buildInvocation(filteringParams);
        return l3PoliciesInvocation.execute().getList();
    }

    private Invocation<L3Policies> buildInvocation(Map<String, String> filteringParams) {
        Invocation<L3Policies> l3PoliciesInvocation = get(L3Policies.class, "/grouppolicy/l3_policies");
        if (filteringParams == null) {
            return l3PoliciesInvocation;
        }
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                l3PoliciesInvocation = l3PoliciesInvocation.param(entry.getKey(), entry.getValue());
            }
        }
        return l3PoliciesInvocation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public L3Policy get(String id) {
        checkNotNull(id);
        return get(GbpL3Policy.class, uri("/grouppolicy/l3_policies/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String id) {
        checkNotNull(id);
        return deleteWithResponse(uri("/grouppolicy/l3_policies/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public L3Policy create(L3Policy l3Policy) {
        return post(GbpL3Policy.class, uri("/grouppolicy/l3_policies")).entity(l3Policy).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public L3Policy update(String l3PolicyId, L3Policy l3Policy) {
        checkNotNull(l3PolicyId);
        checkNotNull(l3Policy);
        return put(GbpL3Policy.class, uri("/grouppolicy/l3_policies/%s", l3PolicyId)).entity(l3Policy).execute();
    }


}
