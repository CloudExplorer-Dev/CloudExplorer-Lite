package org.openstack4j.api.murano.v1;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.murano.v1.domain.AppCatalogSession;

/**
 * @author Nikolay Mahotkin.
 */
public interface MuranoSessionService extends RestService {
    /**
     * Creates a new configuration session.
     *
     * @param environmentId environment identifier.
     * @return Session object.
     */
    AppCatalogSession configure(String environmentId);

    /**
     * Gets session details.
     *
     * @param environmentId environment identifier.
     * @param sessionId     session identifier.
     * @return Session object.
     */
    AppCatalogSession get(String environmentId, String sessionId);

    /**
     * Sends signal to deploy to OpenStack.
     *
     * @param environmentId environment identifier.
     * @param sessionId     session identifier.
     * @return ActionResponse (status 200 in case of success)
     */
    ActionResponse deploy(String environmentId, String sessionId);

    /**
     * Deletes a configuration session.
     *
     * @param environmentId environment identifier.
     * @param sessionId     session identifier.
     * @return ActionResponse (status 200 in case of success)
     */
    ActionResponse delete(String environmentId, String sessionId);
}
