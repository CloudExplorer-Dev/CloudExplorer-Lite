package org.openstack4j.model.workflow.builder;

import org.openstack4j.model.workflow.ActionDefinition;

/**
 * Builder for a {@link ActionDefinition} model class
 *
 * @author Renat Akhmerov
 */
public interface ActionDefinitionBuilder<T extends ActionDefinitionBuilder<T, M>, M extends ActionDefinition>
        extends DefinitionBuilder<T, M> {

    /**
     * @see ActionDefinition#getInput()
     */
    T input(String input);
}
