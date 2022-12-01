package org.openstack4j.model.identity.v2.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.identity.v2.ServiceEndpoint;

/**
 * A Builder which creates an Identity Service Endpoint
 *
 * @author Jeremy Unruh
 */
public interface ServiceEndpointBuilder extends Builder<ServiceEndpointBuilder, ServiceEndpoint> {

    /**
     * @see ServiceEndpoint#getRegion()
     */
    ServiceEndpointBuilder region(String region);

    /**
     * @see ServiceEndpoint#getServiceId()
     */
    ServiceEndpointBuilder serviceId(String serviceId);

    /**
     * @see ServiceEndpoint#getPublicURL()
     */
    ServiceEndpointBuilder publicURL(String publicURL);

    /**
     * @see ServiceEndpoint#getAdminURL()
     */
    ServiceEndpointBuilder adminURL(String adminURL);

    /**
     * @see ServiceEndpoint#getInternalURL()
     */
    ServiceEndpointBuilder internalURL(String internalURL);

}
