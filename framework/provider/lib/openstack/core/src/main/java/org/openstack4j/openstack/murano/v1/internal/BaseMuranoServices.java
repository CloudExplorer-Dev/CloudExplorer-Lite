package org.openstack4j.openstack.murano.v1.internal;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.openstack.common.functions.EnforceVersionToURL;
import org.openstack4j.openstack.internal.BaseOpenStackService;

/**
 * This class is extended by every Murano Service. It is necessary to determine
 * the correct endpoint (url) for murano calls.
 *
 * @author Nikolay Makhotkin
 */
class BaseMuranoServices extends BaseOpenStackService {

    BaseMuranoServices() {
        super(ServiceType.APP_CATALOG, EnforceVersionToURL.instance("/v1", true));
    }

}
