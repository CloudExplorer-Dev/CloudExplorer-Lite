package org.openstack4j.openstack.gbp.internal;

import org.openstack4j.api.gbp.PolicyTargetService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.PolicyTarget;
import org.openstack4j.openstack.gbp.domain.GbpPolicyTarget;
import org.openstack4j.openstack.gbp.domain.GbpPolicyTarget.PolicyTargets;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Policy target API Implementation
 *
 * @author vinod borole
 */
public class PolicyTargetServiceImpl extends BaseNetworkingServices implements PolicyTargetService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends PolicyTarget> list() {
        return get(PolicyTargets.class, uri("/grouppolicy/policy_targets")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends PolicyTarget> list(Map<String, String> filteringParams) {
        Invocation<PolicyTargets> policytargetsInvocation = buildInvocation(filteringParams);
        return policytargetsInvocation.execute().getList();
    }

    private Invocation<PolicyTargets> buildInvocation(Map<String, String> filteringParams) {
        Invocation<PolicyTargets> policytargetsInvocation = get(PolicyTargets.class, "/grouppolicy/policy_targets");
        if (filteringParams == null) {
            return policytargetsInvocation;
        }
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                policytargetsInvocation = policytargetsInvocation.param(entry.getKey(), entry.getValue());
            }
        }
        return policytargetsInvocation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PolicyTarget get(String id) {
        checkNotNull(id);
        return get(GbpPolicyTarget.class, uri("/grouppolicy/policy_targets/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String id) {
        checkNotNull(id);
        return deleteWithResponse(uri("/grouppolicy/policy_targets/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PolicyTarget create(PolicyTarget policyTarget) {
        return post(GbpPolicyTarget.class, uri("/grouppolicy/policy_targets")).entity(policyTarget).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PolicyTarget update(String policyTargetId, PolicyTarget policyTarget) {
        checkNotNull(policyTargetId);
        checkNotNull(policyTarget);
        return put(GbpPolicyTarget.class, uri("/grouppolicy/policy_targets/%s", policyTargetId)).entity(policyTarget).execute();
    }

}
