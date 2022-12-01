package org.openstack4j.model.manila.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.manila.ShareNetworkCreate;

/**
 * Builds a {@link org.openstack4j.model.manila.ShareNetworkCreate}.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface ShareNetworkCreateBuilder extends Buildable.Builder<ShareNetworkCreateBuilder, ShareNetworkCreate> {
    /**
     * Set the neutron network and subnet.
     *
     * @param neutronNetId    the neutron network ID
     * @param neutronSubnetId the neutron subnet ID
     * @return ShareNetworkCreateBuilder
     */
    ShareNetworkCreateBuilder neutronNet(String neutronNetId, String neutronSubnetId);

    /**
     * Set the nova network.
     *
     * @param novaNetId the nova network ID
     * @return ShareNetworkCreateBuilder
     */
    ShareNetworkCreateBuilder novaNet(String novaNetId);

    /**
     * Set the share network name.
     *
     * @param name the share network name
     * @return ShareNetworkCreateBuilder
     */
    ShareNetworkCreateBuilder name(String name);

    /**
     * Set the share network description.
     *
     * @param description the share network description
     * @return ShareNetworkCreateBuilder
     */
    ShareNetworkCreateBuilder description(String description);
}
