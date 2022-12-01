package org.openstack4j.openstack.trove.internal;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.openstack.internal.BaseOpenStackService;


/**
 * Trove services
 *
 * @author sumit gandhi
 */

public class BaseTroveServices extends BaseOpenStackService {

    protected BaseTroveServices() {
        super(ServiceType.DATABASE);
    }

}
