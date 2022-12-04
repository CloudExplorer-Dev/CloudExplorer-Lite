package org.openstack4j.model.octavia;

import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.octavia.status.LoadBalancerV2Status;

/**
 * The status tree of an lbaas v2 loadbalancer
 *
 * @author wei
 */
public interface LoadBalancerV2StatusTree extends ModelEntity {
    /**
     * Get the status tree of a loadbalancer
     *
     * @return the loadbalancer status tree
     */
    public LoadBalancerV2Status getLoadBalancerV2Status();
}
