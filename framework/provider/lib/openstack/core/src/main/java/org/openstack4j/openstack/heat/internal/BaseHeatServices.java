package org.openstack4j.openstack.heat.internal;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.openstack.internal.BaseOpenStackService;

/**
 * This class is extended by every Heat Service. It is necessary to determine
 * the correct endpoint (url) for heat calls.
 *
 * @author Matthias Reisser
 */
public class BaseHeatServices extends BaseOpenStackService {

    protected BaseHeatServices() {
        super(ServiceType.ORCHESTRATION);
    }

}
