package org.openstack4j.openstack.dns.v2.internal;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.openstack.common.functions.EnforceVersionToURL;
import org.openstack4j.openstack.internal.BaseOpenStackService;

public class BaseDNSServices extends BaseOpenStackService {

    protected BaseDNSServices() {
        super(ServiceType.DNS, EnforceVersionToURL.instance("/v2"));
    }
}
