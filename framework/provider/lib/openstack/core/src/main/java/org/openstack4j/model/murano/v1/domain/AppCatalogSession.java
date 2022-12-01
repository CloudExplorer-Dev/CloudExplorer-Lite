package org.openstack4j.model.murano.v1.domain;

import org.openstack4j.model.ModelEntity;

/**
 * @author Nikolay Mahotkin.
 */
public interface AppCatalogSession extends ModelEntity {
    /**
     * @return the session id
     */
    String getId();

    /**
     * @return the environment id
     */
    String getEnvId();

    /**
     * @return the user id
     */
    String getUserId();

    /**
     * @return the created date
     */
    String getCreated();

    /**
     * @return the updated date
     */
    String getUpdated();

    /**
     * @return the state of the session
     */
    String getState();

    /**
     * @return the version
     */
    String getVersion();
}
