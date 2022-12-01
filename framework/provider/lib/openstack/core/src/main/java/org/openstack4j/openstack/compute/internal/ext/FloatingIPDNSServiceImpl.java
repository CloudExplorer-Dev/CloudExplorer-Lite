package org.openstack4j.openstack.compute.internal.ext;

import org.openstack4j.api.Apis;
import org.openstack4j.api.compute.ext.FloatingIPDNSDomainService;
import org.openstack4j.api.compute.ext.FloatingIPDNSEntryService;
import org.openstack4j.api.compute.ext.FloatingIPDNSService;

/**
 * API Service that manages the 'os-floating-ip-dns' extension
 *
 * @author Jeremy Unruh
 */
@Deprecated
public class FloatingIPDNSServiceImpl implements FloatingIPDNSService {

    @Override
    public FloatingIPDNSDomainService domains() {
        return Apis.get(FloatingIPDNSDomainService.class);
    }

    @Override
    public FloatingIPDNSEntryService entries() {
        return Apis.get(FloatingIPDNSEntryService.class);
    }

}
