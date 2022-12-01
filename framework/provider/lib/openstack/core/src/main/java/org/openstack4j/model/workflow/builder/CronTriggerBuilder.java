package org.openstack4j.model.workflow.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.workflow.CronTrigger;
import org.openstack4j.model.workflow.Scope;

import java.util.Date;
import java.util.Map;

/**
 * Builder for a {@link CronTrigger} model class
 *
 * @author Renat Akhmerov
 */
public interface CronTriggerBuilder<T extends CronTriggerBuilder<T, M>, M extends CronTrigger>
        extends Builder<T, M> {
    /**
     * @see CronTrigger#getId()
     */
    T id(String id);

    /**
     * @see CronTrigger#getCreatedAt()
     */
    T createdAt(Date createdAt);

    /**
     * @see CronTrigger#getUpdatedAt()
     */
    T updatedAt(Date updatedAt);

    /**
     * @see CronTrigger#getScope()
     */
    T scope(Scope scope);

    /**
     * @see CronTrigger#getName()
     */
    T name(String name);

    /**
     * @see CronTrigger#getPattern()
     */
    T pattern(String pattern);

    /**
     * @see CronTrigger#getWorkflowName()
     */
    T workflowName(String wfName);

    /**
     * @see CronTrigger#getWorkflowId()
     */
    T workflowId(String wfId);

    /**
     * @see CronTrigger#getWorkflowInput()
     */
    T workflowInput(Map<String, Object> wfInput);

    /**
     * @see CronTrigger#getWorkflowParameters()
     */
    T workflowParameters(Map<String, Object> wfParams);

    /**
     * @see CronTrigger#getRemainingExecutionsCount()
     */
    T remainingExecutionsCount(Integer cnt);

    /**
     * @see CronTrigger#getFirstExecutionTime()
     */
    T firstExecutionTime(Date firstExecTime);

    /**
     * @see CronTrigger#getNextExecutionTime()
     */
    T nextExecutionTime(Date nextExecTime);
}
