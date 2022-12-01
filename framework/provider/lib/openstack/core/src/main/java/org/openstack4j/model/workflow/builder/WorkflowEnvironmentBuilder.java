package org.openstack4j.model.workflow.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.workflow.Scope;
import org.openstack4j.model.workflow.WorkflowEnvironment;

import java.util.Date;
import java.util.Map;

/**
 * Builder for a {@link WorkflowEnvironment} model class
 *
 * @author Renat Akhmerov
 */
public interface WorkflowEnvironmentBuilder<T extends WorkflowEnvironmentBuilder<T, M>, M extends WorkflowEnvironment>
        extends Builder<T, M> {
    /**
     * @see WorkflowEnvironment#getId()
     */
    T id(String id);

    /**
     * @see WorkflowEnvironment#getCreatedAt()
     */
    T createdAt(Date createdAt);

    /**
     * @see WorkflowEnvironment#getUpdatedAt()
     */
    T updatedAt(Date updatedAt);

    /**
     * @see WorkflowEnvironment#getProjectId()
     */
    T projectId(String projectId);

    /**
     * @see WorkflowEnvironment#getScope()
     */
    T scope(Scope scope);

    /**
     * @see WorkflowEnvironment#getName()
     */
    T name(String name);

    /**
     * @see WorkflowEnvironment#getDescription()
     */
    T description(String description);

    /**
     * @see WorkflowEnvironment#getVariables()
     */
    T variables(Map<String, Object> variables);
}
