package org.openstack4j.model.identity.v3.builder;

import org.openstack4j.api.types.Facing;
import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.identity.v3.Endpoint;

import java.net.URL;
import java.util.Map;

/**
 * A Builder which creates an identity v3 endpoint.
 */
public interface EndpointBuilder extends Builder<EndpointBuilder, Endpoint> {

    /**
     * @see Endpoint#getId()
     */
    EndpointBuilder id(String id);

    /**
     * @see Endpoint#getType()
     */
    EndpointBuilder type(String type);

    /**
     * @see Endpoint#getDescription()
     */
    EndpointBuilder description(String description);

    /**
     * @see Endpoint#getIface()
     */
    EndpointBuilder iface(Facing iface);

    /**
     * @see Endpoint#getServiceId()
     */
    EndpointBuilder serviceId(String serviceId);

    /**
     * @see Endpoint#getName()
     */
    EndpointBuilder name(String name);

    /**
     * @see Endpoint#getRegion()
     */
    EndpointBuilder region(String region);

    /**
     * @see Endpoint#getRegionId()
     */
    EndpointBuilder regionId(String regionId);

    /**
     * @see Endpoint#getUrl()
     */
    EndpointBuilder url(URL url);

    /**
     * @see Endpoint#getLinks()
     */
    EndpointBuilder links(Map<String, String> links);

    /**
     * @see Endpoint#isEnabled()
     */
    EndpointBuilder enabled(boolean enabled);

}
