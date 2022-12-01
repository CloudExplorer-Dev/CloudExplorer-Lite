package org.openstack4j.model.identity.v2;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.identity.v2.builder.EndpointBuilder;

import java.net.URI;

/**
 * Endpoint Model is used to describe a network address which is described by URL's and other service
 * information depending on the context it was retrieved in.
 *
 * @author Jeremy Unruh
 * @see <a href="http://docs.openstack.org/api/openstack-identity-service/2.0/content/GET_listEndpointsForToken_v2.0_tokens__tokenId__endpoints_Token_Operations.html#GET_listEndpointsForToken_v2.0_tokens__tokenId__endpoints_Token_Operations-Response"
 */
public interface Endpoint extends ModelEntity, Buildable<EndpointBuilder> {

    String getType();

    /**
     * @return the id for this endpoint (null if a new endpoint is being created)
     */
    String getId();

    /**
     * @return the name of this endpoint, or null when the endpoint is part of the Access ServiceCatalog
     */
    String getName();

    /**
     * @return the admin URL for this endpoint, or null when the endpoint is part of the Access ServiceCatalog
     */
    URI getAdminURL();

    /**
     * @return the URL for this endpoint
     */
    URI getPublicURL();

    /**
     * @return the internal URL for this endpoint
     */
    URI getInternalURL();

    /**
     * @return the region of the endpoint or null
     */
    String getRegion();

    /**
     * @return the tenant identifier for this endpoint or null
     */
    String getTenantId();

    /**
     * @return the version id or null
     */
    String getVersionId();

    /**
     * @return the version information when endpoint is listed as part of Access Service Catalog, otherwise null
     */
    URI getVersionInfo();

    /**
     * @return the version list when endpoint is listed as part of Access Service Catalog, otherwise null
     */
    URI getVersionList();

}
