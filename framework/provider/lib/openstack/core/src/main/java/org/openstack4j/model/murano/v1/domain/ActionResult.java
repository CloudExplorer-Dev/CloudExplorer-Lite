package org.openstack4j.model.murano.v1.domain;

import org.openstack4j.model.ModelEntity;

/**
 * @author Nikolay Mahotkin.
 */
public interface ActionResult extends ModelEntity {
    /**
     * @return true if exception was raised during action execution.
     */
    Boolean isException();

    /**
     * @return the Result object.
     * It might be one of:
     * - String
     * - boolean
     * - int
     * - float
     * - List
     * - Map<String, Object>
     * <p>
     * depending on Action itself.
     */
    Object getResult();
}
