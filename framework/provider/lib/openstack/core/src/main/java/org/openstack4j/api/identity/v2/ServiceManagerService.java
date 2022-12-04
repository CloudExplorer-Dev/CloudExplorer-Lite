package org.openstack4j.api.identity.v2;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.identity.v2.Service;
import org.openstack4j.model.identity.v2.ServiceEndpoint;

import java.util.List;

/**
 * Manages OpenStack service(s), such as Compute (Nova), Object Storage (Swift), or Image Service (Glance).
 *
 * @author Jeremy Unruh
 */
public interface ServiceManagerService extends RestService {

    /**
     * List current Services on the OpenStack System
     *
     * @return the list<? extends service>
     */
    List<? extends Service> list();

    /**
     * Gets the specified Service by it's identifier
     *
     * @param serviceId the service id
     * @return the service
     */
    Service get(String serviceId);

    /**
     * Creates a new Service
     *
     * @param name        the name of the service
     * @param type        the type of service
     * @param description the description for the service
     * @return the service created
     */
    Service create(String name, String type, String description);

    /**
     * Deletes a Service based on it's id
     *
     * @param serviceId the service id
     * @return the action response
     */
    ActionResponse delete(String serviceId);

    /**
     * Queries for service related Endpoints (endpoints mapped against services)
     *
     * @return List of ServiceEndpoint(s)
     */
    List<? extends ServiceEndpoint> listEndpoints();

    /**
     * Creates a new Endpoint associated to a service identifier
     *
     * @param region      the endpoint region
     * @param serviceId   the service identifier the endpoint is associated with
     * @param publicURL   the public URL
     * @param adminURL    the admin URL
     * @param internalURL the internal URL
     * @return the create service endpoint
     */
    ServiceEndpoint createEndpoint(String region, String serviceId, String publicURL, String adminURL, String internalURL);

    /**
     * Deletes an Endpoint
     *
     * @param endpointId the endpoint identifier
     * @return the action response
     */
    ActionResponse deleteEndpoint(String endpointId);

}
