package org.openstack4j.model.workflow.builder;

/**
 * The Workflow service builders.
 */
public interface WorkflowBuilders {

    /**
     * The builder to create a workflow definition.
     *
     * @return the workflow definition builder.
     */
    WorkflowDefinitionBuilder workflowDefinition();

    /**
     * The builder to create a workbook definition.
     *
     * @return the workbook definition builder.
     */
    WorkbookDefinitionBuilder workbookDefinition();

    /**
     * The builder to create an action definition.
     *
     * @return the action definition builder.
     */
    ActionDefinitionBuilder actionDefinition();

    /**
     * The builder to create a workflow execution.
     *
     * @return the workflow execution builder.
     */
    WorkflowExecutionBuilder workflowExecution();
}
