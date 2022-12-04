package org.openstack4j.model.workflow;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.workflow.builder.CronTriggerBuilder;

import java.util.Date;
import java.util.Map;


/**
 * A cron trigger.
 *
 * @author Renat Akhmerov
 */
public interface CronTrigger extends ModelEntity, Buildable<CronTriggerBuilder> {

    /**
     * @return The id of this definition.
     */
    String getId();

    /**
     * @return The time that this trigger was createdAt at.
     */
    Date getCreatedAt();

    /**
     * @return The time that this trigger was last updatedAt at.
     */
    Date getUpdatedAt();

    /**
     * @return The scope of this trigger.
     */
    Scope getScope();

    /**
     * @return The name of this definition.
     */
    String getName();

    /**
     * @return The name of workflow that this trigger needs to run.
     */
    String getWorkflowName();

    /**
     * @return The Id of workflow that this trigger needs to run.
     */
    String getWorkflowId();

    /**
     * @return The input values with which the workflow needs to run.
     */
    Map<String, ?> getWorkflowInput();

    /**
     * @return The workflow type specific parameters with which the workflow needs to run.
     */
    Map<String, ?> getWorkflowParameters();

    /**
     * @return The cron pattern of this trigger.
     */
    String getPattern();

    /**
     * @return The number of remaining executions of this trigger.
     */
    Integer getRemainingExecutionsCount();

    /**
     * @return The first execution time of this trigger.
     */
    Date getFirstExecutionTime();

    /**
     * @return The next execution time of this trigger according the cron pattern.
     */
    Date getNextExecutionTime();
}
