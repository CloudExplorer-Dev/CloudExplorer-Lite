package org.openstack4j.api.gbp;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.PolicyTarget;

import java.util.List;
import java.util.Map;

/**
 * This interface defines all methods for the manipulation of policy targets
 *
 * @author vinod borole
 */
public interface PolicyTargetService {
    /**
     * List all policy target
     *
     * @return List of policy target
     */
    List<? extends PolicyTarget> list();

    /**
     * Returns list of policy target filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends PolicyTarget> list(Map<String, String> filteringParams);

    /**
     * Get the specified policy target by ID
     *
     * @param id the policy target id
     * @return policy target or null if not found
     */
    PolicyTarget get(String id);

    /**
     * Delete of the policy target
     *
     * @param id the policy target id
     * @return the target response
     */
    ActionResponse delete(String id);

    /**
     * Create a new policy target
     *
     * @param policy target
     * @return the newly created policy target
     */
    PolicyTarget create(PolicyTarget policyTarget);

    /**
     * Updates an existing policy target
     *
     * @param policy target identifier
     * @param policy target that is be used to updated
     * @return the updated policy target
     */
    PolicyTarget update(String policyTargetId, PolicyTarget policyTarget);
}
