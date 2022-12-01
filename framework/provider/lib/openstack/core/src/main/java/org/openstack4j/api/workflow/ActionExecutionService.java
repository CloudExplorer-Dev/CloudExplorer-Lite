package org.openstack4j.api.workflow;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.workflow.ActionExecution;

import java.util.List;

/**
 * Service that provides CRUD operations for task executions.
 *
 * @author Renat Akhmerov
 */
public interface ActionExecutionService extends RestService {
    /**
     * List all action executions with details.
     *
     * @return List of action executions.
     */
    List<? extends ActionExecution> list();

    /**
     * Create a new action execution.
     *
     * @param actionExecution Action execution to create.
     * @return Created action execution.
     */
    ActionExecution create(ActionExecution actionExecution);

    /**
     * Get action execution by its ID.
     *
     * @param id Action execution ID.
     * @return Action execution.
     */
    ActionExecution get(String id);

    /**
     * Delete action execution by its ID.
     *
     * @param id Action execution ID.
     * @return Action response from the server.
     */
    ActionResponse delete(String id);
}
