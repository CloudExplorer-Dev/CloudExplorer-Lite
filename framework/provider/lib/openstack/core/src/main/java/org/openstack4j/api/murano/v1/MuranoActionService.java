package org.openstack4j.api.murano.v1;

import org.openstack4j.common.RestService;
import org.openstack4j.model.murano.v1.domain.ActionInfo;
import org.openstack4j.model.murano.v1.domain.ActionResult;

import java.util.List;
import java.util.Map;

/**
 * @author Nikolay Mahotkin.
 */
public interface MuranoActionService extends RestService {
    /**
     * List all actions in current environment
     *
     * @param envId     the environment identifier
     * @param serviceId the service identifier
     * @return list of actions or null.
     */
    List<? extends ActionInfo> list(String envId, String serviceId);

    List<? extends ActionInfo> list(String envId);

    /**
     * List action in current environment by its ID.
     *
     * @param envId    the environment identifier
     * @param actionId the action identifier
     * @return Action instance or null
     */
    ActionInfo get(String envId, String actionId);

    /**
     * Tries to find action with specific name.
     * Returns the first occurrence of given action name.
     *
     * @param envId      the environment identifier
     * @param actionName the name of action
     * @return Action instance
     */
    ActionInfo find(String envId, String actionName);

    /**
     * Tries to find all actions with given name.
     *
     * @param envId      the environment identifier
     * @param actionName the name of action
     * @return Action list or empty list in case of not found
     */
    List<? extends ActionInfo> findAll(String envId, String actionName);

    /**
     * Send signal to run the action and return the taskId
     *
     * @param envId    environment identifier
     * @param actionId action identifier
     * @return String with taskId
     */
    String cast(String envId, String actionId);

    String cast(String envId, String actionId, String jsonString);

    String cast(String envId, String actionId, Map<String, Object> arguments);

    /**
     * Gets the result of running action.
     *
     * @param envId  environment identifier
     * @param taskId task id
     * @return Action result instance
     */
    ActionResult getResult(String envId, String taskId);

    /**
     * It is a simple wrapper for a pair:
     * cast() -> getResult()
     * Starts an action and wait for the result.
     *
     * @param envId    environment identifier
     * @param actionId action identifier
     * @return Action result instance
     */
    ActionResult run(String envId, String actionId);

    ActionResult run(String envId, String actionId, String jsonString);

    ActionResult run(String envId, String actionId, Map<String, Object> arguments);
}
