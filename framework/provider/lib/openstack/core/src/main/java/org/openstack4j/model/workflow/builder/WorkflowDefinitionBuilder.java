package org.openstack4j.model.workflow.builder;

import org.openstack4j.model.workflow.WorkflowDefinition;

/**
 * Builder for {@link WorkflowDefinition} model class.
 *
 * @author Renat Akhmerov
 */
public interface WorkflowDefinitionBuilder<T extends WorkflowDefinitionBuilder<T, M>, M extends WorkflowDefinition>
        extends DefinitionBuilder<T, M> {

    /**
     * @see WorkflowDefinition#getInput()
     */
    T input(String input);
}
