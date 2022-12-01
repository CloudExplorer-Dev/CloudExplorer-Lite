package org.openstack4j.model.senlin;

import org.openstack4j.model.ModelEntity;

/**
 * This interface describes the getter-methods (and thus components) of a response Action.
 *
 * @author lion
 * @see http://developer.openstack.org/api-ref-clustering-v1.html
 */
public interface ActionID extends ModelEntity {

    /**
     * Returns the action ID of the action
     *
     * @return the action ID of the action
     */
    String getActionID();

}
