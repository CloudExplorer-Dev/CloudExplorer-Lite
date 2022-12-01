package org.openstack4j.model.network;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.network.builder.RouterBuilder;

import java.util.List;

/**
 * A router is used to interconnect subnets and forward traffic among them. Another feature of the router is to NAT internal traffic to external networks.
 *
 * @author Jeremy Unruh
 */
public interface Router extends Resource, Buildable<RouterBuilder> {

    /**
     * Host Routing entries for the router
     *
     * @return the routes for the router
     */
    List<? extends HostRoute> getRoutes();

    /**
     * Administrative state of the router
     *
     * @return true, if the administrative state is up
     */
    boolean isAdminStateUp();

    /**
     * Indicates whether a router is currently operational or not
     *
     * @return the state/status of the router
     */
    State getStatus();

    /**
     * Information on external gateway for the router
     *
     * @return the external gateway info
     */
    ExternalGateway getExternalGatewayInfo();

    /**
     * true indicates a distributed router. It is available when dvr extension is enabled.
     */
    Boolean getDistributed();
}
