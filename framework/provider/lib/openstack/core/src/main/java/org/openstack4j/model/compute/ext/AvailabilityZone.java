package org.openstack4j.model.compute.ext;

import org.openstack4j.model.ModelEntity;

import java.util.Date;
import java.util.Map;

/**
 * Availability Zone Model Entity
 *
 * @author octopus zhang
 */
public interface AvailabilityZone extends ModelEntity {

    /**
     * @return zone's state
     */
    ZoneState getZoneState();

    /**
     * @return hosts in the zone and nova services in each host, not really implemented yet
     */
    Map<String, Map<String, ? extends NovaService>> getHosts();

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

    public interface NovaService extends ModelEntity {
        /**
         * @return whether the service is available
         */
        boolean getAvailable();

        /**
         * @return the service's status
         */
        String getStatusActive();

        /**
         * @return the service's latest update time
         */
        Date getUpdateTime();
    }

}

