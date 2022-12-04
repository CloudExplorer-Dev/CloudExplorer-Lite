package org.openstack4j.api.workflow;

import org.openstack4j.common.RestService;

/**
 * Workflow Service (Mistral) API
 * <p>
 * TODO: add 'services' and 'resources'.
 *
 * @author Renat Akhmerov
 */
public interface WorkflowService extends RestService {

    /**
     * Workflow definition Service API.
     *
     * @return The workflow definition service.
     */
    WorkflowDefinitionService workflowDefinitions();

    /**
     * Action definition Service API.
     *
     * @return The action definition service.
     */
    ActionDefinitionService actionDefinitions();

    /**
     * Workbook definition Service API
     *
     * @return The workbook definition service.
     */
    WorkbookDefinitionService workbookDefinitions();

    /**
     * Workflow execution Service API
     *
     * @return The workflow execution service.
     */
    WorkflowExecutionService workflowExecutions();

    /**
     * Task execution Service API
     *
     * @return The task execution service.
     */
    TaskExecutionService taskExecutions();

    /**
     * Action execution Service API
     *
     * @return The workflow execution service.
     */
    ActionExecutionService actionExecutions();

    /**
     * Validation Service API
     *
     * @return The validation service.
     */
    ValidationService validation();

    /**
     * Workflow environment Service API
     *
     * @return The workflow environment service.
     */
    WorkflowEnvironmentService environments();

    /**
     * Cron trigger Service API
     *
     * @return The cron trigger service.
     */
    CronTriggerService cronTriggers();

    /**
     * Event trigger Service API
     *
     * @return The event trigger service.
     */
    EventTriggerService eventTriggers();
}
