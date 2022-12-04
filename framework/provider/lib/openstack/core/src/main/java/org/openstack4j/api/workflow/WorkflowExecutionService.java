package org.openstack4j.api.workflow;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.workflow.WorkflowExecution;

import java.util.List;

/**
 * Service that provides CRUD operations for workflow executions.
 *
 * @author Renat Akhmerov
 */
public interface WorkflowExecutionService extends RestService {
    /**
     * List all workflow executions with details.
     *
     * @return List of workflow executions.
     */
    List<? extends WorkflowExecution> list();

    /**
     * Create a new workflow execution.
     *
     * @param wfExec Workflow execution to create.
     * @return Created workflow execution.
     */
    WorkflowExecution create(WorkflowExecution wfExec);

    /**
     * Get workflow execution by its ID.
     *
     * @param id Workflow execution ID.
     * @return Workflow execution.
     */
    WorkflowExecution get(String id);

    /**
     * Delete workflow execution by its ID.
     *
     * @param id Workflow execution ID.
     * @return Action response from the server.
     */
    ActionResponse delete(String id);
}
