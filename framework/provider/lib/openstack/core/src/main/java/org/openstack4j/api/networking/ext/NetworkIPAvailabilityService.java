package org.openstack4j.api.networking.ext;

import org.openstack4j.common.RestService;
import org.openstack4j.model.network.ext.NetworkIPAvailability;

import java.util.List;

/**
 * Network IP availability and usage stats Extension API
 *
 * @author Xiangbin HAN
 */
public interface NetworkIPAvailabilityService extends RestService {
    /**
     * Lists network IP availability for networks
     *
     * @return the list of quotas
     */
    List<? extends NetworkIPAvailability> get();

    /**
     * Fetches the network IP availability for the specified network
     *
     * @param networkId the network identifier
     * @return the network IP availability
     */
    NetworkIPAvailability get(String networkId);

}
