package org.openstack4j.model.workflow.builder;

import org.openstack4j.model.workflow.WorkbookDefinition;

/**
 * Builder for a {@link WorkbookDefinition} model class
 *
 * @author Renat Akhmerov
 */
public interface WorkbookDefinitionBuilder<T extends WorkbookDefinitionBuilder<T, M>, M extends WorkbookDefinition>
        extends DefinitionBuilder<T, M> {
    // No-op.
}
