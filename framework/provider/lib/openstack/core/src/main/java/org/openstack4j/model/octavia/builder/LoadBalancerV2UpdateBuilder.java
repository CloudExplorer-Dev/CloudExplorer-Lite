package org.openstack4j.model.octavia.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.octavia.LoadBalancerV2Update;

/**
 * Builder to update a lbaas v2 loadbalancer
 *
 * @author wei
 */
public interface LoadBalancerV2UpdateBuilder extends Buildable.Builder<LoadBalancerV2UpdateBuilder, LoadBalancerV2Update> {
    /**
     * Optional
     *
     * @param description Human-readable description for the load balancer.
     * @return LoadBalancerV2UpdateBuilder
     */
    public LoadBalancerV2UpdateBuilder description(String description);

    /**
     * @param name Human-readable name for the load balancer.
     * @return LoadBalancerV2UpdateBuilder
     */
    public LoadBalancerV2UpdateBuilder name(String name);

    /**
     * Optional
     *
     * @param adminStateUp The administrative state of the VIP. A valid value is true
     *                     (UP) or false (DOWN).
     * @return LoadBalancerV2UpdateBuilder
     */
    public LoadBalancerV2UpdateBuilder adminStateUp(boolean adminStateUp);

}
