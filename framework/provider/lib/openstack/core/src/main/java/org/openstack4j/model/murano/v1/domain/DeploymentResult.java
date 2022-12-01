package org.openstack4j.model.murano.v1.domain;

import org.openstack4j.model.ModelEntity;

/**
 * @author Nikolay Mahotkin.
 */
public interface DeploymentResult extends ModelEntity {
    /**
     * @return true if exception is raised during deployment.
     */
    boolean isException();

    /**
     * @return the result of the deployment.
     */
    InnerResult getResult();

    interface InnerResult extends ModelEntity {
        /**
         * @return Detailed message (in case of exception - full traceback).
         */
        String getDetails();

        /**
         * @return result message.
         */
        String getMessage();
    }
}
