package org.openstack4j.openstack.gbp.internal;

import org.openstack4j.api.gbp.PolicyRuleService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.PolicyRule;
import org.openstack4j.openstack.gbp.domain.GbpPolicyRule;
import org.openstack4j.openstack.gbp.domain.GbpPolicyRule.PolicyRules;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Policy Rule API Implementation
 *
 * @author vinod borole
 */
public class PolicyRuleServiceImpl extends BaseNetworkingServices implements PolicyRuleService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends PolicyRule> list() {
        return get(PolicyRules.class, uri("/grouppolicy/policy_rules")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends PolicyRule> list(Map<String, String> filteringParams) {
        Invocation<PolicyRules> policyruleInvocation = buildInvocation(filteringParams);
        return policyruleInvocation.execute().getList();
    }

    private Invocation<PolicyRules> buildInvocation(Map<String, String> filteringParams) {
        Invocation<PolicyRules> policyruleInvocation = get(PolicyRules.class, "/grouppolicy/policy_rules");
        if (filteringParams == null) {
            return policyruleInvocation;
        }
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                policyruleInvocation = policyruleInvocation.param(entry.getKey(), entry.getValue());
            }
        }
        return policyruleInvocation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PolicyRule get(String id) {
        checkNotNull(id);
        return get(GbpPolicyRule.class, uri("/grouppolicy/policy_rules/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String id) {
        checkNotNull(id);
        return deleteWithResponse(uri("/grouppolicy/policy_rules/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PolicyRule create(PolicyRule policyRule) {
        return post(GbpPolicyRule.class, uri("/grouppolicy/policy_rules")).entity(policyRule).execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PolicyRule update(String policyRuleId, PolicyRule policyRule) {
        checkNotNull(policyRuleId);
        checkNotNull(policyRule);
        return put(GbpPolicyRule.class, uri("/grouppolicy/policy_rules/%s", policyRuleId)).entity(policyRule).execute();
    }

}
