package org.openstack4j.api.gbp;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.PolicyRuleSet;

import java.util.List;
import java.util.Map;

public interface PolicyRuleSetService {
    /**
     * List all policy rule set
     *
     * @return List of policy rule set
     */
    List<? extends PolicyRuleSet> list();

    /**
     * Returns list of policy rule set filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends PolicyRuleSet> list(Map<String, String> filteringParams);

    /**
     * Get the specified policy rule set by ID
     *
     * @param id the policy rule set id
     * @return policy rule set or null if not found
     */
    PolicyRuleSet get(String id);

    /**
     * Delete of the policy rule set
     *
     * @param id the policy rule set id
     * @return the rule set response
     */
    ActionResponse delete(String id);

    /**
     * Create a new policy rule set
     *
     * @param policy rule set
     * @return the newly created policy rule set
     */
    PolicyRuleSet create(PolicyRuleSet policyRuleSet);

    /**
     * Updates an existing policy rule set
     *
     * @param policy rule set identifier
     * @param policy rule set that is be used to updated
     * @return the updated policy rule set
     */
    PolicyRuleSet update(String policyRuleSetId, PolicyRuleSet policyRuleSet);
}
