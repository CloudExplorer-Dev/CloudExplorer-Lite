package org.openstack4j.openstack.storage.block.domain;

import org.openstack4j.model.ModelEntity;

/**
 * Availability Zone Model Entity
 *
 * @author Jeff Hu
 */
public interface AvailabilityZone extends ModelEntity {

    /**
     * @return zone's state
     */
    ZoneState getZoneState();


    /**
     * @return zone's name
     */
    String getZoneName();


    public interface ZoneState extends ModelEntity {

        /**
         * @return the state of zone
         */
        boolean getAvailable();
    }


}

