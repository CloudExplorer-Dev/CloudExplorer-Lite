package org.openstack4j.openstack.senlin.internal;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.openstack.common.functions.EnforceVersionToURL;
import org.openstack4j.openstack.internal.BaseOpenStackService;

/**
 * This class is extended by every senlin Service. It is necessary to determine
 * the correct endpoint (url) for senlin calls.
 *
 * @author lion
 */
public class BaseSenlinServices extends BaseOpenStackService {

    protected BaseSenlinServices() {
        super(ServiceType.CLUSTERING, EnforceVersionToURL.instance("/v1", true));
    }

}
