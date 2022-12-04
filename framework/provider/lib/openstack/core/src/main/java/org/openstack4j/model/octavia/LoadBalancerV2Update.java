package org.openstack4j.model.octavia;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.octavia.builder.LoadBalancerV2UpdateBuilder;

/**
 * an entity used to update an lbaas v2 loadbalancer
 *
 * @author wei
 */
public interface LoadBalancerV2Update extends ModelEntity, Buildable<LoadBalancerV2UpdateBuilder> {
    /**
     * Optional
     *
     * @see LoadBalancerV2#getDescription()
     */
    public String getDescription();

    /**
     * Optional
     *
     * @see LoadBalancerV2#getName()
     */
    public String getName();

    /**
     * Optional
     *
     * @see LoadBalancerV2#isAdminStateUp()
     */
    public boolean isAdminStateUp();
}
