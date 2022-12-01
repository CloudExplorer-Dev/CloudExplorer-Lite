package org.openstack4j.api.gbp;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.NatPool;

import java.util.List;
import java.util.Map;

/**
 * This interface defines all methods for the manipulation of nat-pool
 *
 * @author vinod borole
 */
public interface NatPoolService {
    /**
     * List all nat pool
     *
     * @return List of nat pool
     */
    List<? extends NatPool> list();

    /**
     * Returns list of nat pool filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends NatPool> list(Map<String, String> filteringParams);

    /**
     * Get the specified nat pool by ID
     *
     * @param id the nat pool id
     * @return nat pool or null if not found
     */
    NatPool get(String id);

    /**
     * Delete of the nat pool
     *
     * @param id the nat pool id
     * @return the action response
     */
    ActionResponse delete(String id);

    /**
     * Create a new nat pool
     *
     * @param nat pool
     * @return the newly created nat pool
     */
    NatPool create(NatPool natpool);

    /**
     * Updates an existing nat pool
     *
     * @param nat pool identifier
     * @param nat pool that is be used to updated
     * @return the updated nat pool
     */
    NatPool update(String natpoolId, NatPool natpool);
}
