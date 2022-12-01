package org.openstack4j.openstack.networking.internal;

import org.openstack4j.api.networking.SecurityGroupRuleService;
import org.openstack4j.model.network.SecurityGroupRule;
import org.openstack4j.openstack.networking.domain.NeutronSecurityGroupRule;
import org.openstack4j.openstack.networking.domain.NeutronSecurityGroupRule.SecurityGroupRules;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * FloatingIPService implementation that provides Neutron Floating-IP based Service Operations.
 *
 * @author Nathan Anderson
 */
public class SecurityGroupRuleServiceImpl extends BaseNetworkingServices implements SecurityGroupRuleService {

    /**
     * {@inheritDoc}
     */
    @Override
    public SecurityGroupRule get(String id) {
        checkNotNull(id);
        return get(NeutronSecurityGroupRule.class, uri("/security-group-rules/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String ruleId) {
        checkNotNull(ruleId);
        delete(Void.class, uri("/security-group-rules/%s", ruleId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SecurityGroupRule create(SecurityGroupRule rule) {
        checkNotNull(rule);
        return post(NeutronSecurityGroupRule.class, uri("/security-group-rules")).entity(rule).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends SecurityGroupRule> list(Map<String, String> filteringParams) {
        Invocation<SecurityGroupRules> securityGroupRulesInvocation = get(SecurityGroupRules.class, "/security-group-rules");
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                securityGroupRulesInvocation = securityGroupRulesInvocation.param(entry.getKey(), entry.getValue());
            }
        }
        return securityGroupRulesInvocation.execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends SecurityGroupRule> list() {
        return get(SecurityGroupRules.class, uri("/security-group-rules")).execute().getList();
    }
}
