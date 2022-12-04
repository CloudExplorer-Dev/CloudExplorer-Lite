package org.openstack4j.model.compute;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.compute.builder.FloatingIPBuilder;

/**
 * The Interface FloatingIP.
 *
 * @author nanderson
 */
@Deprecated
public interface FloatingIP extends ModelEntity, Buildable<FloatingIPBuilder> {

    /**
     * Gets the id.
     *
     * @return the id
     */
    String getId();

    /**
     * Gets the instance id.
     *
     * @return the instance id
     */
    String getInstanceId();

    /**
     * Gets the floating ip address.
     *
     * @return the floating ip address
     */
    String getFloatingIpAddress();

    /**
     * Gets the fixed ip address.
     *
     * @return the fixed ip address
     */
    String getFixedIpAddress();

    /**
     * Gets the pool.
     *
     * @return the pool name
     */
    String getPool();
}
