package org.openstack4j.openstack.image.internal;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.openstack.common.functions.EnforceVersionToURL;
import org.openstack4j.openstack.internal.BaseOpenStackService;

/**
 * Base Image Operations Implementation is responsible for insuring the proper endpoint is used for all extending operation APIs
 *
 * @author Jeremy Unruh
 */
public class BaseImageServices extends BaseOpenStackService {

    protected BaseImageServices() {
        super(ServiceType.IMAGE, EnforceVersionToURL.instance("/v1", true));
    }
}
