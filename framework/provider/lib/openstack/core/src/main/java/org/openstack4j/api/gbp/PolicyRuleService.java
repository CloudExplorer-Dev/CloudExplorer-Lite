package org.openstack4j.api.gbp;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.PolicyRule;

import java.util.List;
import java.util.Map;

/**
 * This interface defines all methods for the manipulation of policy rule
 *
 * @author vinod borole
 */
public interface PolicyRuleService {
    /**
     * List all policy rules
     *
     * @return List of policy rules
     */
    List<? extends PolicyRule> list();

    /**
     * Returns list of policy rules filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends PolicyRule> list(Map<String, String> filteringParams);

    /**
     * Get the specified policy rule by ID
     *
     * @param id the policy rule id
     * @return policy rule or null if not found
     */
    PolicyRule get(String id);

    /**
     * Delete of the policy rule
     *
     * @param id the policy rule id
     * @return the rule response
     */
    ActionResponse delete(String id);

    /**
     * Create a new policy rule
     *
     * @param policy rule
     * @return the newly created policy rule
     */
    PolicyRule create(PolicyRule policyRule);

    /**
     * Updates an existing policy rule
     *
     * @param policy rule identifier
     * @param policy rule that is be used to updated
     * @return the updated policy rule
     */
    PolicyRule update(String policyRuleId, PolicyRule policyRule);
}
