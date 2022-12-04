package org.openstack4j.api.gbp;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.ExternalSegment;

import java.util.List;
import java.util.Map;

/**
 * This interface defines all methods for the manipulation of external segments
 *
 * @author vinod borole
 */
public interface ExternalSegmentService {
    /**
     * List all external segment
     *
     * @return List of external segment
     */
    List<? extends ExternalSegment> list();

    /**
     * Returns list of external segments filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends ExternalSegment> list(Map<String, String> filteringParams);

    /**
     * Get the specified external segment by ID
     *
     * @param id the external segment id
     * @return external segment or null if not found
     */
    ExternalSegment get(String id);

    /**
     * Delete of the external segment
     *
     * @param id the external segment id
     * @return the action response
     */
    ActionResponse delete(String id);

    /**
     * Create a new external segment
     *
     * @param external segment
     * @return the newly created external segment
     */
    ExternalSegment create(ExternalSegment externalSegment);

    /**
     * Updates an existing external segment
     *
     * @param external segment identifier
     * @param external segment that is be used to updated
     * @return the updated external segment
     */
    ExternalSegment update(String externalSegmentId, ExternalSegment externalSegment);
}
