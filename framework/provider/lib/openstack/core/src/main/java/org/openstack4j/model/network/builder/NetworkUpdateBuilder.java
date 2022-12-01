package org.openstack4j.model.network.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.NetworkUpdate;

/**
 * Builds a NetworkUpdate entity
 *
 * @author Jeremy Unruh
 */
public interface NetworkUpdateBuilder extends Builder<NetworkUpdateBuilder, NetworkUpdate> {

    /**
     * Sets the network name
     *
     * @param name the name of the network
     * @return the builder
     */
    NetworkUpdateBuilder name(String name);

    /**
     * The administrative state of the network, which is up (true) or down (false).
     *
     * @param enabled if true indicated the admin state is up
     * @return the builder
     */
    NetworkUpdateBuilder adminStateUp(boolean enabled);

    /**
     * Admin-only. Indicates whether this network is shared across all tenants.
     *
     * @param shared if true the network is shared
     * @return the builder
     */
    NetworkUpdateBuilder shared(boolean shared);
}
