package org.openstack4j.api.murano.v1;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.murano.v1.domain.Application;

import java.util.List;
import java.util.Map;

/**
 * @author Nikolay Mahotkin.
 */
public interface MuranoApplicationService extends RestService {
    /**
     * List all services
     *
     * @param environmentId the environment identifier
     * @param sessionId     the session identifier
     * @return list of environments or empty list
     */
    List<? extends Application> list(String environmentId, String sessionId);

    List<? extends Application> list(String environmentId);

    /**
     * Gets services by environmentId
     *
     * @param environmentId the environment identifier
     * @param path          the path identifier
     * @param sessionId     the session identifier
     * @return the list of requested services (this is a general case for services().get() )
     */
    List<? extends Application> get(String environmentId, String path, String sessionId);

    List<? extends Application> get(String environmentId, String path);

    /**
     * Creates a new service
     *
     * @param environmentId the environment to create
     * @param sessionId     session identifier
     * @param data          service data structure (object model)
     * @return the created service
     */
    Application create(String environmentId, String sessionId, Map<String, Object> data);

    /**
     * @param jsonString raw String containing the apps configuration.
     * @return the list of created services.
     */
    List<? extends Application> create(String environmentId, String sessionId, String jsonString);

    /**
     * Updates services
     *
     * @param environmentId environment identifier
     * @param sessionId     session identifier
     * @param data          service data structure (object model)
     * @return create service
     */
    Application update(String environmentId, String sessionId, Map<String, Object> data);

    List<? extends Application> update(String environmentId, String sessionId, String jsonString);

    /**
     * Deletes the specified service
     *
     * @param environmentId the environment identifier
     * @param path          the path identifier
     * @param sessionId     the session identifier
     * @return the action response
     */
    ActionResponse delete(String environmentId, String path, String sessionId);
}
