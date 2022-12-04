package org.openstack4j.api.gbp;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.PolicyTargetGroup;
import org.openstack4j.model.gbp.PolicyTargetGroupCreate;

import java.util.List;
import java.util.Map;

/**
 * This interface defines all methods for the manipulation of groups
 *
 * @author vinod borole
 */
public interface GroupService {
    /**
     * List all policy target group
     *
     * @return List of policy target group
     */
    List<? extends PolicyTargetGroup> list();

    /**
     * Returns list of policy target group filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends PolicyTargetGroup> list(Map<String, String> filteringParams);

    /**
     * Get the specified policy target group by ID
     *
     * @param id the policy target group id
     * @return policy target group or null if not found
     */
    PolicyTargetGroup get(String id);

    /**
     * Delete of the policy target group
     *
     * @param id the policy target group id
     * @return the action response
     */
    ActionResponse delete(String id);

    /**
     * Create a new policy target group
     *
     * @param policy target group
     * @return the newly created policy target group
     */
    PolicyTargetGroup create(PolicyTargetGroupCreate policyTargetGroup);

    /**
     * Updates an existing policy target group
     *
     * @param policy target group identifier
     * @param policy target group that is be used to updated
     * @return the updated policy target group
     */
    PolicyTargetGroup update(String policyTargetGroupId, PolicyTargetGroupCreate policyTargetGroup);
}
