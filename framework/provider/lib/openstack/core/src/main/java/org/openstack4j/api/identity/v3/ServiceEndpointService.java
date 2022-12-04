package org.openstack4j.api.identity.v3;

import org.openstack4j.api.types.Facing;
import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.identity.v3.Endpoint;
import org.openstack4j.model.identity.v3.Service;

import java.net.URL;
import java.util.List;

/**
 * Identity V3 ServiceManager
 */
public interface ServiceEndpointService extends RestService {

    /**
     * Create a new service
     *
     * @param service the service
     * @return the created service
     */
    Service create(Service service);

    /**
     * Creates a new service
     *
     * @param type        the type of the service
     * @param name        the name of the service
     * @param description the description of the service
     * @param enabled     the enabled status of the service
     * @return the created service
     */
    Service create(String type, String name, String description, boolean enabled);

    /**
     * Get details of a service by id
     *
     * @param serviceId the id of the service
     * @return the service
     */
    Service get(String serviceId);

    /**
     * Updates a service
     *
     * @param service the service set to update
     * @return the updated service
     */
    Service update(Service service);

    /**
     * Delete a service
     *
     * @param serviceId the id of the service
     * @return the ActionResponse
     */
    ActionResponse delete(String serviceId);

    /**
     * Lists services
     *
     * @return the list of services
     */
    List<? extends Service> list();


    /**
     * Lists available endpoints
     *
     * @return the list of endpoints
     */
    List<? extends Endpoint> listEndpoints();

    /**
     * Creates a new endpoint
     *
     * @param endpoint the endpoint
     * @return the created endpoint
     */
    Endpoint createEndpoint(Endpoint endpoint);

    /**
     * Creates a new endpoint
     *
     * @param name      the name of the endpoint
     * @param url       the url of the endpont
     * @param iface     the interface type of the endpoint
     * @param regionId  the region id of the region that contains the endpoint
     * @param serviceId the service id of the service the endpoint belongs to
     * @param enabled   the enabled status of the endpoint
     */
    Endpoint createEndpoint(String name, URL url, Facing iface, String regionId, String serviceId, boolean enabled);

    /**
     * Get details for an endpoint
     *
     * @param endpointId the endpoint id
     * @return the endpoint
     */
    Endpoint getEndpoint(String endpointId);

    /**
     * Updates a endpoint
     *
     * @param endpoint the endpoint set to update
     * @return the updated endpoint
     */
    Endpoint updateEndpoint(Endpoint endpoint);

    /**
     * Delete an Endpoint
     *
     * @param endpointId the endpoint identifier
     * @return the ActionResponse
     */
    ActionResponse deleteEndpoint(String endpointId);

}
