package org.openstack4j.openstack.octavia.internal;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.openstack.common.functions.EnforceVersionToURL;
import org.openstack4j.openstack.internal.BaseOpenStackService;

/**
 * Base Networking Operations Implementation is responsible for insuring the proper endpoint is used for all extending operation APIs
 *
 * @author wei
 */
public class BaseOctaviaServices extends BaseOpenStackService {

    protected BaseOctaviaServices() {
        super(ServiceType.OCTAVIA, EnforceVersionToURL.instance("/v2.0"));
    }
}
