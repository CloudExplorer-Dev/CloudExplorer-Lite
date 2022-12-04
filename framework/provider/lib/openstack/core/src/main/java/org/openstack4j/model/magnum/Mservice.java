package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;

public interface Mservice extends ModelEntity, Buildable<MserviceBuilder> {

    /**
     * Identifier of Magnum Service
     *
     * @return the of Magnum Service
     */
    String getId();

    /**
     * Binary of the service
     *
     * @return the binary of the service
     */
    String getBinary();

    /**
     * Creation date
     *
     * @return the date of creation
     */
    String getCreatedAt();

    /**
     * Current state if it is up or not
     *
     * @return the state of the service
     */
    String getState();

    /**
     * Report count
     *
     * @return the report count of the service
     */
    int getReportCount();

    /**
     * The date of last updation
     *
     * @return the last updated time
     */
    String getUpdatedAt();

    /**
     * The host name
     *
     * @return the host name
     */
    String getHost();

    /**
     * The disabled reason
     *
     * @return the disabled reason
     */
    String getDisabledReason();
}
