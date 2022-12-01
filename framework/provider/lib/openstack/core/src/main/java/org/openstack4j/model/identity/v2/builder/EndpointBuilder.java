package org.openstack4j.model.identity.v2.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.identity.v2.Endpoint;

import java.net.URI;

/**
 * A Builder which creates an Endpoint
 *
 * @author jeremy
 */
public interface EndpointBuilder extends Builder<EndpointBuilder, Endpoint> {

    /**
     * @see Endpoint#getRegion()
     */
    EndpointBuilder region(String region);

    /**
     * @see Endpoint#getPublicURL()
     */
    EndpointBuilder publicURL(URI publicURL);

    /**
     * @see Endpoint#getInternalURL()
     */
    EndpointBuilder internalURL(URI internalURL);

    /**
     * @see Endpoint#getTenantId()
     */
    EndpointBuilder tenantId(String tenantId);

    /**
     * @see Endpoint#getType()
     */
    EndpointBuilder type(String type);

    /**
     * @see Endpoint#getId()
     */
    EndpointBuilder id(String id);

    /**
     * @see Endpoint#getName()
     */
    EndpointBuilder name(String name);

    /**
     * @see Endpoint#getAdminURL()
     */
    EndpointBuilder adminURL(URI adminURL);

    /**
     * @see Endpoint#getVersionInfo()
     */
    EndpointBuilder versionInfo(URI versionInfo);

    /**
     * @see Endpoint#getVersionList()
     */
    EndpointBuilder versionList(URI versionList);

}
