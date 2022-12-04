package org.openstack4j.model.murano.v1.domain;

import org.openstack4j.model.ModelEntity;

/**
 * @author Nikolay Mahotkin.
 */
public interface ActionInfo extends ModelEntity {
    /**
     * @return Action name.
     */
    String getName();

    /**
     * @return Action title.
     */
    String getTitle();

    /**
     * @return if action is enabled.
     */
    boolean getEnabled();

    /**
     * @return the action id.
     */
    String getId();
}
