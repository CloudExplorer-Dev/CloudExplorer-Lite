package org.openstack4j.model.identity.v2;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.identity.v2.builder.ServiceEndpointBuilder;

import java.net.URI;

/**
 * OpenStack ServiceEndpoint is an endpoint which is mapped to a {@link Service}
 *
 * @author Jeremy Unruh
 */
public interface ServiceEndpoint extends ModelEntity, Buildable<ServiceEndpointBuilder> {

    /**
     * @return the endpoint identifier
     */
    String getId();

    /**
     * @return the region for this endpoint
     */
    String getRegion();

    /**
     * @return the service id this endpoint is asssociated with
     */
    String getServiceId();

    /**
     * @return the public URL for this endpoint
     */
    URI getPublicURL();

    /**
     * @return the admin URL for this endpoint
     */
    URI getAdminURL();

    /**
     * @return the internal URL for this endpoint
     */
    URI getInternalURL();

}
