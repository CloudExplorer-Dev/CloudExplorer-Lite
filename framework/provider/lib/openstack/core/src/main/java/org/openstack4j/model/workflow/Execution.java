/*
 *
 */
package org.openstack4j.model.workflow;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.workflow.builder.ExecutionBuilder;

import java.util.Date;
import java.util.List;


/**
 * Base interface for all execution objects.
 *
 * @author Renat Akhmerov
 */
public interface Execution extends ModelEntity, Buildable<ExecutionBuilder> {
    /**
     * @return The id of this execution.
     */
    String getId();

    /**
     * @return The description of this execution.
     */
    String getDescription();

    /**
     * @return The name of the workflow that this execution belongs to.
     */
    String getWorkflowName();

    /**
     * @return Execution state.
     */
    State getState();

    /**
     * @return Execution state info.
     */
    String getStateInfo();

    /**
     * @return The list of tags.
     */
    List<String> getTags();

    /**
     * @return The time that this entity was created at.
     */
    Date getCreatedAt();

    /**
     * @return The time that this entity was last updated at.
     */
    Date getUpdatedAt();
}
