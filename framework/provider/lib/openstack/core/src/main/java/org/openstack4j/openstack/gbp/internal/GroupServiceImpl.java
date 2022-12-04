package org.openstack4j.openstack.gbp.internal;

import org.openstack4j.api.gbp.GroupService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.PolicyTargetGroup;
import org.openstack4j.model.gbp.PolicyTargetGroupCreate;
import org.openstack4j.openstack.gbp.domain.GbpPolicyTargetGroup;
import org.openstack4j.openstack.gbp.domain.GbpPolicyTargetGroup.PolicyTargetGroups;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Policy target group API Implementation
 *
 * @author vinod borole
 */
public class GroupServiceImpl extends BaseNetworkingServices implements GroupService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends PolicyTargetGroup> list() {
        return get(PolicyTargetGroups.class, uri("/grouppolicy/policy_target_groups")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends PolicyTargetGroup> list(Map<String, String> filteringParams) {
        Invocation<PolicyTargetGroups> policyTargetGroupInvocation = buildInvocation(filteringParams);
        return policyTargetGroupInvocation.execute().getList();
    }

    private Invocation<PolicyTargetGroups> buildInvocation(Map<String, String> filteringParams) {
        Invocation<PolicyTargetGroups> policyTargetGroupInvocation = get(PolicyTargetGroups.class, "/grouppolicy/policy_target_groups");
        if (filteringParams == null) {
            return policyTargetGroupInvocation;
        }
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                policyTargetGroupInvocation = policyTargetGroupInvocation.param(entry.getKey(), entry.getValue());
            }
        }
        return policyTargetGroupInvocation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PolicyTargetGroup get(String id) {
        checkNotNull(id);
        return get(GbpPolicyTargetGroup.class, uri("/grouppolicy/policy_target_groups/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String id) {
        checkNotNull(id);
        return deleteWithResponse(uri("/grouppolicy/policy_target_groups/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PolicyTargetGroup create(PolicyTargetGroupCreate policyTargetGroup) {
        return post(GbpPolicyTargetGroup.class, uri("/grouppolicy/policy_target_groups")).entity(policyTargetGroup).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PolicyTargetGroup update(String policyTargetGroupId, PolicyTargetGroupCreate policyTargetGroup) {
        checkNotNull(policyTargetGroupId);
        checkNotNull(policyTargetGroup);
        return put(GbpPolicyTargetGroup.class, uri("/grouppolicy/policy_target_groups/%s", policyTargetGroupId)).entity(policyTargetGroup).execute();
    }


}
