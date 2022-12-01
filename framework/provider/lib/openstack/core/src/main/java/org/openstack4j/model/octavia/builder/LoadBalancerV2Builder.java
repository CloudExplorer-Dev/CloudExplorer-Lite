package org.openstack4j.model.octavia.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.octavia.LoadBalancerV2;

public interface LoadBalancerV2Builder extends Buildable.Builder<LoadBalancerV2Builder, LoadBalancerV2> {
    /**
     * @param projectId the ID of the project/tenant that owns the resource
     * @return LoadBalancerV2Builder
     */
    LoadBalancerV2Builder projectId(String projectId);

    /**
     * Optional
     *
     * @param name Human-readable name for the loadbalancer. Does not have to be unique.
     * @return LoadBalancerV2Builder
     */
    LoadBalancerV2Builder name(String name);

    /**
     * Optional
     *
     * @param description Human-readable description for the loadbalancer.
     * @return LoadBalancerV2Builder
     */
    LoadBalancerV2Builder description(String description);

    /**
     * Optional
     *
     * @param vipNetworkId The network on which to allocate the load balancer's vip address.
     *                     A tenant can only create load balancer vips on networks authorized by policy (e.g. her own networks or shared/provider networks).
     * @return LoadBalancerV2Builder
     */
    LoadBalancerV2Builder networkId(String vipNetworkId);

    /**
     * Optional
     *
     * @param vipSubnetId The subnet on which to allocate the load balancer's vip address.
     *                    A tenant can only create load balancer vips on subnets authorized by policy.
     * @return LoadBalancerV2Builder
     */
    LoadBalancerV2Builder subnetId(String vipSubnetId);

    /**
     * Optional
     *
     * @param vipPortId The vip port.
     * @return LoadBalancerV2Builder
     */
    LoadBalancerV2Builder portId(String vipPortId);

    /**
     * Optional
     *
     * @param vipAddress The IP address of the VIP.
     *                   If provided, the system will attempt to assign the load balancer's vip address to this.
     * @return LoadBalancerV2Builder
     */
    LoadBalancerV2Builder address(String vipAddress);

    /**
     * Optional
     *
     * @param adminStateUp The administrative state of the VIP. A valid value is true
     *                     (UP) or false (DOWN).
     * @return LoadBalancerV2Builder
     */
    LoadBalancerV2Builder adminStateUp(boolean adminStateUp);

    /**
     * Optional
     *
     * @param provider Set the provider the load balancer will be provisioned with
     */
    LoadBalancerV2Builder provider(String provider);
}
