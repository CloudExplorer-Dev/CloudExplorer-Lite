/*
 *
 */
package org.openstack4j.model.workflow;


/**
 * A workflow definition.
 *
 * @author Renat Akhmerov
 */
public interface WorkflowDefinition extends Definition {

    /**
     * @return The comma-separated list of input parameters of this workflow
     * definition.
     */
    String getInput();
}
