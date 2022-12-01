package org.openstack4j.openstack.placement.internal;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.openstack.internal.BaseOpenStackService;

/**
 * Base class for Placement services
 *
 * @author Jyothi Saroja
 */
public class BasePlacementServices extends BaseOpenStackService {

    /**
     * The constructor
     */
    protected BasePlacementServices() {
        super(ServiceType.PLACEMENT);
    }
}
