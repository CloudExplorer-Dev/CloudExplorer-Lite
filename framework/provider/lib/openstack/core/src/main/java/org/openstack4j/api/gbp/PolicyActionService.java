package org.openstack4j.api.gbp;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.PolicyAction;
import org.openstack4j.model.gbp.PolicyActionUpdate;

import java.util.List;
import java.util.Map;

/**
 * This interface defines all methods for the manipulation of policy actions
 *
 * @author vinod borole
 */
public interface PolicyActionService {
    /**
     * List all policy actions
     *
     * @return List of policy actions
     */
    List<? extends PolicyAction> list();

    /**
     * Returns list of policy actions filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends PolicyAction> list(Map<String, String> filteringParams);

    /**
     * Get the specified policy action by ID
     *
     * @param id the policy action id
     * @return policy action or null if not found
     */
    PolicyAction get(String id);

    /**
     * Delete of the policy action
     *
     * @param id the policy action id
     * @return the action response
     */
    ActionResponse delete(String id);

    /**
     * Create a new policy action
     *
     * @param policy action
     * @return the newly created policy action
     */
    PolicyAction create(PolicyAction policyAction);

    /**
     * Updates an existing policy action
     *
     * @param policy action identifier
     * @param policy action that is be used to updated
     * @return the updated policy action
     */
    PolicyAction update(String policyActionId, PolicyActionUpdate policyAction);
}
