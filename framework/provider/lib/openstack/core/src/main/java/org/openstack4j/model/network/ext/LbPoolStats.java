package org.openstack4j.model.network.ext;

import org.openstack4j.model.ModelEntity;

public interface LbPoolStats extends ModelEntity {
    /**
     * @return The inbound bytes of the loadbalancer.
     */
    public Long getBytesIn();

    /**
     * @return The inbound bytes of the loadbalancer.
     */
    public Long getBytesOut();

    /**
     * @return The number of total connections of the loadbalancer.
     */
    public Integer getTotalConnections();

    /**
     * @return The number of active connections of the loadbalancer.
     */
    public Integer getActiveConnections();
}
