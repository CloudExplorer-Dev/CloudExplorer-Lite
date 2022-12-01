package org.openstack4j.model.compute.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.compute.FloatingIP;

/**
 * Builder for a FloatingIP model class
 *
 * @author Nathan Anderson
 */
@Deprecated
public interface FloatingIPBuilder extends Builder<FloatingIPBuilder, FloatingIP> {

    /**
     * @see FloatingIP#getId()
     */
    FloatingIPBuilder id(String id);

    /**
     * @see FloatingIP#getFixedIpAddress()
     */
    FloatingIPBuilder fixedIpAddress(String fixedIp);

    /**
     * @see FloatingIP#getFloatingIpAddress()
     */
    FloatingIPBuilder floatingIpAddress(String floatingIpAddress);

    /**
     * @see FloatingIP#getInstanceId()
     */
    FloatingIPBuilder instanceId(String instanceId);

    /**
     * @see FloatingIP#getPool()
     */
    FloatingIPBuilder pool(String pool);

}
