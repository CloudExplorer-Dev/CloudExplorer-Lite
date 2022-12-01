package org.openstack4j.api;

import org.openstack4j.api.types.ServiceType;

/**
 * Provides Endpoint information for the current authorized scope
 *
 * @author Jeremy Unruh
 */
public interface EndpointTokenProvider {

    /**
     * Gets the endpoint for the specified ServiceType
     *
     * @param service the service to obtain the endpoint for
     * @return the endpoint
     */
    String getEndpoint(ServiceType service);

    /**
     * Gets the token identifier
     *
     * @return the auth token identifier
     */
    String getTokenId();
}
