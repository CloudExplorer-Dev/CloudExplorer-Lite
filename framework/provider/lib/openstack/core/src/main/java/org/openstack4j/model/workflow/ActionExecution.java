/*
 *
 */
package org.openstack4j.model.workflow;

import java.util.Map;


/**
 * An action execution.
 *
 * @author Renat Akhmerov
 */
public interface ActionExecution extends Execution {
    /**
     * @return The name of the corresponding task.
     */
    String getTaskName();

    /**
     * @return The id of the corresponding task execution.
     */
    String getTaskExecutionId();

    /**
     * @return The name of the action.
     */
    String getName();

    /**
     * @return {@code True} if the result of this action execution is accepted.
     */
    Boolean isAccepted();

    /**
     * @return The input parameters of this action execution.
     */
    Map<String, Object> getInput();

    /**
     * @return The output of this action execution.
     */
    Map<String, Object> getOutput();
}
