package org.openstack4j.model.octavia;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.octavia.builder.LoadBalancerV2Builder;
import org.openstack4j.openstack.octavia.domain.ListItem;

import java.util.List;

/**
 * An entity used to update an lbaas v2 loadbalancer
 *
 * @author wei
 */
public interface LoadBalancerV2 extends ModelEntity, Buildable<LoadBalancerV2Builder> {
    /**
     * @return id. The unique ID for the loadbalancer.
     */
    String getId();

    /**
     * @return the ID of the project/tenant that owns the resource
     */
    String getProjectId();

    /**
     * @return loadbalancer name. Does not have to be unique.
     */
    String getName();

    /**
     * @return Description for the loadbalancer.
     */
    String getDescription();

    /**
     * @return The vip network id of the loadbalancer.
     */
    String getVipNetworkId();

    /**
     * @return The vip subnet id of the loadbalancer.
     */
    String getVipSubnetId();

    /**
     * @return The vip address of the loadbalancer.
     */
    String getVipAddress();

    /***
     * @return the vip port id of the loadbalancer
     */
    String getVipPortId();

    /**
     * @return The administrative state of the loadbalancer, which is up (true) or
     * down (false).
     */
    boolean isAdminStateUp();

    /**
     * @return The listeners of the loadbalancer.
     */
    List<ListItem> getListeners();

    /**
     * @return provisioningStatus.The provisioning status of the loadbalancer. Indicates whether the
     * loadbalancer is provisioning.
     * Either ACTIVE, PENDING_CREATE or ERROR.
     */
    LbProvisioningStatus getProvisioningStatus();

    /**
     * @return operatingStatus.The operating status of the loadbalancer. Indicates whether the
     * loadbalancer is operational.
     */
    LbOperatingStatus getOperatingStatus();

    /**
     * Retrieve provider the load balancer is provisioned with
     *
     * @return provider
     */
    String getProvider();
}
