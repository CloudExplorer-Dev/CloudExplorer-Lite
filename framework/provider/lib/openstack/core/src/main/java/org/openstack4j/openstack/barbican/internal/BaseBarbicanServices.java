package org.openstack4j.openstack.barbican.internal;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.openstack.common.functions.EnforceVersionToURL;
import org.openstack4j.openstack.internal.BaseOpenStackService;

/**
 * Base class for Barbican / Key Management services
 */
public class BaseBarbicanServices extends BaseOpenStackService {
    protected BaseBarbicanServices() {
        super(ServiceType.BARBICAN, EnforceVersionToURL.instance("/v1"));
    }
}
