package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable.Builder;

/**
 * Builder which crates Magnum Service
 *
 * @author Sohan Sangwan
 */

public interface MserviceBuilder extends Builder<MserviceBuilder, Mservice> {
    /**
     * @see Mservice#getId()
     */
    MserviceBuilder id(String id);

    /**
     * @see Mservice#getBinary()
     */
    MserviceBuilder binary(String binary);

    /**
     * @see Mservice#getCreatedAt()
     */
    MserviceBuilder createdAt(String createdAt);

    /**
     * @see Mservice#getState()
     */
    MserviceBuilder state(String state);

    /**
     * @see Mservice#getReportCount()
     */
    MserviceBuilder reportCount(int reportCount);

    /**
     * @see Mservice#getUpdatedAt()
     */
    MserviceBuilder updatedAt(String updatedAt);

    /**
     * @see Mservice#getHost()
     */
    MserviceBuilder host(String host);

    /**
     * @see Mservice#getDisabledReason()
     */
    MserviceBuilder disabledReason(String disabledReason);
}
