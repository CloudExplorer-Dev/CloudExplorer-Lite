package org.openstack4j.model.network;

import org.openstack4j.model.ModelEntity;

/**
 * An interface data model which is returned during interface association with a router
 *
 * @author Jeremy Unruh
 */
public interface RouterInterface extends ModelEntity {

    /**
     * @return the router identifier
     */
    String getId();

    /**
     * @return the subnet identifier or null if no subnet is associated
     */
    String getSubnetId();

    /**
     * @return the port identifier or null if no port is associated
     */
    String getPortId();

    /**
     * @return the tenant identifier or null
     */
    String getTenantId();
}
