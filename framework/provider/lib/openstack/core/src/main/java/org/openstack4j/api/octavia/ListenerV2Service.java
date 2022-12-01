package org.openstack4j.api.octavia;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.octavia.ListenerV2;
import org.openstack4j.model.octavia.ListenerV2Update;

import java.util.List;
import java.util.Map;

/**
 * Octavia V2 listener Extension API
 *
 * @author wei
 */
public interface ListenerV2Service extends RestService {
    /**
     * List all listeners that the current tenant has access to
     *
     * @return list of all listeners
     */
    List<? extends ListenerV2> list();

    /**
     * Returns list of listeners filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     * @return List
     */
    List<? extends ListenerV2> list(Map<String, String> filteringParams);

    /**
     * Get the specified listener by ID
     *
     * @return the listener or null if not found
     */
    ListenerV2 get(String listenerId);

    /**
     * Delete the specified listener by ID
     *
     * @return the action response
     */
    ActionResponse delete(String listenerId);

    /**
     * Create a listener
     *
     * @return ListenerV2
     */
    ListenerV2 create(ListenerV2 listener);

    /**
     * Update a listener
     *
     * @return ListenerV2
     */
    ListenerV2 update(String listenerId, ListenerV2Update listener);
}
