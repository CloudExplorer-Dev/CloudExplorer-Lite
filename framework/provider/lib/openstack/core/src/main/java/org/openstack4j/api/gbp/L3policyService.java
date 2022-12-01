package org.openstack4j.api.gbp;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.L3Policy;

import java.util.List;
import java.util.Map;

/**
 * This interface defines all methods for the manipulation of l3policy
 *
 * @author vinod borole
 */
public interface L3policyService {
    /**
     * List all l3 policies
     *
     * @return List of l3 policies
     */
    List<? extends L3Policy> list();

    /**
     * Returns list of l3 policies filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends L3Policy> list(Map<String, String> filteringParams);

    /**
     * Get the specified l3 policy by ID
     *
     * @param id the l3 policy id
     * @return l3 policy or null if not found
     */
    L3Policy get(String id);

    /**
     * Delete of the l3 policy
     *
     * @param id the l3 policy id
     * @return the action response
     */
    ActionResponse delete(String id);

    /**
     * Create a new l3 policy
     *
     * @param l3 policy
     * @return the newly created l3 policy
     */
    L3Policy create(L3Policy l3Policy);

    /**
     * Updates an existing l3 policy
     *
     * @param l3 policy identifier
     * @param l3 policy that is be used to updated
     * @return the updated l3 policy
     */
    L3Policy update(String l3PolicyId, L3Policy l3Policy);
}
