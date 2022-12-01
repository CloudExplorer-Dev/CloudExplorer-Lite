package org.openstack4j.openstack.workflow.builder;

import org.openstack4j.model.workflow.builder.*;
import org.openstack4j.openstack.workflow.domain.MistralActionDefinition;
import org.openstack4j.openstack.workflow.domain.MistralWorkbookDefinition;
import org.openstack4j.openstack.workflow.domain.MistralWorkflowDefinition;
import org.openstack4j.openstack.workflow.domain.MistralWorkflowExecution;

/**
 * The Mistral v2 Builders.
 */
public class MistralBuilders implements WorkflowBuilders {

    @Override
    public WorkflowDefinitionBuilder workflowDefinition() {
        return MistralWorkflowDefinition.builder();
    }

    @Override
    public WorkbookDefinitionBuilder workbookDefinition() {
        return MistralWorkbookDefinition.builder();
    }

    @Override
    public ActionDefinitionBuilder actionDefinition() {
        return MistralActionDefinition.builder();
    }

    @Override
    public WorkflowExecutionBuilder workflowExecution() {
        return MistralWorkflowExecution.builder();
    }
}
