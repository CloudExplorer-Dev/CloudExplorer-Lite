package org.openstack4j.api.gbp;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.L2Policy;

import java.util.List;
import java.util.Map;

/**
 * This interface defines all methods for the manipulation of l2policy
 *
 * @author vinod borole
 */
public interface L2policyService {
    /**
     * List all l2 policies
     *
     * @return List of l2 policies
     */
    List<? extends L2Policy> list();

    /**
     * Returns list of l2 policies filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends L2Policy> list(Map<String, String> filteringParams);

    /**
     * Get the specified l2 policy by ID
     *
     * @param id the l2 policy id
     * @return l2 policy or null if not found
     */
    L2Policy get(String id);

    /**
     * Delete of the l2 policy
     *
     * @param id the l2 policy id
     * @return the action response
     */
    ActionResponse delete(String id);

    /**
     * Create a new l2 policy
     *
     * @param l2 policy
     * @return the newly created l2 policy
     */
    L2Policy create(L2Policy l2Policy);

    /**
     * Updates an existing l2 policy
     *
     * @param l2 policy identifier
     * @param l2 policy that is be used to updated
     * @return the updated l2 policy
     */
    L2Policy update(String l2PolicyId, L2Policy l2Policy);
}
