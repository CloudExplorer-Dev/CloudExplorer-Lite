package org.openstack4j.openstack.networking.internal;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.openstack.common.functions.EnforceVersionToURL;
import org.openstack4j.openstack.internal.BaseOpenStackService;

/**
 * Base Networking Operations Implementation is responsible for insuring the proper endpoint is used for all extending operation APIs
 *
 * @author Jeremy Unruh
 */
public class BaseNetworkingServices extends BaseOpenStackService {

    protected BaseNetworkingServices() {
        super(ServiceType.NETWORK, EnforceVersionToURL.instance("/v2.0"));
    }
}
