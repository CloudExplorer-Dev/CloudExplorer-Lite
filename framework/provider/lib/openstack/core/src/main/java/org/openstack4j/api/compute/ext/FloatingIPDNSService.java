package org.openstack4j.api.compute.ext;

/**
 * API Service that manages the 'os-floating-ip-dns' extension
 *
 * @author Jeremy Unruh
 * @deprecated Since these APIs are only implemented for nova-network, they are deprecated. These will fail with a 404 starting from microversion 2.36. They were removed in the 18.0.0 Rocky release.
 */
@Deprecated
public interface FloatingIPDNSService {

    /**
     * Service that manages DNS Domains
     *
     * @return the domain service
     */
    FloatingIPDNSDomainService domains();

    /**
     * Service that manages DNS Entries
     *
     * @return the DNS entry service
     */
    FloatingIPDNSEntryService entries();

}
