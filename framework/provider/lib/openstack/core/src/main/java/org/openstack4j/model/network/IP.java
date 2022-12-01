package org.openstack4j.model.network;

import org.openstack4j.model.ModelEntity;


/**
 * A Fixed IP Address
 *
 * @author Jeremy Unruh
 */
public interface IP extends ModelEntity {

    /**
     * @return the fixed ip address
     */
    String getIpAddress();

    /**
     * @return the id of the subnet of this ip
     */
    String getSubnetId();

}
