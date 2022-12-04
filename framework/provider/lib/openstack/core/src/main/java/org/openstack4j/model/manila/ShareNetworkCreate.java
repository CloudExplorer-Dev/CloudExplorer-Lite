package org.openstack4j.model.manila;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.manila.builder.ShareNetworkCreateBuilder;

/**
 * Object used to create new share networks
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface ShareNetworkCreate extends ModelEntity, Buildable<ShareNetworkCreateBuilder> {
    /**
     * @return the neutron network ID
     */
    String getNeutronNetId();

    /**
     * @return the neutron subnet ID
     */
    String getNeutronSubnetId();

    /**
     * @return the nova network ID
     */
    String getNovaNetId();

    /**
     * @return the share network name
     */
    String getName();

    /**
     * @return the share network description
     */
    String getDescription();
}
