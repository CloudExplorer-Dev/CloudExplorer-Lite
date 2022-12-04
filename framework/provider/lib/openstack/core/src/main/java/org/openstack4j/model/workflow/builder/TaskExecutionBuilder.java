package org.openstack4j.model.workflow.builder;

import org.openstack4j.model.workflow.TaskExecution;

import java.util.Map;

/**
 * Builder for a {@link TaskExecution} model class
 *
 * @author Renat Akhmerov
 */
public interface TaskExecutionBuilder<T extends TaskExecutionBuilder<T, M>, M extends TaskExecution>
        extends ExecutionBuilder<T, M> {

    /**
     * @see TaskExecution#getType()
     */
    T type(String type);

    /**
     * @see TaskExecution#getWorkflowDefinitionId()
     */
    T workflowDefinitionId(String wfDefId);

    /**
     * @see TaskExecution#getWorkflowExecutionId()
     */
    T workflowExecutionId(String wfExecId);

    /**
     * @see TaskExecution#getRuntimeContext()
     */
    T runtimeContext(Map<String, Object> runtimeContext);

    /**
     * @see TaskExecution#getResult()
     */
    T result(Object result);

    /**
     * @see TaskExecution#getPublished()
     */
    T published(Map<String, Object> published);

    /**
     * @see TaskExecution#isProcessed()
     */
    T processed(Boolean processed);

    /**
     * @see TaskExecution#isReset()
     */
    T reset(Boolean reset);

    /**
     * @see TaskExecution#getEnvironment()
     */
    T environment(Map<String, Object> env);
}
