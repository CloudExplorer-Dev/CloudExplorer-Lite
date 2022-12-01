/*
 *
 */
package org.openstack4j.model.workflow;


/**
 * An action definition.
 *
 * @author Renat Akhmerov
 */
public interface ActionDefinition extends Definition {

    /**
     * @return The comma-separated list of input parameters of this workflow
     * definition.
     */
    String getInput();
}
