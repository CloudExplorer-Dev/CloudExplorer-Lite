package org.openstack4j.api.networking;

import org.openstack4j.common.RestService;
import org.openstack4j.model.network.AvailabilityZone;

import java.util.List;

/**
 * Neutron Availability Zone Service
 *
 * @author Taemin
 */
public interface AvailabilityZoneService extends RestService {


    /**
     * List all availability zone that the current neutron has.
     *
     * @return list of all availability zones
     * @author Taemin
     */
    List<? extends AvailabilityZone> list();

}
