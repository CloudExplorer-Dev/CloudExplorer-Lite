package org.openstack4j.model.identity.v3;

import org.openstack4j.api.types.Facing;
import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.identity.v3.builder.EndpointBuilder;

import java.net.URL;
import java.util.Map;

/**
 * Endpoint model for identity v3.
 *
 * @see <a href="http://developer.openstack.org/api-ref-identity-v3.html#endpoints-v3">API reference</a>
 */
public interface Endpoint extends ModelEntity, Buildable<EndpointBuilder> {

    /**
     * Globally unique identifier.
     *
     * @return the Id of the endpoint
     */
    String getId();

    /**
     * @return the type of the endpoint
     */
    String getType();

    /**
     * @return the Description of the endpoint
     */
    String getDescription();

    /**
     * @return the Interface of the endpoint
     */
    Facing getIface();

    /**
     * @return the ServiceId of the endpoint
     */
    String getServiceId();

    /**
     * @return the Name of the endpoint
     */
    String getName();

    /**
     * @return the Region of the endpoint
     */
    String getRegion();

    /**
     * @return the region identifier of the endpoint
     */
    String getRegionId();

    /**
     * @return the URL of the endpoint
     */
    URL getUrl();

    /**
     * @return the Links of the endpoint
     */
    Map<String, String> getLinks();

    /**
     * @return true if the endpoint is enabled, otherwise false
     */
    boolean isEnabled();

}
