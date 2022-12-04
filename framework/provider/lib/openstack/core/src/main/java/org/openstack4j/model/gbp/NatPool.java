package org.openstack4j.model.gbp;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.gbp.builder.NatPoolBuilder;

/**
 * Nat Pool Model Entity
 *
 * @author vinod borole
 */
public interface NatPool extends Resource, Buildable<NatPoolBuilder> {

    /**
     * Gets the subnet Id
     *
     * @return the subnet Id
     */
    String getSubnetId();

    /**
     * Gets the Ip Version
     *
     * @return the Ip Version
     */
    String getIpVersion();

    /**
     * Gets the Ip Pool
     *
     * @return the Ip Pool
     */
    String getIpPool();

    /**
     * Gets the External segment Id
     *
     * @return the External segment Id
     */
    String getExternalSegmentId();

    /**
     * Is Nat Pool shared
     *
     * @return the true if shared and false if not shared
     */
    boolean isShared();

    /**
     * Gets the description
     *
     * @return the description
     */
    String getDescription();

}
