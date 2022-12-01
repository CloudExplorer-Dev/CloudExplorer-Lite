package org.openstack4j.api.placement;

import org.openstack4j.api.placement.ext.ResourceProviderService;
import org.openstack4j.common.RestService;

/**
 * Placement Operations API
 *
 * @author Jyothi Saroja
 */
public interface PlacementService extends RestService {

    /**
     * Resource provider Service API
     *
     * @return the resource provider service
     */
    ResourceProviderService resourceProviders();

}
