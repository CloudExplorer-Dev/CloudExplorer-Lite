package org.openstack4j.openstack.tacker.internal;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.openstack.common.functions.EnforceVersionToURL;
import org.openstack4j.openstack.internal.BaseOpenStackService;

/**
 * Base Tacker Operations Implementation is responsible for insuring the proper endpoint is used for all extending operation APIs
 *
 * @author Vishvesh Deshmukh
 */
public class BaseTackerServices extends BaseOpenStackService {

    protected BaseTackerServices() {
        super(ServiceType.TACKER, EnforceVersionToURL.instance("/v1.0"));
    }
}
