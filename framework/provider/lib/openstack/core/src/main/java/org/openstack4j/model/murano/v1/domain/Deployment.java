package org.openstack4j.model.murano.v1.domain;

import org.openstack4j.model.ModelEntity;

/**
 * @author Nikolay Mahotkin.
 */
public interface Deployment extends ModelEntity {
    /**
     * @return state of the deployment.
     */
    String getState();

    /**
     * @return date and time of the start of the deployment.
     */
    String getStarted();

    /**
     * @return date and time of the finish of the deployment.
     */
    String getFinished();

    /**
     * @return the environment id.
     */
    String getEnvironmentId();

    /**
     * @return the id of the deployment.
     */
    String getId();

    /**
     * @return the description Object.
     */
    EnvironmentDescription getDescription();

    /**
     * @return created date.
     */
    String getCreated();

    /**
     * @return updated date.
     */
    String getUpdated();

    /**
     * @return the result.
     */
    DeploymentResult getResult();
}
