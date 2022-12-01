package org.openstack4j.api.identity.v3;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.identity.v3.Region;

import java.util.List;

/**
 * Identity V3 Region operations
 */
public interface RegionService extends RestService {

    /**
     * Create a new region
     *
     * @param region the region
     * @return the newly created region
     */
    Region create(Region region);

    /**
     * Create a new region
     *
     * @param regionId       the user-defined region id
     * @param description    the description of the region
     * @param parentRegionId the region id of the parent region
     * @return the newly created region
     */
    Region create(String regionId, String description, String parentRegionId);

    /**
     * Get details for a region specified by id
     *
     * @param regionId the region id
     * @return the region
     */
    Region get(String regionId);

    /**
     * Update a region
     *
     * @param region the region set to update
     * @return the updated region
     */
    Region update(Region region);

    /**
     * Delete a region specified by id
     *
     * @param regionId the id of the region
     * @return the ActionResponse
     */
    ActionResponse delete(String regionId);

    /**
     * List regions
     *
     * @return a list of regions
     */
    List<? extends Region> list();

}
