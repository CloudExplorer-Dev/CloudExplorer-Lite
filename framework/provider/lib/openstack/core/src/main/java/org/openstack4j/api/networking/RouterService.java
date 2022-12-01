package org.openstack4j.api.networking;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.AttachInterfaceType;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.RouterInterface;

import java.util.List;

/**
 * Provides Neutron Router based Service Operations
 *
 * @author Jeremy Unruh
 */
public interface RouterService extends RestService {

    /**
     * This operation returns a list of routers to which the tenant has access. Default policy settings return only those routers
     * that are owned by the tenant who submits the request, unless the request is submitted by an user with administrative rights.
     *
     * @return List of Routers or empty list
     */
    List<? extends Router> list();

    /**
     * Gets a Router by ID
     *
     * @param routerId the router identifier
     * @return Router or null if not found
     */
    Router get(String routerId);

    /**
     * Deletes the specified Router by ID
     *
     * @param routerId the router identifier to delete
     * @return the action response
     */
    ActionResponse delete(String routerId);

    /**
     * Creates a basic router with minimal params
     *
     * @param name         the name of the router
     * @param adminStateUp the initial administrative state
     * @return the newly create router
     */
    Router create(String name, boolean adminStateUp);

    /**
     * Creates a Router
     *
     * @param router the router to create
     * @return the newly created router
     */
    Router create(Router router);

    /**
     * Updates a Router.  Based on the OpenStack API documentation only [ name, admin_state_up and external_gateway_info ] will be updated.
     * <p>
     * NOTE: The router identifier must be set in the {@code router}.  See {@link Router#setId(String)}
     *
     * @param router the router to update
     * @return the updated router
     */
    Router update(Router router);

    /**
     * Toggles the Administrative state by Router ID
     *
     * @param routerId     the router identifier
     * @param adminStateUp true to enable the administrative state up
     * @return the updated router
     */
    Router toggleAdminStateUp(String routerId, boolean adminStateUp);

    /**
     * Attaches a Subnet or Port to the specified router
     *
     * @param routerId       the router identifier
     * @param type           the type of {@code portOrSubnetId} identifier
     * @param portOrSubnetId the port or subnet identifier
     * @return the newly created router interface
     */
    RouterInterface attachInterface(String routerId, AttachInterfaceType type, String portOrSubnetId);

    /**
     * Removes an internal router interface, which detaches a subnet from the router. Either a subnet or port is allowed to be set and or both.  At least one
     * subnet or port identifier must be set or else a validation exception will be thrown.
     *
     * @param routerId the router identifier
     * @param subnetId the subnet identifier
     * @param portId   the port identifier
     * @return the router interface that was detached
     * @throws ClientResponseException if one of the specified identifiers does not exist
     */
    RouterInterface detachInterface(String routerId, String subnetId, String portId);
}
