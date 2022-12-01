package org.openstack4j.openstack.placement.internal;

import org.openstack4j.api.Apis;
import org.openstack4j.api.placement.PlacementService;
import org.openstack4j.api.placement.ext.ResourceProviderService;

/**
 * Placement Operations API implementation
 *
 * @author Jyothi Saroja
 */
public class PlacementServiceImpl extends BasePlacementServices implements PlacementService {

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceProviderService resourceProviders() {
        return Apis.get(ResourceProviderService.class);
    }

}
